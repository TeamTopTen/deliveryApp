package org.example.delivery.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.delivery.auth.model.request.DeregisterRequest;
import org.example.delivery.auth.model.request.LoginRequest;
import org.example.delivery.auth.model.request.RegisterRequest;
import org.example.delivery.auth.model.response.LoginResponse;
import org.example.delivery.auth.model.response.RegisterResponse;
import org.example.delivery.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
    RegisterResponse response = authService.register(request);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    LoginResponse response = authService.authenticate(request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping("/deregister")
  public ResponseEntity<Void> deregister(
      @Valid @RequestBody DeregisterRequest request,
      HttpServletRequest httpRequest) { //TODO jwt 전용 익셉션 추가
    String email = (String) httpRequest.getAttribute("email");
    authService.deregister(request, email);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
