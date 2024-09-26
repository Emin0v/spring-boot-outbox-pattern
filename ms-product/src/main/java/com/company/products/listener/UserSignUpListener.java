package com.company.products.listener;

import com.company.products.dto.UserInfoDto;
import com.company.products.service.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserSignUpListener {

    private final UserRegistrationService userRegistrationService;

    @KafkaListener(id = "2",
            topics = "db-users_outbox",
            groupId = "product-group-id",
            containerFactory = "kafkaJsonListenerContainerFactory")
    public void userRegisterListener(UserInfoDto dto) {
        log.info("Message received : {}", dto);
        userRegistrationService.saveUser(dto);
    }
}
