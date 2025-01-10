package org.example.delivery.common.domain.entity;

import static org.example.delivery.common.domain.enums.UserRole.OWNER;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.InvalidRequestException;

@Entity
@Table(name="menu")
@Getter
public class Menu extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private Integer price;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "store_id")
  private Store store;

  private Boolean isDeleted = Boolean.FALSE;

  private Menu(String name, Integer price, Store store, User user) {
    this.name = name;
    this.price = price;
    this.store = store;
    this.user = user;
  }

  private Menu(Long id, String name, Integer price, Store store,User user,Boolean isDeleted) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.user = user;
    this.store = store;
    this.isDeleted = isDeleted;
  }

  private Menu(Long id, String name, Integer price, Store store,User user ) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.user = user;
    this.store = store;
  }

  public Menu() {
  }

  public static Menu menuCreateWithTestCode(Long id,String name,Integer price,Store store,User user,boolean isDeleted) {
    return new Menu(id,name,price,store,user,isDeleted);
  }

  public static Menu menuCreate(String name,Integer price,Store store,User user) {
    return new Menu(name,price,store,user);
  }

  public static Menu menuPut(Long id,String name,Integer price,Store store,User user) {
    return new Menu(id,name,price,store,user);
  }

  public static void ownerCheck(User user) { // 1
    if(!(user.getUserRole() == OWNER || user.isDeleted())) {
      throw new InvalidRequestException(ErrorCode.Menu_BAD_REQUEST);
    }
  }

  public static void storeCheck(User user, Store store) { // 2
    if(!(store.getUser() == user || user.isDeleted())) {
      throw new InvalidRequestException(ErrorCode.Menu_BAD_REQUEST);
    }
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public void setDeleted(Boolean deleted) {
    isDeleted = deleted;
  }
}
