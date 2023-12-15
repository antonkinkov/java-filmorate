# База данных проекта
Template repository for Filmorate project.
![Untitled (1).png](..%2F..%2FDownloads%2FUntitled%20%281%29.png)

# Примеры запросов:
```
1) Получение фильма с ID = 10:
   SELECT *
   FROM films
   WHERE id = 10;
   
2) Получение количества лайков у фильма с ID = 1:
   SELECT film_id,
   COUNT(user_id) AS all_likes
   FROM films_likes
   WHERE film_id = 1
   GROUP BY film_id;
```
