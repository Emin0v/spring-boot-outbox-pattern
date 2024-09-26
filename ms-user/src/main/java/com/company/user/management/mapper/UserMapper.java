package com.company.user.management.mapper;

import com.company.user.management.dao.entity.UserEntity;
import com.company.user.management.dto.UserCreateRequest;
import com.company.user.management.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(request.getPassword()))")
    UserEntity toEntity(UserCreateRequest request, PasswordEncoder passwordEncoder);

    UserResponse toResponse(UserEntity userEntity);

}
