package com.netsflow.unitransit.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class OTPUtil {

    /**
     * Generates a 6-digit OTP.
     */
    public String generateOTP() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000); // Generate a 6-digit OTP
        return String.valueOf(otp);
    }
}
