package tranlong5252.menu;

public class MainMenu extends ClientMenu {

	@Override
	public void showMenu() {
		try {
			System.out.println("1. Reservations");
			System.out.println("2. Edit information");
			System.out.println("3. Logout");
			System.out.println("4. Exit");
			System.out.print("Your choice: ");
			int choice = Integer.parseInt(main.getScanner().nextLine());
			switch (choice) {
				case 1 -> new ReserveMenu(this);
				case 3 -> {
					System.out.println("Goodbye!");
					main.setUsername(null);
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
}
