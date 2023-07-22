package tranlong5252.objects;

import tranlong5252.ReserveStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Reservation {
	private final int id;
	private final User user;
	private final Timestamp createdTime;
	private Table table;
	private int numberOfPeople;
	private Timestamp reserveTime;
	private ReserveStatus status;

	public Reservation(int id, Table table, User user, int numberOfPeople,
	                   Timestamp reserveTime, Timestamp createdTime, ReserveStatus status) {
		this.id = id;
		this.table = table;
		this.user = user;
		this.numberOfPeople = numberOfPeople;
		this.reserveTime = reserveTime;
		this.createdTime = createdTime;
		this.status = status;
	}

	@Override
	public String toString() {
		return String.format("Reservation ID: %d%n" +
						"  %s%n" +
						"  Customer: %s%n" +
						"  Number of people: %d%n" +
						"  Reserve time: %s%n" +
						"  Created time: %s%n" +
						"  Status: %s%n",
				id, table, user.username(), numberOfPeople, reserveTime, createdTime, status);
	}

	public int id() {
		return id;
	}

	public Table table() {
		return table;
	}

	public User user() {
		return user;
	}

	public int numberOfPeople() {
		return numberOfPeople;
	}

	public Timestamp reserveTime() {
		return reserveTime;
	}

	public Timestamp createdTime() {
		return createdTime;
	}

	public ReserveStatus status() {
		return status;
	}

	public void setStatus(ReserveStatus status) {
		this.status = status;
	}

	public void setReserveTime(LocalDateTime parse) {
		this.reserveTime = Timestamp.valueOf(parse);
	}

	public void SetNumberOfPeople(int seats) {
		this.numberOfPeople = seats;
	}

	public void setTable(Table table) {
		this.table = table;
	}
}
