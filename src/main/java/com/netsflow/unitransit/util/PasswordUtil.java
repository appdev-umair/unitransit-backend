package com.netsflow.unitransit.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Hashes the password using BCrypt.
     */
    public String hashPassword(String password) {
        return encoder.encode(password);
    }

    /**
     * Checks if the provided password matches the hashed password.
     */
    public boolean matchesPassword(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
}
