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

  /**
   * 매장 등록 메서드. <br>
   * 1. 인증된 유저 정보 {@code authUser}, 생성 요청 {@code request}를 전달받는다. <br>
   * 2. 유저의 등록 매장 수가 3개 미만인지 확인한다. <br>
   * 3. {@code request}의 매장 이름 혹은 주소가 중복되지 않았는지 확인한다.<br>
   * 4. 모든 검증이 통과됐다면 request 대로 매장을 등록한다.<br>
   * 5. 등록 완료 메세지를 담은 응답 Dto 를 반환한다.
   */
  public StoreResponse registerStore(AuthUser authUser, StoreRequest request) {

    validateStoreLimit(authUser.id());
    validateStoreRequest(request);

    User foundUser = userRepository.findById(authUser.id())
        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

    Store store = Store.makeWith(foundUser, request);
    storeRepository.save(store);

    return new StoreResponse("가게가 정상적으로 등록 완료되었습니다.");
  }


  /**
   * 매장 단건조회 메서드 <br>
   * 1. 가게 고유 아이디 {@code storeId}를 전달받는다. <br>
   * 2. 메뉴 리포지토리에서 가게 아이디로 등록된 메뉴를 찾아 리스트를 만든다. <br>
   * 3. 가게 정보와 메뉴 리스트를 담은 {@code GetStoreByIdResponse}를 반환한다.
   */
  public GetStoreByIdResponse getStoreById(Long storeId) {
    Store foundStore = storeRepository.findStoreByIdAndIsDeletedFalse(storeId).
        orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

    List<MenuResponse> menus = menuRepository.findByStore_IdAndIsDeleted(storeId, false).stream()
        .map(MenuResponse::createMenuResponse).toList();

    return GetStoreByIdResponse.with(foundStore, menus);
  }


  /**
   * 매장 전체조회 메서드 <br>
   * 1. 특별한 값을 전달받지 않는다.<br>
   * 2. 가게 리포지토리에서 {@code isDeleted}가 false인 가게를 모두 찾아 리스트를 만들어 반환한다.
   */
  public List<GetStoresResponse> getAllStores() {

    return storeRepository.findAllByIsDeletedFalse()
        .stream()
        .map(GetStoresResponse::toDto)
        .toList();
  }


  /**
   * 가게 수정 메서드 <br>
   * 1. 가게 고유 아이디{@code storeId}와 요청 Dto{@code request}를 전달받는다.<br>
   * 2. {@code request}의 매장 이름 혹은 주소가 중복되지 않았는지 확인한다.<br>
   * 3. 전달받은 요청 Dto 의 값대로 가게 정보를 수정 후 저장한다.<br>
   * 4. 등록 완료 메세지 담은 응답 Dto 를 반환한다.
   */
  public StoreResponse updateStore(Long storeId, StoreRequest request) {

    validateStoreRequest(request);

    Store foundStore = storeRepository.findById(storeId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

    foundStore.updateWith(request);
    storeRepository.save(foundStore);

    return new StoreResponse("수정이 정상적으로 완료되었습니다.");
  }


  /**
   * 가게 삭제 메서드(soft delete)<br>
   * 1. 가게 ID{@code storeId}를 전달받는다.<br>
   * 2. 리포지토리에서 전달받은 id로 가게를 검색한다.<br>
   * 3. 검색된 가게의 삭제 상태 {@code isDeleted}를 false로 변경한다.<br>
   * 4. 변경된 가게의 상태를 저장한다.
   */
  public void deleteStore(Long storeId) {
    Store foundstore = storeRepository.findById(storeId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

    foundstore.softDelete();
    storeRepository.save(foundstore);
  }


  /**
   * 가게 개수 제한 검증 메서드<br>
   * 1. 유저 고유 아이디{@code userId}를 전달받는다.<br>
   * 2. 리포지토리에서 해당 유저의 아이디로 등록된 가게가 3개 이상인지 확인한다.<br>
   * 3. 3개 이상인 경우 예외를 발생시킨다.
   */
  private void validateStoreLimit(Long userId) {
    if (storeRepository.countStoreByUserIdAndIsDeletedFalse(userId) >= 3) {
      throw new BusinessException(ErrorCode.TOO_MANY_STORES);
    }
  }


  /**
   * 요청 유효성 검증 메서드<br>
   * 1. 요청 Dto{@code request}를 전달받는다.<br>
   * 2. 해당 Dto의 가게명/주소로 이미 등록된 가게가 있는지 확인한다.<br>
   * 3. 있을 경우 각 상황에 맞는 예외를 발생시킨다.
   */
  private void validateStoreRequest(StoreRequest request) {
    if (storeRepository.existsStoreByName(request.name())) {
      throw new ConflictException(ErrorCode.STORE_NAME_ALREADY_EXISTS);
    } else if (storeRepository.existsStoreByStoreAddress(request.storeAddress())) {
      throw new ConflictException(ErrorCode.STORE_ADDRESS_ALREADY_EXISTS);
    }
  }
}
