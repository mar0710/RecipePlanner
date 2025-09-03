 <img width="150" height="120" alt="image" src="https://github.com/user-attachments/assets/1e569762-5522-486d-871d-2e7e24e4623f" />

# Recipe Planner



Aplikacja Recipe Planner to aplikacja internetowa, która upraszcza użytkownikom planowanie posiłków, zapewniając uporządkowany sposób zarządzania przepisami i składnikami. Opiera się na przepisach dodawanych przez użytkowników.
Funkcje:
- wyszukiwanie przepisów,
- dodawanie ich do ulubionych,
- planowanie posiłków na konkretne dni,
- komentowanie przepisów ,
- automatyczne tworzenie listy zakupów potrzebnych do posiłków dodanych w planerze

## Technologie użyte
Backend: Java 17, Spring Boot, Spring Security, JWT
Baza danych: PostgreSQL / H2 (dla testów)
Testy: JUnit 5, MockMvc
Frontend: React 
Build tool: Maven

Spring Boot zapewnia szybkie i intuicyjne tworzenie aplikacji webowych w Javie. Dzięki automatycznej konfiguracji, wbudowanemu serwerowi i bogatemu ekosystemowi, idealnie nadaje się do budowy skalowalnych REST API.

Zastosowanie JWT (JSON Web Token) pozwala na bezstanową autoryzację użytkowników. Spring Security integruje się z JWT, zapewniając solidne zabezpieczenia i łatwą kontrolę dostępu do zasobów.

PostgreSQL to wydajna i stabilna baza danych relacyjna, idealna do produkcyjnego środowiska. W testach używana jest baza H2 — lekka, in-memory, co pozwala na szybkie i izolowane testowanie logiki aplikacji.

React umożliwia tworzenie dynamicznych i responsywnych interfejsów użytkownika. Dzięki komponentowej architekturze i integracji z REST API, frontend aplikacji może być lekki, szybki i łatwy w rozbudowie.

Maven jako narzędzie do zarządzania zależnościami i budowania projektu zapewnia przejrzystość, automatyzację i łatwą integrację z CI/CD. Jest standardem w projektach Java i dobrze współpracuje z Spring Boot.

## Klonowanie repozytorium
git clone https://github.com/mar0710/RecipePlanner.git

Plik application.properties: 
spring.datasource.url=jdbc:postgresql://localhost:5432/recipe_db
spring.datasource.username=postgres
spring.datasource.password=haslo
jwt.secret=TWÓJ_KLUCZ_BASE64

Uruchamianie backendu: mvn spring-boot:run
Uruchamianie frontendu: npm start


## Schemat architektury

<img width="1024" height="1024" alt="schemat architektury" src="https://github.com/user-attachments/assets/d271fa26-8ae0-4d7f-a43a-0cfdcc972fd1" />

## Diagram ERD bazy danych

<img width="1123" height="841" alt="Zrzut ekranu 2025-09-03 235920" src="https://github.com/user-attachments/assets/e08dd416-9ca4-4ec8-a572-39e993e414bc" />


## API Endpoints

### Authorization

POST	/api/auth/signUp	Rejestruje nowego użytkownika
POST	/api/auth/login	Loguje użytkownika i zwraca token JWT


### Recipes

GET	/api/recipes	Pobiera listę zatwierdzonych przepisów 

GET	/api/recipes/{id}	Pobiera szczegóły konkretnego przepisu 

GET	/api/recipes/appove	Pobiera listę nie zatwierdzonych przepisów

GET	/api/recipes/approve/{id}	Pobiera szczegóły konkretnego przepisu do zatwierdzenia

PUT	/api/recipes/approve/{id}	Akceptuje przepis przez admina

DELETE	/api/recipes/approve/{id}	Usuwa przepis przez admina

GET	/api/recipes/favoriterecipes	Pobiera listę ulubionych przepisów

GET	/api/recipes/myrecipes	Pobiera listę przepisów zalogowanego użytkownika

GET	/api/recipes/homephotos	Pobiera zdjęcia 4 przepisów do home page

GET	/api/recipes/search/{keyword}	Pobiera listę zatwierdzonych przepisów ze słowem keyword

POST	/api/recipes/upload	Dodaje nowy przepis

POST	/api/recipes/{id}/planner/{day}/{meal} Dodaje przepis do planeru

POST	/api/recipes/{id}/rating	Dodaje ocenę przepisu

GET	/api/recipes/{id}/rating	Pobiera średnią ocenę przepisu


### Comments

POST	/api/recipes/{id}/comment	Dodaje komentarz do przepisu

DELETE	/api/recipes/{id}/deletecomment/{commentId}	Usuwa komentarz


### Planner 

GET	/api/planner/{day}	Pobiera plan posiłków użytkownika na dany dzień

DELETE	/api/planner/delete/{id}	Usuwa przepis z planeru


### Shopping list

GET /api/shoppinglist Pobiera listę produktów z przepisów w planerze
