package org.example.delivery.store;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import org.example.delivery.common.domain.User;


@Entity
@Table(name="table")
@Getter
public class ProxyStore {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  public ProxyStore(String name, User user) {
    this.name = name;
    this.user = user;
  }

  public ProxyStore() {
  }
}
