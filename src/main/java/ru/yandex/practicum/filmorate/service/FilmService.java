package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film add(Film film) {
        validateFilm(film);
        return filmStorage.addFilm(film);
    }

    public Film update(Film film) {
        validateFilm(film);
        findById(film.getId());
        return filmStorage.updateFilm(film);
    }

    public Film findById(Long id) {
        return filmStorage.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Фильм с id " + id + " не найден"));
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше " + EARLIEST_RELEASE_DATE);
        }
        if (film.getDescription() == null || film.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
    }

    public void addLike(Long filmId, Long userId) {
        Film film = findById(filmId);
        userStorage.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + userId + " не найден"));
        film.getLikes().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = findById(filmId);
        userStorage.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + userId + " не найден"));
        if (!film.getLikes().contains(userId)) {
            throw new ValidationException("Пользователь не ставил лайк фильму");
        }
        film.getLikes().remove(userId);
    }

    public List<Film> getMostPopular(int count) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
