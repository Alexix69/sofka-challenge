package com.sofka.challenge.common.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class NotFoundException extends EntityNotFoundException {

    private NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException of(Class<?> resourceClass, Object identifier) {
        return new NotFoundException(resourceClass.getSimpleName() + " not found" + formatIdentifier(identifier));
    }

    public static NotFoundException of(String resourceName, Object identifier) {
        return new NotFoundException(resourceName + " not found" + formatIdentifier(identifier));
    }

    private static String formatIdentifier(Object identifier) {
        return identifier == null ? "" : " with id " + identifier;
    }
}
