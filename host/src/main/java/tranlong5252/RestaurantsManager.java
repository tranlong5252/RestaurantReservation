package tranlong5252;

import tranlong5252.menu.MainMenu;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestaurantsManager {

    private MySQL mySQL;
    private final Scanner scanner;
    private static RestaurantsManager instance;

    public RestaurantsManager() {
        instance = this;
        scanner = new Scanner(System.in);
        try {
            mySQL = new MySQL("localhost", 3306, "restaurants", "root", "123!");
            System.out.println("Welcome to Restaurant Reservation System");
            if (!mySQL.existRestaurant()) {
                System.out.println("Create your restaurant:");
                System.out.println("Name: ");
                String name = input();
                System.out.println("Address: ");
                String address = input();
                System.out.println("Phone number: ");
                String phone = input();
                System.out.println("Email: ");
                String email = scanner.nextLine();
                mySQL.updateRestaurantDetail(name, address, phone, email);
            }
            new MainMenu();
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, "An error has occurred when login to the database: ", ex);
            return;
        }
    }

    public static RestaurantsManager getInstance() {
        return instance;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public String input() {
        String s = scanner.nextLine();
        if (s.isBlank()) s = input();
        return s;
    }
}
