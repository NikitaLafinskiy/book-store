package com.bookstore.service.user;

import com.bookstore.dto.user.UserRegistrationRequestDto;
import com.bookstore.dto.user.UserResponseDto;
import com.bookstore.entity.Role;
import com.bookstore.entity.User;
import com.bookstore.exception.RegistrationException;
import com.bookstore.mapper.UserMapper;
import com.bookstore.repository.RoleRepository;
import com.bookstore.repository.UserRepository;
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

    @Override
    public UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto) {
        if (userRepository.existsByEmail(userRegistrationRequestDto.getEmail())) {
            throw new RegistrationException("User already exists");
        }
        User user = userMapper.toEntity(userRegistrationRequestDto);
        user.setPassword(passwordEncoder.encode(userRegistrationRequestDto.getPassword()));
        user.setRoles(Set.of(roleRepository.findByRoleName(DEFAULT_ROLE)));
        return userMapper.toDto(userRepository.save(user));
    }
}
