package tranlong5252.menu;

import tranlong5252.ReserveStatus;
import tranlong5252.objects.Reservation;

import java.util.List;

public class ReservationsMenu extends HostMenu {

	private final MainMenu mainMenu;
	public ReservationsMenu(MainMenu mainMenu) {
		this.mainMenu = mainMenu;
	}

	@Override
	public void showMenu() {
		try {
			System.out.println("1. View Reservations");
			System.out.println("2. Edit Reservation status");
			System.out.println("3. Back");
			System.out.print("Your choice: ");
			int choice = Integer.parseInt(main.getScanner().nextLine());
			switch (choice) {
				case 1 -> {
					printReservations();
					showMenu();
				}
				case 2 -> {
					editStatusReservation();
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

	private void editStatusReservation() {
		int reservations = printReservations();
		if (reservations == 0) {
			return;
		}
		try {
			System.out.print("Reservation ID: ");
			int id = Integer.parseInt(main.getScanner().nextLine());
			Reservation reservation = main.getMySQL().getReservation(id);
			if (reservation == null) {
				System.out.println("Reservation not found!");
				return;
			}
			System.out.println(reservation);
			System.out.println("1. Change status");
			System.out.println("2. Delete");
			System.out.println("3. Back");
			System.out.print("Your choice: ");
			int choice = Integer.parseInt(main.getScanner().nextLine());
			switch (choice) {
				case 1 -> {
					if (reservation.status().equals(ReserveStatus.CANCELLED)) {
						System.out.println("Reservation is cancelled by the customer, can not change!");
						return;
					}
					editStatusReservation(reservation);
					editStatusReservation();
				}
				//case 2 -> deleteReservation(reservation);
				case 3 -> showMenu();
				default -> {
					System.out.println("Invalid choice!");
					editStatusReservation();
				}
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid choice!");
			showMenu();
		}
	}

	private void editStatusReservation(Reservation reservation) {
		System.out.println("1. Confirmed");
		System.out.println("2. Rejected");
		System.out.println("3. Back");
		try {
			int choice = Integer.parseInt(main.getScanner().nextLine());
			if (choice == 3) {
				return;
			}
			var status = ReserveStatus.getById(choice);
			if (status == null) {
				System.out.println("Invalid choice!");
				return;
			}
			main.getMySQL().changeStatusReservation(reservation, status);
			System.out.printf("Status changed to %s!", status);
		} catch (NumberFormatException e) {
			System.out.println("Invalid choice!");
		}
	}

	private int printReservations() {
		List<Reservation> reservations = main.getMySQL().getReservations();
		if (reservations.size() == 0) {
			System.out.println("No reservations!");
		}
		else {
			System.out.println("Reservations:");
			for (int i = 0; i < reservations.size(); i++) {
				System.out.printf("%d. %s\n", i + 1, reservations.get(i).toString());
			}
		}
		return reservations.size();
	}
}
