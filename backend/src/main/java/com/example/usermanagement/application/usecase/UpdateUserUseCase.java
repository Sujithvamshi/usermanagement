package com.example.usermanagement.application.usecase;

import com.example.usermanagement.domain.User;

public interface UpdateUserUseCase {

    User updateUser(UpdateUserCommand command);
}
