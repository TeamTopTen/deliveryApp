package org.example.delivery.store;

import java.util.List;
import org.example.delivery.common.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProxyStoreRepository extends JpaRepository<ProxyStore, Long> {

  ProxyStore findByEmail(String email);
}
