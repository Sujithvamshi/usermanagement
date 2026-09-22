package com.example.usermanagement.application.service;

import com.example.usermanagement.application.exception.UserNotFoundException;
import com.example.usermanagement.domain.User;
import com.example.usermanagement.ports.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private GetUserService getUserService;

    @Test
    void returnsUserWhenFound() {
        User user = new User(1L, "Jane Doe", "jane@example.com", Instant.now(), Instant.now());
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(user));

        User result = getUserService.getUser(1L);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void throwsWhenNotFound() {
        when(userRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getUserService.getUser(99L))
                .isInstanceOf(UserNotFoundException.class);
    }
}
