package com.stark.webbanhang.api.user.service;

import com.stark.webbanhang.api.user.dto.request.CartDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
public interface CartService {
    CartDto addCart(UUID id,String authHeader,int quantity);
    HashMap<UUID, HashMap<UUID, CartDto>> editCart(UUID id, int quantity, String authHeader);
    HashMap<UUID, HashMap<UUID, CartDto>> deleteCart(UUID idProduct, String authHeader);
    int totalQuantity(String authHeader);
    double totalPrice(String authHeader);
    HashMap<UUID, HashMap<UUID, CartDto>>  getAllCart();
    void removeAll();
    public String syncCartData(HashMap<UUID, HashMap<UUID, CartDto>> cartData, String authHeader);
}
