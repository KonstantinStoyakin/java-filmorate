# java-filmorate
Template repository for Filmorate project.

## Диаграмма базы данных
![Диаграмма базы данных](/Users/konstantinstoyakin/IdeaProjects/java-filmorate/diagram.png)

## Пример запросов

**Добавление фильма**:
```sql
INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES
('The Matrix', 'A computer hacker learns the truth.', '1999-03-31', 136, 4),
('Inception', 'Dream within a dream.', '2010-07-16', 148, 4),
('Forrest Gump', 'Life is like a box of chocolates.', '1994-07-06', 142, 2);
```

**Получение списка всех фильмов**
```sql
SELECT * FROM films;
```
**Обновление данных фильма**
```sql
UPDATE films SET description = 'Обновленное описание' WHERE id = 1;
```

**Удаление фильма**
```sql
DELETE FROM films WHERE id = 1;
```

