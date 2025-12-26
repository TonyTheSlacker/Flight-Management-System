# Airline Reservation System (Java)

![Language](https://img.shields.io/badge/Language-Java-ED8B00?logo=java&logoColor=white)
![Pattern](https://img.shields.io/badge/Pattern-Dependency_Injection-blue)
![Type](https://img.shields.io/badge/Type-Console_Simulation-4EAA25)

A comprehensive console-based simulation of an Airline Reservation System (ARS). This application manages the complex relationships between **Flights**, **Passengers**, and **Reservations** without relying on a SQL database.

It demonstrates advanced Object-Oriented principles, including manual **Dependency Injection**, custom **Comparators** for scheduling, and robust string parsing for data persistence.

---

## ✈️ Key Features

### 📅 Flight Scheduling Engine
* **Management:** create and modify flight schedules with validation for unique Flight IDs (`Fxxxx` format).
* **Sorting Algorithm:** Implements `Collections.sort` with a custom `Comparator` to organize flights dynamically by departure time.

### 🎫 Reservation Pipeline
* **Booking Logic:** A multi-step process that links specific Passengers to Flights, generating a unique Reservation ID.
* **Seat Allocation:** Handles "Check-In" logic to assign gates and seats to passengers.
* **Capacity Checks:** Automatically validates available seats (`AvailSeat`) before confirming a reservation.

### 💾 Data Persistence
* **State Preservation:** The system creates and reads from structured text files (`ReservationID.txt`, `FlightDetails.txt`) to maintain state between sessions.
* **Complex Parsing:** Utilizes `BufferedReader` and regex to reconstruct complex Object graphs (Reservation -> Flight -> List<Passenger>) from raw text.

---

## 🛠️ Technical Highlights

### Dependency Injection
To ensure data consistency between different modules, the system uses constructor injection to share the `FlightList` instance with the `ReservationList`.

```java
// From ReservationList.java
public ReservationList(FlightList flightList) {
    this(); 
    this.flightList = flightList; // Logic injection for data consistency
}
```

### Relational Data Modeling

Unlike simple list-based apps, this project models real-world constraints where objects encapsulate other objects:
```java
public class Reservation {
    private int reservationId;
    private Flight flight; // Object composition
    ArrayList<Passenger> passenger; // One-to-Many relationship
    // ...
}
```


## 🚀 How to Run

  1. Clone the repository:

  ```git clone https://github.com/TonyTheSlacker/FlightReservation.git```

  2. Compile the source:

  ```javac Main.java```

  3. Run the application:

  ```java Main```
  
---
*****Note: The application expects data files (FlightDetails.txt, etc.) to exist. If they are missing, the system will start with an empty database and generate them upon saving.*****
