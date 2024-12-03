package com.netsflow.unitransit.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.netsflow.unitransit.model.SignInRequest;
import com.netsflow.unitransit.model.User;
import com.netsflow.unitransit.service.UserService;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public String registerUser(@RequestBody User user) {
        return userService.signUp(user);
    }

    @PostMapping("/verify-otp")
    public String verifyEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String context = request.get("context");
        return userService.verifyOTP(email, otp, context);

    }

    @PostMapping("/signin")
    public String signIn(@RequestBody SignInRequest signInRequest) {
        return userService.signIn(signInRequest.getEmail(), signInRequest.getPassword(), signInRequest.getRole());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        userService.requestPasswordReset(email);
        return ResponseEntity.ok("Password reset OTP sent to your email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");
        userService.resetPassword(email, newPassword);
        return ResponseEntity.ok("Password reset successfully.");
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String context = request.get("context");

        userService.resendOtp(email, context);
        return ResponseEntity.ok("OTP resent successfully to your email.");
    }

}
