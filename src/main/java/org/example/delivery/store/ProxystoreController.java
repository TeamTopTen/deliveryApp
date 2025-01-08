package org.example.delivery.store;

import lombok.RequiredArgsConstructor;
import org.example.delivery.auth.Annotation.Auth;
import org.example.delivery.auth.model.dto.AuthUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/stores")
@RequiredArgsConstructor
public class ProxystoreController {

  ProxystoreService proxystoreService;

  @PostMapping()
  public ResponseEntity<String> test(@Auth AuthUser authUser) {

    proxystoreService.test(authUser.email());

    return ResponseEntity.ok().body("성공");
  }

}
