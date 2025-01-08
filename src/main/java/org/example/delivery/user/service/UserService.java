package org.example.delivery.user.service;

import lombok.RequiredArgsConstructor;
import org.example.delivery.common.config.encode.PasswordEncoder;
import org.example.delivery.common.domain.User;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.ConflictException;
import org.example.delivery.user.model.UserRole;
import org.example.delivery.user.model.request.RegisterRequest;
import org.example.delivery.user.model.response.RegisterResponse;
import org.example.delivery.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

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

  // 로그아웃

  // 회원 삭제

}
