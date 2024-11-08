package com.stark.webbanhang.api.user.controller;

import com.stark.webbanhang.api.user.dto.request.CartDto;
import com.stark.webbanhang.api.user.service.CartService;
import com.stark.webbanhang.helper.base.constant.StatusMessage;
import com.stark.webbanhang.helper.base.response.ResponseObject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CartController {
    CartService cartService;

    @PostMapping("/add/{id}")
    public ResponseObject<CartDto> add(@PathVariable UUID id,
                                       @RequestHeader("Authorization") String authHeader,
                                        @RequestParam int quantity) {
            var cart = cartService.addCart(id,authHeader,quantity);
            return ResponseObject.<CartDto>builder()
                    .data(cart)
                    .code(200)
                    .message(StatusMessage.SUCCESS)
                    .build();
    }

    @PutMapping("/edit/{id}")
    public ResponseObject<HashMap<UUID, HashMap<UUID, CartDto>>> edit(@PathVariable UUID id,
                                                       @RequestParam("quantity") int quantity,
                                                       @RequestHeader("Authorization") String authHeader) {
        var cart = cartService.editCart(id, quantity,authHeader);
        return ResponseObject.<HashMap<UUID, HashMap<UUID, CartDto>>>builder()
                .data(cart)
                .code(200)
                .message(StatusMessage.SUCCESS)
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HashMap<UUID, HashMap<UUID, CartDto>>>delete(@PathVariable UUID id,
                                                         @RequestHeader("Authorization") String authHeader) {
        var cart = cartService.deleteCart(id,authHeader);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/total-quantity")
    public ResponseEntity<Integer> totalQuantity(@RequestHeader("Authorization") String authHeader) {
        int totalQuantity = cartService.totalQuantity(authHeader);
        return ResponseEntity.ok(totalQuantity);
    }

    @GetMapping("/total-price")
    public ResponseEntity<Double> totalPrice( @RequestHeader("Authorization") String authHeader) {
        double totalPrice = cartService.totalPrice(authHeader);
        return ResponseEntity.ok(totalPrice);
    }

    @GetMapping("/all")
    public ResponseEntity<HashMap<UUID, HashMap<UUID, CartDto>>> getAll(){
        return ResponseEntity.ok(cartService.getAllCart());
    }

    @GetMapping("/clearAll")
    public void removeAll(){
        cartService.removeAll();
    }

    @PostMapping("/sync")
    public ResponseEntity<String> syncCart(@RequestBody HashMap<UUID, HashMap<UUID, CartDto>> cartData,
                                           @RequestHeader("Authorization") String authHeader) {
        String message = cartService.syncCartData(cartData, authHeader);
        return ResponseEntity.ok(message);
    }
}
