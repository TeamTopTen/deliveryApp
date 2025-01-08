package org.example.delivery.store.model.dto.response;

import java.sql.Time;
import java.util.List;
import org.example.delivery.common.domain.Menu;
import org.example.delivery.common.domain.Store;

public record GetStoreResponse(

    String name,
    String storeNumber,
    String address,
    Integer minOrderPrice,
    Time openingTime,
    Time closingTime,
    List<Menu> menus
) {

  public static GetStoreResponse with(
      Store store, List<Menu> menus) {

    return new GetStoreResponse(store.getName(), store.getStoreNumber(), store.getStoreAddress(),
        store.getMinOrderPrice(), store.getOpeningTime(), store.getClosingTime(), menus
    );
  }
}
