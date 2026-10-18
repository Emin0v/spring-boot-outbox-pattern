package com.company.user.management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import com.company.user.management.dao.entity.Outbox;
import com.company.user.management.dao.repository.OutboxRepository;
import com.company.user.management.dao.repository.UserRepository;
import com.company.user.management.dto.UserCreateRequest;
import com.company.user.management.mapper.UserMapperImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.database=H2"
})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import({UserService.class, UserMapperImpl.class, UserOutboxTransactionTest.PasswordConfiguration.class})
class UserOutboxTransactionTest {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository users;

    @SpyBean
    private OutboxRepository outbox;

    @AfterEach
    void cleanDatabase() {
        outbox.deleteAll();
        users.deleteAll();
    }

    @Test
    void successfulRegistrationCommitsUserAndOutboxTogether() {
        service.save(new UserCreateRequest("Example", "example@example.com", "example-password"));
        assertThat(users.count()).isEqualTo(1);
        assertThat(outbox.count()).isEqualTo(1);
        assertThat(outbox.findAll().get(0).getUserId()).isEqualTo(users.findAll().get(0).getId());
    }

    @Test
    void failingOutboxInsertRollsBackTheUserInsert() {
        doThrow(new IllegalStateException("outbox unavailable")).when(outbox).save(any(Outbox.class));
        assertThatThrownBy(() -> service.save(
                new UserCreateRequest("Example", "example@example.com", "example-password")))
                .isInstanceOf(IllegalStateException.class);
        assertThat(users.count()).isZero();
        assertThat(outbox.count()).isZero();
    }

    @TestConfiguration
    static class PasswordConfiguration {
        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
}
