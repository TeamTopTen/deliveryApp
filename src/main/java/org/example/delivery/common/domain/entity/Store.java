package org.example.delivery.common.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Time;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.store.model.dto.request.StoreRequest;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "store")
public class Store extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "store_number", nullable = false)
  private String storeNumber;

  @Column(name = "store_address", nullable = false)
  private String storeAddress;

  @Column(name = "registration_number", nullable = false)
  private String registrationNumber;

  @Column(name = "min_order_price", nullable = false)
  private Integer minOrderPrice;

  @Column(name = "opening_time", nullable = false)
  private Time openingTime;

  @Column(name = "closing_time", nullable = false)
  private Time closingTime;

  @Column(name = "is_deleted", nullable = false)
  private boolean isDeleted;

  public Store(User user, String name, String storeNumber, String storeAddress, String registrationNumber,
      Integer minOrderPrice,Time openingTime, Time closingTime) {
    this.user = user;
    this.name = name;
    this.storeNumber = storeNumber;
    this.storeAddress = storeAddress;
    this.registrationNumber = registrationNumber;
    this.minOrderPrice = minOrderPrice;
    this.openingTime = openingTime;
    this.closingTime = closingTime;
    this.isDeleted = false;
  }

  public static Store makeWith(User user, StoreRequest request){
    return new Store(
        user,
        request.name(),
        request.storeNumber(),
        request.storeAddress(),
        request.registrationNumber(),
        request.minOrderPrice(),
        request.openingTime(),
        request.closingTime());
  }

  public void updateWith(StoreRequest request){
    this.name = request.name();
    this.storeNumber = request.storeNumber();
    this.storeAddress = request.storeAddress();
    this.registrationNumber = request.registrationNumber();
    this.minOrderPrice = request.minOrderPrice();
    this.openingTime = request.openingTime();
    this.closingTime = request.closingTime();
  }

  public void softDelete () {
    this.isDeleted = true;
  }
}
