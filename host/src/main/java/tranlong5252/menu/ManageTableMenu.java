package tranlong5252.menu;

public class ManageTableMenu extends HostMenu {

    private final MainMenu mainMenu;
    public ManageTableMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        showMenu();
    }

    @Override
    public void showMenu() {
        try {
            System.out.println("1. Show Tables");
            System.out.println("2. Add Table");
            System.out.println("3. Edit Table");
            System.out.println("4. Back");
            System.out.print("Your choice: ");
            int choice = Integer.parseInt(main.getScanner().nextLine());
            switch (choice) {
                case 1 -> {
                    printTables();
                    showMenu();
                }
                case 2 -> {
                    addTable();
                    showMenu();
                }
                case 3 -> {
                    editTable();
                    showMenu();
                }
                case 4 -> mainMenu.showMenu();
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

    private void editTable() {
        int tables = printTables();
        if (tables == 0) {
            return;
        }
        try {
            System.out.print("Table number: ");
            int number = Integer.parseInt(main.getScanner().nextLine());
            var table = main.getMySQL().getTable(number);
            if (table == null) {
                System.out.println("Table not found!");
                return;
            }
            System.out.printf("Table %d: %d seats | Type: %s\n", table.number(), table.capacity(), table.type());
            System.out.print("New seats: ");
            int capacity = Integer.parseInt(main.getScanner().nextLine());
            if (capacity < 0) {
                System.out.println("The seats should be greater or equal 0!");
                return;
            }
            System.out.print("New type: ");
            String type = main.getScanner().nextLine();
            main.getMySQL().updateTable(number, capacity, type);
            System.out.println("Table updated!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
    }

    private void addTable() {
        try {
            System.out.print("Table number: ");
            int number = Integer.parseInt(main.getScanner().nextLine());
            System.out.print("Table capacity: ");
            int capacity = Integer.parseInt(main.getScanner().nextLine());
            if (capacity < 0) {
                System.out.println("The seats should be greater or equal 0!");
                return;
            }
            System.out.print("Table type: ");
            String type = main.getScanner().nextLine();
            main.getMySQL().updateTable(number, capacity, type);
            System.out.println("Table added!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
    }

    private int printTables() {
        var tables = main.getMySQL().getTables();
        if (tables.isEmpty()) {
            System.out.println("No table found!");
        }
        else {
            System.out.println("Tables:");
            tables.forEach(System.out::println);
        }
        return tables.size();
    }
}
