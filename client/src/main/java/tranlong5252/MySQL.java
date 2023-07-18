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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MySQL {
	private Connection sql;
	
	//region QUERIES
	//AUTH
	private static final String GET_USER = "SELECT * FROM `user` WHERE `username` = ?";
	private static final String LOGIN = "SELECT * FROM `user` WHERE `username` = ? AND `password` = ?";
	private static final String REGISTER = "INSERT INTO `user` (`username`, `password`) VALUES (?, ?)";
	//INFO
	private static final String RESTAURANT_DETAILS = "SELECT * FROM `restaurant` WHERE `id` = 0";
	private static final String GET_AVAILABLE_TABLES = """
		SELECT * FROM `table` WHERE `capacity` >= ?
		AND `number` NOT IN (
			SELECT `table_number` FROM `reservation`
			WHERE `reserve_time` > CURRENT_DATE() + INTERVAL 1 HOUR
		)
	""";

	private static final String GET_RESERVATION = "SELECT * FROM `reservation` WHERE `id` = ?";
	private static final String GET_RESERVATION_TIME = """
        SELECT * FROM `reservation`
        WHERE `table_number` = ? AND `reserve_time` < ? + INTERVAL 1 HOUR
    """;
	private static final String GET_TABLE = "SELECT * FROM `table` WHERE `number` = ?";
	private static final String GET_RESERVATIONS = """
		SELECT * FROM `reservation` WHERE `username` = ? AND `reserve_time` > CURRENT_TIMESTAMP()
		ORDER BY reserve_time
	""";
	//UPDATE
	private static final String CREATE_RESERVATION = """
		INSERT INTO `reservation` (`table_number`, `username`, `no_of_people`, `reserve_time`, `status`)
		VALUES (@table_number:=?, @username:=?, @no_of_people:=?, @reserve_time:=?, @status:=?)
	""";
	private static final String UPDATE_RESERVATION = """
		INSERT INTO `reservation` (`id`, `table_number`, `username`, `no_of_people`, `reserve_time`, `status`)
		VALUES (@id:=?, @table_number:=?, @username:=?, @no_of_people:=?, @reserve_time:=?, @status:=?)
		ON DUPLICATE KEY UPDATE `table_number` = @table_number, `username` = @username,
		`no_of_people` = @no_of_people, `reserve_time` = @reserve_time, `status` = @status
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

	//region reservation
	public boolean createReservation(String username, int tableNumber, int numberOfPeople,
	                                 LocalDateTime reserveTime, ReserveStatus status) {
		PreparedStatement statement = null;
		try {
			statement = sql.prepareStatement(CREATE_RESERVATION);
			statement.setInt(1, tableNumber);
			statement.setString(2, username);
			statement.setInt(3, numberOfPeople);
			statement.setTimestamp(4, Timestamp.valueOf(reserveTime));
			statement.setInt(5, status.getId());
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			cleanup(null, statement);
		}
	}

	public void updateReservation(int id, int tableNumber, String username, int numberOfPeople, String reserveTime, String status) {
		PreparedStatement statement = null;
		try {
			statement = sql.prepareStatement(UPDATE_RESERVATION);
			statement.setInt(1, id);
			statement.setInt(2, tableNumber);
			statement.setString(3, username);
			statement.setInt(4, numberOfPeople);
			statement.setString(5, reserveTime);
			statement.setString(6, status);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(null, statement);
		}
	}

	public List<Reservation> getReservations(String username) {
		List<Reservation> reservations = new ArrayList<>();
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(GET_RESERVATIONS);
			statement.setString(1, username);
			result = statement.executeQuery();

			while (result.next()) {
				reservations.add(new Reservation(
						result.getInt("id"),
						getTable(result.getInt("table_number")),
						result.getString("username"),
						result.getInt("no_of_people"),
						result.getTimestamp("reserve_time"),
						result.getTimestamp("created_time"),
						ReserveStatus.getById(result.getInt("status"))));
			}
			return reservations;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
		return reservations;
	}

	public List<Table> getAvailableTables() {
		List<Table> tables = new ArrayList<>();
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(GET_AVAILABLE_TABLES);
			result = statement.executeQuery();

			while (result.next()) {
				tables.add(new Table(
						result.getInt("number"),
						result.getInt("capacity"),
						result.getString("status")));
			}
			return tables;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
		return tables;
	}

	public Table getTable(int number) {
		Table table = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(GET_TABLE);
			statement.setInt(1, number);
			result = statement.executeQuery();
			if (result.next()) {
				table = new Table(
						result.getInt("number"),
						result.getInt("capacity"),
						result.getString("status"));
			}
			return table;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
		return table;
	}

	public Reservation getReservation(int id) {
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(GET_RESERVATION);
			statement.setInt(1, id);
			result = statement.executeQuery();
			if (result.next()) {
				return new Reservation(
						result.getInt("id"),
						getTable(result.getInt("table_number")),
						result.getString("username"),
						result.getInt("no_of_people"),
						result.getTimestamp("reserve_time"),
						result.getTimestamp("created_time"),
						ReserveStatus.getById(result.getInt("status")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
		return null;
	}

	public Reservation getReservation(int number, LocalDateTime time) {
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(GET_RESERVATION_TIME);
			statement.setInt(1, number);
			statement.setTimestamp(2, Timestamp.valueOf(time));
			result = statement.executeQuery();
			if (result.next()) {
				return new Reservation(
						result.getInt("id"),
						getTable(result.getInt("table_number")),
						result.getString("username"),
						result.getInt("no_of_people"),
						result.getTimestamp("reserve_time"),
						result.getTimestamp("created_time"),
						ReserveStatus.getById(result.getInt("status")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
		return null;
	}
}
