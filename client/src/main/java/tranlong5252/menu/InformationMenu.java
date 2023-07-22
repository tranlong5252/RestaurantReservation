package tranlong5252.menu;

import tranlong5252.Utils;
import tranlong5252.objects.Customer;

import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.HashMap;

public class InformationMenu extends ClientMenu {

	private final MainMenu mainMenu;
	private final HashMap<String, String> details = new HashMap<>();
	private final Customer customer;
	public InformationMenu(MainMenu mainMenu) {
		this.mainMenu = mainMenu;
		customer = main.getMySQL().getCustomer(main.getUsername());
		details.put("name", customer.name());
		details.put("phone", customer.phone());
		details.put("gender", customer.gender());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		details.put("dob", sdf.format(customer.getDob()));
		showMenu();
	}

	@Override
	public void showMenu() {
		try {
			System.out.println("1. Show information");
			System.out.println("2. Edit information");
			System.out.println("3. Back");
			System.out.print("Your choice: ");
			int choice = Integer.parseInt(main.getScanner().nextLine());
			switch (choice) {
				case 1 -> {
					showInformation();
					showMenu();
				}
				case 2 -> {
					editInformation();
					showMenu();
				}
				case 3 -> mainMenu.showMenu();
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

	private void editInformation() {
		showInformation();
		try {
			System.out.println("1. Edit name");
			System.out.println("2. Edit phone");
			System.out.println("3. Edit gender");
			System.out.println("4. Edit date of birth (dd/MM/yyyy)");
			System.out.println("5. Back");
			System.out.print("Your choice: ");
			int choice = Integer.parseInt(main.getScanner().nextLine());
			switch (choice) {
				case 1 -> {
					edit("name");
					showMenu();
				}
				case 2 -> {
					edit("phone");
					showMenu();
				}
				case 3 -> {
					edit("gender");
					showMenu();
				}
				case 4 -> {
					edit("dob");
					showMenu();
				}
				case 5 -> mainMenu.showMenu();
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

	private void edit(String field) {
		System.out.printf("New %s: ", field);
		String newValue = main.getScanner().nextLine();
		if (newValue.isBlank()) {
			System.out.println("Invalid input! Try again.");
			edit(field);
			return;
		}
		details.put(field, newValue);
		try {
			var dob = LocalDate.from(Utils.getDateTimeFormatter().parse(details.get("dob")));
			main.getMySQL().updateCustomerInfo(
					customer.username(),
					details.get("name"),
					details.get("phone"),
					dob,
					details.get("gender"));
			System.out.println("Update success!");
		} catch (DateTimeException e) {
			System.out.println("Invalid date of birth! Try again.");
			edit("dob");
		}
	}

	private void showInformation() {
		System.out.println(" Name: " + details.get("name"));
		System.out.println(" Phone: " + details.get("phone"));
		System.out.println(" Gender: " + details.get("gender"));
		System.out.println(" Date of birth: " + details.get("dob"));
	}
}
