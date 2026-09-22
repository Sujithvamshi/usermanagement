package com.example.usermanagement.application.service;

import com.example.usermanagement.application.exception.DuplicateEmailException;
import com.example.usermanagement.application.exception.UserNotFoundException;
import com.example.usermanagement.application.usecase.UpdateUserCommand;
import com.example.usermanagement.application.usecase.UpdateUserUseCase;
import com.example.usermanagement.domain.User;
import com.example.usermanagement.ports.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserService implements UpdateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public UpdateUserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public User updateUser(UpdateUserCommand command) {
        User existing = userRepositoryPort.findById(command.id())
                .orElseThrow(() -> new UserNotFoundException(command.id()));

        if (userRepositoryPort.existsByEmailAndIdNot(command.email(), command.id())) {
            throw new DuplicateEmailException(command.email());
        }

        existing.setFullName(command.fullName());
        existing.setEmail(command.email());
        return userRepositoryPort.save(existing);
    }
}
