
# Customer Management System

## Introduction
Customer Management System is a Java Spring Boot application that provides RESTful APIs for managing customer data. The application allows users to create, read, update, and delete customer records. It also includes user authentication using Bearer token-based authorization for securing the APIs.

## Features
- User authentication using Bearer token-based authorization.
- CRUD operations (Create, Read, Update, Delete) for managing customer data.
- Secure communication with HTTPS.
- Error handling for various scenarios.

## Technologies Used
- Java 11
- Spring Boot 2.x
- Thymeleaf (for frontend HTML templates)
- Maven (for project management)
- H2 in-memory database (for simplicity; can be replaced with a persistent database)

## API Endpoints

### 1. Login
- Path: `/login`
- Method: POST
- Request Body:
  ```json
  {
    "username": "your_username",
    "password": "your_password"
  }
### Response:
Success: Status code: 200, 
Response body: Bearer token
Failure: Status code: 401, Unauthorized

### 2. Create a New Customer
Path: /api/customers

### Method: POST

Request Header: Authorization: Bearer

 <your_bearer_token>

### Request Body:
{
  "first_name": "Jane",
  "last_name": "Doe",
  "street": "Elvnu Street",
  "address": "H no 2",
  "city": "Delhi",
  "state": "Delhi",
  "email": "jane.doe@example.com",
  "phone": "1234567890"
}

### Response:
Success: Status code: 201, Response body: "Successfully Created"
Failure: Status code: 400, Response body: "First Name or Last Name is missing"

### 3. Get Customer List
Path: /api/customers
Method: GET
Request Header: Authorization: Bearer <your_bearer_token>
Response: Status code: 200, Response body: Array of customer objects

### 4. Update Customer
Path: /api/customers/{uuid}
Method: PUT
Request Header: Authorization: Bearer <your_bearer_token>

### Request Body:

{
  "first_name": "Updated First Name",
  "last_name": "Updated Last Name",
  "street": "Updated Street",
  "address": "Updated Address",
  "city": "Updated City",
  "state": "Updated State",
  "email": "updated.email@example.com",
  "phone": "9876543210"
}

### Response:
Success: Status code: 200, Response body: "Successfully Updated"
Failure: Status code: 500, Response body: "UUID not found"

### 5. Delete Customer
Path: /api/customers/{uuid}
Method: DELETE
Request Header: Authorization: Bearer <your_bearer_token>

### Response:
Success: Status code: 200, Response body: "Successfully Deleted"
Failure: Status code: 500, Response body: "Error Not Deleted"

### Getting Started
1. Clone the repository: git clone https://github.com/your_username/your_repository.git

2. Change directory to the project folder: cd your_repository

3. Run the Spring Boot application: mvn spring-boot:run

4. Access the application in your web browser at http://localhost:8080



