# TicTacToe
## О проекте:
REST API приложения для игры в крестики нолики на Java Spring/Boot. 
Поддерживает регистрацию пользователей, логин из базы данных PostgreSQL с использованием CRUD-repository, создание игр в 2-х режимах: Игра против ПК (используется алгоритм минимакс) или против другого игрока.
## Технологии:
- Java 18
- Spring Boot
- Spring Security
- PostgreSQL
- Gradle
## Как запустить:
Клонируем проект при помощи git clone
Переходим в папку TicTacToe
Используем ./gradlew build для сборки приложения
Далее ./gradlew bootRun для запуски приложения
## API endpoints:
### Регистрация
    POST http://localhost:8080/auth/register
    Header:
    Content-type: application/json
    Request:
    {
        "login":"<your_username>",
        "password":"<your_password>"
    }

### Логин
    GET http://localhost:8080/auth/login
    Header:
    Content-type: application/json
    Request:
    {
        "login":"<your_username>",
        "password":"<your_password>"
    }    
    
    При выводе получаем 2 токена. Access Token и Refresh Token.
    
    ![Пример Вывода](https://github.com/VladimirTitovIO/Tic-Tac-Toe/blob/main/screenshots/LoginExample.png)
    
### Создание игры
    POST http://localhost:8080/game
    Request:
    Content-type: application/json
    {
        "mode":"<PC/Player>",
        "userId":"<UUID>"
    }

### Присоединение к игре
    POST http://localhost:8080/game/join/<game_UUID>
    Header:
    Content-type: application/json
    Authorization: Basic <Закодированные в Base64 логин и пароль в формате: "login:password">

### Ход в игре
    POST http://localhost:8080/game/<game_UUID>
    Header:
    Content-type: application/json
    Authorization: Basic <Закодированные в Base64 логин и пароль в формате: "login:password">
    Request:
    {
        "row":"<значение от 0 до 2>",
        "column":<значение от 0 до 2>"
    }

### Получение информации об игре по её UUID
    GET http://localhost:8080/game/<game_UUID>
    Header:
    Content-type: application/json
    Authorization: Basic <Закодированные в Base64 логин и пароль в формате: "login:password">

### Вывод всех доступных для игры сессий
    GET http://localhost:8080/game/available
    Header:
    Content-type: application/json
    Authorization: Basic <Закодированные в Base64 логин и пароль в формате: "login:password">

### Вывод только UUID всех доступных для игры сессий
    GET http://localhost:8080/game/availableId
    Header:
    Content-type: application/json
    Authorization: Basic <Закодированные в Base64 логин и пароль в формате: "login:password">

### Вывод информации (логин, пароль) игрока по UUID
    GET http://localhost:8080/game/users/<user_id>
    Header:
    Content-type: application/json
    Authorization: Basic <Закодированные в Base64 логин и пароль в формате: "login:password">

**По пути src/main/java/tictactoe доступен файл test.http со всеми вышеописанными командами.**
