package org.example.delivery.common.domain;

import static org.example.delivery.auth.model.UserRole.OWNER;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import org.example.delivery.store.proxy.ProxyStore;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
  private ProxyStore store;

  private Boolean isDeleted = Boolean.FALSE;

  private Menu(String name, Integer price, ProxyStore store, User user) {
    this.name = name;
    this.price = price;
    this.store = store;
    this.user = user;
  }

  public Menu(Long id, String name, Integer price, ProxyStore store,User user) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.user = user;
    this.store = store;
  }

  public Menu() {

  }

  public static Menu menuCreate(String name,Integer price,ProxyStore store,User user) {
    return new Menu(name,price,store,user);
  }

  public static Menu menuPut(Long id,String name,Integer price,ProxyStore store,User user) {
    return new Menu(id,name,price,store,user);
  }

  public static void ownerCheck(User user) { // 1
    if(!(user.getUserRole() == OWNER || user.isDeleted())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"권한이 없습니다.");
    }
  }

  public static void storeCheck(User user, ProxyStore store) { // 2
    if(!(store.getUser() == user || user.isDeleted())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"권한이 없습니다.");
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
