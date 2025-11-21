package com.evaluacion.msvc.users.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evaluacion.msvc.users.DTO.UserDTO;
import com.evaluacion.msvc.users.DTO.UserResponseDTO;
import com.evaluacion.msvc.users.entities.User;
import com.evaluacion.msvc.users.services.IUserService;
import com.evaluacion.msvc.users.services.UserServiceImpl;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/all_users")
    public ResponseEntity<List> findAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAllUsers());
    }

    @GetMapping("/find_user_by_id/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUserById(id));
    }

    @GetMapping("/find_user_by_email/{email}")
    public ResponseEntity<UserResponseDTO> findUserByEmail(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUserByEmail(email));
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> postMethodName(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDTO));
    }

    @PutMapping("/update_user_by_id/{id}")
    public ResponseEntity<UserResponseDTO> updateUserById(@PathVariable UUID id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateById(id, userDTO));
    }

    @PutMapping("/update_user_by_email/{email}")
    public ResponseEntity<UserResponseDTO> updateUserByEmail(@PathVariable String email, @RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateByEmail(email, userDTO));
    }

    @DeleteMapping("/delete_by_user_id/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable UUID id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete_by_user_email/{email}")
    public ResponseEntity<Void> deleteUserByEmail(@PathVariable String email) {
        userService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }

    // PATCH — Actualización parcial ID
    @PatchMapping("/partial_update_by_id/{id}")
    public ResponseEntity<UserResponseDTO> partialUpdate(@PathVariable UUID id,
            @RequestBody Map<String, Object> fields) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.partialUpdateById(id, fields));
    }

    // PATCH — Actualización parcial EMAIL
    @PatchMapping("/partial_update_by_email/{email}")
    public ResponseEntity<UserResponseDTO> partialUpdate(@PathVariable String email,
            @RequestBody Map<String, Object> fields) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.partialUpdateByEmail(email, fields));
    }
}
