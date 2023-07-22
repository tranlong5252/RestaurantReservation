package tranlong5252.menu;

import java.time.LocalDateTime;
import java.util.List;

import tranlong5252.ReserveStatus;
import tranlong5252.Utils;
import tranlong5252.objects.Reservation;
import tranlong5252.objects.Table;

public class ReserveMenu extends ClientMenu {

	private final MainMenu mainMenu;
	public ReserveMenu(MainMenu mainMenu) {
		this.mainMenu = mainMenu;
		showMenu();
	}

	@Override
	public void showMenu() {
		try {
			System.out.println("1. Show all reservations");
			System.out.println("2. Add reservation");
			System.out.println("3. Edit reservation");
			System.out.println("4. Cancel reservation");
			System.out.println("5. Back");
			System.out.print("Your choice: ");
			int choice = Integer.parseInt(main.getScanner().nextLine());
			switch (choice) {
				case 1 -> {
					int size = printReservations();
					if (size == 0) {
						System.out.println("No reservations!");
					}
					showMenu();
				}
				case 2 -> {
					addReservation();
					showMenu();
				}
				case 3 -> {
					editReservation();
					showMenu();
				}
				case 4 -> {
					cancelReservation();
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

	private void cancelReservation() {
		int size = printReservations();
		if (size == 0) {
			System.out.println("No reservations to cancel!");
			return;
		}
		System.out.println("Notice: Can not cancel reservation 2 hours before reservation time");

		System.out.print("Reservation ID: ");
		int id = Integer.parseInt(main.getScanner().nextLine());
		Reservation reservation = main.getMySQL().getReservation(id);
		if (reservation == null) {
			System.out.println("Reservation not found!");
			return;
		}
		System.out.println(reservation);
		if (reservation.reserveTime().toLocalDateTime().isBefore(LocalDateTime.now().plusHours(2))) {
			System.out.println("Can not cancel reservation 2 hours before reservation time");
			return;
		}
		reservation.setStatus(ReserveStatus.CANCELLED);
		main.getMySQL().updateReservation(reservation);
		System.out.println("Reservation cancelled!");
	}

	private void editReservation() {
		int size = printReservations();
		if (size == 0) {
			System.out.println("No reservations to edit!");
			return;
		}
		System.out.println("Notice: Can not edit reservation 2 hours before reservation time");

		System.out.print("Reservation ID: ");
		int id = Integer.parseInt(main.getScanner().nextLine());
		Reservation reservation = main.getMySQL().getReservation(id);
		if (reservation == null) {
			System.out.println("Reservation not found!");
			return;
		}
		System.out.println(reservation);
		if (reservation.reserveTime().toLocalDateTime().isBefore(LocalDateTime.now().plusHours(2))) {
			System.out.println("Can not edit reservation 2 hours before reservation time");
			return;
		}
		editReservation(reservation);
	}

	private void editReservation(Reservation reservation) {
		System.out.println("Edit reservation: " + reservation);
		try {
			System.out.println("1. Change table");
			System.out.println("2. Change number of seats");
			System.out.println("3. Change reservation time");
			System.out.println("4. Cancel reservation");
			System.out.println("5. Back");
			System.out.print("Your choice: ");
			int choice = Integer.parseInt(main.getScanner().nextLine());
			switch (choice) {
				case 1 -> {
					List<Table> tables = main.getMySQL().getAvailableTables(reservation.numberOfPeople());
					tables.forEach(System.out::println);
					System.out.print("Table number: ");
					int number = Integer.parseInt(main.getScanner().nextLine());
					Table table = main.getMySQL().getTable(number);
					if (table == null) {
						System.out.println("Table not found!");
						editReservation(reservation);
						return;
					}
					if (reservation.numberOfPeople() > table.capacity()) {
						System.out.println("Not enough seats!");
						editReservation(reservation);
						return;
					}
					reservation.setTable(table);
					System.out.println("Table changed!");
				}
				case 2 -> {
					System.out.print("Number of seats: ");
					int seats = Integer.parseInt(main.getScanner().nextLine());
					if (seats > reservation.table().capacity()) {
						System.out.println("Not enough seats!");
						editReservation(reservation);
						return;
					}
					reservation.SetNumberOfPeople(seats);
					System.out.println("Number of seats changed!");
				}
				case 3 -> {
					System.out.print("Reservation time (yyyy-MM-dd HH:mm): ");
					String time = main.getScanner().nextLine();
					reservation.setReserveTime(LocalDateTime.parse(time, Utils.getDateTimeHourFormatter()));
					System.out.println("Reservation time changed!");
				}
				case 4 -> {
					reservation.setStatus(ReserveStatus.CANCELLED);
					System.out.println("Reservation cancelled!");
				}
				case 5 -> {

				}
				default -> {
					System.out.println("Invalid choice!");
					editReservation(reservation);
				}
			}
			main.getMySQL().updateReservation(reservation);
		} catch (NumberFormatException e) {
			System.out.println("Invalid choice!");
			editReservation(reservation);
		}
	}

	private void addReservation() {
		System.out.print("Number of seats: ");
		int seats = Integer.parseInt(main.getScanner().nextLine());
		List<Table> tables = main.getMySQL().getAvailableTables(seats);
		tables.forEach(System.out::println);
		System.out.print("Table number: ");
		int number = Integer.parseInt(main.getScanner().nextLine());
		Table table = main.getMySQL().getTable(number);
		if (table == null) {
			System.out.println("Table not found!");
			return;
		}
		System.out.println(table);
		if (seats > table.capacity()) {
			System.out.println("Not enough seats!");
			return;
		}
		System.out.print("Date (dd/MM/yyyy HH:mm): ");
		String timeStr = main.getScanner().nextLine();
		try {
			LocalDateTime time = LocalDateTime.parse(timeStr, Utils.getDateTimeHourFormatter());
			if (time.isBefore(LocalDateTime.now().plusHours(3))) {
				System.out.println("Cannot reserve the date in the date before 3 hours from now");
				return;
			}
			Reservation reservation = main.getMySQL().getReservation(number, time);
			if (reservation != null) {
				System.out.printf("This table is already reserved until %s!", reservation.reserveTime());
				return;
			}
			if (main.getMySQL().createReservation(main.getUsername(), number, seats, time, ReserveStatus.CONFIRMED)) {
				System.out.println("Reservation added!");
				return;
			}
			System.out.println("Failed to add reservation!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Invalid date!");
		}
	}

	private int printReservations() {
		var reservations = main.getMySQL().getReservations(main.getUsername());
		reservations.forEach(System.out::println);
		return reservations.size();
	}
}
