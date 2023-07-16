package tranlong5252.menu;

public class CustomersMenu extends HostMenu {

	private final MainMenu mainMenu;
	public CustomersMenu(MainMenu mainMenu) {
		this.mainMenu = mainMenu;
		showMenu();
	}

	@Override
	public void showMenu() {
		try {
			System.out.println("1. View Customers");
			System.out.println("2. Edit customer info");
			System.out.println("3. Suspend customer");
			System.out.println("4. Back");
			System.out.print("Your choice: ");
			int choice = Integer.parseInt(main.getScanner().nextLine());
			switch (choice) {
				case 1 -> printCustomers();
				case 2 -> editCustomerInfo();
				case 3 -> suspendCustomer();
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

	private void printCustomers() {
		//TODO
	}

	private void editCustomerInfo() {
		//TODO
	}

	private void suspendCustomer() {
		//TODO
	}
}
