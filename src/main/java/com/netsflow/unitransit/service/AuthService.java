package com.netsflow.unitransit.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netsflow.unitransit.domain.Role;
import com.netsflow.unitransit.exception.EmailAlreadyRegisteredException;
import com.netsflow.unitransit.exception.ExpiredOTPException;
import com.netsflow.unitransit.exception.InvalidCredentialsException;
import com.netsflow.unitransit.exception.InvalidOTPException;
import com.netsflow.unitransit.exception.UserNotFoundException;
import com.netsflow.unitransit.model.Admin;
import com.netsflow.unitransit.model.Driver;
import com.netsflow.unitransit.model.Student;
import com.netsflow.unitransit.model.User;
import com.netsflow.unitransit.repository.UserRepository;
import com.netsflow.unitransit.security.JwtTokenUtil;
import com.netsflow.unitransit.util.OTPUtil;
import com.netsflow.unitransit.util.PasswordUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OTPUtil otpUtil;

    @Autowired
    private PasswordUtil passwordUtil;

    /**
     * Registers a new user. Checks if the email is already registered,
     * generates an OTP, and sends it to the user's email.
     */
    public String signUp(Map<String, Object> userRequest) {
        validateEmailNotRegistered((String) userRequest.get("email"));

        String role = (String) userRequest.get("role");
        User user;

        switch (Role.valueOf(role.toUpperCase())) {
            case ROLE_STUDENT ->
                user = createStudent(userRequest);
            case ROLE_DRIVER ->
                user = createDriver(userRequest);
            case ROLE_ADMIN ->
                user = createAdmin(userRequest);
            default ->
                throw new IllegalArgumentException("Invalid role specified.");
        }

        user.setVerificationCode(otpUtil.generateOTP());
        user.setOtpExpirationTime(LocalDateTime.now().plusMinutes(5));
        user.setPassword(passwordUtil.hashPassword((String) userRequest.get("password")));
        user.setIsVerified(false);

        userRepository.save(user);
        emailService.sendOtpEmail(user.getEmail(), user.getVerificationCode());

        return "Check your email for the OTP.";
    }

    private Student createStudent(Map<String, Object> userRequest) {
        Student student = new Student();
        student.setEmail((String) userRequest.get("email"));
        student.setRole(Role.ROLE_STUDENT);
        student.setName((String) userRequest.get("name"));
        student.setGender((String) userRequest.get("gender"));
        return student;
    }

    private Driver createDriver(Map<String, Object> userRequest) {
        Driver driver = new Driver();
        driver.setEmail((String) userRequest.get("email"));
        driver.setRole(Role.ROLE_DRIVER);
        driver.setName((String) userRequest.get("name"));
        driver.setPhone((String) userRequest.get("phone"));
        driver.setLicense((String) userRequest.get("license"));
        return driver;
    }

    private Admin createAdmin(Map<String, Object> userRequest) {
        Admin admin = new Admin();
        boolean activeAdminExists = userRepository.findByRole(Role.ROLE_ADMIN)
                .stream()
                .anyMatch(user -> user instanceof Admin && ((Admin) user).isActive());
        if (activeAdminExists) {
            throw new IllegalStateException("An active admin account already exists.");
        }
        admin.setEmail((String) userRequest.get("email"));
        admin.setRole(Role.ROLE_ADMIN);
        admin.setActive(true);
        return admin;
    }

    private void validateEmailNotRegistered(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyRegisteredException("Email is already registered.");
        }
    }

    /**
     * Handles user sign-in by checking if the user exists, if the password
     * matches, and if the user is verified. Generates and returns a JWT token
     * on successful login.
     */
    public String signIn(String email, String password, String role) {
        User user = findUserByEmail(email);

        if (!user.getIsVerified()) {
            throw new InvalidCredentialsException("Please verify your email first.");
        }

        if (Role.valueOf(role.toUpperCase()) == Role.ROLE_DRIVER) {
            if (user instanceof Driver driver && !driver.getIsApproved()) {
                throw new InvalidCredentialsException("Your account has not been approved by the admin.");
            }
        }

        System.out.println(password);
        System.out.println(user.getPassword());
        if (!passwordUtil.matchesPassword(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials.");
        }

        return jwtTokenUtil.generateToken(email, role);
    }

    /**
     * Resends the OTP to the user's email. Checks the context (signup or forgot
     * password) to apply specific validations.
     */
    public void resendOtp(String email, String context) {
        User user = findUserByEmail(email);
        validateResendOtpContext(user, context);

        String otp = otpUtil.generateOTP();
        user.setVerificationCode(otp);
        user.setOtpExpirationTime(LocalDateTime.now().plusMinutes(5)); // OTP valid for 5 minutes
        userRepository.save(user);

        emailService.sendOtpEmail(email, otp);
    }

    private void validateResendOtpContext(User user, String context) {
        if ("signup".equalsIgnoreCase(context)) {
            if (user.getIsVerified()) {
                throw new IllegalStateException("Email already verified!");
            }
        } else if ("forgot_password".equalsIgnoreCase(context)) {
            if (!user.getIsVerified()) {
                throw new IllegalStateException("Email not verified yet!");
            }
        } else {
            throw new IllegalArgumentException("Invalid context provided!");
        }
    }

    /**
     * Initiates password reset by generating an OTP, saving it, and sending it
     * to the user.
     */
    public void requestPasswordReset(String email) {
        User user = findUserByEmail(email);

        String otp = otpUtil.generateOTP();
        user.setVerificationCode(otp);
        user.setOtpExpirationTime(LocalDateTime.now().plusMinutes(10)); // OTP valid for 10 minutes
        userRepository.save(user);

        emailService.sendOtpEmail(email, otp);
    }

    /**
     * Resets the user's password if the OTP is valid.
     */
    public void resetPassword(String email, String newPassword) {
        User user = findUserByEmail(email);

        if (passwordUtil.matchesPassword(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as the old password.");
        }

        user.setPassword(passwordUtil.hashPassword(newPassword));
        userRepository.save(user);
    }

    /**
     * Verifies the OTP for either user signup verification or password reset.
     *
     * @param email the email of the user
     * @param otp the OTP to be verified
     * @param context the context of verification ("signup" or
     * "forgot_password")
     * @return JWT token if the context is "signup" and verification is
     * successful
     */
    public String verifyOTP(String email, String otp, String context) {
        User user = findUserByEmail(email);

        // Validate OTP
        if (!otp.equals(user.getVerificationCode())) {
            throw new InvalidOTPException("Invalid OTP entered.");
        }

        if (user instanceof Driver driver && !driver.getIsApproved()) {
            throw new InvalidCredentialsException("Your account has not been approved by the admin.");
        }

        // Check if OTP has expired
        if (user.getOtpExpirationTime().isBefore(LocalDateTime.now())) {
            throw new ExpiredOTPException("OTP has expired.");
        }

        // Handle context-specific logic
        if ("signup".equalsIgnoreCase(context)) {
            user.setIsVerified(true); // Mark the user as verified
            // Generate a JWT token including user's role
            String token = jwtTokenUtil.generateToken(user.getEmail(), user.getRole().toString());
            // Clear OTP-related fields after successful verification
            clearOtpFields(user);
            return token;
        } else if ("forgot_password".equalsIgnoreCase(context)) {
            // Just verify OTP for password reset
            clearOtpFields(user);
            return "OTP verified successfully.";
        } else {
            throw new IllegalArgumentException("Invalid context provided!");
        }
    }

    /**
     * Clears OTP-related fields from the user entity.
     *
     * @param user the user entity to update
     */
    private void clearOtpFields(User user) {
        user.setVerificationCode(null);
        user.setOtpExpirationTime(null);
        userRepository.save(user);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found."));
    }
}
