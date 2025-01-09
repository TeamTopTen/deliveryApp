package org.example.delivery.store.repository;

import java.util.List;
import org.example.delivery.common.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

  long countStoreByUserIdAndIsDeletedFalse(long userId);

  boolean existsStoreByName(String name);
  boolean existsStoreByStoreAddress(String address);
  List<Store> findAllByIsDeletedFalse();

}
