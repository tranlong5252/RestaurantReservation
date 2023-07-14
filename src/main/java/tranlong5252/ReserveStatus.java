package tranlong5252;

public enum ReserveStatus {
	PENDING(1),
	CONFIRMED(2),
	REJECTED(3)
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
