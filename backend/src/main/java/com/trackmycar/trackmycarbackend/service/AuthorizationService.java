package com.trackmycar.trackmycarbackend.service;

import com.trackmycar.trackmycarbackend.exception.AuthorizationFailedException;
import com.trackmycar.trackmycarbackend.model.AppUser;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AuthorizationService {
    // Generalized method for checking ownership or admin role
    public <T> void checkResourceAccessAuthorization(T entity, AppUser user, Function<T, AppUser> ownerExtractor) {
        if (!isOwner(entity, user, ownerExtractor) && !isAdmin(user)) {
            throw new AuthorizationFailedException("Resource access denied");
        }
    }

    private <T> boolean isOwner(T entity, AppUser user, Function<T, AppUser> ownerExtractor) {
        // Check if the user is the owner of entity
        //return ownerExtractor.apply(entity).equals(user.getUsername());
        AppUser owner = ownerExtractor.apply(entity);
        // Check if the user is the owner or has an admin role
        return owner != null && (owner.equals(user));
    }

    private boolean isAdmin(AppUser user) {
        // Check if the user has the "ROLE_ADMIN" authority
        return user.getAuthorities()
                .stream()
                .anyMatch(authority -> "ADMIN"
                        .equals(authority.getAuthority())
                );
    }
}
