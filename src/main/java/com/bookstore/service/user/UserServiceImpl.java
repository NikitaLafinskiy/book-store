package com.bookstore.service.user;

import com.bookstore.dto.cart.ShoppingCartDto;
import com.bookstore.dto.user.UserLoginRequestDto;
import com.bookstore.dto.user.UserLoginResponseDto;
import com.bookstore.dto.user.UserRegistrationRequestDto;
import com.bookstore.dto.user.UserRegistrationResponseDto;
import com.bookstore.entity.Role;
import com.bookstore.entity.ShoppingCart;
import com.bookstore.entity.User;
import com.bookstore.exception.RegistrationException;
import com.bookstore.mapper.ShoppingCartMapper;
import com.bookstore.mapper.UserMapper;
import com.bookstore.repository.RoleRepository;
import com.bookstore.repository.ShoppingCartRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.token.JwtUtil;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;

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

    @Override
    public UserLoginResponseDto login(UserLoginRequestDto userLoginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                userLoginRequestDto.getEmail(), userLoginRequestDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        String token = jwtUtil.generateToken(authentication);
        return new UserLoginResponseDto(token);
    }
}
