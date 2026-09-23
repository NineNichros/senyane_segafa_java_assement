# Enviro365 Investments – Withdrawal Notice System

> **eTalente Junior Developer Assessment 2026**
> Full-Stack: Spring Boot (Java 17) + HTML/CSS/JS
> Candidate: Senyane Segafa

---

## Overview

A full-stack web application that allows Enviro365 investors to:
- View their investment portfolio and product balances
- Submit withdrawal notices subject to business rule validation
- View their withdrawal history
- Download a CSV statement of withdrawals

---

## Tech Stack

| Layer     | Technology                          |
|-----------|-------------------------------------|
| Backend   | Java 17, Spring Boot 3.2.5, Maven   |
| Database  | H2 In-Memory (auto-seeded on start) |
| ORM       | Spring Data JPA / Hibernate         |
| Validation| Jakarta Bean Validation             |
| Frontend  | HTML5, CSS3, Vanilla JavaScript     |

---

## Project Structure

```
enviro365/
├── src/
│   ├── main/
│   │   ├── java/com/enviro/assessment/junior/senyane/
│   │   │   ├── Enviro365Application.java       ← Spring Boot entry point
│   │   │   ├── config/
│   │   │   │   └── DataSeeder.java             ← Seeds H2 with test data
│   │   │   ├── controller/
│   │   │   │   ├── PortfolioController.java
│   │   │   │   ├── WithdrawalController.java
│   │   │   │   └── CsvExportController.java
│   │   │   ├── dto/
│   │   │   │   ├── ApiResponseDTO.java         ← Generic response envelope
│   │   │   │   ├── InvestorDTO.java
│   │   │   │   ├── InvestmentProductDTO.java
│   │   │   │   ├── PortfolioDTO.java
│   │   │   │   ├── WithdrawalRequestDTO.java   ← Input validation annotations
│   │   │   │   └── WithdrawalResponseDTO.java
│   │   │   ├── exception/
│   │   │   │   └── GlobalExceptionHandler.java ← @RestControllerAdvice
│   │   │   ├── model/
│   │   │   │   ├── Investor.java
│   │   │   │   ├── Portfolio.java
│   │   │   │   ├── InvestmentProduct.java
│   │   │   │   └── WithdrawalNotice.java
│   │   │   ├── repository/
│   │   │   │   ├── InvestorRepository.java
│   │   │   │   ├── PortfolioRepository.java
│   │   │   │   ├── InvestmentProductRepository.java
│   │   │   │   └── WithdrawalNoticeRepository.java
│   │   │   └── service/
│   │   │       ├── PortfolioService.java
│   │   │       ├── WithdrawalService.java      ← Business rules enforced here
│   │   │       └── CsvExportService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           ├── index.html                  ← Frontend UI
│   │           ├── style.css
│   │           └── app.js
│   └── test/
│       └── java/com/enviro/assessment/junior/senyane/
└── pom.xml
```

---

## Setup & Running

### Prerequisites
- Java 17+
- Maven 3.8+

### Run the application

```bash
cd enviro365
mvn spring-boot:run
```

The app starts on **http://localhost:8081**

### Access points

| URL                                      | Description              |
|------------------------------------------|--------------------------|
| http://localhost:8081/index.html         | Frontend UI              |
| http://localhost:8081/h2-console         | H2 database console      |
| http://localhost:8081/api/portfolios     | REST API base            |

### H2 Console login

| Field    | Value                                        |
|----------|----------------------------------------------|
| JDBC URL | `jdbc:h2:mem:enviro365db`                    |
| Username | `sa`                                         |
| Password | *(leave blank)*                              |

---

## Test Data (Auto-Seeded)

Three investors are seeded on every startup to cover all business rule scenarios:

| # | Investor          | Age | Products                     | Notes                          |
|---|-------------------|-----|------------------------------|--------------------------------|
| 1 | Thabo Nkosi       | 70  | Retirement Annuity + Savings | ✅ Qualifies for RETIREMENT     |
| 2 | Lerato Dlamini    | 45  | Pension Fund + Tax-Free TFSA | ❌ Too young for RETIREMENT     |
| 3 | Sipho Mokoena     | 68  | Living Annuity + Money Market| ✅ Tests the 90% cap rule       |

---

## Business Rules

All rules are enforced in `WithdrawalService.java`:

| Rule | Description |
|------|-------------|
| **Age restriction** | RETIREMENT product withdrawals are only allowed if investor age > 65 |
| **Balance cap** | Withdrawal amount must not exceed the current product balance |
| **90% cap** | Withdrawal amount must not exceed 90% of the current product balance |

Any rule violation returns HTTP 400 with a descriptive error message.

---

## API Documentation

All responses follow this envelope format:

```json
{
  "success": true,
  "message": "Description of result",
  "data": { ... }
}
```

---

### Portfolio Endpoints

#### GET /api/portfolios
Returns all investor portfolios (used to populate investor dropdown).

**Response 200:**
```json
{
  "success": true,
  "message": "Portfolios retrieved successfully",
  "data": [
    {
      "portfolioId": 1,
      "portfolioName": "Thabo's Investment Portfolio",
      "investor": {
        "id": 1,
        "firstName": "Thabo",
        "lastName": "Nkosi",
        "email": "thabo.nkosi@enviro365.co.za",
        "dateOfBirth": "1954-03-15",
        "age": 70
      },
      "products": [...]
    }
  ]
}
```

---

#### GET /api/portfolios/investor/{investorId}
Returns a single investor's portfolio with all products.

**Path variable:** `investorId` – the investor's ID

**Response 200:**
```json
{
  "success": true,
  "message": "Portfolio retrieved successfully",
  "data": {
    "portfolioId": 1,
    "portfolioName": "Thabo's Investment Portfolio",
    "investor": { ... },
    "products": [
      {
        "id": 1,
        "name": "Retirement Annuity Fund",
        "productType": "RETIREMENT",
        "balance": 850000.00
      }
    ]
  }
}
```

**Error 400** (investor not found):
```json
{ "success": false, "message": "Investor not found with ID: 99", "data": null }
```

---

### Withdrawal Endpoints

#### POST /api/withdrawals
Submits a new withdrawal notice. Enforces all business rules.

**Request body:**
```json
{
  "investorId": 1,
  "productId": 1,
  "amount": 50000.00
}
```

**Response 201 (success):**
```json
{
  "success": true,
  "message": "Withdrawal submitted successfully",
  "data": {
    "noticeId": 4,
    "productId": 1,
    "productName": "Retirement Annuity Fund",
    "productType": "RETIREMENT",
    "amountWithdrawn": 50000.00,
    "balanceAfter": 800000.00,
    "status": "APPROVED",
    "note": "Withdrawal approved successfully",
    "createdAt": "2026-09-22T10:30:00"
  }
}
```

**Error 400 (business rule violation):**
```json
{
  "success": false,
  "message": "Retirement withdrawals are only allowed for investors older than 65. Investor age: 45",
  "data": null
}
```

**Error 400 (validation failure):**
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "amount": "Withdrawal amount must be greater than zero"
  }
}
```

---

#### GET /api/withdrawals/history/{investorId}
Returns the full withdrawal history for an investor.

**Path variable:** `investorId`

**Response 200:**
```json
{
  "success": true,
  "message": "Withdrawal history retrieved successfully",
  "data": [
    {
      "noticeId": 1,
      "productId": 1,
      "productName": "Retirement Annuity Fund",
      "productType": "RETIREMENT",
      "amountWithdrawn": 15000.00,
      "balanceAfter": 850000.00,
      "status": "APPROVED",
      "note": "Withdrawal approved successfully",
      "createdAt": "2026-08-23T10:00:00"
    }
  ]
}
```

---

### CSV Export Endpoint

#### GET /api/export/withdrawals/{investorId}
Downloads a CSV file of withdrawal statements.

**Path variable:** `investorId`

**Optional query parameters:**

| Parameter | Format                  | Example                    |
|-----------|-------------------------|----------------------------|
| `from`    | ISO datetime            | `2026-01-01T00:00:00`      |
| `to`      | ISO datetime            | `2026-12-31T23:59:59`      |

**Example with date filter:**
```
GET /api/export/withdrawals/1?from=2026-01-01T00:00:00&to=2026-12-31T23:59:59
```

**Response:** `text/csv` file download — `withdrawals_investor_1.csv`

**CSV format:**
```
Notice ID,Product ID,Product Name,Product Type,Amount Withdrawn,Status,Note,Date
1,1,Retirement Annuity Fund,RETIREMENT,15000.00,APPROVED,Withdrawal approved,2026-08-23 10:00:00
```

---

## Advanced Features Implemented

| Feature                  | Location                          |
|--------------------------|-----------------------------------|
| DTO layer                | `dto/` package                    |
| Input validation         | `WithdrawalRequestDTO` + `@Valid` |
| Global exception handling| `GlobalExceptionHandler.java`     |
| UI validation            | `app.js` client-side checks       |

---

## AI Usage Disclosure

This project was built with AI assistance (Kiro IDE). The candidate understands and can explain:
- All design decisions (DTO pattern, service layer separation, global exception handler)
- Each business rule implementation in `WithdrawalService`
- The JPA entity relationships (OneToOne, OneToMany, ManyToOne)
- The repository query method naming conventions used
- The frontend fetch/async flow and error handling

