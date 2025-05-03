package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    private final FriendshipStorage friendshipStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    public User add(User user) {
        enrichUser(user);
        validateUser(user);
        return userStorage.addUser(user);
    }

    public User update(User user) {
        userStorage.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + user.getId() + " не найден"));
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userStorage.updateUser(user);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(Long id) {
        return userStorage.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + id + " не найден"));
    }

    private void validateUser(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    private void enrichUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public void addFriend(Long userId, Long friendId) {
        findById(userId);
        findById(friendId);
        friendshipStorage.addFriendship(new Friendship(userId, friendId, FriendshipStatus.CONFIRMED));
    }

    public void removeFriend(Long userId, Long friendId) {
        findById(userId);
        findById(friendId);

        friendshipStorage.findByUsers(userId, friendId).ifPresent(friendship -> {
            friendshipStorage.updateFriendship(new Friendship(userId, friendId, FriendshipStatus.DELETED));
        });
    }


    public List<User> getFriends(Long userId) {
        findById(userId);
        return friendshipStorage.findFriendsByUser(userId).stream()
                .map(Friendship::getFriendId)
                .map(this::findById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        Set<Long> friends1 = friendshipStorage.findFriendsByUser(userId).stream()
                .map(Friendship::getFriendId)
                .collect(Collectors.toSet());

        Set<Long> friends2 = friendshipStorage.findFriendsByUser(otherId).stream()
                .map(Friendship::getFriendId)
                .collect(Collectors.toSet());

        return friends1.stream()
                .filter(friends2::contains)
                .map(this::findById)
                .collect(Collectors.toList());
    }
}


