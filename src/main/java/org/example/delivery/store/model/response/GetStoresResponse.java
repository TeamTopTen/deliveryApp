package org.example.delivery.store.model.response;

import java.sql.Time;
import org.example.delivery.common.domain.entity.Store;

public record GetStoresResponse(

    String name,
    String address,
    Time openingTime,
    Time closingTime
) {

  /**
   * {@code store}객체 받아서 {@code GetStoresResponse}객체 만들어주는 정적 팩토리 메서드
   */
  public static GetStoresResponse toDto(Store store) {
    return new GetStoresResponse(
        store.getName(),
        store.getStoreAddress(),
        store.getOpeningTime(),
        store.getClosingTime()
    );
  }
}
