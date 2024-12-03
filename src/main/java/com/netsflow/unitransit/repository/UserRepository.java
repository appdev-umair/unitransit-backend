package com.netsflow.unitransit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.netsflow.unitransit.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationCode(String verificationCode);
}
