package org.example.delivery.store;

import lombok.RequiredArgsConstructor;
import org.example.delivery.auth.repository.UserRepository;
import org.example.delivery.common.domain.Store;
import org.example.delivery.common.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProxystoreService {

  private ProxyStoreRepository proxyStoreRepository;
  private UserRepository userRepository;

  public void test(String email) {
    User user = userRepository.findUsersByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    ProxyStore test = new ProxyStore("가게",user);
    proxyStoreRepository.save(test);
  }
}
