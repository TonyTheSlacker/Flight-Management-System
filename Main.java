import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ReservationList reservationList = new ReservationList();
        FlightList flightList = new FlightList(reservationList);
        Menu menu = new Menu();
        Scanner sc = new Scanner(System.in);
        while(true) {
            int mainchoice = menu.displayMainMenu();
            switch(mainchoice) {
                case 1:
                    flightList.addFlight();
                    break;
                case 2:
                    while(true) {
                        int choiceTwo = menu.displayOption2();
                        switch(choiceTwo) {
                            case 1:
                                reservationList.searchFlights();
                                break;
                            case 2:
                                reservationList.reserveFlight();
                                break;
                            case 3: 
                                System.out.println("Returning to Main Menu...");
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                        if(choiceTwo == 3) {
                            break;
                        }
                    }
                    break;
                case 3:
                    reservationList.checkIn();
                    break;
                case 4:
                    flightList.manageCrew();
                    break;
                case 5:
                    System.out.println("Saving...");
                    System.out.println("Passenger information saved to ReservationID.txt");
                    System.out.println("Flight details saved to FlightDetails.txt");
                    System.out.println("Seat maps saved to SeatMap.txt");
                    break;
                case 6:
                    flightList.printAllListsFromFile();
                    break;
                case 7:
                    sc.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}