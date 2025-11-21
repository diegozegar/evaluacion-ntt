package com.evaluacion.msvc.users.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.evaluacion.msvc.users.repositories.IUserRepository;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final IUserRepository userRepository;

    public JwtFilter(JwtUtil jwtUtil, IUserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // Si no hay token o es incorrecto, sigue sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        try {
            final String email = jwtUtil.getEmailFromToken(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                var user = userRepository.findByCorreo(email).orElse(null);

                if (user != null) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user, null, java.util.Collections.emptyList());

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            }
        } catch (Exception e) {
            System.out.println("Token inválido: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}