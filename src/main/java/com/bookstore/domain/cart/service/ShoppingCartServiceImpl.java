package com.bookstore.domain.cart.service;

import com.bookstore.domain.cart.dto.AddBookCartRequestDto;
import com.bookstore.domain.cart.dto.CartItemDto;
import com.bookstore.domain.cart.dto.ShoppingCartDto;
import com.bookstore.domain.cart.dto.UpdateCartRequestDto;
import com.bookstore.domain.cart.entity.CartItem;
import com.bookstore.domain.cart.entity.ShoppingCart;
import com.bookstore.domain.cart.mapper.ShoppingCartMapper;
import com.bookstore.domain.cart.repository.CartItemRepository;
import com.bookstore.domain.cart.repository.ShoppingCartRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartDto getCart(String email) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserEmail(email);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto addCartItem(String email, AddBookCartRequestDto addBookCartRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserEmail(email);
        CartItem cartItem = shoppingCartMapper.dtoToCartItem(addBookCartRequestDto);
        shoppingCart.addCartItem(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public CartItemDto updateCartItem(UpdateCartRequestDto updateCartRequestDto,
                                      Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        shoppingCartMapper.updateCartItem(updateCartRequestDto, cartItem);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.cartItemToDto(cartItem);
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
