# Secure To-Do List Application
This is a REST API where registered users can create, view, edit, delete and check their own tasks, like in a To-Do list. It features Basic HTTP user authentication via Spring Security 6 and persists data using PostgreSQL. Configuration is managed through environment variables for flexibility across different environments.

## Features
- Create and update user accounts.
- Log in using username and password.
- Create To-Do tasks.
- View the list of To-Do tasks create.
- Change a To-Do task status.
- Update To-Do task.
- Delete To-Do task.

## Technologies
- Java 17
- Spring Boot 3
- Spring Data JPA
- Spring Web
- Spring Security 6
- Spring Validation
- PostgreSQL
- Basic HTTP Authentication
- Maven

## Getting Started
### Prerequisites
- Java 17 or higher
- Maven 3.9.9
- PostgreSQL database

### Installation
1. Clone the repository.
```bash
git clone https://github.com/cortzero/todo-list-app.git
cd todo-list-app
```

2. Set up environment variables.
Create a `.env` file in the project root or set the following environment variables in your system:
```env
SPRING_PROFILES_ACTIVE=profile
DEV_DATABASE_URL=jdbc:postgresql://localhost:5432/to_do_db_dev
TEST_DATABASE_URL=jdbc:postgresql://localhost:5432/to_do_db_test
PROD_DATABASE_URL=jdbc:postgresql://localhost:5432/to_do_db_prod
DATABASE_USERNAME=your_db_username
DATABASE_PASSWORD=your_db_password
```
Alternatively, you can provide an `application.properties` or `application.yml` file with the necessary configurations.

3. Build the project.
```bash
mvn clean install
```

4. Run the application.
```bash
mvn spring-boot:run
```
The application will start on `http://localhost:8080`.

## API Endpoints
### Authentication
- **Register**: `POST /api/auth/signup`

### User management
- **View user information**: `GET /api/users/account`
- **Update user information**: `PUT /api/users/account/update`

### To-Do management
- **Create a To-Do task**: `POST /api/todos`
- **Get all To-Do tasks**: `GET /api/todos`
- **Change a To-Do task status**: `PATCH /api/todos/{toDoId}/status`
- **Update a To-Do task**: `PUT /api/todos/{toDoId}`
- **Delete a To-Do task**: `DELETE /api/todos/{toDoId}`

Note: All `/api/todos` and `/api/users/account` endpoints require a valid Basic HTTP credentials in the Authorization header.

## Testing
You can use tools like `Postman` or `cURL` to test the API endpoints. Ensure you include the Base64 encoded credentials username:password in the Authentication header:

```http
Authentication: Basic base64_encoded_credentials
```
