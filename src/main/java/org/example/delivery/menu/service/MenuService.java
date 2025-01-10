package org.example.delivery.menu.service;


import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.delivery.auth.repository.UserRepository;
import org.example.delivery.common.domain.Menu;
import org.example.delivery.common.domain.Store;
import org.example.delivery.common.domain.User;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.AccessDeniedException;
import org.example.delivery.common.exception.base.NotFoundException;
import org.example.delivery.menu.model.request.MenuRequest;
import org.example.delivery.menu.model.response.MenuResponse;
import org.example.delivery.menu.repository.MenuRepository;
import org.example.delivery.store.repository.StoreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {

  private final MenuRepository menuRepository;
  private final UserRepository userRepository;
  private final StoreRepository storeRepository;

  @Transactional
  public void createMenu(MenuRequest request, String email,Long storeId) {

    User needCheckUser = userRepository.findUsersByEmail(email)
        .orElseThrow(() -> new NotFoundException(ErrorCode.MENU_NOT_FOUND));

    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.MENU_NOT_FOUND));

    Menu.ownerCheck(needCheckUser);
    Menu.storeCheck(needCheckUser,store);

    Menu menu = Menu.menuCreate(request.getName(), request.getPrice(), store , needCheckUser );

    menuRepository.save(menu);
  }

  @Transactional
  public List<MenuResponse> findMenu(Long storeId) {

    storeRepository.findById(storeId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.MENU_NOT_FOUND));

    List<Menu> findMenuList = menuRepository.findByStore_IdAndIsDeleted(storeId,false);

    return MenuResponse.createMenuResponseList(findMenuList);
  }

  @Transactional
  public MenuResponse putMenu(Long id,MenuRequest request,String email) {

    Menu checkMenu = menuRepository.findByIdOrElseThrow(id);

    menuRepository.checkDeleteById(id);

    crossCheckUser(email, checkMenu);

    Menu menu = Menu.menuPut(
        checkMenu.getId(),
        request.getName(),
        request.getPrice(),
        checkMenu.getStore(),
        checkMenu.getUser()
    );

    menuRepository.save(menu);

    return MenuResponse.createMenuResponse(menu);
  }

  @Transactional
  public void softDeleteMenu(Long id,String email) {

    Menu checkMenu = menuRepository.findByIdOrElseThrow(id);

    crossCheckUser(email, checkMenu);

    menuRepository.checkDeleteById(id);

    checkMenu.setDeleted(true);
  }

  private void crossCheckUser(String email, Menu checkMenu) {
    if(!(checkMenu.getUser()==userRepository.findUsersByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)))) {
      throw new AccessDeniedException(ErrorCode.MENU_ACCESS_DENIED);
    }
  }
}

