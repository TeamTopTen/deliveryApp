package org.example.delivery.store.proxy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.delivery.auth.repository.UserRepository;
import org.example.delivery.common.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProxystoreService {

  private final ProxyStoreRepository proxyStoreRepository;
  private final UserRepository userRepository;

  public void test(String email) {
    log.info("1");
    User user = userRepository.findUsersByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    log.info("2");
    ProxyStore test = new ProxyStore("가게",user);
    log.info("3");
    proxyStoreRepository.save(test);
    log.info("4");
  }

}
