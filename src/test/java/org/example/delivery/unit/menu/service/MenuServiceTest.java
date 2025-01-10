package org.example.delivery.unit.menu.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.delivery.auth.model.UserRole;
import org.example.delivery.auth.repository.UserRepository;
import org.example.delivery.common.domain.Menu;
import org.example.delivery.common.domain.Store;
import org.example.delivery.common.domain.User;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.AccessDeniedException;
import org.example.delivery.common.exception.base.InvalidRequestException;
import org.example.delivery.common.exception.base.NotFoundException;
import org.example.delivery.menu.model.request.MenuRequest;
import org.example.delivery.menu.model.response.MenuResponse;
import org.example.delivery.menu.repository.MenuRepository;
import org.example.delivery.menu.service.MenuService;
import org.example.delivery.store.repository.StoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

  private  static final int ONE_TIME =1;

  @InjectMocks
  MenuService menuService;

  @Mock
  MenuRepository menuRepository;
  @Mock
  UserRepository userRepository;
  @Mock
  StoreRepository storeRepository;

  @Test
  void 메뉴_생성_성공_테스트() {
    //given
    MenuRequest request = new MenuRequest("test1@test.com","맛있는 음식",1000);

    String email = "test1@test.com";
    Long storeId = 1L;
    User user = new User("test1@test.com",
                  "Test1234!@#$",
                  "test",
                  "0101111111",
                  "testaddress",
                  UserRole.OWNER);

    Store store = new Store(user,"맛나식당","01011111111","한국 어딘가","111-11-11111",
        100, Time.valueOf(LocalTime.now()),Time.valueOf(LocalTime.now()));

    Menu menu = Menu.menuCreate("맛있는 음식",1000,store,user);
    when(menuRepository.save(any(Menu.class))).thenReturn(menu); // 함수가 호출 되었는지 검증
    when(userRepository.findUsersByEmail(email)).thenReturn(Optional.of(user));
    when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

    //when
    menuService.createMenu(request,"test1@test.com",storeId);

    //then
    verify(menuRepository,times(ONE_TIME)).save(any(Menu.class));
  }

  @Test
  void 메뉴_생성_실패_테스트_유저일때() {
    //given
    MenuRequest request = new MenuRequest("test1@test.com","맛있는 음식",1000);

    String email = "test1@test.com";
    Long storeId = 1L;
    User user = new User("test1@test.com", "Test1234!@#$", "test", "0101111111", "testaddress",
        UserRole.USER);
    Store store = new Store(user,"맛나식당","01011111111","한국 어딘가","111-11-11111",
        100, Time.valueOf(LocalTime.now()),Time.valueOf(LocalTime.now()));

    Menu menu = Menu.menuCreate("맛있는 음식",1000,store,user);
    //when(menuRepository.save(any(Menu.class))).thenReturn(menu); // 함수가 호출 되었는지 검증
    when(userRepository.findUsersByEmail(email)).thenReturn(Optional.of(user));
    when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

    //when&then
    Assertions.assertThrows(InvalidRequestException.class,() -> {
      menuService.createMenu(request,email,storeId);
    });
    verify(menuRepository, never()).save(any(Menu.class));

  }

  @Test
  void 메뉴_생성_실패_테스트_자신의_가게가_아닐때() {
    //given
    MenuRequest request = new MenuRequest("test1@test.com","맛있는 음식",1000);

    String email = "test1@test.com";
    Long storeId = 2L;
    User user = new User("test1@test.com", "Test1234!@#$", "test", "0101111111", "testaddress",
        UserRole.OWNER);
    User testuser = new User("test12@test.com", "Test1234!@#$", "test", "0101111111", "testaddress",
        UserRole.OWNER);
    Store store = new Store(testuser,"맛나식당","01011111111","한국 어딘가","111-11-11111",
        100, Time.valueOf(LocalTime.now()),Time.valueOf(LocalTime.now()));

    Menu menu = Menu.menuCreate("맛있는 음식",1000,store,user);

    when(userRepository.findUsersByEmail(email)).thenReturn(Optional.of(user));
    when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

    //when&then
    InvalidRequestException invalidRequestException = Assertions.assertThrows(
        InvalidRequestException.class, () -> {
          menuService.createMenu(request, email, storeId);
        });

    Assertions.assertEquals(ErrorCode.Menu_BAD_REQUEST.getMessage(),invalidRequestException.getMessage());
    verify(menuRepository,never()).save(menu);
  }

  @Test
  void 메뉴_조회_성공_테스트() {
    //given
    Long storeId = 1L;
    User user = new User("test1@test.com", "Test1234!@#$", "test", "0101111111", "testaddress",
        UserRole.OWNER);
    Store store = new Store(user,"맛나식당","01011111111","한국 어딘가","111-11-11111",
        100, Time.valueOf(LocalTime.now()),Time.valueOf(LocalTime.now()));

    Menu menu = Menu.menuCreate("맛있는 음식",1000,store,user);
    List<Menu> findMenuList = new ArrayList<>();
    findMenuList.add(menu);

    when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
    when(menuRepository.findByStore_IdAndIsDeleted(storeId,false)).thenReturn(findMenuList);

    //when
    menuService.findMenu(storeId);

    //then
    org.assertj.core.api.Assertions.assertThat(menu.getId()).isEqualTo(findMenuList.get(0).getId());
  }

  @Test
  void 메뉴_조회_실패_가게_정보가_없을때_테스트() {
    //given
    Long storeId = 2L;
    User user = new User("test1@test.com", "Test1234!@#$", "test", "0101111111", "testaddress",
        UserRole.OWNER);
    Store store = new Store(user,"맛나식당","01011111111","한국 어딘가","111-11-11111",
        100, Time.valueOf(LocalTime.now()),Time.valueOf(LocalTime.now()));
    Store teststore = null;
    Menu menu = Menu.menuCreate("맛있는 음식",1000,store,user);
    List<Menu> findMenuList = new ArrayList<>();
    findMenuList.add(menu);

    when(storeRepository.findById(storeId)).thenReturn(Optional.ofNullable(teststore));

    //when&then
    NotFoundException notFoundException = Assertions.assertThrows(NotFoundException.class, () -> {
      menuService.findMenu(storeId);
    });

    org.assertj.core.api.Assertions.assertThat(menu.getStore().getId()).isNotEqualTo(storeId); // isNotEqualTo 널 값일때 받기 위해
    Assertions.assertEquals(ErrorCode.MENU_NOT_FOUND.getMessage(),notFoundException.getMessage());
    verify(menuRepository,never()).findByStore_IdAndIsDeleted(storeId,false);
  }

  @Test
  public void 메뉴_수정_성공_테스트() {
    //given
    MenuRequest request = new MenuRequest("test1@test.com","수정된 맛있는 음식",2000);
    Long menuId = 1L;
    String email = "test1@test.com";
    User user = new User("test1@test.com", "Test1234!@#$", "test", "0101111111", "testaddress",
        UserRole.OWNER);
    Store store = new Store(user,"맛나식당","01011111111","한국 어딘가","111-11-11111",
        100, Time.valueOf(LocalTime.now()),Time.valueOf(LocalTime.now()));
    Menu menu = Menu.menuCreateWithTestCode(menuId,"맛있는 음식",1000,store,user,false);
    Menu testMenu = Menu.menuCreateWithTestCode(menuId,"수정된 맛있는 음식",2000,store,user,false);

    MenuResponse menuResponse = MenuResponse.createMenuResponse(testMenu);

    when(menuRepository.findByIdOrElseThrow(menuId)).thenReturn(menu);

    doAnswer(invocation -> {
      Long id = invocation.getArgument(0);
      if (menu.getIsDeleted()) {
        throw new NotFoundException(ErrorCode.MENU_NOT_FOUND);
      }
      return null;
    }).when(menuRepository).checkDeleteById(menuId);

    when(menuRepository.save(any(Menu.class))).thenReturn(testMenu);
    when(userRepository.findUsersByEmail(email)).thenReturn(Optional.of(user));

    //when
    menuService.putMenu(menuId,request,email);

    //then
    verify(menuRepository,times(ONE_TIME)).save(any(Menu.class));
    org.assertj.core.api.Assertions.assertThat(menuResponse.getName()).isEqualTo(request.getName());
    org.assertj.core.api.Assertions.assertThat(menuResponse.getPrice()).isEqualTo(request.getPrice());

  }

  @Test
  public void 메뉴_수정_실패_CASE_menuId가_조회가_안될때() {
    MenuRequest request = new MenuRequest("test1@test.com","수정된 맛있는 음식",2000);
    Long menuId = 1L;
    Long testmenuId = 2L;
    String email = "test1@test.com";
    User user = new User("test1@test.com", "Test1234!@#$", "test", "0101111111", "testaddress",
        UserRole.OWNER);
    Store store = new Store(user,"맛나식당","01011111111","한국 어딘가","111-11-11111",
        100, Time.valueOf(LocalTime.now()),Time.valueOf(LocalTime.now()));
    Menu menu = Menu.menuCreateWithTestCode(menuId,"맛있는 음식",1000,store,user,false);

    when(menuRepository.findByIdOrElseThrow(testmenuId)).thenThrow(new NotFoundException(ErrorCode.MENU_NOT_FOUND));

    //when
    NotFoundException notFoundException = Assertions.assertThrows(NotFoundException.class, () -> {
          menuService.putMenu(testmenuId, request, email);
        });

    //then
    org.assertj.core.api.Assertions.assertThat(testmenuId).isNotEqualTo(menu.getId());
    Assertions.assertEquals(ErrorCode.MENU_NOT_FOUND.getMessage(),notFoundException.getMessage());
  }

  @Test
  public void 메뉴_수정_실패_CASE_메뉴가_softDelete_상태일때() {
    MenuRequest request = new MenuRequest("test1@test.com","수정된 맛있는 음식",2000);
    Long menuId = 1L;
    String email = "test1@test.com";
    User user = new User("test1@test.com", "Test1234!@#$", "test", "0101111111", "testaddress",
        UserRole.OWNER);
    Store store = new Store(user,"맛나식당","01011111111","한국 어딘가","111-11-11111",
        100, Time.valueOf(LocalTime.now()),Time.valueOf(LocalTime.now()));
    Menu menu = Menu.menuCreateWithTestCode(menuId,"맛있는 음식",1000,store,user,true);

    when(menuRepository.findByIdOrElseThrow(menuId)).thenReturn(menu);

    doAnswer(invocation -> {
      Long id = invocation.getArgument(0);
      if(menu.getIsDeleted()) {
        throw new NotFoundException(ErrorCode.MENU_NOT_FOUND);
      }
      return null;
        }).when(menuRepository).checkDeleteById(menuId);

    //when
    NotFoundException notFoundException = Assertions.assertThrows(NotFoundException.class, () -> {
      menuService.putMenu(menuId, request, email);
    });


    //then
    verify(menuRepository,times(ONE_TIME)).checkDeleteById(menuId);
    verify(menuRepository,never()).save(menu);
    Assertions.assertEquals(ErrorCode.MENU_NOT_FOUND.getMessage(),notFoundException.getMessage());
  }

  @Test
  public void 메뉴_수정_실패_CASE_메뉴가_자신의_가게_것이_아닐때() {
    MenuRequest request = new MenuRequest("test1@test.com","수정된 맛있는 음식",2000);
    Long menuId = 1L;
    String email = "test1@test.com";
    User user = new User("test1@test.com", "Test1234!@#$", "test", "0101111111", "testaddress",
        UserRole.OWNER);
    User testUser = new User("failtest1@test.com", "Test1234!@#$", "test", "0101111111", "testaddress",
        UserRole.OWNER);
    Store store = new Store(user,"맛나식당","01011111111","한국 어딘가","111-11-11111",
        100, Time.valueOf(LocalTime.now()),Time.valueOf(LocalTime.now()));
    Menu menu = Menu.menuCreateWithTestCode(menuId,"맛있는 음식",1000,store,testUser,false);

    when(menuRepository.findByIdOrElseThrow(menuId)).thenReturn(menu);

    doAnswer(invocation -> {
      Long id = invocation.getArgument(0);
      if(menu.getIsDeleted()) {
        throw new NotFoundException(ErrorCode.MENU_NOT_FOUND);
      }
      return null;
    }).when(menuRepository).checkDeleteById(menuId);

    when(userRepository.findUsersByEmail(email)).thenReturn(Optional.ofNullable(user));

    //when
    AccessDeniedException accessDeniedException = Assertions.assertThrows(AccessDeniedException.class, () -> {
      menuService.putMenu(menuId, request, email);
    });


    //then
    verify(userRepository,times(ONE_TIME)).findUsersByEmail(email);
    verify(menuRepository,never()).save(menu);
    Assertions.assertEquals(ErrorCode.MENU_ACCESS_DENIED.getMessage(),accessDeniedException.getMessage());
    org.assertj.core.api.Assertions.assertThat(menu.getUser().getEmail()).isNotEqualTo(user.getEmail());
  }

  @Test
  public void 메뉴_삭제_성공_테스트() {
    Long menuId = 1L;
    String email = "test1@test.com";
    User user = new User("test1@test.com", "Test1234!@#$", "test", "0101111111", "testaddress",
        UserRole.OWNER);
    Store store = new Store(user,"맛나식당","01011111111","한국 어딘가","111-11-11111",
        100, Time.valueOf(LocalTime.now()),Time.valueOf(LocalTime.now()));
    Menu menu = Menu.menuCreateWithTestCode(menuId,"맛있는 음식",1000,store,user,false);

    when(menuRepository.findByIdOrElseThrow(menuId)).thenReturn(menu);

    when(userRepository.findUsersByEmail(email)).thenReturn(Optional.ofNullable(user));

    doAnswer(invocation -> {
      Long id = invocation.getArgument(0);
      if(menu.getIsDeleted()) {
        throw new NotFoundException(ErrorCode.MENU_NOT_FOUND);
      }
      return null;
    }).when(menuRepository).checkDeleteById(menuId);

    //when
    menuService.softDeleteMenu(menuId, email);

    //then
    verify(menuRepository,times(ONE_TIME)).checkDeleteById(menuId); // setDeleted 검증 불가 확인 필요
  }

}