package org.example.delivery.menu.repository;

import java.util.List;
import org.example.delivery.common.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

  List<Menu> findByStore_Id(Long storeId);

}
