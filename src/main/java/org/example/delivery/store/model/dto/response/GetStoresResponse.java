//package org.example.delivery.store.model.dto.response;
//
//import java.sql.Time;
//import org.example.delivery.common.domain.Store;
//
//public record GetStoresResponse(
//
//    String name,
//    String address,
//    Time openingTime,
//    Time closingTime
//) {
//
//  public static GetStoresResponse toDto(Store store) {
//    return new GetStoresResponse(store.getName(), store.getStoreAddress(),
//        store.getOpeningTime(), store.getClosingTime());
//  }
//}
