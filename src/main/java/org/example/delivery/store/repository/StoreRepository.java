package org.example.delivery.store.repository;

import java.util.List;
import java.util.Optional;
import org.example.delivery.common.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

  long countStoreByUserIdAndIsDeletedFalse(long userId);

  boolean existsStoreByName(String name);
  boolean existsStoreByStoreAddress(String address);
  Optional<Store> findStoreByIdAndIsDeletedFalse(long id);
  List<Store> findAllByIsDeletedFalse();

}
