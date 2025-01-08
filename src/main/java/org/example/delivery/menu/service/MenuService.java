package org.example.delivery.menu.service;


import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.delivery.auth.repository.UserRepository;
import org.example.delivery.common.domain.Menu;
import org.example.delivery.common.domain.User;
import org.example.delivery.menu.model.request.MenuRequest;
import org.example.delivery.menu.model.response.MenuResponse;
import org.example.delivery.menu.repository.MenuRepository;
import org.example.delivery.store.proxy.ProxyStore;
import org.example.delivery.store.proxy.ProxyStoreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MenuService {

  private final MenuRepository menuRepository;
  private final UserRepository userRepository;
  private final ProxyStoreRepository proxyStoreRepository;

  @Transactional
  public void createMenu(MenuRequest request, String email) {

    User needCheckUser = userRepository.findUsersByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    ProxyStore proxyStore = proxyStoreRepository.findById(request.getStoreId())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND));

    Menu.ownerCheck(needCheckUser);
    Menu.storeCheck(needCheckUser,proxyStore);
    Menu menu = Menu.menuCreate(request.getName(), request.getPrice(), needCheckUser, proxyStore);
    menuRepository.save(menu);
  }

  @Transactional
  public List<MenuResponse> findMenu(Long storeId) {

    List<Menu> findMenuList = menuRepository.findByStore_IdAndIsDeleted(storeId,false);

    return MenuResponse.createMenuResponseList(findMenuList);
  }

  @Transactional
  public MenuResponse putMenu(Long id,MenuRequest request,String email) {

    Menu checkMenu = menuRepository.findByIdOrElseThrow(id);
    menuRepository.checkDeleteById(id);
    crossCheckUser(email, checkMenu);

    Menu menu = Menu.menuCreate(request.getName(), request.getPrice(), checkMenu.getUser(), checkMenu.getStore());
    menuRepository.save(menu);

    return MenuResponse.createMenuResponse(request.getName(),request.getPrice());
  }

  @Transactional
  public void softDeleteMenu(Long id,String email) {

    Menu checkMenu = menuRepository.findByIdOrElseThrow(id);
    crossCheckEmail(email,checkMenu.getUser());

    menuRepository.checkDeleteById(id);

    crossCheckUser(email, checkMenu);
    checkMenu.setDeleted(true);
  }

  private void crossCheckEmail(String email, User needCheckUser) {
    if(!(needCheckUser.getEmail().equals(email))) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  private void crossCheckUser(String email, Menu checkMenu) {
    if(!(checkMenu.getUser()==userRepository.findUsersByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)))) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }
}

