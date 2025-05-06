package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(ValidationException e) {
        log.warn("Ошибка валидации: {}", e.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return error;
    }

    @ExceptionHandler(ConditionsNotMetException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleConditionsException(ConditionsNotMetException e) {
        log.warn("Нарушены условия: {}", e.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return error;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(ResourceNotFoundException e) {
        log.warn("Объект не найден: {}", e.getMessage(), e);
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return error;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleThrowable(Throwable e) {
        log.error("Непредвиденная ошибка: {}", e.getMessage(), e);
        Map<String, String> error = new HashMap<>();
        error.put("error", "Произошла непредвиденная ошибка.");
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        Map<String, String> error = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(err -> {
            error.put(err.getField(), err.getDefaultMessage());
        });
        return error;
    }

    @ExceptionHandler(UnableToFindException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUnableToFindException(UnableToFindException e) {
        log.warn("Не удалось найти объект: {}", e.getMessage(), e);
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return error;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.warn("Ошибка типа аргумента: {}", e.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "Некорректный формат параметра: " + e.getName());
        return error;
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleSQLException(SQLException e) {
        log.error("Ошибка при взаимодействии с базой данных: {}", e.getMessage(), e);
        Map<String, String> error = new HashMap<>();
        error.put("error", "Ошибка при взаимодействии с базой данных.");
        return error;
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleNullPointerException(NullPointerException e) {
        log.error("NullPointerException: {}", e.getMessage(), e);
        return Map.of("error", "Внутренняя ошибка сервера.");
    }
}
