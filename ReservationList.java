import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class ReservationList {
    private List<Reservation> reservations;
    private List<Flight> flights;
    private FlightList flightList;

    public ReservationList() {
        this.reservations = new ArrayList<>();
        this.flights = new ArrayList<>();
        this.flightList = new FlightList(this); // Inject FlightList 
    }

    public ReservationList(FlightList flightList) {
        this(); // default constructor to initialize lists
        this.flightList = flightList; // Inject FlightList 
    }

    public void searchFlights() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Departure City: ");
        String departureCity = sc.nextLine();
        departureCity = capitalizeWords(departureCity); 
        System.out.print("Enter Destination City: ");
        String destinationCity = sc.nextLine();
        destinationCity = capitalizeWords(destinationCity);
        loadFromFile();
        System.out.println("--------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-10s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s |\n", "Flight", "Departure", "Destination", "Departure Time", "Arrival Time", "Duration(minutes)", "Flight Date");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------");
        for(Flight flight: flights) {
            if(flight.getDepartCity().equalsIgnoreCase(departureCity) && flight.getDestiCity().equalsIgnoreCase(destinationCity) && flight.getAvailSeat() > 0) {
                System.out.printf("| %-10s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s |\n",
                        flight.getFlightNumber(), flight.getDepartCity(), flight.getDestiCity(),
                        flight.getDepartTime(), flight.getArriveTime(), flight.getDuration(), flight.getDateFlight());
            }
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------------------");
    }
      
    private String capitalizeWords(String input) {
        return input.toUpperCase();
    }

    public void reserveFlight() {
        Scanner sc = new Scanner(System.in);
        int reservationId = generateUniqueReservationId();
        System.out.println("Available Flights: ");
        for(Flight flight: flights) {
            if(flight.getAvailSeat() > 0) {
                System.out.println(flight.getFlightNumber());
            }
        }
        Flight selectedFlight = null;
        while(selectedFlight == null) {
            System.out.print("Select a Flight for Reservation(Enter Flight Number): ");
            String selectedFlightNumber = sc.nextLine();
            selectedFlight = findFlightByNumber(selectedFlightNumber);
            if(selectedFlight != null) {
                if(selectedFlight.getAvailSeat() > 0) {
                    ArrayList<Passenger> passengers = new ArrayList<>();
                    int numPassengers = 0;
                    do {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        System.out.print("Enter Passenger Name: ");
                        String passengerName = sc.nextLine();
                        System.out.print("Enter Passenger ID: ");
                        int passengerID = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter Passenger Birthdate(dd/mm/yyyy): ");
                        String birthdateStr = sc.nextLine();
                        Date birthdate = null;
                        try {
                            birthdate = dateFormat.parse(birthdateStr);
                        } catch(ParseException e) {
                            System.out.println("Invalid birthdate format. Please use dd/MM/yyyy.");
                            continue;
                        }
                        passengers.add(new Passenger(passengerName, passengerID, birthdate));
                        numPassengers++;
                        System.out.print("Do you want to add more passengers?(Y/N): ");
                        String addMore = sc.nextLine();
                        if(!addMore.equalsIgnoreCase("Y")) {
                            break;
                        }
                    } while(true);
                    System.out.print("Enter Gate Number: ");
                    int gateNum = sc.nextInt();
                    sc.nextLine();
                    Reservation reservation = new Reservation(reservationId, gateNum, selectedFlight, passengers);
                    reservations.add(reservation);
                    selectedFlight.setAvailSeat(selectedFlight.getAvailSeat() - numPassengers); // Reduce available seats
                    System.out.println("Reservation successful. Your Reservation ID is: " + reservationId);
                    saveReservationIdToFile(reservationId, gateNum, passengers, selectedFlightNumber);
                } else {
                    System.out.println("Flight is full. Please select another flight.");
                    selectedFlight = null;
                }
            } else {
                System.out.println("Invalid flight number. Please enter a valid flight number.");
            }
        }
    }
            
    private Flight findFlightByNumber(String flightNumber) {
        for(Flight flight: flights) {
            if(flight.getFlightNumber().equalsIgnoreCase(flightNumber)) {
                return flight;
            }
        }
        return null;
    }

    private int generateUniqueReservationId() {
        Random rand = new Random();
        int min = 1000000;
        int max = 9999999;
        return rand.nextInt((max - min) + 2) + min;
    }
    
    public void loadFromFile() {
        try(BufferedReader reader = new BufferedReader(new FileReader("FlightDetails.txt"))) {
            String line;
            Flight currentFlight = null;
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            while((line = reader.readLine()) != null) {
                if(line.startsWith("Flight Number: ")) {
                    if(currentFlight != null) {
                        flights.add(currentFlight);
                    }
                    String flightNumber = line.split(": ")[1];
                    String departureCity = reader.readLine().split(": ")[1];
                    String destinationCity = reader.readLine().split(": ")[1];
                    String departureTime = reader.readLine().split(": ")[1];
                    String arrivalTime = reader.readLine().split(": ")[1];
                    int duration = Integer.parseInt(reader.readLine().split(": ")[1]);
                    int availableSeats = Integer.parseInt(reader.readLine().split(": ")[1]);
                    String dateFlightString = reader.readLine().split(": ")[1];
                    Date dateFlight = sdf.parse(dateFlightString);
                    currentFlight = new Flight(flightNumber, departureCity, destinationCity, departureTime, arrivalTime, duration, availableSeats, dateFlight);
                }
            }
            if(currentFlight != null) {
                flights.add(currentFlight);
            }
            System.out.println("Flight details loaded from FlightDetails.txt");
        } catch(IOException | ParseException e) {
            System.out.println("Error loading flight details from file: " + e.getMessage());
        }
    }
    
    private void saveReservationIdToFile(int reservationId, int gateNum, ArrayList<Passenger> passengers, String flightCode) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("ReservationID.txt", true))) {
            writer.write("Reservation ID: " + Integer.toString(reservationId));
            writer.newLine();
            writer.write("Gate Number: " + gateNum);
            writer.newLine();
            writer.write("Flight: " + flightCode);
            writer.newLine();
            writer.write("Number of Passengers: " + passengers.size()); // Count passengers
            writer.newLine();
            writer.write("Passenger Information:");
            writer.newLine();
            for(Passenger passenger: passengers) {
                writer.write("Name: " + passenger.getPname());
                writer.newLine();
                writer.write("ID: " + passenger.getID());
                writer.newLine();
                writer.write("Birthdate: " + passenger.getBirthdate());
                writer.newLine();
            }
            writer.newLine();
        } catch(IOException e) {
            System.out.println("Error saving reservation information to file: " + e.getMessage());
        }
    }
    
    public void checkIn() {
        loadFromReservationIdFile();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Reservation ID: ");
        int reservationId = sc.nextInt();
        sc.nextLine();
        if(isReservationIdValid(reservationId)) {
            Reservation selectedReservation = findReservationById(reservationId);
            if(selectedReservation != null) {
                Flight selectedFlight = selectedReservation.getFlight();
                ArrayList<Integer> availableSeats = new ArrayList<Integer>();
                for(int seatNumber = 1; seatNumber <= selectedFlight.getAvailSeat(); seatNumber++) {
                    availableSeats.add(seatNumber);
                }
                System.out.println("Passenger List: ");
                ArrayList<Passenger> passengers = selectedReservation.getPassenger();
                for(int i = 0; i < passengers.size(); i++) {
                    System.out.println((i + 1) + ". " + passengers.get(i).getPname());
                }
                String seatMap = flightList.loadSeatMapFromFile(selectedFlight.getFlightNumber());
                if(!seatMap.isEmpty()) {
                    flightList.displaySeatMap(seatMap);
                    for(int i = 0; i < passengers.size(); i++) {
                        int selectedSeat;
                        while(true) {
                            System.out.print("Please select a seat for passenger " + passengers.get(i).getPname() + " (0 means taken): ");
                            selectedSeat = sc.nextInt();
                            sc.nextLine();
                            if(availableSeats.contains(selectedSeat) && selectedSeat != 0) {
                                availableSeats.remove(Integer.valueOf(selectedSeat)); // Remove seat
                                int startIdx = (selectedSeat - 1) * 2;
                                char[] seatArray = seatMap.toCharArray();
                                seatArray[startIdx] = '0';
                                seatMap = new String(seatArray);
                                selectedFlight.setAvailSeat(availableSeats.size());
                                System.out.println("Seat " + selectedSeat + " successfully checked in.");
                                System.out.println("Boarding Pass for " + passengers.get(i).getPname() + ":");
                                System.out.println("Reservation ID: " + selectedReservation.getReservationId());
                                System.out.println("Flight Details: " + selectedFlight);
                                try(FileWriter writer = new FileWriter("ReservationID.txt", true)) {
                                    writer.write("Boarding Pass for " + passengers.get(i).getPname() + ":\n");
                                    writer.write("Reservation ID: " + selectedReservation.getReservationId() + "\n");
                                    writer.write("Flight Details: " + selectedFlight + "\n");
                                    writer.write("\n");
                                } catch(IOException e) {
                                    System.out.println("Error saving boarding pass in ReservationID.txt: " + e.getMessage());
                                }
                                flightList.saveToFile();
                                break;
                            } else {
                                System.out.println("Invalid seat selection. Please choose an available seat.");
                            }
                        }
                    }
                } else {
                    System.out.println("Error: Seat map for Flight " + selectedFlight.getFlightNumber() + " not found.");
                }
            } else {
                System.out.println("There is no valid flight for the Reservation ID.");
            }
        } else {
            System.out.println("Invalid Reservation ID.");
        }
    }    
    
    private boolean isReservationIdValid(int reservationId) {
        try(BufferedReader reader = new BufferedReader(new FileReader("ReservationID.txt"))) {
            String line;
            while((line = reader.readLine()) != null) {
                if(line.startsWith("Reservation ID: ")) {
                    int storedId = Integer.parseInt(line.split(": ")[1]);
                    if (storedId == reservationId) {
                        return true;
                    }
                }
            }
        } catch(IOException e) {
            System.out.println("Error reading reservation IDs from file: " + e.getMessage());
        }
        return false;
    }    
    
    private Reservation findReservationById(int reservationId) {
        for(Reservation reservation: reservations) {
            if(reservation.getReservationId() == reservationId) {
                return reservation;
            }
        }
        return null;
    }   

    public void loadFromReservationIdFile() {
        try(BufferedReader reader = new BufferedReader(new FileReader("ReservationID.txt"))) {
            String line;
            Reservation currentReservation = null;
            while((line = reader.readLine()) != null) {
                if(line.startsWith("Reservation ID: ")) {
                    if(currentReservation != null) {
                        reservations.add(currentReservation);
                    }
                    int reservationId = Integer.parseInt(line.split(": ")[1]);
                    int gateNum = -1;
                    String flightCode = null;
                    ArrayList<Passenger> passengers = new ArrayList<>();
                    while((line = reader.readLine()) != null) {
                        if(line.startsWith("Gate Number: ")) {
                            gateNum = Integer.parseInt(line.split(": ")[1]);
                        } else if(line.startsWith("Flight: ")) {
                            flightCode = line.split(": ")[1];
                        } else if(line.startsWith("Number of Passengers: ")) {
                            // Skip line
                        } else if(line.startsWith("Passenger Information:")) {
                            // Skip line
                        } else if(line.startsWith("Name: ")) {
                            String passengerName = line.split(": ")[1];
                            line = reader.readLine();
                            int passengerId = Integer.parseInt(line.split(": ")[1]);
                            line = reader.readLine();
                            String passengerBirthdateStr = line.split(": ")[1];
                            Date passengerBirthdate = null;
                            try {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                                passengerBirthdate = dateFormat.parse(passengerBirthdateStr);
                            } catch (ParseException e) {
                                System.out.println("Error parsing passenger birthdate: " + e.getMessage());
                            }
                            passengers.add(new Passenger(passengerName, passengerId, passengerBirthdate));
                        } else {
                            //something
                        }
                        String nextLine = reader.readLine();
                        if(nextLine == null || nextLine.isEmpty()) {
                            break;
                        }
                    }
                    if(gateNum != -1 && flightCode != null && !passengers.isEmpty()) {
                        Flight flight = findFlightByNumber(flightCode);
                        currentReservation = new Reservation(reservationId, gateNum, flight, passengers);
                    }
                }
            }
            if(currentReservation != null) {
                reservations.add(currentReservation);
            }
            System.out.println("Reservation details loaded from ReservationID.txt");
        } catch (IOException e) {
            System.out.println("Error loading reservation details from file: " + e.getMessage());
        }
    }     
}