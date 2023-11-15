package com.trackmycar.trackmycarbackend.repository;

import com.trackmycar.trackmycarbackend.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> {
    @Override
    Optional<AppUser> findById(Integer integer);

    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByEmail(String email);
}
