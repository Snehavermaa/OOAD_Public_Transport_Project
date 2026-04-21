# BMTC Extension UML Diagrams

Here are the requested UML diagrams rendered with PlantUML.

## 1. Updated Class Diagram

```plantuml
@startuml
abstract class User {
    - Long id
    - String email
    - String password
    - String role
}

class ConductorUser extends User {
    + String getUserType()
}

class Admin extends User {
    + String getUserType()
}

class RegularUser extends User {
    + String getUserType()
}

class Route {
    - Long id
    - String source
    - String destination
    - double distance
}

class Schedule {
    - Long id
    - String travelDate
    - String departureTime
    - Route route
}

class Bus {
    - Long id
    - String number
    - int capacity
    - Route route
}

class Driver {
    - Long id
    - String name
    - String licenseNumber
}

class Ticket {
    - Long id
    - Route route
    - String source
    - String destination
    - double fare
    - int passengerCount
}

class TicketFactory {
    + Ticket createTicket(Route, String, String, int)
}

Route "1" *-- "many" Schedule
Route "1" *-- "many" Bus
Route "1" *-- "many" Ticket
TicketFactory ..> Ticket : Creates
@enduml
```

## 2. Activity Diagrams

### User Booking Flow
```plantuml
@startuml
start
:User Logs In;
:Dashboard Redirect (/user/home);
:Clicks "Search Routes & Book Ticket";
:Enters Source & Destination;
if (Routes Found?) then (Yes)
  :View Schedules;
  :Select Schedule;
  :Enter Payment / Booking details;
  :Confirm Booking;
  :Display Booking Summary;
else (No)
  :Show Error / Empty State;
endif
stop
@enduml
```

### Conductor Ticket Flow
```plantuml
@startuml
start
:Conductor Logs In;
:Dashboard Redirect (/conductor/home);
:Selects Route;
:Selects Source Stop;
:Selects Destination Stop;
:Enters Passenger Count;
:Clicks "Generate Ticket";
:System Calls TicketFactory;
:Calculates Fare based on Distance & count;
:Save Ticket to Database;
:Display Ticket Summary Screen;
stop
@enduml
```

## 3. State Diagram

### Ticket Lifecycle
```plantuml
@startuml
[*] --> Created : TicketFactory initialized
Created --> FareCalculated : Calculate Base Fare * Passenger Count
FareCalculated --> SavedToDB : Repository Save action (Persisted)
SavedToDB --> Issued : Handed/Displayed to Passenger
Issued --> [*]
@enduml
```
