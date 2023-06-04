# ITConferenceREST

## General info
Ten projekt jest implementacją backendu dla aplikacji obsługującej konferencję.
	
## Technologies
Projekt został stworzony za pomocą:
* Java version: 17
* Spring Boot version: 3.1.0
* Maven version: 3.8.1
	
## Setup
Aby uruchomić program, zainstaluj go lokalnie za pomocą komend:

```
$ cd ../lorem
$ npm install
$ npm start
```

Aplikacja jest dostępna pod adresem localhost:8080

## REST API
Dostęp do usług odybywa się za pomocą REST API. Adresy do usług:

### Sprawdź plan konferencji

#### Request

`GET http://localhost:8080/api/plan`

#### Response Body

    Conference Plan:
      01-06-2023 10:00 to 01-06-2023 11:45 SUBJECT_1
      01-06-2023 12:00 to 01-06-2023 13:45 SUBJECT_1
      01-06-2023 14:00 to 01-06-2023 15:45 SUBJECT_1
      01-06-2023 10:00 to 01-06-2023 11:45 SUBJECT_2
      01-06-2023 12:00 to 01-06-2023 13:45 SUBJECT_2
      01-06-2023 14:00 to 01-06-2023 15:45 SUBJECT_2
      01-06-2023 10:00 to 01-06-2023 11:45 SUBJECT_3
      01-06-2023 12:00 to 01-06-2023 13:45 SUBJECT_3
      01-06-2023 14:00 to 01-06-2023 15:45 SUBJECT_3

### Zobacz rezerwacje danego użytkownika

#### Request

`GET http://localhost:8080/api/reservations/user?login={user login}`

    e.g. http://localhost:8080/api/reservations/user?login=user

#### Response Body

    [
      {
          "id": 1,
          "lectureID": 6
      },
      {
          "id": 2,
          "lectureID": 2
      },
      {
          "id": 3,
          "lectureID": 4
      }
    ]

### Zapisz się na prelekcję

#### Request

`POST http://localhost:8080/api/reservations/add?lectureID={id}`

    e.g. http://localhost:8080/api/reservations/add?lectureID=1
    
    Request Body
    {
      "id": 0,
      "login": "user3",
      "email": "user3@gmail.com",
      "reservations": {}
    }

#### Response Body

    {
      "id": 7,
      "lectureID": 1
    }

### Anuluj rezerwację

#### Request

`DELETE http://localhost:8080/api/reservation/delete/{id}`

    e.g. http://localhost:8080/api/reservation/delete/3

#### Response

    Status: 404 No Content

### Zaktualizuj adres email

#### Request

`PUT http://localhost:8080/api/update/email/user/{id}?email={email address}`

    e.g. http://localhost:8080/api/update/email/user/1?email=user4@gmail.com

#### Response Body

    {
      "id": 1,
      "login": "user",
      "email": "user4@gmail.com"
    }

### Wyświetl listę zarejestrowanych użytkowników

#### Request

`GET http://localhost:8080/api/users`

#### Response Body

    Users:
      Login: user Email: user4@gmail.com
      Login: user2 Email: user2@gmail.com
      Login: user3 Email: user3@gmail.com

### Wyświetl zestawienie wykładów wg zainteresowania

#### Request

`GET http://localhost:8080/api/attendees/lecture/{id}`

    e.g. http://localhost:8080/api/attendees/lecture/8

#### Response Body

    This lecture was attended by 1.0 of 3.0 attendees which is 33,33%
    
### Wyświetl zestawienie ścieżek tematycznych wg zainteresowania

#### Request

`GET http://localhost:8080/api/attendees/subject?subject={subject}`

    e.g. http://localhost:8080/api/attendees/subject?subject=SUBJECT_1

#### Response Body

    This subject was chosen for 3.0 of 6.0 reservations which is 50,00% 

