# Financial Transaction Processing Service

## Overview

Financial Transaction Processing Service is a production-grade Spring Boot application designed to manage secure and reliable banking transactions. The project supports account management, credit/debit operations, money transfers, fraud detection, transaction auditing, and retry mechanisms for failed transactions.

The application is built using Java 17, Spring Boot, MySQL, JPA, and Swagger/OpenAPI.

---

# Technologies Used

| Technology      | Version |
| --------------- | ------- |
| Java            | 17      |
| Spring Boot     | 3.3.5   |
| Spring Data JPA | Latest  |
| MySQL           | Latest  |
| Lombok          | Latest  |
| Swagger OpenAPI | 2.5.0   |
| Maven           | Latest  |

---

# Project Structure

```text
src/main/java/com/financialtransaction
│
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── enums
├── exception
├── repository
├── scheduler
├── security
├── service
├── serviceimpl
├── util
└── FinancialTransactionProcessingServiceApplication.java
```

# API Endpoints

## Account APIs

| Method | Endpoint           | Description       |
| ------ | ------------------ | ----------------- |
| POST   | /api/accounts      | Create Account    |
| GET    | /api/accounts/{id} | Get Account By ID |
| GET    | /api/accounts      | Get All Accounts  |

---

## Transaction APIs

| Method | Endpoint                   | Description           |
| ------ | -------------------------- | --------------------- |
| POST   | /api/transactions/credit   | Credit Amount         |
| POST   | /api/transactions/debit    | Debit Amount          |
| POST   | /api/transactions/transfer | Transfer Money        |
| GET    | /api/transactions/{id}     | Get Transaction By ID |
| GET    | /api/transactions          | Get All Transactions  |

---

# Sample Request Payloads

## Create Account

```json
{
  "accountHolderName": "Nikhil",
  "email": "nikhil@gmail.com",
  "phoneNumber": "9876543210",
  "initialBalance": 5000
}
```

## Credit Transaction

```json
{
  "accountId": 1,
  "amount": 1000
}
```

## Debit Transaction

```json
{
  "accountId": 1,
  "amount": 500
}
```

## Transfer Transaction

```json
{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 2000
}
```

---

# Database Configuration

Update `application.properties` file:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/financial_transaction_db
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

# Swagger Documentation

After running the application:

```text
http://localhost:8080/swagger-ui/index.html
```

---

# How to Run the Project

## Clone Repository

```bash
git clone <repository-url>
```

## Navigate to Project

```bash
cd FinancialTransactionProcessingService
```

## Build Project

```bash
mvn clean install
```

## Run Application

```bash
mvn spring-boot:run
```
