package com.netsflow.unitransit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.netsflow.unitransit.domain.Role;
import com.netsflow.unitransit.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationCode(String verificationCode);
    List<User> findByRole(Role role); 

}
