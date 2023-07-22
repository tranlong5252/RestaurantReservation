package tranlong5252.menu;

import tranlong5252.Utils;

import java.time.LocalDate;

public class MainMenu extends ClientMenu {

	public MainMenu() {
		showMenu();
	}

	@Override
	public void showMenu() {
		try {
			String username = main.getUsername();
			if (username == null) {
				return;
			}
			var customer = main.getMySQL().getCustomer(username);
			if (customer == null) {
				setupCustomer(null, null, null);
				return;
			}
			if (customer.isSuspended()) {
				System.out.println("Your account is suspended!");
				System.out.println("Please contact the restaurant to un-suspend your account!");
				System.out.println("Goodbye!");
				main.setUsername(null);
				return;
			}
			System.out.println("1. Reservations");
			System.out.println("2. Your information");
			System.out.println("3. Logout");
			System.out.println("4. Exit");
			System.out.print("Your choice: ");
			int choice = Integer.parseInt(main.getScanner().nextLine());
			switch (choice) {
				case 1 -> new ReserveMenu(this);
				case 2 -> new InformationMenu(this);
				case 3 -> {
					System.out.println("Goodbye!");
					main.setUsername(null);
					new LoginMenu();
				}
				case 4 -> {
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

	private void setupCustomer(String name, String gender, String phone) {
		try {
			System.out.println("Please enter your information!");
			if (name == null) {
				System.out.print("Name: ");
				name = main.input();
			}
			if (gender == null) {
				System.out.println("Gender (Male/Female)");
				gender = main.input();
			}
			if (!gender.equalsIgnoreCase("male") && !gender.equalsIgnoreCase("female")) {
				System.out.println("Gender is only \"Male\" or \"Female\"");
				setupCustomer(name, null, null);
				return;
			}
			if (phone == null) {
				System.out.print("Phone: ");
				phone = main.input();
				if (!Utils.validatePhoneNumber(phone)) {
					System.out.println("Invalid phone number!");
					setupCustomer(name, gender, null);
					return;
				}
			}
			System.out.println("Date of birth (dd/MM/yyyy): ");
			String dobStr = main.input();
			LocalDate dob = LocalDate.from(Utils.getDateTimeFormatter().parse(dobStr));
			if (dob.isAfter(LocalDate.now())) {
				System.out.println("Invalid date of birth!");
				setupCustomer(name, gender, phone);
				return;
			}
			main.getMySQL().updateCustomerInfo(main.getUsername(), name, phone, dob, Utils.capitalize(gender));
			showMenu();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Invalid date, the date format should be dd/MM/yyyy!");
			setupCustomer(name, gender, phone);
		}
	}
}
