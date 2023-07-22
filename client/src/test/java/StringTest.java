import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class StringTest {

	public StringTest() {
	}

	@Test
	public void valid() {
		assert !invalidPassword("12345678");
		assert invalidPassword("12345678 ");
		assert !invalidUsername("asdqe12");
		assert invalidUsername("");
	}

	public boolean invalidUsername(String username) {
		return !username.matches("^[a-zA-Z0-9_]{5,32}$");
	}

	public boolean invalidPassword(String password) {
		return password.contains(" ") ||
				password.length() < 8 ||
				password.length() > 32 ||
				!password.matches("^[a-zA-Z0-9@!#$%^&*()_+\\-=]{8,32}$");
	}

	@Test
	public void whenMatchesPhoneNumber_thenCorrect() {
		String patterns
				= "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
				+ "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
				+ "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";

		String[] validPhoneNumbers
				= {"2055550125","202 555 0125", "(202) 555-0125", "+111 (202) 555-0125",
				"636 856 789", "+111 636 856 789", "636 85 67 89", "+111 636 85 67 89"};

		Pattern pattern = Pattern.compile(patterns);
		for(String phoneNumber : validPhoneNumbers) {
			Matcher matcher = pattern.matcher(phoneNumber);
			assertTrue(matcher.matches());
		}
	}

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}
}
