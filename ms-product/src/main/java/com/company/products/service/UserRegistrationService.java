package com.company.products.service;

import com.company.products.dao.entity.UserEntity;
import com.company.products.dao.repository.UserRepository;
import com.company.products.dto.UserInfoDto;
import com.company.products.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;


    public void saveUser(UserInfoDto dto) {
        UserEntity userEntity = userMapper.toEntity(dto);
        userRepository.save(userEntity);
    }
}
