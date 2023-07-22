package tranlong5252;

import tranlong5252.menu.LoginMenu;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservationApplication {

    private MySQL mySQL;
    private final Scanner scanner;
    private static ReservationApplication instance;
    private String username;

    public ReservationApplication() {
        instance = this;
        scanner = new Scanner(System.in);
        try {
            mySQL = new MySQL("localhost", 3306, "restaurants", "root", "123!");
            System.out.println("Welcome to Restaurant Reservation System");
            new LoginMenu();
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, "An error has occurred when login to the database: ", ex);
        }
    }

    public static ReservationApplication getInstance() {
        return instance;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String input() {
        String s = scanner.nextLine();
        if (s.isBlank()) s = input();
        return s;
    }
}
