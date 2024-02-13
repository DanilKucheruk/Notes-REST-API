# Notes-REST-API

## Описание

Notes-REST-API - это сервис, который предоставляет REST API для работы с заметками.

В приложении реализована регистрация и авторизация по JWT токену. Для выполнения большинства запросов к Api необходимо передавать access-токен.

Хранение заметок производится в PostgreSQL.

Через простые HTTP запросы к Api (GET, POST, PUT, DELETE), авторизованному пользователю можно выполнить весь набор CRUD операций с заметками.

## Схема приложения 
![scheme](https://github.com/DanilKucheruk/Notes-REST-API/assets/147261620/a2c51fc1-d5a1-4ea2-9c88-fb7e354fa158)


## Схема базы данных 
![scheme-data-base](https://github.com/DanilKucheruk/Notes-REST-API/assets/147261620/34196a10-1032-4b37-b45d-591a2487f7eb)




## Установка
Чобы запустить приложение следуйте этим инструкциям:

1. Скачайте репозиторий с GitHub, содержащий файлы Dockerfile и docker-compose.yml.

2. Убедитесь, что у вас установлен Docker на вашем компьютере. Если Docker не установлен, вы можете скачать его с официального сайта Docker.

3. Откройте терминал или командную строку и перейдите в папку скачанного репозитория.

4. Выполните команду `$ docker-compose up --build` , чтобы создать образы сервисов на основе Dockerfile и запустить сервисы.

! В случае ошибки при первом запуске серисов выполните команду `$ docker-compose up` !

После запуска сервисы будут доступны и готовы к использованию.


Примечание: Если вы хотите остановить и удалить контейнеры, выполните команду `$ docker-compose down`.


## Использование


Для использования развернутого REST Api достаточно совершать HTTP запросы к нему(например, через Postman).


Регистрация пользователя: POST запрос по адерсу http://localhost:8085/api/registration. В теле запроса указать:

`{ "username":"Bob", "rawPassword":"pass"}`

В случае успешного исхода вернется json сущности.

Авторизация пользователя: POST запрос по адерсу http://localhost:8085/api/auth. В теле запроса указать:
`{ "username":"Bob", "password":"pass"}`

В случае успешного исхода в ответ придет token.

Создание папки с заметками: POST запрос по адерсу http://localhost:8085/api/lists.
В теле запроса указать:

`{ "title": "List1", "description": "Description1"}`

Authorization: Brear Token: полученный при авторизации token.

В случае успешного исхода вернется json сущности.

Получение всех папок: GET запрос по адерсу http://localhost:8085/api/lists.
Authorization: Brear Token: полученный при авторизации token


Получение папки по id: GET запрос по адерсу http://localhost:8085/api/lists/{id}
Authorization: Brear Token: полученный при авторизации token


Редактирование записи: PUT запрос по адерсу http://localhost:8085/api/lists/{id}
В теле запроса указать:

`{ "title": "newList1", "description": "newDescription1"}`

Authorization: Brear Token: полученный при авторизации token.

В случае успешного исхода вернется json сущности, иначе вернется текст с ошибкой.


Удаление записи: DELETE запрос по адерсу  http://localhost:8085/api/lists/{id}
Authorization: Brear Token: полученный при авторизации refresh_token

В случае успешного исхода вернется пустой json.




Создание заметки: POST запрос по адерсу http://localhost:8085/api/notes.
В теле запроса указать:

`{ "title": "Note1", "content": "Content1"}`

Authorization: Brear Token: полученный при авторизации token.

В случае успешного исхода вернется json сущности.

Получение всех заметок: GET запрос по адерсу http://localhost:8085/api/notes.
Authorization: Brear Token: полученный при авторизации token

Получение заметки по id: GET запрос по адерсу http://localhost:8085/api/notes/{id}
Authorization: Brear Token: полученный при авторизации token

Редактирование записи: PUT запрос по адерсу http://localhost:8085/api/notes/{id}
В теле запроса указать:

`{ "title": "newNote1", "content": "newContent1"}`

Authorization: Brear Token: полученный при авторизации token.

В случае успешного исхода вернется json сущности, иначе вернется текст с ошибкой.


Удаление записи: DELETE запрос по адерсу  http://localhost:8085/api/notes/{id}
Authorization: Brear Token: полученный при авторизации refresh_token

В случае успешного исхода вернется пустой json.
