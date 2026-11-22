# 🏨 GrandStay – Hotel Reservation System

**GrandStay** is a full-stack Hotel Reservation System developed using Java, Spring Boot, MySQL, HTML, CSS and JavaScript. It provides a convenient platform for customers to search hotel rooms, make reservations, complete payments and manage their bookings.

The application also includes additional features such as customer authentication, email OTP password recovery, coupon management, reviews, invoice generation and an admin dashboard.

---

## 📌 Project Overview

- **Project Name:** GrandStay – Hotel Reservation System
- **Project Type:** Full-Stack Web Application
- **Backend:** Java, Spring Boot
- **Frontend:** HTML, CSS, JavaScript
- **Database:** MySQL
- **Build Tool:** Maven
- **Architecture:** MVC Architecture

## 🎯 Project Objectives

1. Simplify hotel room reservation and management.
2. Allow customers to search and book available rooms.
3. Provide convenient online payment functionality.
4. Enable customers to manage their bookings.
5. Provide administrative tools for hotel management.
6. Improve customer experience through additional services.

---

## ✨ Features and Modules

### 1. Home Page
- User-friendly hotel website interface.
- Navigation between different pages.
- Hotel information and room browsing.

### 2. Room Management
- Display available hotel rooms.
- View room details.
- Search rooms based on availability.
- Manage room information.

### 3. Customer Management
- Customer information management.
- Customer registration.
- Customer account management.

### 4. User Authentication
- Customer registration using email and password.
- Customer login.
- BCrypt password hashing.
- Customer ID management.
- Login and registration validation.

### 5. Room Search and Availability
- Search rooms based on check-in and check-out dates.
- Display available rooms.
- Support room selection.

### 6. Hotel Booking
- Select preferred rooms.
- Enter booking information.
- Create reservations.
- View existing bookings.
- Manage booking details.

### 7. Payment Management
- Payment functionality.
- Payment information management.
- Booking and payment integration.

### 8. My Bookings
- View customer bookings.
- Access booking information.
- Review reservation details.

### 9. Admin Dashboard
- Administrative dashboard.
- Manage hotel information.
- Access booking management features.

### 10. Coupon Management
- Coupon code management.
- Coupon validation.
- Discount preview.

### 11. Customer Reviews and Ratings
- Submit hotel reviews.
- Provide ratings.
- Display customer feedback.
- Associate reviews with bookings.

### 12. Invoice Generation
- Generate booking invoices.
- View invoice details.
- Print or save invoices as PDF through the browser.

### 13. Customer Profile
- Customer account interface.
- Login and registration interface.
- Account information management.

### 14. Forgot Password
- Request password reset through email.
- Generate a six-digit OTP.
- Send OTP using Gmail SMTP.
- OTP expiration after five minutes.
- Verify OTP.
- Reset password with BCrypt hashing.

---

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java 17 | Backend programming |
| Spring Boot 3.4.7 | Backend framework |
| Spring Web | REST API development |
| Spring Data JPA | Database operations |
| Hibernate | ORM |
| MySQL 8 | Relational database |
| HTML5 | Web page structure |
| CSS3 | Website styling |
| JavaScript | Frontend functionality |
| Maven | Dependency management and build |
| BCrypt | Password hashing |
| Gmail SMTP | Email OTP delivery |
| Git | Version control |
| GitHub | Source code hosting |
| VS Code | Development environment |

---

## 🏗️ Project Architecture

The application follows the Model-View-Controller (MVC) architecture.

- **Model:** Entities representing customers, rooms, bookings, payments, coupons, reviews and accounts.
- **View:** HTML, CSS and JavaScript frontend pages.
- **Controller:** Spring Boot REST controllers that handle client requests.
- **Repository:** Spring Data JPA interfaces for database operations.
- **Service:** Business logic for bookings, customers, payments and password recovery.

---

## 📂 Project Structure

```text
GrandStay-Hotel-Reservation-System/
│
├── .mvn/
│   └── wrapper/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/codealpha/hotelreservation/
│   │   │       │
│   │   │       ├── config/
│   │   │       │   └── PasswordConfig.java
│   │   │       │
│   │   │       ├── controller/
│   │   │       │   ├── AdminController.java
│   │   │       │   ├── BookingController.java
│   │   │       │   ├── CouponController.java
│   │   │       │   ├── CustomerAccountController.java
│   │   │       │   ├── CustomerController.java
│   │   │       │   ├── PasswordResetController.java
│   │   │       │   ├── PaymentController.java
│   │   │       │   ├── ReviewController.java
│   │   │       │   ├── RoomController.java
│   │   │       │   └── RoomSearchController.java
│   │       │       │
│   │       │       ├── entity/
│   │       │       │   ├── Booking.java
│   │       │       │   ├── Coupon.java
│   │       │       │   ├── Customer.java
│   │       │       │   ├── CustomerAccount.java
│   │       │       │   ├── PasswordResetOtp.java
│   │       │       │   ├── Payment.java
│   │       │       │   ├── Review.java
│   │       │       │   └── Room.java
│   │       │       │
│   │       │       ├── repository/
│   │       │       ├── service/
│   │       │       └── HotelreservationApplication.java
│   │       │
│   │       └── resources/
│   │           ├── static/
│   │           │   ├── index.html
│   │           │   ├── rooms.html
│   │           │   ├── room-search.html
│   │           │   ├── booking.html
│   │           │   ├── payment.html
│   │           │   ├── my-bookings.html
│   │           │   ├── profile.html
│   │           │   ├── forgot-password.html
│   │           │   ├── admin.html
│   │           │   ├── reviews.html
│   │           │   ├── invoice.html
│   │           │   ├── style.css
│   │           │   └── JavaScript files
│   │           │
│   │           └── application.properties
│   │
│   └── test/
│
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

---

## ⚙️ Installation and Setup

### Prerequisites

Install the following:

- Java JDK 17 or later
- MySQL Server 8
- Visual Studio Code or IntelliJ IDEA
- Git
- Maven (Maven Wrapper is included)

### Step 1: Clone the repository

```bash
git clone https://github.com/Pradeepa-Nandhagopal/GrandStay-Hotel-Reservation-System.git
```

### Step 2: Open the project

Open the cloned project folder in VS Code or IntelliJ IDEA.

### Step 3: Create the MySQL database

```sql
CREATE DATABASE hotel_reservation;
```

### Step 4: Configure application.properties

Configure the database and email settings using environment variables.

```properties
spring.application.name=hotelreservation

server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/hotel_reservation
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}

spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Step 5: Configure environment variables

Set your database and Gmail credentials as environment variables.

Windows PowerShell:

```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"

$env:MAIL_USERNAME="your_gmail_address"
$env:MAIL_PASSWORD="your_gmail_app_password"
```

Never upload actual passwords or credentials to GitHub.

### Step 6: Build the application

```powershell
.\mvnw.cmd clean install
```

### Step 7: Run the application

```powershell
.\mvnw.cmd spring-boot:run
```

### Step 8: Access the application

Open your browser:

```text
http://localhost:8080
```

---

## 🔐 Security Features

- BCrypt password hashing.
- Environment-based credential configuration.
- Email OTP password recovery.
- OTP expiration.
- Input validation.

**Security note:** The current login implementation is a demonstration and stores the customer ID in browser localStorage. Production deployment requires secure session or token authentication, authorization and additional OTP protections.

---

## 🗄️ Database

The project uses MySQL to store application data.

Main database entities include:

- Customer
- CustomerAccount
- Room
- Booking
- Payment
- Coupon
- Review
- PasswordResetOtp

Hibernate/JPA manages database persistence.

---

## 🔗 GitHub Repository

**Project Repository:**

https://github.com/Pradeepa-Nandhagopal/GrandStay-Hotel-Reservation-System

---

## 👩‍💻 Developer

**Pradeepa Nandhagopal**

GitHub: https://github.com/Pradeepa-Nandhagopal

---

## 🚀 Future Enhancements

- Secure authentication and role-based authorization.
- Real payment gateway integration.
- Improved admin access controls.
- Booking cancellation and refund management.
- Responsive mobile-first design.
- Deployment to a cloud platform.
- Automated testing and CI/CD integration.

---

## 📜 License

This project is developed for educational and portfolio purposes.
