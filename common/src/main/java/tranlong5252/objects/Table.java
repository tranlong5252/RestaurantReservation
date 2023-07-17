package tranlong5252.objects;

public record Table(int number, int capacity, String type) {

	@Override
	public String toString() {
		return String.format("Table %d: %d seats | Type: %s", number, capacity, type);
	}
}
