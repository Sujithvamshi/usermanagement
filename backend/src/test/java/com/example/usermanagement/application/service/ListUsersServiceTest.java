package com.example.usermanagement.application.service;

import com.example.usermanagement.domain.User;
import com.example.usermanagement.ports.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListUsersServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private ListUsersService listUsersService;

    @Test
    void returnsAllUsers() {
        User user1 = new User(1L, "Jane Doe", "jane@example.com", Instant.now(), Instant.now());
        User user2 = new User(2L, "John Smith", "john@example.com", Instant.now(), Instant.now());
        when(userRepositoryPort.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = listUsersService.listUsers();

        assertThat(result).containsExactly(user1, user2);
    }
}
