package org.example.delivery.store.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.sql.Time;
import java.util.List;
import org.example.delivery.auth.model.UserRole;
import org.example.delivery.auth.model.dto.AuthUser;
import org.example.delivery.common.domain.Menu;
import org.example.delivery.common.domain.Store;
import org.example.delivery.common.domain.User;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.BusinessException;
import org.example.delivery.common.exception.base.ConflictException;
import org.example.delivery.common.exception.base.NotFoundException;
import org.example.delivery.menu.repository.MenuRepository;
import org.example.delivery.store.model.dto.request.StoreRequest;
import org.example.delivery.store.model.dto.response.GetStoreByIdResponse;
import org.example.delivery.store.model.dto.response.StoreResponse;
import org.example.delivery.store.repository.StoreRepository;
import org.example.delivery.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

  @Mock
  private StoreRepository storeRepository;

  @Mock
  private MenuRepository menuRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private StoreService storeService;

  private AuthUser authUser;
  private StoreRequest storeRequest;

  @BeforeEach
  void setUp() {
    authUser = new AuthUser(1L, "TestUser", UserRole.OWNER);
    storeRequest = new StoreRequest(
        "Test Store", "01012345678", "123 Main St", "123-45-67890", 10000,
        java.sql.Time.valueOf("08:00:00"), java.sql.Time.valueOf("20:00:00")
    );
  }

  @Test
  void 정상_등록_완료() {
    // given
    User mockUser = mock(User.class);
    when(userRepository.findById(authUser.id())).thenReturn(Optional.of(mockUser));
    when(storeRepository.existsStoreByName(storeRequest.name())).thenReturn(false);
    when(storeRepository.existsStoreByStoreAddress(storeRequest.storeAddress())).thenReturn(false);
    when(storeRepository.countStoreByUserIdAndIsDeletedFalse(authUser.id())).thenReturn(2L);

    // when
    StoreResponse response = storeService.registerStore(authUser, storeRequest);

    // then
    assertThat(response.message()).isEqualTo("가게가 정상적으로 등록 완료되었습니다.");
    verify(storeRepository, times(1)).save(any(Store.class));
  }

  @Test
  void 가게_등록_갯수_초과() {
    // given
    when(storeRepository.countStoreByUserIdAndIsDeletedFalse(authUser.id())).thenReturn(3L);

    // when & then
    assertThatThrownBy(() -> storeService.registerStore(authUser, storeRequest))
        .isInstanceOf(BusinessException.class)
        .extracting("errorCode")
        .isEqualTo(ErrorCode.TOO_MANY_STORES);
  }

  @Test
  void 중복된_이름으로_가게_등록() {
    // given
    when(storeRepository.existsStoreByName(storeRequest.name())).thenReturn(true);

    // when & then
    assertThatThrownBy(() -> storeService.registerStore(authUser, storeRequest))
        .isInstanceOf(ConflictException.class)
        .extracting("errorCode")
        .isEqualTo(ErrorCode.STORE_NAME_ALREADY_EXISTS);
  }

  @Test
  void 중복된_주소로_가게_등록() {
    // given
    when(storeRepository.existsStoreByStoreAddress(storeRequest.storeAddress())).thenReturn(true);

    // when & then
    assertThatThrownBy(() -> storeService.registerStore(authUser, storeRequest))
        .isInstanceOf(ConflictException.class)
        .extracting("errorCode")
        .isEqualTo(ErrorCode.STORE_ADDRESS_ALREADY_EXISTS);
  }

  @Test
  void 유저_찾을_수_없는_경우() {
    // given
    when(userRepository.findById(authUser.id())).thenReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> storeService.registerStore(authUser, storeRequest))
        .isInstanceOf(NotFoundException.class)
        .extracting("errorCode")
        .isEqualTo(ErrorCode.USER_NOT_FOUND);
  }

  @Test
  void 가게_찾을_수_없는_경우() {
    // given
    when(storeRepository.findById(1L)).thenReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> storeService.getStoreById(1L))
        .isInstanceOf(NotFoundException.class)
        .extracting("errorCode")
        .isEqualTo(ErrorCode.STORE_NOT_FOUND);
  }

  @Test
  void 가게_단건_조회(){
    // given
    User mockUser = mock(User.class);
    Store mockStore = new Store(mockUser, "Test Store", "010-0000-0000",
        "서울시 강북구 번동","123-12-12345", 15000,
        Time.valueOf("09:00:00"), Time.valueOf("20:00:00"));
    Menu mockMenu1 = Menu.menuCreateWithTestCode(1L, "Menu1", 10000, mockStore, mockUser, false);
    Menu mockMenu2 = Menu.menuCreateWithTestCode(2L, "Menu2", 12000, mockStore, mockUser, false);
    Menu mockMenu3 = Menu.menuCreateWithTestCode(3L, "Menu3", 13000, mockStore, mockUser, false);
    List<Menu> mockMenus = List.of(mockMenu1, mockMenu2, mockMenu3);
    when(storeRepository.findById(1L)).thenReturn(Optional.of(mockStore));
    when(menuRepository.findByStore_IdAndIsDeleted(1L, false)).thenReturn(mockMenus);

    // when
    GetStoreByIdResponse response = storeService.getStoreById(1L);

    // then
    assertThat(response)
        .extracting("name", "storeNumber", "address", "minOrderPrice",
            "openingTime", "closingTime")
        .containsExactly("Test Store", "010-0000-0000", "서울시 강북구 번동",
            15000, Time.valueOf("09:00:00"), Time.valueOf("20:00:00"));
    assertThat(response.menus()).hasSize(3);
    assertThat(response.menus())
        .extracting("name", "price")
        .containsExactly(
            tuple("Menu1", 10000),
            tuple("Menu2", 12000),
            tuple("Menu3", 13000)
        );
  }
}
