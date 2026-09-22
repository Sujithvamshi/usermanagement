package com.example.usermanagement.application.service;

import com.example.usermanagement.application.exception.DuplicateEmailException;
import com.example.usermanagement.application.usecase.CreateUserCommand;
import com.example.usermanagement.application.usecase.CreateUserUseCase;
import com.example.usermanagement.domain.User;
import com.example.usermanagement.ports.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService implements CreateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public CreateUserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public User createUser(CreateUserCommand command) {
        if (userRepositoryPort.existsByEmail(command.email())) {
            throw new DuplicateEmailException(command.email());
        }
        User user = User.createNew(command.fullName(), command.email());
        return userRepositoryPort.save(user);
    }
}
