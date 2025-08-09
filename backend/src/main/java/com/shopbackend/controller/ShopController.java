package com.shopbackend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/shop")
public class ShopController {

    @PostMapping("/order")
    public ResponseEntity<String> placeOrder(@RequestBody String orderDetails) {
        return new ResponseEntity<>("Order placed successfully: " + orderDetails, HttpStatus.OK);
    }

    @GetMapping("/photos")
    public String[] getShopPhotos() {
        return new String[]{"/images/shop1.jpg", "/images/shop2.jpg"};
    }
}

