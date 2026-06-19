package com.loginyuk.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter yang jalan di setiap request REST API.
 * Baca header "Authorization: Bearer <token>", validasi,
 * lalu set Authentication ke SecurityContext kalau valid.
 *
 * Principal di-set sebagai org.springframework.security.core.userdetails.User
 * (UserDetails minimal) supaya kompatibel dengan controller yang sudah
 * pakai @AuthenticationPrincipal UserDetails.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (JwtUtil.isValidToken(token)) {
                String username = JwtUtil.extractUsername(token);

                // Authority default "ROLE_USER" — sesuaikan kalau ada role lain
                var authorities = Collections.<GrantedAuthority>singletonList(
                        new SimpleGrantedAuthority("ROLE_USER")
                );

                // UserDetails minimal — password dikosongkan karena tidak relevan di sini
                UserDetails userDetails = new User(username, "", authorities);

                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                System.out.println("JWT tidak valid atau expired: " + request.getRequestURI());
            }
        }

        filterChain.doFilter(request, response);
    }
}
