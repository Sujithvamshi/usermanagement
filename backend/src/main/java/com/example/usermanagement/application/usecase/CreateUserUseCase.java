package com.example.usermanagement.application.usecase;

import com.example.usermanagement.domain.User;

public interface CreateUserUseCase {

    User createUser(CreateUserCommand command);
}
