package tranlong5252.objects;

import tranlong5252.ReserveStatus;

import java.sql.Timestamp;

public record Reservation(int id, int tableNumber, String username, int numberOfPeople,
                          Timestamp reserveTime, Timestamp createdTime, ReserveStatus status) {

	@Override
	public String toString() {
		return String.format("Reservation %d: Table %d | %d people | %s", id, tableNumber, numberOfPeople, reserveTime);
	}
}
