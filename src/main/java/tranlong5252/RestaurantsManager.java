package tranlong5252;

import tranlong5252.menu.LoginMenu;

import java.util.Scanner;

public class RestaurantsManager {

    private final MySQL mySQL;
    private final Scanner scanner;
    private String username;
    private int restaurantId;

    public RestaurantsManager() {
        scanner = new Scanner(System.in);
        mySQL = new MySQL("localhost", 3306, "restaurants", "root", "123!");

		System.out.println("Welcome to Restaurant Reservation System");
        System.out.println("Please login or register to continue");
		new LoginMenu(this);
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
