# 🚍 Transport Management System

### (Object Oriented Analysis & Design Mini Project)

## 📌 Project Overview

The **Transport Management System** is a web-based application designed to automate and manage bus transportation logistics efficiently.

The system allows:

* Users to search routes and book seats
* Admins to manage buses, schedules, and drivers
* The system to automatically handle fare calculation and booking management

This project is developed using **Java Spring Boot (MVC Architecture)** and follows **Object-Oriented Design Principles** and **Design Patterns**.

---

## 🎯 Objectives

The main objectives of this system are:

* Automate bus transport management
* Provide real-time seat booking
* Prevent double booking
* Manage fleet and drivers efficiently
* Calculate fare and cancellation fines automatically
* Maintain a scalable and maintainable architecture

---

## 👥 Team Members & Responsibilities

| Member        | Major Feature                  | Minor Feature                        |
| ------------- | ------------------------------ | ------------------------------------ |
| **Sindhu**    | Multi-Role User Authentication | Profile Management & Booking History |
| **Swathi**    | Real-Time Seat Reservation     | Booking Cancellation & Refund        |
| **Sneha**     | Fleet Management (Bus CRUD)    | Driver Assignment & Tracking         |
| **Vaishnavi** | Route & Schedule Management    | Fare Calculation & Fine Management   |

Each member is responsible for implementing their module **end-to-end (UI + Backend + Database)**.

---

## 🛠️ Technology Stack

### Backend

* Java
* Spring Boot (MVC Architecture)
* Hibernate / JPA

### Database

* MySQL / PostgreSQL

### Frontend (Optional)

* Thymeleaf / JavaFX / Swing

### Tools

* GitHub
* PlantUML
* IntelliJ / Eclipse

---

## 🏗️ System Architecture

The project follows **MVC Architecture**:

* **Model:** Database entities & business logic
* **View:** User interface
* **Controller:** Request handling & flow control

This ensures:

* Separation of concerns
* Easy maintenance
* Scalability

---

## 🧠 Design Principles Used

* Single Responsibility Principle (SRP)
* DRY (Don’t Repeat Yourself)
* SOLID Principles
* Encapsulation & Abstraction

---

## 🎨 Design Patterns Implemented

| Type       | Pattern              | Purpose                             |
| ---------- | -------------------- | ----------------------------------- |
| Creational | Singleton            | Database connection handling        |
| Structural | Adapter              | Payment integration flexibility     |
| Behavioral | Observer             | Notify users about schedule changes |
| Framework  | Dependency Injection | Managed by Spring                   |

---

## 📊 Functional Features

### 👤 User Management

* Multi-role login (Admin/User/Driver)
* Profile management
* Booking history tracking

### 🎟️ Seat Reservation

* Real-time seat availability
* Seat booking confirmation
* Booking cancellation and refunds

### 🚌 Fleet Management

* Add, update, delete buses
* Maintain bus status
* Assign drivers to buses

### 🗺️ Route & Schedule Management

* Manage routes and stops
* Set departure/arrival schedules
* Fare calculation based on distance
* Fine calculation for cancellations

---

## ⚙️ Non-Functional Requirements

* MVC architecture compliance
* Data persistence in relational database
* Concurrent seat booking handling
* Scalable and extensible design

---

## 📂 UML Diagrams Included

* Use Case Diagram
* Class Diagram
* Activity Diagrams
* State Diagrams

---

## 💾 Database Entities

Main tables include:

* Users
* Buses
* Seats
* Routes
* Schedules
* Bookings
* Drivers
* Payments

---

## 🚀 How to Run the Project

### Prerequisites

* Java 17+
* MySQL installed
* Maven installed

### Steps

```bash
# Clone repository
git clone <repo-link>

# Navigate into project
cd transport-management-system

# Run Spring Boot application
mvn spring-boot:run
```

Open browser:

```
http://localhost:8080
```

## 📌 Project Status

🔹 Phase 1: Completed (SRS & Planning)
🔹 Phase 2: In Progress (Design & Implementation)

---

## 📜 Course Details

**Course:** UE23CS352B – Object Oriented Analysis & Design
**Institution:** PES University
**Project Type:** Mini Project (Team of 4)

---

## 🤝 Contribution
Sindhu H
Swathi D
Vaishnavi R
Sneha V

---

## ⭐ Future Enhancements

* Real-time GPS tracking
* Online payment gateway integration
* Mobile application support
* AI-based route optimization
