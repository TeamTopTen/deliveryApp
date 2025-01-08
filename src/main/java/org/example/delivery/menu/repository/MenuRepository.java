package org.example.delivery.menu.repository;

import java.util.List;
import java.util.Optional;
import org.example.delivery.common.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface MenuRepository extends JpaRepository<Menu, Long> {

  List<Menu> findByStore_Id(Long storeId);

  Optional<Menu> findById();

  default Menu findByIdOrElseThrow(Long id) {
    return findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

}
