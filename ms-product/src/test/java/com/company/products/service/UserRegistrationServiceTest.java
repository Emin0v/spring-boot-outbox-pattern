package com.company.products.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.company.products.dao.repository.UserRepository;
import com.company.products.dto.UserInfoDto;
import com.company.products.mapper.UserMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.database=H2"
})
@Import({UserRegistrationService.class, UserMapperImpl.class})
class UserRegistrationServiceTest {

    @Autowired
    private UserRegistrationService registration;

    @Autowired
    private UserRepository users;

    @Test
    void replayingRegistrationKeepsOneLocalUser() {
        UserInfoDto event = new UserInfoDto(42L, "Example", "example@example.com");
        registration.saveUser(event);
        users.flush();
        registration.saveUser(event);
        users.flush();
        assertThat(users.count()).isEqualTo(1);
        assertThat(users.findAll().get(0).getUserId()).isEqualTo(42L);
    }

    @Test
    void repeatedSourceUserUpdatesExistingProjection() {
        registration.saveUser(new UserInfoDto(42L, "Before", "example@example.com"));
        users.flush();
        registration.saveUser(new UserInfoDto(42L, "After", "example@example.com"));
        users.flush();
        assertThat(users.findAll()).singleElement().satisfies(user -> {
            assertThat(user.getUserId()).isEqualTo(42L);
            assertThat(user.getName()).isEqualTo("After");
        });
    }

    @Test
    void distinctSourceUsersRemainDistinct() {
        registration.saveUser(new UserInfoDto(42L, "First", "first@example.com"));
        registration.saveUser(new UserInfoDto(43L, "Second", "second@example.com"));
        users.flush();
        assertThat(users.count()).isEqualTo(2);
    }

    @Test
    void missingSourceUserIdentityIsRejected() {
        assertThatThrownBy(() -> registration.saveUser(new UserInfoDto(null, "Unknown", "unknown@example.com")))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(users.count()).isZero();
    }
}
