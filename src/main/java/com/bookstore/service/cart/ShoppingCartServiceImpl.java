package com.bookstore.service.cart;

import com.bookstore.dto.cart.AddBookCartRequestDto;
import com.bookstore.dto.cart.ShoppingCartDto;
import com.bookstore.dto.cart.UpdateCartRequestDto;
import com.bookstore.entity.CartItem;
import com.bookstore.entity.ShoppingCart;
import com.bookstore.entity.User;
import com.bookstore.mapper.CartItemMapper;
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
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto getCart(String id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart for the user with an id of "
                        + id
                        + " not found"));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto addCartItem(String id, AddBookCartRequestDto addBookCartRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart for the user with an id of "
                        + id
                        + " not found"));
        CartItem cartItem = cartItemMapper.toCartItem(addBookCartRequestDto);
        shoppingCart.addCartItem(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCart createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart(user);
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto updateCartItem(String id,
                                          UpdateCartRequestDto updateCartRequestDto,
                                          Long cartItemId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart for the user with an id of "
                        + id
                        + " not found"));
        CartItem cartItem = shoppingCart.getCartItems()
                .stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Cart item with an id of "
                        + cartItemId
                        + " not found"));
        cartItemMapper.updateCartItem(updateCartRequestDto, cartItem);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
