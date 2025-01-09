package org.example.delivery.store.service;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.delivery.auth.model.dto.AuthUser;
import org.example.delivery.auth.repository.UserRepository;
import org.example.delivery.common.domain.Store;
import org.example.delivery.common.domain.User;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.BusinessException;
import org.example.delivery.common.exception.base.ConflictException;
import org.example.delivery.common.exception.base.NotFoundException;
import org.example.delivery.menu.model.response.MenuResponse;
import org.example.delivery.menu.repository.MenuRepository;
import org.example.delivery.store.model.dto.request.StoreRequest;
import org.example.delivery.store.model.dto.response.GetStoresResponse;
import org.example.delivery.store.model.dto.response.GetStoreByIdResponse;
import org.example.delivery.store.model.dto.response.StoreResponse;
import org.example.delivery.store.repository.StoreRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class StoreService {

  private StoreRepository storeRepository;
  private UserRepository userRepository;
  private MenuRepository menuRepository;

  public StoreResponse createStore(AuthUser authUser, StoreRequest request) {

    validateStoreLimit(authUser.id());
    validateStoreRequest(request);

    User foundUser = userRepository.findById(authUser.id())
        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

    Store store = Store.makeWith(foundUser, request);
    storeRepository.save(store);

    return new StoreResponse("가게가 정상적으로 등록 완료되었습니다.");
  }

  public GetStoreByIdResponse getStoreById(Long storeId) {
    Store foundStore = storeRepository.findById(storeId).
        orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

    List<MenuResponse> menus = menuRepository.findByStore_IdAndIsDeleted(storeId, false).stream()
        .map(MenuResponse::createMenuResponse).toList();

    return GetStoreByIdResponse.with(foundStore, menus);
  }


  public List<GetStoresResponse> getAllStores() {

    return storeRepository.findAll()
        .stream()
        .map(GetStoresResponse::toDto)
        .toList();
  }


  public StoreResponse updateStore(Long storeId, StoreRequest request) {

    validateStoreRequest(request);

    Store foundStore = storeRepository.findById(storeId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

    foundStore.updateWith(request);
    storeRepository.save(foundStore);

    return new StoreResponse("수정이 정상적으로 완료되었습니다.");
  }


  public void deleteStore(Long storeId) {
    Store foundstore = storeRepository.findById(storeId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

    foundstore.softDelete();
    storeRepository.save(foundstore);
  }

  public void reOpenStore(AuthUser authUser, Long storeId) {
    validateStoreLimit(authUser.id());
    Store foundStore = storeRepository.findById(storeId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

    foundStore.reOpenStore();
  }

  private void validateStoreLimit(Long userId) {
    if (storeRepository.countStoreByUserIdAndDeletedFalse(userId) >= 3) {
      throw new BusinessException(ErrorCode.TOO_MANY_STORES);
    }
  }


  private void validateStoreRequest(StoreRequest request) {
    if (storeRepository.existsStoreByName(request.name())) {
      throw new ConflictException(ErrorCode.STORE_NAME_ALREADY_EXISTS);
    } else if (storeRepository.existsStoreByStoreAddress(request.storeAddress())) {
      throw new ConflictException(ErrorCode.STORE_ADDRESS_ALREADY_EXISTS);
    }
  }
}
