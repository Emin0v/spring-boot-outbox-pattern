package com.company.user.management.service;

import com.company.user.management.dao.entity.Outbox;
import com.company.user.management.dao.entity.UserEntity;
import com.company.user.management.dao.repository.OutboxRepository;
import com.company.user.management.dao.repository.UserRepository;
import com.company.user.management.dto.UserCreateRequest;
import com.company.user.management.dto.UserResponse;
import com.company.user.management.mapper.UserMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final OutboxRepository outboxRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse save(UserCreateRequest dto) {
        UserEntity userEntity = userMapper.toEntity(dto, passwordEncoder);
        System.out.println("userEntity="+userEntity);
        UserEntity savedUser = userRepository.save(userEntity);

        Outbox outbox = Outbox.builder()
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .username(savedUser.getUsername())
                .build();

        outboxRepository.save(outbox);
        return userMapper.toResponse(savedUser);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
