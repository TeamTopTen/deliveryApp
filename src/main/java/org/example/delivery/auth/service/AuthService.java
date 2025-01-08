package org.example.delivery.auth.service;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.example.delivery.auth.model.request.DeregisterRequest;
import org.example.delivery.auth.model.request.LoginRequest;
import org.example.delivery.auth.model.response.LoginResponse;
import org.example.delivery.common.config.encode.PasswordEncoder;
import org.example.delivery.common.config.jwt.JwtUtil;
import org.example.delivery.common.domain.User;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.AuthException;
import org.example.delivery.common.exception.base.ConflictException;
import org.example.delivery.auth.model.UserRole;
import org.example.delivery.auth.model.request.RegisterRequest;
import org.example.delivery.auth.model.response.RegisterResponse;
import org.example.delivery.auth.repository.UserRepository;
import org.example.delivery.common.exception.base.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  //회원 생성
  @Transactional
  public RegisterResponse register(RegisterRequest request) {

    if (userRepository.existsByEmail(request.email())) {
      throw new ConflictException(ErrorCode.USER_CONFLICT);
    }

    UserRole userRole = UserRole.of(request.userRole());

    String encodePassword = passwordEncoder.encode(request.password());

    User newUser = new User(
        request.email(),
        encodePassword,
        request.name(),
        request.phoneNumber(),
        request.address(),
        userRole //사용자 입력에 따라 사용자와 사장 권한이 주어짐
    );

    User savedUser = userRepository.save(newUser);

    return new RegisterResponse(savedUser.getId(), savedUser.getName(), savedUser.getUserRole());
  }

  // 로그인
  public LoginResponse authenticate(LoginRequest request) {
    User user = userRepository.findUsersByEmail(request.email())
        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new AuthException(ErrorCode.AUTHENTICATION_FAILED);
    }

    String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());

    return new LoginResponse(bearerToken);
  }

  // 회원 삭제
  @Transactional
  public void deregister(DeregisterRequest request, String email) {

    System.out.println(email);
    User user = userRepository.findUsersByEmail(email)
        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new AuthException(ErrorCode.AUTHENTICATION_FAILED);
    }
    user.deleteUser();
  }
}
