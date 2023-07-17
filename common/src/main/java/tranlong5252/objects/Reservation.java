package tranlong5252.objects;

import tranlong5252.ReserveStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Reservation {
	private final int id;
	private final String username;
	private final Timestamp createdTime;
	private Table table;
	private int numberOfPeople;
	private Timestamp reserveTime;
	private ReserveStatus status;

	public Reservation(int id, Table table, String username, int numberOfPeople,
	                   Timestamp reserveTime, Timestamp createdTime, ReserveStatus status) {
		this.id = id;
		this.table = table;
		this.username = username;
		this.numberOfPeople = numberOfPeople;
		this.reserveTime = reserveTime;
		this.createdTime = createdTime;
		this.status = status;
	}

	@Override
	public String toString() {
		return String.format("Reservation %d: Table %d | %d people | %s", id, table, numberOfPeople, reserveTime);
	}

	public int id() {
		return id;
	}

	public Table table() {
		return table;
	}

	public String username() {
		return username;
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
