package com.evaluacion.msvc.users.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil();

    @Test
    void generateAndParseToken_shouldReturnSameEmail() {
        String email = "diego@zegarra.org";
        String token = jwtUtil.generateToken(email);

        assertNotNull(token);
        // Un JWT válido tiene 3 partes
        assertEquals(3, token.split("\\.").length);

        String extracted = jwtUtil.getEmailFromToken(token);
        assertEquals(email, extracted);
    }
}
