package org.example.delivery.menu.service;


import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.delivery.common.domain.Menu;
import org.example.delivery.common.domain.User;
import org.example.delivery.menu.model.request.MenuRequest;
import org.example.delivery.menu.model.response.MenuResponse;
import org.example.delivery.menu.repository.MenuRepository;
import org.example.delivery.store.ProxyStore;
import org.example.delivery.store.ProxyStoreRepository;
import org.example.delivery.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MenuService {

  private MenuRepository menuRepository;
  private UserRepository userRepository;
  private ProxyStoreRepository proxyStoreRepository;

  @Transactional
  public void createMenu(MenuRequest request, String email) {

    User needCheckUser = userRepository.findByEmailOrElesThrow(request.getEmail());

    crossCheckEmail(email, needCheckUser);

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

    List<Menu> byStoreId = menuRepository.findByStore_Id(storeId);

    return MenuResponse.createMenuResponseList(byStoreId);
  }

  @Transactional
  public MenuResponse putMenu(Long id,MenuRequest request,String email) {

    Menu checkMenu = menuRepository.findByIdOrElseThrow(id);
    crossCheckUser(email, checkMenu);

    checkMenu.setName(request.getName());
    checkMenu.setPrice(request.getPrice());

    return MenuResponse.createMenuResponse(request.getName(),request.getPrice());
  }

  private void crossCheckEmail(String email, User needCheckUser) {
    if(!(needCheckUser.getEmail().equals(email))) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  private void crossCheckUser(String email, Menu checkMenu) {
    if(!(checkMenu.getUser()==userRepository.findByEmailOrElesThrow(email))) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

}
