package com.example.usermanagement.application.service;

import com.example.usermanagement.application.exception.UserNotFoundException;
import com.example.usermanagement.ports.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private DeleteUserService deleteUserService;

    @Test
    void deletesWhenUserExists() {
        when(userRepositoryPort.existsById(1L)).thenReturn(true);

        deleteUserService.deleteUser(1L);

        verify(userRepositoryPort).deleteById(1L);
    }

    @Test
    void throwsWhenUserMissing() {
        when(userRepositoryPort.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> deleteUserService.deleteUser(1L))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepositoryPort, never()).deleteById(anyLong());
    }
}
