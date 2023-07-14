package tranlong5252.menu;

import tranlong5252.RestaurantsManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainMenu {

    private final RestaurantsManager main;
    private final String username;

    public MainMenu(RestaurantsManager main) {
        this.main = main;
        this.username = main.getUsername();
        mainMenu(0);
    }

    public void mainMenu(int lastChoice) {
        boolean res = main.getRestaurantId() > 0;
        if (res && lastChoice == 7 || !res && lastChoice == 3) {
            return;
        }
        int choice = lastChoice;
        try {
            if (res) {
                System.out.println("1. Manage restaurant details");
                System.out.println("2. Manage table");
                System.out.println("3. Create reservation request");
                System.out.println("4. View all the details about the reservation requests");
                System.out.println("5. Confirm/ Reject customer reservation requests");
                System.out.println("6. Manage customers");
                System.out.println("7. Logout");
            }
            else {
                System.out.println("1. Create restaurant");
                System.out.println("2. Choose restaurant");
                System.out.println("3. Logout");
            }
            System.out.print("Your choice: ");
            choice = Integer.parseInt(main.getScanner().nextLine());
            if (res) {
                switch (choice) {
                    case 1 -> manageRestaurantDetails();
                    case 2 -> manageTables();
                    case 3 -> createReservationRequest();
                    case 4 -> viewAllReservationRequests();
                    case 5 -> confirmOrRejectReservationRequests();
                    case 6 -> manageCustomers();
                    case 7 -> logout();
                    default -> System.out.println("Invalid choice!");
                }
            }
            else {
                switch (choice) {
                    case 1 -> addRestaurantDetails();
                    case 2 -> chooseRestaurant(2);
                    case 3 -> logout();
                    default -> System.out.println("Invalid choice!");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice!");
        }
        finally {
            mainMenu(choice);
        }
    }

    private void manageCustomers() {
        System.out.println("Manage customers");
        System.out.println("1. Add customer");
        System.out.println("2. View customer details");
        System.out.println("3. Update customer details");
        System.out.println("4. Back");
        System.out.print("Your choice: ");
        int choice = Integer.parseInt(main.getScanner().nextLine());
        switch (choice) {
            case 1 -> addCustomers();
            case 2 -> viewCustomerDetails();
            case 4 -> {
            }
            default -> System.out.println("Invalid choice!");
        }
    }

    private void addCustomers() {
        String name, phone, dob;
        System.out.print("Name: ");
        name = main.getScanner().nextLine();
        System.out.print("Phone: ");
        phone = main.getScanner().nextLine();
        System.out.println("Gender (Male/Female): ");
        String gender = main.getScanner().nextLine();
        System.out.println("Date of birth (dd/MM/yyyy) (Enter to skip)");
        dob = main.getScanner().nextLine();
        main.getMySQL().getOrAddCustomer(name, gender, phone, dob, main.getRestaurantId());
    }

    private void viewCustomerDetails() {
        System.out.println("View customer details:");
        main.getMySQL().getCustomers(main.getRestaurantId());
        System.out.print("Customer id: ");
        int id = Integer.parseInt(main.getScanner().nextLine());
        main.getMySQL().getCustomer(id, main.getRestaurantId());
    }

    //region Restaurant Details
    private void manageRestaurantDetails() {
        boolean hasRestaurant = main.getRestaurantId() != -1;
        if (!hasRestaurant) {
            System.out.println("1. Create restaurant details");
            System.out.println("2. Back");
        } else {
            System.out.println("1. View restaurant details");
            System.out.println("2. Update restaurant details");
            System.out.println("3. Delete restaurant details");
            System.out.println("4. Choose restaurant");
            System.out.println("5. Back");
        }
        System.out.print("Your choice: ");
        int choice = Integer.parseInt(main.getScanner().nextLine());
        if (hasRestaurant) {
            switch (choice) {
                case 1 -> viewRestaurantDetails();
                case 2 -> updateRestaurantDetails();
                case 3 -> deleteRestaurantDetails();
                case 4 -> chooseRestaurant(4);
                case 5 -> {
                }
                default -> System.out.println("Invalid choice!");
            }
        } else {
            switch (choice) {
                case 1 -> addRestaurantDetails();
                case 2 -> {
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void chooseRestaurant(int lastChoice) {
        System.out.println("Choose restaurant");
        main.getMySQL().viewRestaurants(username);
        System.out.print("Restaurant id: ");
        int id = Integer.parseInt(main.getScanner().nextLine());
        if (main.getMySQL().existRestaurant(id, username)) {
            main.setRestaurantId(id);
            mainMenu(lastChoice);
        } else {
            System.out.println("Restaurant not exist!");
            manageRestaurantDetails();
        }
    }

    private void deleteRestaurantDetails() {
        System.out.println("Delete restaurant details");
        String id = main.getScanner().nextLine();
        main.getMySQL().deleteRestaurantDetails(id);
    }

    private void addRestaurantDetails() {
        System.out.println("Add restaurant details");
        System.out.print("Restaurant name (Enter to exit): ");
        String name = main.getScanner().nextLine();
        if (name.isBlank()) {
            manageRestaurantDetails();
            return;
        }
        System.out.print("Address: ");
        String address = main.getScanner().nextLine();
        System.out.print("Phone: ");
        String phone = main.getScanner().nextLine();
        System.out.print("Email: ");
        String email = main.getScanner().nextLine();
        System.out.print("Description: ");
        String description = main.getScanner().nextLine();
        int id = main.getMySQL().addRestaurantDetails(name, address, phone, email, description);
        main.getMySQL().setRestaurantOwner(id, username);
        manageRestaurantDetails();
    }

    private void updateRestaurantDetails() {
        System.out.println("Update restaurant details");
        System.out.print("Restaurant id: ");
        try {
            int id = Integer.parseInt(main.getScanner().nextLine());
            System.out.print("Restaurant name: ");
            String name = main.getScanner().nextLine();
            System.out.print("Address: ");
            String address = main.getScanner().nextLine();
            System.out.print("Phone: ");
            String phone = main.getScanner().nextLine();
            System.out.print("Email: ");
            String email = main.getScanner().nextLine();
            System.out.print("Description: ");
            String description = main.getScanner().nextLine();
            main.getMySQL().updateRestaurantDetails(id, name, address, phone, email, description);
        } catch (NumberFormatException e) {
            System.out.println("Invalid id!");
        }
        manageRestaurantDetails();
    }

    private void viewRestaurantDetails() {
        System.out.println("View restaurant details");
        main.getMySQL().viewRestaurantDetails(main.getRestaurantId());
        manageRestaurantDetails();
    }
    //endregion

    //region Table
    private void manageTables() {
        System.out.println("1. View table");
        System.out.println("2. Update table");
        System.out.println("3. Delete table");
        System.out.println("4. Back");
        System.out.print("Your choice: ");
        int choice = Integer.parseInt(main.getScanner().nextLine());
        switch (choice) {
            case 1 -> viewTable();
            case 2 -> updateTable();
            case 3 -> deleteTable();
            case 4 -> {}
            default -> System.out.println("Invalid choice!");
        }
    }

    private void deleteTable() {
        System.out.println("Delete table");
        System.out.print("Table id: ");
        int id = Integer.parseInt(main.getScanner().nextLine());
        main.getMySQL().deleteTable(main.getRestaurantId(), id);
        manageTables();
    }

    private void updateTable() {
        System.out.println("Update table");
        System.out.print("Table no.: ");
        int number = Integer.parseInt(main.getScanner().nextLine());
        System.out.print("Capacity (No. of people): ");
        int capacity = Integer.parseInt(main.getScanner().nextLine());
        System.out.print("Type: ");
        String type = main.getScanner().nextLine();
        main.getMySQL().updateTable(main.getRestaurantId(), number, capacity, type);
        manageTables();
    }

    private void viewTable() {
        System.out.println("View table");
        main.getMySQL().viewTable(main.getRestaurantId());
        manageTables();
    }
    //endregion

    //region reservation
    private void confirmOrRejectReservationRequests() {
        System.out.println("Confirm/ Reject customer reservation requests:");
        System.out.print("Reservation request id: ");
        String id = main.getScanner().nextLine();
        System.out.print("Status:\n 1. Pending\n 2. Confirmed\n 3. Rejected\n ");
        String status = main.getScanner().nextLine();
        main.getMySQL().confirmOrRejectReservationRequests(id, status);
    }

    private void viewAllReservationRequests() {
        System.out.println("View all the details about the reservation requests:");
        main.getMySQL().viewReservationRequests();
    }

    private void createReservationRequest() {
        System.out.println("Create reservation request");
        //customer details
        System.out.print("Customer name: ");
        String customerName = main.getScanner().nextLine();
        System.out.print("Customer phone: ");
        String customerPhone = main.getScanner().nextLine();
        System.out.println("Gender (Male/Female): ");
        String gender = main.getScanner().nextLine();
        System.out.println("Date of birth (dd/MM/yyyy) (Enter to skip)");
        String dob = main.getScanner().nextLine();
        int customerId = main.getMySQL().getOrAddCustomer(customerName, gender, customerPhone, dob, main.getRestaurantId());

        System.out.print("No. of people: ");
        int noOfPeople = Integer.parseInt(main.getScanner().nextLine());
        int availableTables = main.getMySQL().viewTables(main.getRestaurantId(), noOfPeople);
        if (availableTables == 0) {
            System.out.println("No table available!");
            return;
        }
        System.out.print("Table: ");
        int table = Integer.parseInt(main.getScanner().nextLine());
        System.out.print("Time (dd/MM/yyyy HH:mm:ss): ");
        String timeStr = main.getScanner().nextLine();
        System.out.print("Status:\n 1. Pending\n 2. Confirmed\n 3. Rejected\n ");
        String status = main.getScanner().nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime time = LocalDateTime.parse(timeStr, formatter);
        main.getMySQL().createReservationRequest(table, customerId, noOfPeople, time, Integer.parseInt(status));
    }
    //endregion

    private void logout() {
        System.out.println("Logout success!");
        //new LoginMenu(main);
    }
}
