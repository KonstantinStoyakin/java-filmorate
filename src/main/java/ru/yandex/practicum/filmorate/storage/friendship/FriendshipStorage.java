package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;
import java.util.Optional;

public interface FriendshipStorage {

    void addFriendship(Friendship friendship);

    Optional<Friendship> findByUsers(Long userId, Long friendId);

    void updateFriendship(Friendship friendship);

    List<Friendship> findFriendsByUser(Long userId);
}
