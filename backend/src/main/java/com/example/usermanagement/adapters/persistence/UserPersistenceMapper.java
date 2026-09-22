package com.example.usermanagement.adapters.persistence;

import com.example.usermanagement.domain.User;
import org.springframework.stereotype.Component;

/**
 * Converts between the persistence entity and the domain model, keeping the
 * mapping logic in one place.
 */
@Component
public class UserPersistenceMapper {

    public UserJpaEntity toEntity(User user) {
        return new UserJpaEntity(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public User toDomain(UserJpaEntity entity) {
        return new User(
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
