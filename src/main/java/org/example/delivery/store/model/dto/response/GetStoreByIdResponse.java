package org.example.delivery.store.model.dto.response;

import java.sql.Time;
import java.util.List;
import org.example.delivery.common.domain.Store;
import org.example.delivery.menu.model.response.MenuResponse;

public record GetStoreByIdResponse(

    String name,
    String storeNumber,
    String address,
    Integer minOrderPrice,
    Time openingTime,
    Time closingTime,
    List<MenuResponse> menus
) {

  public static GetStoreByIdResponse with(Store store, List<MenuResponse> menus) {

    return new GetStoreByIdResponse(store.getName(), store.getStoreNumber(),
        store.getStoreAddress(), store.getMinOrderPrice(), store.getOpeningTime(),
        store.getClosingTime(), menus
    );
  }
}
