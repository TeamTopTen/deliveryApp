package org.example.delivery.menu.repository;

import java.util.List;
import org.example.delivery.common.domain.entity.Menu;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

  List<Menu> findByStore_IdAndIsDeleted(Long storeId, Boolean isDeleted);

  default Menu findByIdOrElseThrow(Long id) {
    return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.MENU_NOT_FOUND));
  }

  default void checkDeleteById(Long id) {
    if(findByIdOrElseThrow(id).getIsDeleted()) {
      throw new NotFoundException(ErrorCode.MENU_NOT_FOUND);
    }
  }
}
