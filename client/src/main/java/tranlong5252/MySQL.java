package tranlong5252;

import tranlong5252.objects.Reservation;
import tranlong5252.objects.User;
import tranlong5252.objects.Table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {
	private Connection sql;
	
	//region QUERIES

	private static final String LOGIN = "SELECT * FROM `user` WHERE `username` = ? AND `password` = ?";
	private static final String REGISTER = "INSERT INTO `user` (`username`, `password`) VALUES (?, ?)";
	//INFO
	private static final String RESTAURANT_DETAILS = "SELECT * FROM `restaurant` WHERE `id` = 0";

	//UPDATE
	private static final String EDIT_RESTAURANT_DETAIL = """
		INSERT INTO `restaurant` (`id`, `name`, `address`, `phone`, `email`)
		VALUES (0, @name:=?, @address:=?, @phone:=?, @email:=?)
		ON DUPLICATE KEY UPDATE `name` = @name, `address` = @address, `phone` = @phone, `email` = @email
	""";

	//endregion

	public MySQL(String host, int port, String dbname, String user, String pwd) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			try {
			Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
		try {
			sql = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbname, user, pwd);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected void cleanup(ResultSet result, Statement statement) {
		if (result != null) {
			try {
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}



	public boolean login(String username, String password) {
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(LOGIN);
			statement.setString(1, username);
			statement.setString(2, password);
			result = statement.executeQuery();
			return result.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
		return false;
	}

	public User getUser(String username) {
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(GET_USER);
			statement.setString(1, username);
			result = statement.executeQuery();
			if (result.next()) {
				return new User(
						result.getString("username"),
						result.getString("password"),
						result.getString("email"),
						result.getTimestamp("created_time"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
		return null;
	}

	public void register(String username, String password) {
		PreparedStatement statement = null;
		try {
			statement = sql.prepareStatement(REGISTER);
			statement.setString(1, username);
			statement.setString(2, password);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(null, statement);
		}
	}
}
