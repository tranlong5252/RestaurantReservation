package tranlong5252.menu;

public class MainMenu extends HostMenu {

    public MainMenu() {
        showMenu();
    }

    @Override
    public void showMenu() {
        try {
            System.out.println("1. Restaurant details");
            System.out.println("2. Tables");
            System.out.println("3. Reservations");
            System.out.println("4. Customers");
            System.out.println("5. Exit");
            System.out.print("Your choice: ");
            int choice = Integer.parseInt(main.getScanner().nextLine());
            switch (choice) {
                case 1 -> new ManageDetailsMenu(this);
                case 2 -> new ManageTableMenu(this);
                case 3 -> new ReservationsMenu(this);
                case 4 -> new CustomersMenu(this);
                case 5 -> {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                default -> {
                    System.out.println("Invalid choice!");
                    showMenu();
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice!");
            showMenu();
        }
    }
}
