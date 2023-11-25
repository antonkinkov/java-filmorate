# База данных проекта
Template repository for Filmorate project.
![Схема БД](https://github.com/antonkinkov/java-filmorate/assets/138143328/3f9e07fe-1919-4eac-907b-2b774cd7a7db)

# Примеры запросов:

1) Получение фильма с ID = 10:
   SELECT*
   FROM films
   WHERE film_id = 10.
   
2) Получение количества лайков у фильма с ID = 1:
   SELECT film_id,
   COUNT(user_id) AS all_likes
   FROM likes
   WHERE film_id = 1
   GROUP BY film_id;
