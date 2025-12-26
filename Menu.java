import java.util.Scanner;

public class Menu {
    private Scanner sc;
    public Menu() {
        this.sc = new Scanner(System.in);
    }

    public int displayMainMenu() {
        System.out.println("Flight Management System:");
        System.out.println("1. Flight schedule management");
        System.out.println("2. Passenger reservation and booking");
        System.out.println("3. Passenger check-in and seat allocation");
        System.out.println("4. Crew Management and Administrator Access");
        System.out.println("5. Save to file");
        System.out.println("6. Print all lists from file");
        System.out.println("7. Quit Program");
        return getUserChoice();
    }

    public int displayOption2() {
        System.out.println("1. Search for available flights");
        System.out.println("2. Reserve a flight");
        System.out.println("3. Back to Main Menu");
        return getUserChoice();
    }

    public int getUserChoice() {
        System.out.print("Enter your choice: ");
        return sc.nextInt();
    }
}