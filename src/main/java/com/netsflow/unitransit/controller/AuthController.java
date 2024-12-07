// AuthController.java
package com.netsflow.unitransit.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netsflow.unitransit.dto.SignInRequest;
import com.netsflow.unitransit.service.AuthService;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public String registerUser(@RequestBody Map<String, Object> userRequest) {
        return authService.signUp(userRequest);
    }

    @PostMapping("/signin")
    public String signIn(@RequestBody SignInRequest signInRequest) {
        return authService.signIn(signInRequest.getEmail(), signInRequest.getPassword(), signInRequest.getRole());
    }

    @PostMapping("/verify-otp")
    public String verifyEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String context = request.get("context");
        return authService.verifyOTP(email, otp, context);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        authService.requestPasswordReset(email);
        return ResponseEntity.ok("Password reset OTP sent to your email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");
        authService.resetPassword(email, newPassword);
        return ResponseEntity.ok("Password reset successfully.");
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String context = request.get("context");
        authService.resendOtp(email, context);
        return ResponseEntity.ok("OTP resent successfully to your email.");
    }
}
