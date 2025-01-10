package org.example.delivery.common.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.example.delivery.common.domain.enums.UserRole;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {
  private static final String BEARER_PREFIX = "Bearer ";
  private static final long TOKEN_TIME = 60 * 60 * 1000L;

  @Value("${jwt.secret.key}")
  private String secretKey;
  private Key key;
  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  @PostConstruct
  public void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    key = Keys.hmacShaKeyFor(bytes);
  }

  public String createToken(Long userId, String email, UserRole userRole) {
    Date date = new Date();

    return BEARER_PREFIX +
        Jwts.builder()
            .setSubject(String.valueOf(userId))
            .claim("email", email)
            .claim("userRole", userRole)
            .setExpiration(new Date(date.getTime() + TOKEN_TIME))
            .setIssuedAt(date)
            .signWith(key, signatureAlgorithm) // 암호화 알고리즘
            .compact();
  }

  public String substringToken(HttpServletRequest request) {

    String bearerToken = request.getHeader("Authorization");

    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    throw new RuntimeException("토큰을 찾을 수 없습니다.");
  }

  public Claims extractClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}
