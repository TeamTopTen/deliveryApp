package org.example.delivery.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.user.model.UserRole;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User extends BaseEntity{

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

  public void updateUser(String name, String password, String address) { //TODO passwordEncoder 추가 후 변경 예정
    this.name = name;
    this.password = password;
    this.address = address;
  }

  public void deleteUser() {
    this.isDeleted = true;
  }
}
