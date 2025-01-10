package org.example.delivery.unit.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.print.Pageable;
import java.sql.Time;
import java.time.LocalDate;
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
import org.example.delivery.order.model.dto.OrderDto;
import org.example.delivery.order.model.dto.OrderPageDto;
import org.example.delivery.order.repository.OrderRepository;
import org.example.delivery.order.service.OrderService;
import org.example.delivery.store.repository.StoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;


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
    assertThat(businessException.getMessage()).isEqualTo(ErrorCode.ORDER_ACCESS_DENIED.getMessage());

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
    assertThat(businessException.getMessage()).isEqualTo(ErrorCode.USER_NOT_FOUND.getMessage());

  }

  @Test
  public void 주문_생성_실패_CASE_2_user가_없을때_테스트_개선() {
    //given
    UserRole userRole = UserRole.USER;

    Long userId = 1L;
    Long storeId = 1L;
    Long menuId = 1L;

    AuthUser authUserMock = mock(AuthUser.class);
    when(authUserMock.id()).thenReturn(userId);
    when(authUserMock.userRole()).thenReturn(userRole);

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    //when
    BusinessException businessException = Assertions.assertThrows(
        BusinessException.class,
        () -> orderService.createOrder(authUserMock, storeId, menuId)
    );

    //then
    verify(userRepository,times(ONE_TIME)).findById(userId);
    verify(storeRepository,never()).findById(storeId);
    verify(orderRepository,never()).save(any(Order.class));
    assertThat(businessException.getMessage()).isEqualTo(ErrorCode.USER_NOT_FOUND.getMessage());

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

     // 방법 1
//    User userMock = mock(User.class);
//    when(userRepository.findById(userId)).thenReturn(Optional.of(userMock));

    // 방법2
//    when(userRepository.findById(userId)).thenReturn(Optional.of(mock()));

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(storeRepository.findById(storeId)).thenReturn(Optional.ofNullable(null));

    //when
    BusinessException businessException = Assertions.assertThrows(BusinessException.class, () -> {
      orderService.createOrder(authUser, storeId, menuId);
    });

    //then
    verify(storeRepository,times(ONE_TIME)).findById(storeId);
    verify(menuRepository,never()).findById(menuId);
    assertThat(businessException.getMessage()).isEqualTo(ErrorCode.STORE_NOT_FOUND.getMessage());

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
    assertThat(businessException.getMessage()).isEqualTo(ErrorCode.ORDER_TIME_BAD_REQUEST.getMessage());

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
    assertThat(businessException.getMessage()).isEqualTo(ErrorCode.ORDER_TIME_BAD_REQUEST.getMessage());

  }

  @Test
  public void 주문_생성_실패_CASE_6_메뉴가_없을때_테스트() {
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
    when(menuRepository.findById(menuId)).thenReturn(Optional.ofNullable(null));

    //when
    BusinessException businessException = Assertions.assertThrows(BusinessException.class, () -> {
      orderService.createOrder(authUser, storeId, menuId);
    });

    //then
    verify(menuRepository,times(ONE_TIME)).findById(menuId);
    verify(orderRepository,never()).save(any(Order.class));
    assertThat(businessException.getMessage()).isEqualTo(ErrorCode.MENU_NOT_FOUND.getMessage());

  }

  @Test
  public void 주문_생성_실패_CASE_7_최소_주문_금액_미달_테스트() {
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

    String menuName = "맛있는 음식";
    Integer menuPrice = 500;

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

    //when
    BusinessException businessException = Assertions.assertThrows(BusinessException.class, () -> {
      orderService.createOrder(authUser, storeId, menuId);
    });

    //then
    verify(menuRepository,times(ONE_TIME)).findById(menuId);
    verify(orderRepository,never()).save(any(Order.class));
    assertThat(businessException.getMessage()).isEqualTo(ErrorCode.ORDER_MIN_PRICE_BAD_REQUEST.getMessage());

  }

  @Test
  public void OWNER_주문_전체_조회_성공_테스트() {
    //given
    Long userId = 1L;
    UserRole userRole = UserRole.OWNER;
// 불러오는 값이 없어서 굳이 안써도됨
    Page<OrderPageDto> pageMock = mock(Page.class);
    org.springframework.data.domain.Pageable pageableMock = mock(
        org.springframework.data.domain.Pageable.class
    );

    AuthUser authUserMock = mock(AuthUser.class);
    //doReturn(userId).when(authUserMock).id();
    when(authUserMock.id()).thenReturn(userId);
    when(authUserMock.userRole()).thenReturn(userRole);

    when(orderRepository.findOrdersByStoreUserId(userId,
        pageableMock)).thenReturn(pageMock);
    //when
    orderService.findOrders(authUserMock,pageableMock);

    //then
    verify(orderRepository,times(ONE_TIME)).findOrdersByStoreUserId(userId,pageableMock);
    verify(orderRepository,never()).findOrdersByUserId(userId,pageableMock);

  }

  @Test
  public void USER_주문_전체_조회_성공_테스트() {
    //given
    Long userId = 1L;
    UserRole userRole = UserRole.USER;
// 불러오는 값이 없어서 굳이 안써도됨
    Page<OrderPageDto> pageMock = mock(Page.class);
    org.springframework.data.domain.Pageable pageableMock = mock(
        org.springframework.data.domain.Pageable.class
    );

    AuthUser authUserMock = mock(AuthUser.class);
    //doReturn(userId).when(authUserMock).id();
    when(authUserMock.id()).thenReturn(userId);
    when(authUserMock.userRole()).thenReturn(userRole);

    when(orderRepository.findOrdersByUserId(userId,
        pageableMock)).thenReturn(pageMock);
    //when
    orderService.findOrders(authUserMock,pageableMock);

    //then
    verify(orderRepository,times(ONE_TIME)).findOrdersByUserId(userId,pageableMock);

  }

  @Test
  public void USER_주문_단건_조회_성공_테스트() {
    //given
    Long userId = 1L;
    Long orderId = 1L;
    UserRole userRole = UserRole.USER;

    AuthUser authUserMock = mock(AuthUser.class);
    //doReturn(userId).when(authUserMock).id();
    when(authUserMock.id()).thenReturn(userId);
    when(authUserMock.userRole()).thenReturn(userRole);

    User userMock = mock(User.class);
    when(userMock.getName()).thenReturn("test");

    Store storeMock = mock(Store.class);
    when(storeMock.getName()).thenReturn("test");

    Menu menuMock = mock(Menu.class);
    when(menuMock.getName()).thenReturn("test");
    when(menuMock.getPrice()).thenReturn(1000);

    Order orderMock = mock(Order.class);
    when(orderMock.getId()).thenReturn(1L);
    when(orderMock.getUser()).thenReturn(userMock);
    when(orderMock.getStore()).thenReturn(storeMock);
    when(orderMock.getMenu()).thenReturn(menuMock);
    when(orderMock.getOrderStatus()).thenReturn(OrderStatus.ORDERED);
    when(orderMock.getCreatedAt()).thenReturn(LocalDate.now().atStartOfDay());
    when(orderMock.getUpdatedAt()).thenReturn(LocalDate.now().atStartOfDay());

    when(orderRepository.findOrderByUserIdAndOrderIdOrElseThrow(userId,
        orderId)).thenReturn(orderMock);
    //when
    orderService.findOrderbyOrderId(authUserMock,orderId);

    //then
    verify(orderRepository,times(ONE_TIME)).findOrderByUserIdAndOrderIdOrElseThrow(userId,
        orderId);

  }

  @Test
  public void OWNER_주문_단건_조회_성공_테스트() {
    //given
    Long userId = 1L;
    Long orderId = 1L;
    UserRole userRole = UserRole.OWNER;

    AuthUser authUserMock = mock(AuthUser.class);
    //doReturn(userId).when(authUserMock).id();
    when(authUserMock.id()).thenReturn(userId);
    when(authUserMock.userRole()).thenReturn(userRole);

    User userMock = mock(User.class);
    when(userMock.getName()).thenReturn("test");

    Store storeMock = mock(Store.class);
    when(storeMock.getName()).thenReturn("test");

    Menu menuMock = mock(Menu.class);
    when(menuMock.getName()).thenReturn("test");
    when(menuMock.getPrice()).thenReturn(1000);

    Order orderMock = mock(Order.class);
    when(orderMock.getId()).thenReturn(1L);
    when(orderMock.getUser()).thenReturn(userMock);
    when(orderMock.getStore()).thenReturn(storeMock);
    when(orderMock.getMenu()).thenReturn(menuMock);
    when(orderMock.getOrderStatus()).thenReturn(OrderStatus.CONFIRMED);
    when(orderMock.getCreatedAt()).thenReturn(LocalDate.now().atStartOfDay());
    when(orderMock.getUpdatedAt()).thenReturn(LocalDate.now().atStartOfDay());

    when(orderRepository.findOrderByStoreUserIdAndOrderIdOrElseThrow(userId,
        orderId)).thenReturn(orderMock);
    //when
    orderService.findOrderbyOrderId(authUserMock,orderId);

    //then
    verify(orderRepository,times(ONE_TIME)).findOrderByStoreUserIdAndOrderIdOrElseThrow(userId,
        orderId);

  }
}
