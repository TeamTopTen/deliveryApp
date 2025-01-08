package org.example.delivery.store.proxy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.delivery.auth.Annotation.Auth;
import org.example.delivery.auth.model.dto.AuthUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/stores")
@RequiredArgsConstructor
@Slf4j
public class ProxystoreController {

  private final ProxystoreService proxystoreService;

  @PostMapping()
  public ResponseEntity<String> test(@Auth AuthUser authUser) {

    log.info("user_email:{}",authUser.email());
    proxystoreService.test(authUser.email());

    return ResponseEntity.ok().body("성공");
  }

}
