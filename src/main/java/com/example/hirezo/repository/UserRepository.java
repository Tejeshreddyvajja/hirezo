package com.example.hirezo.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.example.hirezo.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByEmail(String email);

	boolean existsByEmail(String email);


}
