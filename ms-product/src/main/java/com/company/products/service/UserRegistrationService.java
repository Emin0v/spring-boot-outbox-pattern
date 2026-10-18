package com.company.products.service;

import com.company.products.dao.entity.UserEntity;
import com.company.products.dao.repository.UserRepository;
import com.company.products.dto.UserInfoDto;
import com.company.products.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;


    @Transactional
    public void saveUser(UserInfoDto dto) {
        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("userId is required");
        }
        UserEntity userEntity = userRepository.findByUserId(dto.getUserId())
                .orElseGet(() -> userMapper.toEntity(dto));
        userEntity.setName(dto.getName());
        userEntity.setUsername(dto.getUsername());
        userRepository.save(userEntity);
    }
}
