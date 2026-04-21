# User Management & Authentication System Documentation

## Overview
This document describes the User Management and Authentication System implemented for the Public Transport System application. The system uses **Object-Oriented Programming (OOP)** concepts with **Inheritance** and the **Factory Design Pattern** to manage user registration, login, and role-based access control.

---

## Architecture & Design Patterns

### 1. Inheritance Hierarchy

The system implements a class hierarchy to represent different user types:

```
                    ┌─────────────┐
                    │    User     │ (Abstract Base Class)
                    │  (Abstract) │
                    └──────┬──────┘
                           │
                ┌──────────┴──────────┐
                │                     │
           ┌────▼────┐           ┌────▼────┐
           │  Admin   │           │ Regular │
           │          │           │  User   │
           └──────────┘           └─────────┘
```

#### **User (Abstract Base Class)**
- **Location:** `model/User.java`
- **Responsibilities:**
  - Stores common properties: id, email, password, firstName, lastName, phoneNumber, role, createdAt, isActive
  - Defines abstract method `getUserType()` for polymorphic behavior
  - Uses JPA `@Inheritance(strategy = InheritanceType.SINGLE_TABLE)` for database mapping

#### **Admin Class**
- **Location:** `model/Admin.java`
- **Extends:** User
- **Additional Properties:**
  - `department` - Department of the admin
  - `permissions` - Admin permissions level
- **Role:** ADMIN
- **Responsibilities:** Administrative tasks like user management

#### **RegularUser Class**
- **Location:** `model/RegularUser.java`
- **Extends:** User
- **Additional Properties:**
  - `address` - Street address
  - `city` - City name
  - `zipCode` - Postal code
- **Role:** USER
- **Responsibilities:** Booking tickets and accessing public features

### 2. Factory Pattern Implementation

The **UserFactory** class encapsulates the creation logic for different user types.

- **Location:** `factory/UserFactory.java`
- **Key Methods:**
  ```java
  // Create user by type
  public static User createUser(String userType, String email, String password, 
                                 String firstName, String lastName, String phoneNumber)
  
  // Create specific user types
  public static Admin createAdmin(...)
  public static RegularUser createRegularUser(...)
  ```
- **Benefits:**
  - Centralized user creation logic
  - Easy to extend for new user types
  - Decouples object creation from business logic

**Supported User Types:**
- `ADMIN` → Creates Admin instance
- `USER` → Creates RegularUser instance

---

## Database Structure

### User Table (Single Table Inheritance)
```sql
users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_type VARCHAR(31),  -- ADMIN or USER discriminator
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  phone_number VARCHAR(255) NOT NULL,
  role VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  is_active BOOLEAN NOT NULL,
  -- Admin specific
  department VARCHAR(255),
  permissions VARCHAR(255),
  -- RegularUser specific
  address VARCHAR(255),
  city VARCHAR(255),
  zip_code VARCHAR(255)
)
```

---

## Core Components

### 1. Repositories
**UserRepository** (`repository/UserRepository.java`)
- Extends JpaRepository for database operations
- Custom query methods:
  - `findByEmail(String email)` - Lookup user by email
  - `existsByEmail(String email)` - Check email existence
  - `findByRole(String role)` - Get all users by role
  - `findByRoleAndIsActive(String role, boolean isActive)` - Get active users by role

### 2. Services

#### UserService (`service/UserService.java`)
**Responsibilities:**
- User registration and creation
- User profile management
- User activation/deactivation
- User query operations

**Key Methods:**
```java
public User registerUser(...)                    // Register regular user
public RegularUser registerUserWithAddress(...)  // Register with address
public Admin registerAdmin(...)                  // Register admin
public User updateUser(...)                      // Update profile
public User deactivateUser(Long id)              // Deactivate account
public User activateUser(Long id)                // Activate account
public void deleteUser(Long id)                  // Delete user
public List<User> getUsersByRole(String role)    // Get users by role
```

#### AuthService (`service/AuthService.java`)
**Responsibilities:**
- User authentication
- Password and email validation
- Role-based verification

**Key Methods:**
```java
public Optional<User> authenticate(String email, String password)
public boolean isAdmin(User user)
public boolean isRegularUser(User user)
public boolean isPasswordStrong(String password)
public boolean isValidEmail(String email)
public boolean logout()
```

### 3. Controllers

#### AuthController (`controller/AuthController.java`)
**Endpoints:**
- `GET /auth/login` - Display login page
- `POST /auth/login` - Handle user login
- `GET /auth/register` - Display registration page
- `POST /auth/register` - Handle user registration
- `GET /auth/logout` - Logout user
- `GET /auth/user/dashboard` - Regular user dashboard
- `GET /auth/admin/dashboard` - Admin dashboard
- `POST /auth/api/register` - REST API registration
- `POST /auth/api/login` - REST API login

#### UserManagementController (`controller/UserManagementController.java`)
**Endpoints (Admin Only):**
- `GET /admin/users` - List all users
- `GET /admin/users/admins` - List admins
- `GET /admin/users/regular` - List regular users
- `GET /admin/users/{id}` - View user details
- `GET /admin/users/create-admin` - Show create admin form
- `POST /admin/users/create-admin` - Create new admin
- `POST /admin/users/{id}/update` - Update user info
- `POST /admin/users/{id}/deactivate` - Deactivate user
- `POST /admin/users/{id}/activate` - Activate user
- `POST /admin/users/{id}/delete` - Delete user

---

## Sample Users (Data Loader)

The system automatically creates sample users on startup:

### Admin User
```
Email: admin@transport.com
Password: admin123
Name: Admin User
Department: Management
Permissions: ALL_PERMISSIONS
```

### Regular Users
```
1. Email: john@email.com, Password: john123
2. Email: jane@email.com, Password: jane123
3. Email: bob@email.com, Password: bob123 (with address info)
```

---

## User Flow

### Registration Flow
```
User visits /auth/register
    ↓
Fills registration form
    ↓
Validates email and password
    ↓
Creates RegularUser via UserFactory
    ↓
Saves to database
    ↓
Redirects to login page
```

### Login Flow
```
User visits /auth/login
    ↓
Enters email and password
    ↓
AuthService authenticates credentials
    ↓
Creates session with user data
    ↓
Redirects based on role:
  - ADMIN → /auth/admin/dashboard
  - USER → /auth/user/dashboard
```

### Admin User Creation Flow
```
Admin visits /admin/users/create-admin
    ↓
Fills admin creation form
    ↓
Validates input (email, password strength)
    ↓
Creates Admin via UserFactory
    ↓
Saves to database
    ↓
Redirects to admin list
```

---

## Views/Templates

### HTML Templates Created

1. **login.html** - User login page
2. **register.html** - User registration page
3. **user-dashboard.html** - Regular user dashboard
4. **admin-dashboard.html** - Admin dashboard
5. **user-management.html** - Admin user management list
6. **create-admin.html** - Admin creation form
7. **index.html** - Home page with login/register links

---

## Security Features

### 1. Authentication
- Email and password verification
- User active status checking
- Session-based authentication using `HttpSession`

### 2. Password Validation
- Minimum 6 characters
- Must contain letters
- Must contain numbers

### 3. Email Validation
- Standard email format check
- Unique email enforcement (database unique constraint)

### 4. Access Control
- Role-based access (ADMIN/USER)
- Session validation
- Redirects unauthorized users to login

---

## OOP Principles Applied

### 1. Encapsulation
- Private fields with public getters/setters
- Validation logic in services
- Separation of concerns

### 2. Inheritance
- Abstract User class with concrete Admin and RegularUser subclasses
- Polymorphic behavior through abstract methods
- Code reuse through inheritance

### 3. Abstraction
- Abstract User class hides implementation details
- Service classes abstract business logic
- Repository pattern abstracts data access

### 4. Polymorphism
- Different user types implement `getUserType()` differently
- Controllers handle both Admin and RegularUser polymorphically
- Factory pattern uses polymorphic creation

### 5. Design Patterns
- **Factory Pattern:** UserFactory for creating users
- **Singleton Pattern:** Spring Service beans
- **Repository Pattern:** Data access layer
- **DAO Pattern:** UserRepository

---

## Integration with Existing System

### No Breaking Changes
- Existing Route, Schedule, Booking, Stop models remain unchanged
- New authentication layer added without modifying existing features
- All existing controllers and services work independently

### Data Flow
```
User Authentication Layer (NEW)
          ↓
User Management Layer (NEW)
          ↓
Existing Transport Logic
(Routes, Schedules, Bookings, Stops)
```

---

## Testing Sample Data

### Login Credentials
```
Admin Access:
- Email: admin@transport.com
- Password: admin123

User Access:
- Email: john@email.com
- Password: john123
```

### Testing Steps
1. Start the application
2. Navigate to http://localhost:8080/
3. Click "Login" or "Register"
4. Test with sample credentials
5. Verify role-based redirects

---

## Future Enhancement Opportunities

1. **Password Encryption:** Implement BCrypt for password hashing
2. **JWT Tokens:** Replace session-based authentication with JWT
3. **Email Verification:** Add email verification during registration
4. **Two-Factor Authentication:** Implement MFA for security
5. **Role-Based Permissions:** Granular permission system
6. **Audit Logging:** Track user actions
7. **Profile Management:** Allow users to update their profiles
8. **Password Reset:** Implement password recovery mechanism

---

## Dependencies

### Maven Dependencies Added
- spring-boot-starter-data-jpa
- spring-boot-starter-thymeleaf
- spring-boot-starter-webmvc
- jakarta.servlet-api (implicit via spring-boot-starter-webmvc)

### Session Management
- HttpSession (Java Servlet API)
- Spring Session (optional, for distributed sessions)

---

## Conclusion

The User Management and Authentication System successfully implements Object-Oriented Programming principles with Inheritance and the Factory Design Pattern. It provides a robust foundation for role-based access control while maintaining compatibility with the existing transport management system.

The architecture is extensible and can be easily enhanced with additional features like JWT authentication, email verification, and advanced permission management.
