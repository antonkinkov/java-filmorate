# База данных проекта
Template repository for Filmorate project.
![](C:\Users\antok\Downloads\Untitled.png)

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
