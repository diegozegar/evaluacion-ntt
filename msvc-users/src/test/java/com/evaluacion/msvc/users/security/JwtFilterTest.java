package com.evaluacion.msvc.users.security;

import com.evaluacion.msvc.users.entities.User;
import com.evaluacion.msvc.users.repositories.IUserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.*;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtFilterTest {

    @Mock private JwtUtil jwtUtil;
    @Mock private IUserRepository userRepository;
    @InjectMocks private JwtFilter jwtFilter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void doFilterInternal_noAuthorizationHeader_shouldProceedWithoutAuth() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        jwtFilter.doFilterInternal(request, response, chain);

        // No exception thrown and status is default 200 for MockFilterChain
        assertEquals(200, response.getStatus());
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void doFilterInternal_withValidToken_shouldAuthenticateAndProceed() throws Exception {
        String token = "header.payload.signature";
        String email = "diego@test.com";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        when(jwtUtil.getEmailFromToken(token)).thenReturn(email);
        User user = new User();
        user.setCorreo(email);
        when(userRepository.findByCorreo(email)).thenReturn(Optional.of(user));

        jwtFilter.doFilterInternal(request, response, chain);

        assertEquals(200, response.getStatus());
        verify(jwtUtil).getEmailFromToken(token);
        verify(userRepository).findByCorreo(email);
    }

    @Test
    void doFilterInternal_withInvalidToken_shouldCatchExceptionAndProceed() throws Exception {
        String token = "bad-token";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        when(jwtUtil.getEmailFromToken(token)).thenThrow(new RuntimeException("Invalid"));

        jwtFilter.doFilterInternal(request, response, chain);

        // debe no propagar excepción
        assertEquals(200, response.getStatus());
        verify(jwtUtil).getEmailFromToken(token);
    }
}
