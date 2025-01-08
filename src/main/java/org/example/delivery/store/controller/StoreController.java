package org.example.delivery.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.delivery.store.model.request.StoreCreateRequest;
import org.example.delivery.store.model.response.StoreResponse;
import org.example.delivery.store.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
  public ResponseEntity<StoreResponse> createStore(@RequestBody StoreCreateRequest request) {

    StoreResponse response = storeService.createStore(request);

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

}
