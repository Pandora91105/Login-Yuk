package com.loginyuk.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import javax.crypto.SecretKey;

/**
 * Util untuk validasi dan extract claim dari JWT.
 * SECRET_KEY harus SAMA PERSIS dengan yang dipakai di
 * TestTokenController dan JwtHandshakeInterceptor.
 */
public class JwtUtil {

    private static final String SECRET_KEY = "your-secret-key-here-must-be-long-enough";

    private static final SecretKey KEY =
            Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    /**
     * Validasi token. Return true kalau signature & expiry valid.
     */
    public static boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Ambil semua claims dari token. Panggil isValidToken() dulu
     * sebelum ini kalau belum yakin token-nya valid.
     */
    public static Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public static Long extractUserId(String token) {
        Object userId = extractClaims(token).get("userId");
        if (userId == null) return null;
        // JWT numeric claim biasanya ke-decode sebagai Integer/Long tergantung library
        return Long.valueOf(userId.toString());
    }
}
