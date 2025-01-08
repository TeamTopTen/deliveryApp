package org.example.delivery.store.service;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.delivery.common.domain.Store;
import org.example.delivery.common.domain.User;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.NotFoundException;
import org.example.delivery.store.model.dto.request.StoreRequest;
import org.example.delivery.store.model.dto.response.StoreResponse;
import org.example.delivery.store.repository.StoreRepository;
import org.example.delivery.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class StoreService {

  private StoreRepository storeRepository;
  private UserRepository userRepository;
  private final HttpSession session;

  public StoreResponse createStore(StoreRequest request) {

    Long userId = (Long) session.getAttribute("logged_user");
    User foundUser = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

    Store store = Store.makeWith(foundUser, request);
    storeRepository.save(store);

    return new StoreResponse("가게가 정상적으로 등록 완료되었습니다.");
  }


  public StoreResponse updateStore(Long storeId, StoreRequest request) {

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
}
