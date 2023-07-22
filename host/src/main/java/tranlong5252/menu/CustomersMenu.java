package tranlong5252.menu;

import tranlong5252.objects.Customer;

import java.util.List;

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
			System.out.println("2. Suspend customer");
			System.out.println("3. Un-suspend customer");
			System.out.println("4. Back");
			System.out.print("Your choice: ");
			int choice = Integer.parseInt(main.getScanner().nextLine());
			switch (choice) {
				case 1 -> {
					printCustomers();
					showMenu();
				}
				case 2 -> {
					suspendCustomer();
					showMenu();
				}
				case 3 -> {
					unSuspendCustomer();
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

	private void unSuspendCustomer() {
		List<Customer> customers = main.getMySQL().getCustomers(true);
		if (customers.isEmpty()) {
			System.out.println("No customers to un-suspend!");
			return;
		}
		try {
			var customer = getCustomer(true);
			if (customer == null) {
				return;
			}
			main.getMySQL().suspendCustomer(customer, false);
			System.out.println("Customer is un-suspended!");
		} catch (NumberFormatException e) {
			System.out.println("Invalid choice!");
			suspendCustomer();
		}
	}

	private void printCustomers() {
		var customers = main.getMySQL().getCustomers();
		if (customers.isEmpty()) {
			System.out.println("No customers!");
			return;
		}
		customers.forEach(System.out::println);
	}

	private void suspendCustomer() {
		List<Customer> customers = main.getMySQL().getCustomers(false);
		if (customers.isEmpty()) {
			System.out.println("No customers to suspend!");
			return;
		}
		try {
			var customer = getCustomer(false);
			if (customer == null) {
				return;
			}
			System.out.print("Are you sure you want to suspend this customer? (Y/N): ");
			String prompt = main.getScanner().nextLine();
			if (prompt.equalsIgnoreCase("y")) {
				main.getMySQL().suspendCustomer(customer, true);
				System.out.println("Customer suspended!");
			} else {
				System.out.println("Canceled!");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid choice!");
			suspendCustomer();
		}
	}

	private Customer getCustomer(boolean suspended) {
		System.out.print("Customer username: ");
		String username = main.getScanner().nextLine();
		if (username.isBlank()) {
			System.out.println("Invalid username!");
			return getCustomer(suspended);
		}
		var customer = main.getMySQL().getCustomer(username);
		if (customer == null) {
			System.out.println("Customer not found!");
			return getCustomer(suspended);
		}
		if (customer.isSuspended() == suspended) {
			System.out.println("Customer is already " + (suspended ? "suspended" : "not suspended") + "!");
			return null;
		}
		return customer;
	}
}
