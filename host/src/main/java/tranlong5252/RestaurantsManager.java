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
                createRestaurant(null, null, null);
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
    
    private void createRestaurant(String name, String address, String phone) {
        System.out.println("Create your restaurant");
        if (name == null) {
            System.out.print("Restaurant name: ");
            name = input();
        }
        if (address == null) {
            System.out.println("Restaurant address: ");
            address = input();
        }
        if (phone == null) {
            System.out.print("Restaurant phone: ");
            phone = input();
            if (!Utils.validatePhoneNumber(phone)) {
                System.out.println("Invalid phone number!");
                createRestaurant(name, address, null);
                return;
            }
        }

        System.out.print("Restaurant email: ");
        String email = input();
        if (!Utils.validateEmail(email)) {
            System.out.println("Invalid email!");
            createRestaurant(name, address, phone);
            return;
        }
        mySQL.updateRestaurantDetail(name, address, phone, email);
    }
}
