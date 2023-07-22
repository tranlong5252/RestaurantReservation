package tranlong5252.menu;

import tranlong5252.Utils;

import java.util.HashMap;

public class ManageDetailsMenu extends HostMenu {

	private final MainMenu mainMenu;
	private final HashMap<String, String> details = new HashMap<>();
	public ManageDetailsMenu(MainMenu mainMenu) {
		this.mainMenu = mainMenu;
		var mysql = main.getMySQL();
		details.put("name", mysql.getRestaurantDetail("name"));
		details.put("address", mysql.getRestaurantDetail("address"));
		details.put("phone", mysql.getRestaurantDetail("phone"));
		details.put("email", mysql.getRestaurantDetail("email"));
		showMenu();
	}

	@Override
	public void showMenu() {
		System.out.println("Restaurant Details:");
		try {
			System.out.printf("1. Edit name (%s)\n", details.get("name"));
			System.out.printf("2. Edit address (%s)\n", details.get("address"));
			System.out.printf("3. Edit phone (%s)\n", details.get("phone"));
			System.out.printf("4. Edit email (%s)\n", details.get("email"));
			System.out.println("5. Back");
			System.out.print("Your choice: ");
			int choice = Integer.parseInt(main.getScanner().nextLine());
			switch (choice) {
				case 1 -> {
					edit("name");
					showMenu();
				}
				case 2 -> {
					edit("address");
					showMenu();
				}
				case 3 -> {
					edit("phone");
					showMenu();
				}
				case 4 -> {
					edit("email");
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
		if (field.equals("email") && !Utils.validateEmail(newValue)) {
			System.out.println("Invalid email! Try again.");
			edit(field);
			return;
		}
		if (field.equals("phone") && !Utils.validatePhoneNumber(newValue)) {
			System.out.println("Invalid phone! Try again.");
			edit(field);
			return;
		}
		details.put(field, newValue);
		main.getMySQL().updateRestaurantDetail(details.get("name"), details.get("address"), details.get("phone"), details.get("email"));
	}
}
