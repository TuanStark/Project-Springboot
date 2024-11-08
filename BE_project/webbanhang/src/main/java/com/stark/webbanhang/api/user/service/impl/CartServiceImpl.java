package com.stark.webbanhang.api.user.service.impl;

import com.stark.webbanhang.api.user.dto.request.CartDto;
import com.stark.webbanhang.api.user.dto.response.GalleryResponse;
import com.stark.webbanhang.api.user.dto.response.ProductResponse;
import com.stark.webbanhang.api.user.entity.Gallery;
import com.stark.webbanhang.api.user.entity.Product;
import com.stark.webbanhang.api.user.mapper.CategoryMapper;
import com.stark.webbanhang.api.user.mapper.ProductMapper;
import com.stark.webbanhang.api.user.repository.GalleryRepository;
import com.stark.webbanhang.api.user.repository.ProductRepository;
import com.stark.webbanhang.api.user.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    CategoryMapper categoryMapper;
    GalleryRepository galleryRepository;
    AuthenticationService authenticationService;


    HashMap<UUID, HashMap<UUID, CartDto>> carts  = new HashMap<>();

    @Override
    public CartDto addCart(UUID id, String authHeader, int quantity) {
        String token = authHeader.substring(7);
        UUID idUser = authenticationService.getCurrentUserId(token);

        CartDto cartDto = new CartDto();
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found Product"));
        ProductResponse response = this.convertToResponse(product);
        // Lấy giỏ hàng của người dùng hoặc tạo mới nếu chưa có
        HashMap<UUID, CartDto> cart = carts.getOrDefault(idUser, new HashMap<>());

        if (cart.containsKey(id)) {
            cartDto = cart.get(id);
            cartDto.setQuantity(cartDto.getQuantity() + 1);
        } else {
            cartDto.setQuantity(quantity);
            cartDto.setProduct(response);
            cartDto.setIdUser(idUser);
        }
        if (response.isPromotionProduct()) {
            cartDto.setPriceDiscount(response.getPrice() - response.getPrice() * response.getDiscount() / 100);
            double priceReal = cartDto.getPriceDiscount();
            cartDto.setTotalPrice(cartDto.getQuantity() * priceReal);
        } else {
            cartDto.setPriceDiscount(0);
            cartDto.setTotalPrice(cartDto.getQuantity() * response.getPrice());
        }

        cart.put(id, cartDto);
        carts.put(idUser, cart);

        return cartDto;
    }

    @Override
    public HashMap<UUID, HashMap<UUID, CartDto>> editCart(UUID id, int quantity, String authHeader) {
        String token = authHeader.substring(7);
        UUID idUser = authenticationService.getCurrentUserId(token);

        HashMap<UUID, CartDto> cart = carts.getOrDefault(idUser, new HashMap<>());
        if (cart.containsKey(id) && quantity > 0) {
            CartDto itemCart = cart.get(id);
            itemCart.setQuantity(quantity);
            if (itemCart.getProduct().isPromotionProduct()) {
                itemCart.setTotalPrice(itemCart.getQuantity() * itemCart.getPriceDiscount());
            } else {
                itemCart.setTotalPrice(itemCart.getQuantity() * itemCart.getProduct().getPrice());
            }
            cart.put(id, itemCart);
            carts.put(idUser, cart);
        } else if (cart.containsKey(id) && quantity <= 0) {
            cart.remove(id);
            carts.put(idUser, cart);
        }
        return carts;
    }

    @Override
    public HashMap<UUID, HashMap<UUID, CartDto>> deleteCart(UUID id, String authHeader) {
        String token = authHeader.substring(7);
        UUID idUser = authenticationService.getCurrentUserId(token);

        HashMap<UUID, CartDto> cart = carts.get(idUser);
        if (cart != null && cart.containsKey(id)) {
            cart.remove(id);

        }
        return carts;
    }
    @Override
    public int totalQuantity(String authHeader) {
        String token = authHeader.substring(7);
        UUID userId = authenticationService.getCurrentUserId(token);
        int sum = 0;

        HashMap<UUID, CartDto> cart = carts.getOrDefault(userId, new HashMap<>());

        for(Map.Entry<UUID,CartDto> cartDto : cart.entrySet()){
            if (cartDto.getValue().getIdUser().equals(userId)){
                sum += cartDto.getValue().getQuantity();
            }
        }
        return sum;
    }

    @Override
    public double totalPrice(String authHeader) {
        String token = authHeader.substring(7);
        UUID userId = authenticationService.getCurrentUserId(token);
        double totalPrice = 0;

        HashMap<UUID, CartDto> cart = carts.getOrDefault(userId, new HashMap<>());

        for (Map.Entry<UUID, CartDto> cartDto : cart.entrySet()) {
            totalPrice += cartDto.getValue().getTotalPrice();
        }
        return totalPrice;
    }

    @Override
    public HashMap<UUID, HashMap<UUID, CartDto>> getAllCart() {
        return carts;
    }

    @Override
    public void removeAll() {
        carts.clear();
    }

    @Override
    public String syncCartData(HashMap<UUID, HashMap<UUID, CartDto>> cartData, String authHeader) {
        String token = authHeader.substring(7);
        UUID idUser = authenticationService.getCurrentUserId(token);
        if(carts.isEmpty()){
            carts.put(idUser, cartData.getOrDefault(idUser, new HashMap<>()));
        }else{
            carts.clear();
            carts.put(idUser, cartData.getOrDefault(idUser, new HashMap<>()));
        }
        return "Đồng bộ giỏ hàng thành công!";
    }

    public ProductResponse convertToResponse(Product product) {
        ProductResponse response = productMapper.toProductResponse(product);
        response.setCategory(categoryMapper.toCategoryResponse(product.getCategory()));
        List<GalleryResponse> list = galleryRepository.findImagesAndDefaultsByProductId(product.getId());
        response.setImages(list);
        Gallery gallery = galleryRepository.findDefaultImagesByProductId(product.getId());
        response.setImage(gallery.getImage());
        return response;
    }
}
