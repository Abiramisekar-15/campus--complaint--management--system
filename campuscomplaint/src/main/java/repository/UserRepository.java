package com.smart.campuscomplaint.repository;

import com.smart.campuscomplaint.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional <User> findByEmail(String email);

}
