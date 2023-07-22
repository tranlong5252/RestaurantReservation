package tranlong5252;

import tranlong5252.objects.Customer;
import tranlong5252.objects.Reservation;
import tranlong5252.objects.Table;
import tranlong5252.objects.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQL {
	private Connection sql;

	//region QUERIES
	//INFO
	private static final String GET_CUSTOMERS = "SELECT * FROM `customer`";
	private static final String GET_AVAILABLE_CUSTOMERS = "SELECT * FROM `customer` WHERE `is_suspended` = ?";
	private static final String GET_USER = "SELECT * FROM `user` WHERE `username` = ?";
	private static final String GET_CUSTOMER = "SELECT * FROM `customer` WHERE `username` = ?";
	private static final String RESTAURANT_DETAILS = "SELECT * FROM `restaurant` WHERE `id` = 0";
	private static final String GET_TABLES = "SELECT * FROM `table`";
	private static final String GET_TABLE = "SELECT * FROM `table` WHERE `number` = ?";
	private static final String GET_RESERVATIONS = """
		SELECT * FROM `reservation` WHERE `reserve_time` > CURRENT_TIMESTAMP()
		ORDER BY reserve_time
	""";
	private static final String GET_RESERVATION = "SELECT * FROM `reservation` WHERE `id` = ?";

	//UPDATE
	private static final String EDIT_RESTAURANT_DETAIL = """
		INSERT INTO `restaurant` (`id`, `name`, `address`, `phone`, `email`)
		VALUES (0, @name:=?, @address:=?, @phone:=?, @email:=?)
		ON DUPLICATE KEY UPDATE `name` = @name, `address` = @address, `phone` = @phone, `email` = @email
	""";
	private static final String EDIT_TABLE = """
		INSERT INTO `table` (`number`, `capacity`, `type`)
		VALUES (?, @capacity:=?, @type:=?)
		ON DUPLICATE KEY UPDATE `capacity` = @capacity, `type` = @type
	""";

	private static final String CHANGE_STATUS_RESERVATION = "UPDATE `reservation` SET `status` = ? WHERE `id` = ?";
	private static final String SUSPEND_CUSTOMER = "UPDATE `customer` SET `is_suspended` = ? WHERE `username` = ?";
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

	//region restaurant detail
	public boolean existRestaurant() {
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(RESTAURANT_DETAILS);
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

	public String getRestaurantDetail(String column) {
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(RESTAURANT_DETAILS);
			result = statement.executeQuery();
			if (result.next()) {
				return result.getString(column);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
		return "N/A";
	}

	public void updateRestaurantDetail(String name, String address, String phone, String email) {
		PreparedStatement statement = null;
		try {
			statement = sql.prepareStatement(EDIT_RESTAURANT_DETAIL);
			statement.setString(1, name);
			statement.setString(2, address);
			statement.setString(3, phone);
			statement.setString(4, email);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(null, statement);
		}
	}
	//endregion

	//region tables
	public List<Table> getTables() {
		List<Table> tables = new ArrayList<>();
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(GET_TABLES);
			result = statement.executeQuery();
			while (result.next()) {
				tables.add(new Table(
								result.getInt("number"),
								result.getInt("capacity"),
								result.getString("type")
						)
				);
			}
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
						result.getString("type")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
		return table;
	}

	public void updateTable(int number, int capacity, String type) {
		PreparedStatement statement = null;
		try {
			statement = sql.prepareStatement(EDIT_TABLE);
			statement.setInt(1, number);
			statement.setInt(2, capacity);
			statement.setString(3, type);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(null, statement);
		}
	}
	//endregion

	//region reservations
	public List<Reservation> getReservations() {
		List<Reservation> reservations = new ArrayList<>();
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(GET_RESERVATIONS);
			result = statement.executeQuery();
			while (result.next()) {
				reservations.add(new Reservation(
						result.getInt("id"),
						getTable(result.getInt("table_number")),
						getUser(result.getString("customer")),
						result.getInt("no_of_people"),
						result.getTimestamp("reserve_time"),
						result.getTimestamp("create_time"),
						ReserveStatus.getById(result.getInt("status"))
						)
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
		return reservations;
	}

	public User getUser(String user) {
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(GET_USER);
			statement.setString(1, user);
			result = statement.executeQuery();
			if (result.next()) {
				return new User(
						result.getString("username"),
						result.getString("password"),
						result.getTimestamp("create_time"),
						getCustomer(user));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
		return null;
	}

	public Customer getCustomer(String user) {
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(GET_CUSTOMER);
			statement.setString(1, user);
			result = statement.executeQuery();
			if (result.next()) {
				return new Customer(
						result.getString("username"),
						result.getString("name"),
						result.getString("phone"),
						result.getString("gender"),
						result.getDate("dob"),
						result.getBoolean("is_suspended")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
		return null;
	}

	public Reservation getReservation(int id) {
		Reservation reservation = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(GET_RESERVATION);
			statement.setInt(1, id);
			result = statement.executeQuery();
			if (result.next()) {
				reservation = new Reservation(
						result.getInt("id"),
						getTable(result.getInt("table_number")),
						getUser(result.getString("customer")),
						result.getInt("no_of_people"),
						result.getTimestamp("reserve_time"),
						result.getTimestamp("create_time"),
						ReserveStatus.getById(result.getInt("status"))
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
		return reservation;
	}

	public void changeStatusReservation(Reservation reservation, ReserveStatus status) {
		PreparedStatement statement = null;
		try {
			statement = sql.prepareStatement(CHANGE_STATUS_RESERVATION);
			statement.setInt(1, status.getId());
			statement.setInt(2, reservation.id());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(null, statement);
		}
	}

	public List<Customer> getCustomers() {
		List<Customer> customers = new ArrayList<>();
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(GET_CUSTOMERS);
			result = statement.executeQuery();
			while (result.next()) {
				customers.add(new Customer(
						result.getString("username"),
						result.getString("name"),
						result.getString("phone"),
						result.getString("gender"),
						result.getDate("dob"),
						result.getBoolean("is_suspended")
						)
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
		return customers;
	}

	public void suspendCustomer(Customer customer, boolean suspend) {
		PreparedStatement statement = null;
		try {
			statement = sql.prepareStatement(SUSPEND_CUSTOMER);
			statement.setBoolean(1, suspend);
			statement.setString(2, customer.username());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(null, statement);
		}
	}

	public List<Customer> getCustomers(boolean suspended) {
		List<Customer> customers = new ArrayList<>();
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(GET_AVAILABLE_CUSTOMERS);
			statement.setBoolean(1, suspended);
			result = statement.executeQuery();
			while (result.next()) {
				customers.add(new Customer(
						result.getString("username"),
						result.getString("name"),
						result.getString("phone"),
						result.getString("gender"),
						result.getDate("dob"),
						result.getBoolean("is_suspended"))
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
		return customers;
	}
	//endregion
}
