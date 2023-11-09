# Person API

Сервис по созданию и обработке сотрудников.

Сущность Cотрудника описывается следующим java-классом:
```java
@Entity
@Getter
@Setter
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

}
```

1. Запуск проекта


Запустите Postgresql внутри Docker Compose:
```shell
docker compose up -d
```
Сбилдите проект
```shell
./gradlew clean build
```
Запустите сервис
```shell
cd build/libs
java -jar person-api-0.0.1-SNAPSHOT.jar
```

2. Тестирование сервиса

Существуют следующие эндпоиты:
```
POST /api/v1/persons - Создание сотрудника
GET /api/v1/persons - Получение списка всех сотрудников
GET /api/v1/persons/{id} - Получение сотрудника по ID
PUT /api/v1/persons/{id} - Изменение сотрудника по ID
DELETE /api/v1/persons/{id} - Удаление сотрудника по ID
```

Примеры тестовых запросов с помощью curl'а:
```shell
# Создание сотрудника
curl -X POST http://localhost:8080/api/v1/persons -H 'Content-Type: application/json' -d '{"name":"Sam","age":22}'

# Получение списка всех сотрудников
curl -X GET http://localhost:8080/api/v1/persons -H 'Accept: application/json'

# Получение сотрудника по ID
curl -X GET http://localhost:8080/api/v1/persons/1 -H 'Accept: application/json'

# Изменение сотрудника по ID
curl -X PUT http://localhost:8080/api/v1/persons/1 -H 'Content-Type: application/json' -d '{"name": "Tom", "age": 28}'

# Удаление сотрудника по ID
curl -X DELETE http://localhost:8080/api/v1/persons/1 -H 'Host: localhost:8080'
```