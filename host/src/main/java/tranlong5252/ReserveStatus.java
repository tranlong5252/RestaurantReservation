package tranlong5252;

public enum ReserveStatus {
	CONFIRMED(1),
	REJECTED(2), //By restaurant
	CANCELLED(3) //By customer
	;

	private final int id;

	ReserveStatus(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static ReserveStatus getById(int id) {
		for (ReserveStatus status : ReserveStatus.values()) {
			if (status.getId() == id) {
				return status;
			}
		}
		return null;
	}
}
