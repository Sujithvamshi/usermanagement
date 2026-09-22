package com.example.usermanagement.application.usecase;

import com.example.usermanagement.domain.User;

public interface GetUserUseCase {

    User getUser(Long id);
}
