package tranlong5252;

import java.time.format.DateTimeFormatter;

public class Utils {

	public static boolean validatePhoneNumber(String phoneNumber) {
		return phoneNumber.matches("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
				+ "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
				+ "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$");
	}

	public static String capitalize(String str) {
		String[] words = str.split(" ");
		StringBuilder capitalize = new StringBuilder();
		for (String word : words) {
			capitalize.append(word.substring(0, 1).toUpperCase()).append(word.substring(1)).append(" ");
		}
		return capitalize.toString().trim();
	}

	public static DateTimeFormatter getDateTimeHourFormatter() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	}

	public static DateTimeFormatter getDateTimeFormatter() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy");
	}

	public static boolean validateEmail(String email) {
		return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
	}
}
