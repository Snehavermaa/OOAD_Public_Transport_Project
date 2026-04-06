# User Management System - Quick Reference Guide

## Implementation Summary

### ✅ What Has Been Implemented

#### 1. **OOP Inheritance Hierarchy**
```
User (Abstract Base Class)
├── Admin (Concrete Class)
└── RegularUser (Concrete Class)
```

**Model Files Created:**
- `model/User.java` - Abstract base class with common properties
- `model/Admin.java` - Admin user with department & permissions
- `model/RegularUser.java` - Regular user with address info

#### 2. **Factory Pattern**
**File:** `factory/UserFactory.java`
- `createUser(userType, email, password, firstName, lastName, phoneNumber)`
- `createAdmin(email, password, firstName, lastName, phoneNumber, department, permissions)`
- `createRegularUser(email, password, firstName, lastName, phoneNumber, address, city, zipCode)`

#### 3. **Data Access Layer**
**File:** `repository/UserRepository.java`
- Extends JpaRepository
- Custom queries: findByEmail, existsByEmail, findByRole, findByRoleAndIsActive

#### 4. **Business Logic Layer**
**Files:**
- `service/UserService.java` - User management (CRUD, activation/deactivation)
- `service/AuthService.java` - Authentication & validation

#### 5. **Controller Layer**
**Files:**
- `controller/HomeController.java` - Home page
- `controller/AuthController.java` - Login, Register, Logout
- `controller/UserManagementController.java` - Admin user management

#### 6. **View Templates**
```
templates/
├── index.html                 # Home page
├── login.html                 # Login page
├── register.html              # Registration page
├── user-dashboard.html        # Regular user dashboard
├── admin-dashboard.html       # Admin dashboard
├── user-management.html       # User list (Admin)
└── create-admin.html          # Create admin form
```

---

## URL Endpoints

### Public Endpoints
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/` | Home page |
| GET | `/auth/login` | Login page |
| POST | `/auth/login` | Handle login |
| GET | `/auth/register` | Registration page |
| POST | `/auth/register` | Handle registration |
| GET | `/auth/logout` | Logout |

### Admin Endpoints
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/auth/admin/dashboard` | Admin dashboard |
| GET | `/admin/users` | List all users |
| GET | `/admin/users/admins` | List admins |
| GET | `/admin/users/regular` | List regular users |
| GET | `/admin/users/{id}` | View user details |
| GET | `/admin/users/create-admin` | Create admin form |
| POST | `/admin/users/create-admin` | Create admin |
| POST | `/admin/users/{id}/deactivate` | Deactivate user |
| POST | `/admin/users/{id}/activate` | Activate user |

### User Endpoints
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/auth/user/dashboard` | User dashboard |

### REST API Endpoints
| Method | URL | Description |
|--------|-----|-------------|
| POST | `/auth/api/register` | API Registration |
| POST | `/auth/api/login` | API Login |

---

## Sample Test Credentials

### Admin User
```
Email: admin@transport.com
Password: admin123
Role: ADMIN
```

### Regular Users
```
User 1:
Email: john@email.com
Password: john123
Role: USER

User 2:
Email: jane@email.com
Password: jane123
Role: USER

User 3:
Email: bob@email.com
Password: bob123
Role: USER
```

---

## Class Diagram

```
┌─────────────────────────────────────────┐
│              User (Abstract)            │
├─────────────────────────────────────────┤
│ - id: Long                              │
│ - email: String                         │
│ - password: String                      │
│ - firstName: String                     │
│ - lastName: String                      │
│ - phoneNumber: String                   │
│ - role: String                          │
│ - createdAt: LocalDateTime              │
│ - isActive: boolean                     │
├─────────────────────────────────────────┤
│ + getUserType(): String (abstract)      │
│ + getters/setters                       │
└────────────┬──────────────────────────┬─┘
             │                          │
      ┌──────▼─────┐           ┌───────▼──────┐
      │    Admin    │           │ RegularUser  │
      ├────────────┤           ├──────────────┤
      │- department│           │- address     │
      │- permissions│          │- city        │
      └────────────┘           │- zipCode     │
                                └──────────────┘


┌──────────────────────────────────┐
│     UserFactory (Singleton)      │
├──────────────────────────────────┤
│ + createUser(...): User          │
│ + createAdmin(...): Admin        │
│ + createRegularUser(...):        │
│   RegularUser                    │
└──────────────────────────────────┘


┌──────────────────────────────────┐
│   UserRepository                 │
├──────────────────────────────────┤
│ + findByEmail(email): Optional   │
│ + existsByEmail(email): boolean  │
│ + findByRole(role): List         │
│ + findByRoleAndIsActive(...):    │
│   List                           │
└──────────────────────────────────┘
```

---

## Database Schema

### Single Table Inheritance
The system uses a single `users` table with a discriminator column:

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_type VARCHAR(31),  -- 'ADMIN' or 'USER'
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone_number VARCHAR(255),
    role VARCHAR(255),
    created_at TIMESTAMP,
    is_active BOOLEAN,
    -- Admin specific columns
    department VARCHAR(255),
    permissions VARCHAR(255),
    -- RegularUser specific columns
    address VARCHAR(255),
    city VARCHAR(255),
    zip_code VARCHAR(255)
);
```

---

## Key Features

### 1. Authentication
- Email and password verification
- Session management with HttpSession
- Role-based access control
- User activation/deactivation

### 2. User Management
- User registration (with optional address)
- Admin creation (with permissions)
- User profile updates
- User deactivation/activation
- User deletion

### 3. Security Features
- Password validation (min 6 chars, letters + numbers)
- Email format validation
- Unique email enforcement
- User active status checking

### 4. Design Patterns
- **Factory Pattern:** UserFactory encapsulates creation logic
- **Inheritance:** User hierarchy (Admin, RegularUser)
- **Repository Pattern:** Data access abstraction
- **Service Layer:** Business logic separation

---

## File Structure

```
vaishnavi-module/
├── src/main/java/com/transport/transport_system/
│   ├── model/
│   │   ├── User.java
│   │   ├── Admin.java
│   │   ├── RegularUser.java
│   │   ├── Booking.java (existing)
│   │   ├── Route.java (existing)
│   │   ├── Schedule.java (existing)
│   │   └── Stop.java (existing)
│   ├── factory/
│   │   └── UserFactory.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── BookingRepository.java (existing)
│   │   ├── RouteRepository.java (existing)
│   │   ├── ScheduleRepository.java (existing)
│   │   └── StopRepository.java (existing)
│   ├── service/
│   │   ├── UserService.java
│   │   ├── AuthService.java
│   │   ├── BookingService.java (existing)
│   │   ├── FareService.java (existing)
│   │   ├── RouteService.java (existing)
│   │   ├── ScheduleService.java (existing)
│   │   ├── StopService.java (existing)
│   │   └── DataLoader.java (updated)
│   ├── controller/
│   │   ├── HomeController.java
│   │   ├── AuthController.java
│   │   ├── UserManagementController.java
│   │   ├── BookingController.java (existing)
│   │   ├── FareController.java (existing)
│   │   ├── RouteController.java (existing)
│   │   ├── ScheduleController.java (existing)
│   │   └── StopController.java (existing)
│   └── TransportSystemApplication.java (existing)
├── src/main/resources/
│   ├── templates/
│   │   ├── index.html
│   │   ├── login.html
│   │   ├── register.html
│   │   ├── user-dashboard.html
│   │   ├── admin-dashboard.html
│   │   ├── user-management.html
│   │   ├── create-admin.html
│   │   └── [other templates]
│   ├── application.properties
│   └── [other resources]
├── pom.xml
└── USER_MANAGEMENT_DOCUMENTATION.md
```

---

## How to Test

### 1. Start the Application
```bash
cd vaishnavi-module
mvn spring-boot:run
```

### 2. Access the Application
Open browser: `http://localhost:8080/`

### 3. Login as Admin
- Email: `admin@transport.com`
- Password: `admin123`
- Verify redirection to `/auth/admin/dashboard`

### 4. Login as Regular User
- Email: `john@email.com`
- Password: `john123`
- Verify redirection to `/auth/user/dashboard`

### 5. Register New User
- Click "Register" on home page
- Fill in all required fields
- Submit and login with new credentials

### 6. Admin Operations
- Login as Admin
- Navigate to `/admin/users`
- View all users, create new admin, manage users

---

## Validation Rules

### Password Requirements
- Minimum 6 characters
- Must contain at least one letter
- Must contain at least one number

### Email Requirements
- Valid email format (user@domain.com)
- Must be unique in database

### Registration Requirements
- All fields mandatory (except address, city, zipCode)
- Passwords must match in confirmation
- Phone number format: any format accepted

---

## Integration Notes

### ✅ No Breaking Changes
- Existing models remain unchanged
- Existing controllers/services work independently
- Authentication layer is isolated
- Can be disabled/bypassed if needed

### ✅ Session Management
- Uses standard HttpSession from Servlet API
- Session attributes: user, userId, userRole, userName
- Session invalidated on logout

### ✅ Database
- Uses existing H2 in-memory database
- Single users table with inheritance
- Can be easily migrated to MySQL/PostgreSQL

---

## Next Steps (Optional)

### Recommended Enhancements
1. **Password Hashing:** Use BCrypt instead of plain text
2. **JWT Authentication:** Replace session with JWT tokens
3. **Email Verification:** Send verification emails
4. **Audit Logging:** Track all user actions
5. **Role Permissions:** Granular permission system
6. **Two-Factor Authentication:** Enhanced security
7. **API Documentation:** Swagger/OpenAPI
8. **Unit Tests:** Add comprehensive test suite

---

## Support & Documentation

- **Full Documentation:** See `USER_MANAGEMENT_DOCUMENTATION.md`
- **Java Docs:** Available in code comments
- **Model Classes:** Well-documented with JavaDoc

---

## Conclusion

The User Management System is fully implemented with:
- ✅ OOP Inheritance Pattern
- ✅ Factory Design Pattern
- ✅ Complete Registration System
- ✅ Complete Login System
- ✅ Role-Based Access Control (Admin/User)
- ✅ User Management Features
- ✅ Beautiful UI Templates
- ✅ No Breaking Changes to Existing Code

The system is ready for use and can be easily extended with additional features!
