package org.example.delivery.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.delivery.store.model.dto.request.StoreRequest;
import org.example.delivery.store.model.dto.response.GetStoreResponse;
import org.example.delivery.store.model.dto.response.StoreResponse;
import org.example.delivery.store.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
@Slf4j
public class StoreController {

  private final StoreService storeService;


  @PostMapping("/owners")
  public ResponseEntity<StoreResponse> createStore(@RequestBody StoreRequest request) {

    StoreResponse response = storeService.createStore(request);

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }


  @GetMapping("/{store_id}/users")
  public ResponseEntity<GetStoreResponse> getStore(@PathVariable long store_id) {
    GetStoreResponse response = storeService.getStore(store_id);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }


  @PutMapping("{store_id}/owner")
  public ResponseEntity<StoreResponse> updateStore(
      @PathVariable(name = "store_id") Long storeId, @RequestBody StoreRequest request) {

    StoreResponse response = storeService.updateStore(storeId, request);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }


  @PatchMapping("/{store_id}/owner")
  public ResponseEntity<Void> deleteStore(@PathVariable(name = "store_id") Long storeId) {
    storeService.deleteStore(storeId);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
