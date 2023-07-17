package tranlong5252.menu;
public class LoginMenu extends ClientMenu {

    public LoginMenu() {
        showMenu();
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
        new MainMenu();
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

    @Override
    public void showMenu() {
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
                    case 0 -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice!");
            }
        } while (choice != 0);
    }
}