package ru.yandex.practicum.filmorate.exception;

public class UnableToFindException extends RuntimeException {
    public UnableToFindException(String message) {
        super(message);
    }
}
