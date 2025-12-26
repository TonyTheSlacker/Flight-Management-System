import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FlightList {
    private List<Flight> flights;
    private ReservationList reservationList;

    public FlightList(ReservationList reservationList) {
        this.flights = new ArrayList<>();
        this.reservationList = reservationList;
    }

    public void addFlight() {
        reservationList.loadFromFile();
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("Enter flight details:");
            String flightNumber;
            while(true) {
                System.out.print("Flight Number (Fxyzt, e.g., F0001): ");
                flightNumber = sc.nextLine().toUpperCase();
                if(flightNumber.matches("F\\d{4}") && !isFlightCodeDuplicate(flightNumber)) {
                    break;
                } else {
                    System.out.println("Invalid flight number format or duplicate code. Please use Fxyzt format(e.g., F0001).");
                }
            }
            String departureCity;
            while(true) {
                System.out.print("Departure City: ");
                departureCity = sc.nextLine();
                if(departureCity.matches("^[A-Za-z]+( [A-Za-z]+)*$")) {
                    departureCity = capitalizeWords(departureCity);
                    break;
                } else {
                    System.out.println("Invalid city name. Please enter only letters and capitalize each word.");
                }
            }
            String destinationCity;
            while(true) {
                System.out.print("Destination City: ");
                destinationCity = sc.nextLine();
                if(destinationCity.matches("^[A-Za-z]+( [A-Za-z]+)*$")) {
                    destinationCity = capitalizeWords(destinationCity);
                    break;
                } else {
                    System.out.println("Invalid city name. Please enter only letters and capitalize each word.");
                }
            }
            String departureTime;
            while(true) {
                System.out.print("Departure Time (hh:mm): ");
                departureTime = sc.nextLine();
                if(departureTime.matches("^([01]\\d|2[0-3]):([0-5]\\d)$")) {
                    break;
                } else {
                    System.out.println("Invalid departure time format. Please use hh:mm format.");
                }
            }
            String arrivalTime;
            while(true) {
                System.out.print("Arrival Time (hh:mm): ");
                arrivalTime = sc.nextLine();
                if(arrivalTime.matches("^([01]\\d|2[0-3]):([0-5]\\d)$")) {
                    break;
                } else {
                    System.out.println("Invalid arrival time format. Please use hh:mm format.");
                }
            }
            int departHour = Integer.parseInt(departureTime.split(":")[0]);
            int departMinute = Integer.parseInt(departureTime.split(":")[1]);
            int arriveHour = Integer.parseInt(arrivalTime.split(":")[0]);
            int arriveMinute = Integer.parseInt(arrivalTime.split(":")[1]);
            int duration = (arriveHour - departHour) * 60 + (arriveMinute - departMinute);
            if(duration < 0) {
                duration += 24 * 60; // Add 24 hours for overnight flights
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String dateFlight;
            Date flightDate = null;
            while(true) {
                System.out.print("Date of Flight (dd-MM-yyyy): ");
                dateFlight = sc.nextLine();
                if(isValidDate(dateFlight)) {
                    try {
                        flightDate = sdf.parse(dateFlight);
                        break;
                    } catch(ParseException e) {
                        System.out.println("Error parsing date: " + e.getMessage());
                    }
                } else {
                    System.out.println("Invalid date format or date is in the past. Please use dd-MM-yyyy format.");
                }
            }
            int availableSeats;
            while(true) {
                System.out.print("Available Seats (Maximum 200 seats): ");
                try {
                    availableSeats = Integer.parseInt(sc.nextLine());
                    if(availableSeats >= 1 && availableSeats <= 200) {
                        break;
                    } else {
                        System.out.println("Invalid input. Please enter a number between 1 and 200 for available seats.");
                    }
                } catch(NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number for available seats.");
                }
            }
            Flight flight = new Flight(flightNumber, departureCity, destinationCity, departureTime, arrivalTime, duration, availableSeats, flightDate);
            flights.add(flight);
            System.out.println("Flight added successfully.");
            System.out.println(flight);
            try(FileWriter writer = new FileWriter("SeatMap.txt", true)) { //save
                writer.write("Flight Code: " + flight.getFlightNumber() + "\n");
                writer.write("Seat Map:\n");
                for(int seatNumber = 1; seatNumber <= availableSeats; seatNumber++) {
                    writer.write(seatNumber + " ");
                }
                writer.write("\n\n");
                System.out.println("Seat map for Flight " + flight.getFlightNumber() + " saved to SeatMap.txt");
            } catch (IOException e) {
                System.out.println("Error saving seat map to file: " + e.getMessage());
            }
            saveToFile();
            System.out.print("Do you want to add another flight?(Y/N): ");
            String choice = sc.nextLine().toLowerCase();
            if(!choice.equals("y")) {
                break;
            }
        }
    }
    
    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);
        try {
            Date parsedDate = sdf.parse(date);
            Date currentDate = new Date();
            return !parsedDate.before(currentDate);
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isFlightCodeDuplicate(String flightCode) {
        try(BufferedReader reader = new BufferedReader(new FileReader("FlightDetails.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Flight Number: ")) {
                    String existingFlightCode = line.split(": ")[1];
                    if (existingFlightCode.equals(flightCode)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading flight details file: " + e.getMessage());
        }
        return false; // No duplicates found
    }
    
    private String capitalizeWords(String input) {
        return input.toUpperCase();
    }

    public void saveToFile() {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("FlightDetails.txt", true))) {
            for(Flight flight: flights) {
                writer.write("Flight Number: " + flight.getFlightNumber() + "\n");
                writer.write("Departure City: " + flight.getDepartCity() + "\n");
                writer.write("Destination City: " + flight.getDestiCity() + "\n");
                writer.write("Departure Time: " + flight.getDepartTime() + "\n");
                writer.write("Arrival Time: " + flight.getArriveTime() + "\n");
                writer.write("Duration: " + flight.getDuration()+ "\n");
                writer.write("Available Seats: " + flight.getAvailSeat() + "\n");
                writer.write("Date: " + flight.getDateFlight() + "\n\n");
            }
            System.out.println("Flight details saved to FlightDetails.txt");
        } catch (IOException e) {
            System.out.println("Error saving flight details to file: " + e.getMessage());
        }
    } 

    public String loadSeatMapFromFile(String flightNumber) {
        try(BufferedReader reader = new BufferedReader(new FileReader("SeatMap.txt"))) {
            String line;
            StringBuilder seatMapBuilder = new StringBuilder();
            while((line = reader.readLine()) != null) {
                if(line.startsWith("Flight Code: " + flightNumber)) {
                    reader.readLine(); // Skip the "Seat Map:" line
                    while ((line = reader.readLine()) != null && !line.isEmpty()) {
                        seatMapBuilder.append(line);
                    }
                    return seatMapBuilder.toString().trim();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading seat map from file: " + e.getMessage());
        }
        return ""; // empty string if error or seat map not found
    }
    
    public void displaySeatMap(String seatMap) {
        System.out.println("Seat Map: ");
        System.out.println(seatMap);
    }    
    
    public void manageCrew() {
        try(BufferedReader reader = new BufferedReader(new FileReader("CrewMember.txt"))) {
            String line;
            System.out.println("----------------------------------------------------------------------");
            System.out.printf("| %-25s | %-20s | %-15s |\n", "Crew Type", "Available", "Name");
            System.out.println("----------------------------------------------------------------------");
            while((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if(parts.length == 4) {
                    String crewType = getCrewType(Integer.parseInt(parts[0]));
                    boolean available = Integer.parseInt(parts[1]) == 1;
                    String name = parts[3];
                    System.out.printf("| %-25s | %-20s | %-15s |\n", crewType, available, name);
                } else if(parts.length == 5) {
                    String crewType = getCrewType(Integer.parseInt(parts[0]));
                    boolean available = Integer.parseInt(parts[1]) == 1;
                    String[] flights = parts[2].split("-");
                    String name = parts[4];
                    for(String flight: flights) {
                        System.out.printf("| %-25s | %-20s | %-15s |\n", crewType, available, name + " (Flight " + flight + ")");
                    }
                }
            }
            System.out.println("----------------------------------------------------------------------");
        } catch (IOException e) {
            System.out.println("Error reading crew member data: " + e.getMessage());
        }
    }    

    private String getCrewType(int code) {
        switch(code) {
            case 0:
                return "Pilot";
            case 1:
                return "Flight Attendant";
            case 2:
                return "Ground Attendant";
            default:
                return "Unknown";
        }
    }
    
    public void printAllListsFromFile() {
        List<Flight> flightsFromFile = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader("FlightDetails.txt"))) {
            String line;
            Flight currentFlight = null;
            while((line = reader.readLine()) != null) {
                if (line.startsWith("Flight Number: ")) {
                    currentFlight = new Flight();
                    currentFlight.setFlightNumber(line.substring("Flight Number: ".length()));
                } else if(line.startsWith("Departure City: ")) {
                    currentFlight.setDepartCity(line.substring("Departure City: ".length()));
                } else if(line.startsWith("Destination City: ")) {
                    currentFlight.setDestiCity(line.substring("Destination City: ".length()));
                } else if(line.startsWith("Departure Time: ")) {
                    currentFlight.setDepartTime(line.substring("Departure Time: ".length()));
                } else if(line.startsWith("Arrival Time: ")) {
                    currentFlight.setArriveTime(line.substring("Arrival Time: ".length()));
                } else if(line.startsWith("Duration: ")) {
                    currentFlight.setDuration(Integer.parseInt(line.substring("Duration: ".length())));
                } else if(line.startsWith("Available Seats: ")) {
                    currentFlight.setAvailSeat(Integer.parseInt(line.substring("Available Seats: ".length())));
                } else if(line.startsWith("Date: ")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                    try {
                        Date date = sdf.parse(line.substring("Date: ".length()));
                        currentFlight.setDateFlight(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    flightsFromFile.add(currentFlight);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        Collections.sort(flightsFromFile, new Comparator<Flight>() {
            @Override
            public int compare(Flight flight1, Flight flight2) {
                return flight2.getDateFlight().compareTo(flight1.getDateFlight());
            }
        });
        System.out.println("--------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-10s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s |\n", "Flight", "Departure", "Destination", "Departure Time", "Arrival Time", "Duration(minutes)", "Flight Date");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------");
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        for(Flight flight: flightsFromFile) {
            System.out.printf("| %-10s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s |\n",
                    flight.getFlightNumber(), flight.getDepartCity(), flight.getDestiCity(),
                    flight.getDepartTime(), flight.getArriveTime(), flight.getDuration(), sdf.format(flight.getDateFlight()));
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------------------");
    }
}