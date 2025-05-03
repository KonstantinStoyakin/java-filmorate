package ru.yandex.practicum.filmorate.storage.friendship;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;

import java.util.List;
import java.util.Optional;

@Repository
public class FriendshipDbStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriendship(Friendship friendship) {
        String sql = "MERGE INTO friendships (user_id, friend_id, status) KEY (user_id, friend_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                friendship.getUserId(),
                friendship.getFriendId(),
                friendship.getStatus().name()
        );
    }

    @Override
    public Optional<Friendship> findByUsers(Long userId, Long friendId) {
        String sql = "SELECT * FROM friendships WHERE user_id = ? AND friend_id = ?";
        List<Friendship> list = jdbcTemplate.query(sql, (rs, rowNum) -> new Friendship(
                rs.getLong("user_id"),
                rs.getLong("friend_id"),
                FriendshipStatus.valueOf(rs.getString("status"))
        ), userId, friendId);
        return list.stream().findFirst();
    }

    @Override
    public void updateFriendship(Friendship friendship) {
        String sql = "UPDATE friendships SET status = ? WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql,
                friendship.getStatus().name(),
                friendship.getUserId(),
                friendship.getFriendId());
    }

    @Override
    public List<Friendship> findFriendsByUser(Long userId) {
        String sql = "SELECT * FROM friendships WHERE user_id = ? AND status = 'CONFIRMED'";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Friendship(
                rs.getLong("user_id"),
                rs.getLong("friend_id"),
                FriendshipStatus.valueOf(rs.getString("status"))
        ), userId);
    }
}
