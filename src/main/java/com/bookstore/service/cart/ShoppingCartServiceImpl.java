package com.bookstore.service.cart;

import com.bookstore.dto.cart.AddBookCartRequestDto;
import com.bookstore.dto.cart.CartItemDto;
import com.bookstore.dto.cart.ShoppingCartDto;
import com.bookstore.dto.cart.UpdateCartRequestDto;
import com.bookstore.entity.CartItem;
import com.bookstore.entity.ShoppingCart;
import com.bookstore.mapper.ShoppingCartMapper;
import com.bookstore.repository.CartItemRepository;
import com.bookstore.repository.ShoppingCartRepository;
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
        shoppingCart.getCartItems()
                .add(shoppingCartMapper
                        .dtoToCartItem(addBookCartRequestDto));
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
