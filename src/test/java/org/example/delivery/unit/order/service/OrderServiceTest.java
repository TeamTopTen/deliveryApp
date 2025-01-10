package org.example.delivery.unit.order.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Optional;
import org.example.delivery.auth.model.UserRole;
import org.example.delivery.auth.model.dto.AuthUser;
import org.example.delivery.auth.repository.UserRepository;
import org.example.delivery.common.domain.Menu;
import org.example.delivery.common.domain.Order;
import org.example.delivery.common.domain.OrderStatus;
import org.example.delivery.common.domain.Store;
import org.example.delivery.common.domain.User;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.BusinessException;
import org.example.delivery.menu.repository.MenuRepository;
import org.example.delivery.order.repository.OrderRepository;
import org.example.delivery.order.service.OrderService;
import org.example.delivery.store.repository.StoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

  private static final int ONE_TIME = 1;

  @InjectMocks
  private OrderService orderService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private StoreRepository storeRepository;

  @Mock
  private MenuRepository menuRepository;

  @Mock
  private OrderRepository orderRepository;

  @Test
  public void 주문_생성_성공_테스트() {
    //given
    String email = "Test1234@test.com";
    UserRole userRole = UserRole.USER;
    String userName = "test";
    String password = "Test1234!@#$";
    String phoneNumber = "0101111111";
    String userAddress = "testaddress";

    String storeName = "맛나식당";
    String storeNumber = "01011111111";
    String storeAddress = "한국 어딘가";
    String registrationNumber = "111-11-11111";
    Integer minOrederPrice = 1000;
    String  stringOpeningTime = "7:00:00";
    Time openingTime = Time.valueOf(stringOpeningTime);
    String  stringClosingTime = "22:00:00";
    Time closingTime = Time.valueOf(stringClosingTime);
    String  stringCurrentTime = "12:11:10";
    Time currentTime = Time.valueOf(stringCurrentTime);

    String menuName = "맛있는 음식";
    Integer menuPrice = 2000;

    OrderStatus orderStatus = OrderStatus.of("ORDERED");

    Long userId = 1L;
    Long storeId = 1L;
    Long menuId = 1L;

    AuthUser authUser = new AuthUser(userId, email, userRole);
    User user = new User(email,password,userName,phoneNumber,userAddress,userRole);
    Store store = new Store(user,storeName,storeNumber,storeAddress,registrationNumber,minOrederPrice,openingTime,closingTime);
    Menu menu = Menu.menuCreate(menuName, menuPrice, store, user);
    Order order = new Order(user, store, menu, orderStatus);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
    when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
    when(orderRepository.save(any(Order.class))).thenReturn(order);

    //when
    orderService.createOrder(authUser,storeId,menuId);

    //then
    verify(orderRepository,times(ONE_TIME)).save(any(Order.class));

  }

  @Test
  public void 주문_생성_실패_CASE_1_UserRole이_OWNER_일_때_테스트() {
    //given
    String email = "Test1234@test.com";
    UserRole userRole = UserRole.OWNER;
    String userName = "test";
    String password = "Test1234!@#$";
    String phoneNumber = "0101111111";
    String userAddress = "testaddress";

    String storeName = "맛나식당";
    String storeNumber = "01011111111";
    String storeAddress = "한국 어딘가";
    String registrationNumber = "111-11-11111";
    Integer minOrederPrice = 1000;
    String  stringOpeningTime = "7:00:00";
    Time openingTime = Time.valueOf(stringOpeningTime);
    String  stringClosingTime = "22:00:00";
    Time closingTime = Time.valueOf(stringClosingTime);
    String  stringCurrentTime = "12:11:10";
    Time currentTime = Time.valueOf(stringCurrentTime);

    String menuName = "맛있는 음식";
    Integer menuPrice = 2000;

    OrderStatus orderStatus = OrderStatus.of("ORDERED");

    Long userId = 1L;
    Long storeId = 1L;
    Long menuId = 1L;

    AuthUser authUser = new AuthUser(userId, email, userRole);
    User user = new User(email,password,userName,phoneNumber,userAddress,userRole);
    Store store = new Store(user,storeName,storeNumber,storeAddress,registrationNumber,minOrederPrice,openingTime,closingTime);
    Menu menu = Menu.menuCreate(menuName, menuPrice, store, user);
    Order order = new Order(user, store, menu, orderStatus);

    //when
    BusinessException businessException = Assertions.assertThrows(BusinessException.class, () -> {
      orderService.createOrder(authUser, storeId, menuId);
    });

    //then
    verify(userRepository,never()).findById(userId);
    org.assertj.core.api.Assertions.assertThat(businessException.getMessage()).isEqualTo(ErrorCode.ORDER_ACCESS_DENIED.getMessage());

  }

  @Test
  public void 주문_생성_실패_CASE_2_user가_없을때_테스트() {
    //given
    String email = "Test1234@test.com";
    UserRole userRole = UserRole.USER;
    String userName = "test";
    String password = "Test1234!@#$";
    String phoneNumber = "0101111111";
    String userAddress = "testaddress";

    String storeName = "맛나식당";
    String storeNumber = "01011111111";
    String storeAddress = "한국 어딘가";
    String registrationNumber = "111-11-11111";
    Integer minOrederPrice = 1000;
    String  stringOpeningTime = "7:00:00";
    Time openingTime = Time.valueOf(stringOpeningTime);
    String  stringClosingTime = "22:00:00";
    Time closingTime = Time.valueOf(stringClosingTime);
    String  stringCurrentTime = "12:11:10";
    Time currentTime = Time.valueOf(stringCurrentTime);

    String menuName = "맛있는 음식";
    Integer menuPrice = 2000;

    OrderStatus orderStatus = OrderStatus.of("ORDERED");

    Long userId = 1L;
    Long storeId = 1L;
    Long menuId = 1L;

    AuthUser authUser = new AuthUser(userId, email, userRole);
    User user = new User(email,password,userName,phoneNumber,userAddress,userRole);
    Store store = new Store(user,storeName,storeNumber,storeAddress,registrationNumber,minOrederPrice,openingTime,closingTime);
    Menu menu = Menu.menuCreate(menuName, menuPrice, store, user);
    Order order = new Order(user, store, menu, orderStatus);

    when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(null));
    //when(userRepository.findById(userId)).thenThrow(BusinessException.class);

    //when
    BusinessException businessException = Assertions.assertThrows(BusinessException.class, () -> {
      orderService.createOrder(authUser, storeId, menuId);
    });

    //then
    verify(userRepository,times(ONE_TIME)).findById(userId);
    verify(storeRepository,never()).findById(storeId);
    org.assertj.core.api.Assertions.assertThat(businessException.getMessage()).isEqualTo(ErrorCode.USER_NOT_FOUND.getMessage());

  }

  @Test
  public void 주문_생성_실패_CASE_3_store가_없을때_테스트() {
    //given
    String email = "Test1234@test.com";
    UserRole userRole = UserRole.USER;
    String userName = "test";
    String password = "Test1234!@#$";
    String phoneNumber = "0101111111";
    String userAddress = "testaddress";

    String storeName = "맛나식당";
    String storeNumber = "01011111111";
    String storeAddress = "한국 어딘가";
    String registrationNumber = "111-11-11111";
    Integer minOrederPrice = 1000;
    String  stringOpeningTime = "7:00:00";
    Time openingTime = Time.valueOf(stringOpeningTime);
    String  stringClosingTime = "22:00:00";
    Time closingTime = Time.valueOf(stringClosingTime);
    String  stringCurrentTime = "12:11:10";
    Time currentTime = Time.valueOf(stringCurrentTime);

    String menuName = "맛있는 음식";
    Integer menuPrice = 2000;

    OrderStatus orderStatus = OrderStatus.of("ORDERED");

    Long userId = 1L;
    Long storeId = 1L;
    Long menuId = 1L;

    AuthUser authUser = new AuthUser(userId, email, userRole);
    User user = new User(email,password,userName,phoneNumber,userAddress,userRole);
    Store store = new Store(user,storeName,storeNumber,storeAddress,registrationNumber,minOrederPrice,openingTime,closingTime);
    Menu menu = Menu.menuCreate(menuName, menuPrice, store, user);
    Order order = new Order(user, store, menu, orderStatus);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(storeRepository.findById(storeId)).thenReturn(Optional.ofNullable(null));

    //when
    BusinessException businessException = Assertions.assertThrows(BusinessException.class, () -> {
      orderService.createOrder(authUser, storeId, menuId);
    });

    //then
    verify(storeRepository,times(ONE_TIME)).findById(storeId);
    verify(menuRepository,never()).findById(menuId);
    org.assertj.core.api.Assertions.assertThat(businessException.getMessage()).isEqualTo(ErrorCode.STORE_NOT_FOUND.getMessage());

  }

  @Test
  public void 주문_생성_실패_CASE_4_영업시작_전_테스트() {
    //given
    String email = "Test1234@test.com";
    UserRole userRole = UserRole.USER;
    String userName = "test";
    String password = "Test1234!@#$";
    String phoneNumber = "0101111111";
    String userAddress = "testaddress";

    String storeName = "맛나식당";
    String storeNumber = "01011111111";
    String storeAddress = "한국 어딘가";
    String registrationNumber = "111-11-11111";
    Integer minOrederPrice = 1000;
    String  stringOpeningTime = "11:00:00";
    Time openingTime = Time.valueOf(stringOpeningTime);
    String  stringClosingTime = "22:00:00";
    Time closingTime = Time.valueOf(stringClosingTime);
    String  stringCurrentTime = "8:11:10";
    Time currentTime = Time.valueOf(stringCurrentTime);

    String menuName = "맛있는 음식";
    Integer menuPrice = 2000;

    OrderStatus orderStatus = OrderStatus.of("ORDERED");

    Long userId = 1L;
    Long storeId = 1L;
    Long menuId = 1L;

    AuthUser authUser = new AuthUser(userId, email, userRole);
    User user = new User(email,password,userName,phoneNumber,userAddress,userRole);
    Store store = new Store(user,storeName,storeNumber,storeAddress,registrationNumber,minOrederPrice,openingTime,closingTime);
    Menu menu = Menu.menuCreate(menuName, menuPrice, store, user);
    Order order = new Order(user, store, menu, orderStatus);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

    //when
    BusinessException businessException = Assertions.assertThrows(BusinessException.class, () -> {
      orderService.createOrder(authUser, storeId, menuId);
    });

    //then
    verify(storeRepository,times(ONE_TIME)).findById(storeId);
    verify(menuRepository,never()).findById(menuId);
    org.assertj.core.api.Assertions.assertThat(businessException.getMessage()).isEqualTo(ErrorCode.ORDER_TIME_BAD_REQUEST.getMessage());

  }

  @Test
  public void 주문_생성_실패_CASE_5_영업종료_후_테스트() {
    //given
    String email = "Test1234@test.com";
    UserRole userRole = UserRole.USER;
    String userName = "test";
    String password = "Test1234!@#$";
    String phoneNumber = "0101111111";
    String userAddress = "testaddress";

    String storeName = "맛나식당";
    String storeNumber = "01011111111";
    String storeAddress = "한국 어딘가";
    String registrationNumber = "111-11-11111";
    Integer minOrederPrice = 1000;
    String  stringOpeningTime = "7:00:00";
    Time openingTime = Time.valueOf(stringOpeningTime);
    String  stringClosingTime = "8:00:00";
    Time closingTime = Time.valueOf(stringClosingTime);
    String  stringCurrentTime = "8:11:10";
    Time currentTime = Time.valueOf(stringCurrentTime);

    String menuName = "맛있는 음식";
    Integer menuPrice = 2000;

    OrderStatus orderStatus = OrderStatus.of("ORDERED");

    Long userId = 1L;
    Long storeId = 1L;
    Long menuId = 1L;

    AuthUser authUser = new AuthUser(userId, email, userRole);
    User user = new User(email,password,userName,phoneNumber,userAddress,userRole);
    Store store = new Store(user,storeName,storeNumber,storeAddress,registrationNumber,minOrederPrice,openingTime,closingTime);
    Menu menu = Menu.menuCreate(menuName, menuPrice, store, user);
    Order order = new Order(user, store, menu, orderStatus);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

    //when
    BusinessException businessException = Assertions.assertThrows(BusinessException.class, () -> {
      orderService.createOrder(authUser, storeId, menuId);
    });

    //then
    verify(storeRepository,times(ONE_TIME)).findById(storeId);
    verify(menuRepository,never()).findById(menuId);
    org.assertj.core.api.Assertions.assertThat(businessException.getMessage()).isEqualTo(ErrorCode.ORDER_TIME_BAD_REQUEST.getMessage());

  }

}
