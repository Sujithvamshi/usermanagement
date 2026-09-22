package com.example.usermanagement.application.service;

import com.example.usermanagement.application.exception.DuplicateEmailException;
import com.example.usermanagement.application.usecase.CreateUserCommand;
import com.example.usermanagement.domain.User;
import com.example.usermanagement.ports.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private CreateUserService createUserService;

    @Test
    void createsUserWhenEmailIsUnique() {
        CreateUserCommand command = new CreateUserCommand("Jane Doe", "jane.doe@example.com");
        when(userRepositoryPort.existsByEmail(command.email())).thenReturn(false);
        when(userRepositoryPort.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = createUserService.createUser(command);

        assertThat(result.getFullName()).isEqualTo("Jane Doe");
        assertThat(result.getEmail()).isEqualTo("jane.doe@example.com");
        verify(userRepositoryPort).save(any(User.class));
    }

    @Test
    void rejectsDuplicateEmail() {
        CreateUserCommand command = new CreateUserCommand("Jane Doe", "jane.doe@example.com");
        when(userRepositoryPort.existsByEmail(command.email())).thenReturn(true);

        assertThatThrownBy(() -> createUserService.createUser(command))
                .isInstanceOf(DuplicateEmailException.class);

        verify(userRepositoryPort, never()).save(any(User.class));
    }
}
