package com.example.usermanagement.application.service;

import com.example.usermanagement.application.exception.DuplicateEmailException;
import com.example.usermanagement.application.exception.UserNotFoundException;
import com.example.usermanagement.application.usecase.UpdateUserCommand;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private UpdateUserService updateUserService;

    @Test
    void updatesUserWhenFoundAndEmailAvailable() {
        User existing = new User(1L, "Old Name", "old@example.com", Instant.now(), Instant.now());
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepositoryPort.existsByEmailAndIdNot("new@example.com", 1L)).thenReturn(false);
        when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = updateUserService.updateUser(new UpdateUserCommand(1L, "New Name", "new@example.com"));

        assertThat(result.getFullName()).isEqualTo("New Name");
        assertThat(result.getEmail()).isEqualTo("new@example.com");
    }

    @Test
    void throwsWhenUserMissing() {
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateUserService.updateUser(new UpdateUserCommand(1L, "Name", "e@example.com")))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void throwsWhenEmailTaken() {
        User existing = new User(1L, "Old Name", "old@example.com", Instant.now(), Instant.now());
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepositoryPort.existsByEmailAndIdNot("new@example.com", 1L)).thenReturn(true);

        assertThatThrownBy(() -> updateUserService.updateUser(new UpdateUserCommand(1L, "New Name", "new@example.com")))
                .isInstanceOf(DuplicateEmailException.class);
    }
}
