package com.evaluacion.msvc.users.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evaluacion.msvc.users.entities.User;

public interface IUserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByCorreo(String email);

    void deleteByCorreo(String email);

}
