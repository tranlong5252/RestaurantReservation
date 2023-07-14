package tranlong5252.menu;

import tranlong5252.RestaurantsManager;

public class LoginMenu {
    private final RestaurantsManager main;

    public LoginMenu(RestaurantsManager main) {
        this.main = main;
        int choice = -1;
        do {
            try {
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("0. Exit");
                System.out.print("Your choice: ");
                choice = Integer.parseInt(main.getScanner().nextLine());
                switch (choice) {
                    case 1 -> login();
                    case 2 -> register();
                    case 0 -> exit();
                    default -> System.out.println("Invalid choice!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice!");
            }
        } while (choice != 0);
    }

    private void exit() {
        System.out.println("Goodbye!");
        System.exit(0);
    }

    private void login() {
        System.out.print("Username: ");
        String username = main.getScanner().nextLine();
        System.out.print("Password: ");
        String password = main.getScanner().nextLine();
        if (!main.getMySQL().login(username, password)) {
            System.out.println("Login failed!");
            return;
        }
        System.out.println("Login success!");
        main.setUsername(username);
        new MainMenu(main);
    }

    private void register() {
        System.out.print("Username: ");
        String username = main.getScanner().nextLine();
        System.out.print("Password: ");
        String password = main.getScanner().nextLine();
        System.out.print("Confirm password: ");
        String confirm = main.getScanner().nextLine();
        if (password.equals(confirm)) {
            System.out.println("Register success!");
            main.getMySQL().register(username, password);
        } else {
            System.out.println("Password not match!");
        }
    }
}
