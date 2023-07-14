package tranlong5252;

public class Main {

	/**
	 * <h1>Restaurant Reservation System</h1>
	 * A restaurant reservation system is a software solution to help your customers make dining reservations
	 * The system allows you to manage dining reservations conveniently. With the reservation system,
	 * your restaurant can adequately monitor how many tables you have at a given time.
	 * <h3>Actors: Restaurant Owner, Customer</h3>
	 * <h3>Major Features:</h3>
	 * Login, Logout.<br>
	 * Manage restaurant details<br>
	 * Manage table.(No. Of People, Type, etc)<br>
	 * Create reservation request (No. of People, Table, Day, Hour, etc)<br>
	 * View all the details about the reservation requests.<br>
	 * Confirm/ Reject customer reservation requests.
	 */

	public static void main(String[] args) {
		new RestaurantsManager();
	}
}