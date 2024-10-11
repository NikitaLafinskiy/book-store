package com.bookstore.domain.user.service;

import com.bookstore.domain.user.dto.UserRegistrationRequestDto;
import com.bookstore.domain.user.dto.UserRegistrationResponseDto;
import com.bookstore.domain.user.entity.Role;
import com.bookstore.domain.cart.entity.ShoppingCart;
import com.bookstore.domain.user.entity.User;
import com.bookstore.exception.RegistrationException;
import com.bookstore.domain.user.mapper.UserMapper;
import com.bookstore.domain.user.repository.RoleRepository;
import com.bookstore.domain.cart.repository.ShoppingCartRepository;
import com.bookstore.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Role.RoleName DEFAULT_ROLE = Role.RoleName.USER;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    @Transactional
    public UserRegistrationResponseDto register(UserRegistrationRequestDto
                                                            userRegistrationRequestDto) {
        if (userRepository.existsByEmail(userRegistrationRequestDto.getEmail())) {
            throw new RegistrationException("User already exists");
        }
        User user = userMapper.toEntity(userRegistrationRequestDto);
        user.setPassword(passwordEncoder.encode(userRegistrationRequestDto.getPassword()));
        user.setRoles(Set.of(roleRepository.findByRoleName(DEFAULT_ROLE)));
        userRepository.save(user);
        ShoppingCart shoppingCart = new ShoppingCart(user);
        shoppingCartRepository.save(shoppingCart);
        return userMapper.toDto(user);
    }
}
