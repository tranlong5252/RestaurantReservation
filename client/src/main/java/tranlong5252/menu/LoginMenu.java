package tranlong5252.menu;
public class LoginMenu extends ClientMenu {

    public LoginMenu() {
        showMenu();
    }

    private void login() {
        System.out.print("Username: ");
        String username = main.getScanner().nextLine();
        if (invalidUsername(username)) {
            System.out.println("Invalid username, the username must contains only [A-Za-z0-9_] and length from 5 to 32 characters!");
            return;
        }
        System.out.print("Password: ");
        String password = main.getScanner().nextLine();
        if (!main.getMySQL().login(username, password)) {
            System.out.println("Login failed, please check your username and password!");
            return;
        }
        System.out.println("Login success!");
        main.setUsername(username);
        new MainMenu();
    }

    private void register() {
        System.out.print("Username: ");
        String username = main.getScanner().nextLine();
        if (invalidUsername(username)) {
            System.out.println("Invalid username, the username must contains only [A-Za-z0-9_] and length from 5 to 32 characters!");
            return;
        }
        if (main.getMySQL().getUser(username) != null) {
            System.out.println("Username already exists!");
            return;
        }
        System.out.print("Password: ");
        String password = main.getScanner().nextLine();
        if (invalidPassword(password)) {
            System.out.println("Invalid password, the password should not be empty, not contains space, length from 8 to 32 characters, not contains special characters other than @!#$%^&*()_+-=");
            return;
        }
        System.out.print("Confirm password: ");
        String confirm = main.getScanner().nextLine();
        if (!password.equals(confirm)) {
            System.out.println("Password not match!");
            return;

        }
        System.out.println("Register success!");
        main.getMySQL().register(username, password);
    }

    @Override
    public void showMenu() {
        int choice = -1;
        do {
            try {
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");
                System.out.print("Your choice: ");
                choice = Integer.parseInt(main.getScanner().nextLine());
                switch (choice) {
                    case 1 -> {
                        login();
                        showMenu();
                    }
                    case 2 -> {
                        register();
                        showMenu();
                    }
                    case 3 -> {
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

    private boolean invalidUsername(String username) {
        return !username.matches("^[a-zA-Z0-9_]{5,32}$");
    }

    private boolean invalidPassword(String password) {
        return password.contains(" ") ||
                password.length() < 8 ||
                password.length() > 32 ||
                !password.matches("^[a-zA-Z0-9@!#$%^&*()_+\\-=]{8,32}$");
    }
}
