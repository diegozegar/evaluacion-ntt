package com.evaluacion.msvc.users.repositories;

import com.evaluacion.msvc.users.entities.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired private IUserRepository repository;

    @Test
    void saveAndFindByCorreo() {
        User u = new User();
        u.setNombre("Diego Test");
        u.setCorreo("diego@test.com");
        u.setContraseña("password");
        u.setActive(true);

        repository.save(u);

        Optional<User> found = repository.findByCorreo("diego@test.com");
        assertTrue(found.isPresent());
        assertEquals("diego@test.com", found.get().getCorreo());
    }
}
