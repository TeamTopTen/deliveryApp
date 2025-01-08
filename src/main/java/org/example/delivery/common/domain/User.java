package org.example.delivery.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.common.config.encode.PasswordEncoder;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.AuthException;
import org.example.delivery.auth.model.UserRole;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@Entity
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "phone_number", nullable = false)
  private Integer phoneNumber;

  @Column(name = "address", nullable = false)
  private String address;

  @Column(name = "user_role", nullable = false)
  @Enumerated(EnumType.STRING)
  private UserRole userRole;
  private boolean isDeleted = Boolean.FALSE; // 삭제 여부 기본값 false 설정

  public User(String email, String password, String name, Integer phoneNumber, String address,
      UserRole userRole) {
    this.email = email;
    this.password = password;
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.userRole = userRole;
  }

  // 비밀번호 검증 메서드
  public boolean matches(String password, PasswordEncoder passwordEncoder) {
    if (!passwordEncoder.matches(password, this.password)) {
      throw new AuthException(ErrorCode.AUTHENTICATION_FAILED);
    }
    return false;
  }

  public void deleteUser() {
    this.isDeleted = true;
  }
}
