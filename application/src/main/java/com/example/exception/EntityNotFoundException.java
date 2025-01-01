package com.example.exception;

/**
 * Exception thrown when an entity is not found.
 * This is an unchecked exception (extends {@link RuntimeException}).
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * Creates a new {@code EntityNotFoundException} with a custom message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public EntityNotFoundException(String message) {
        super(message);
    }

}
