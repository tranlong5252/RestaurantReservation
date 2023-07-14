package tranlong5252;

import java.sql.*;
import java.time.LocalDateTime;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MySQL {
	private Connection sql;
	
	//region QUERIES

	//AUTHENTICATION
	private static final String LOGIN = "SELECT * FROM `user` WHERE `username` = ? AND `password` = ?";
	private static final String REGISTER = "INSERT INTO `user` (`username`, `password`) VALUES (?, ?)";
	private static final String EXIST_RESTAURANT = "SELECT * FROM `manager` WHERE `restaurant_id` = ? AND `user` = ?";

	//INFO
	private static final String VIEW_RESTAURANTS = """
		SELECT * FROM `restaurants`.`manager`
		INNER JOIN `restaurants`.`restaurant` on `restaurant`.`id` = `manager`.`restaurant_id`  WHERE `user` = ?
	""";
	private static final String VIEW_RESTAURANT_DETAILS = """
		SELECT * FROM `restaurant` WHERE `id` = ?
		ORDER BY `capacity`, `type`
	""";

	private static final String VIEW_TABLE_DETAILS = "SELECT * FROM `table` WHERE `restaurant_id` = ?";
	private static final String VIEW_TABLES_BY_CAPACITY = """
		SELECT * FROM `table` WHERE `capacity` >= ? AND `restaurant_id` = ?
		ORDER BY `capacity`, `type`
	""";

	private static final String VIEW_ALL_RESERVATION_REQUESTS = """
		SELECT `id`, `table_number`, customer_id, `reserve_time`, `customers`, `status`, `table`.`type` AS `type`
		FROM `reservation`
		INNER JOIN `table` ON `reservation`.`table_number` = `table`.`number`
		ORDER BY `reservation`.`reserve_time`, `reservation`.`status`
	""";

	//UPDATE
	private static final String UPDATE_TABLE = """
        INSERT INTO `table` (`number`, `restaurant_id`, `capacity`, `type`) VALUES (?, ?, @capacity:=?, @type:=?)
        ON DUPLICATE KEY UPDATE `capacity` = @capacity, `type` = @type
    """;
	private static final String DELETE_TABLE = "DELETE FROM `table` WHERE `number` = ? AND `restaurant_id` = ?";

	private static final String ADD_RESTAURANT = "INSERT INTO `restaurant`(`name`, `address`, `phone_number`, `email`, `description`) VALUES (?, ?, ?, ?, ?)";
	private static final String UPDATE_RESTAURANT = """
		INSERT INTO `restaurant` (`id`, `name`, `address`, `phone_number`, `email`, `description`)
		VALUES (?, @name:=?, @address:=?, @phone_number:=?, @email:=?, @description:=?)
		ON DUPLICATE KEY UPDATE `name` = @name, `address` = @address, `phone_number` = @phone_number,
		`email` = @email, `description` = @description""";
	private static final String DELETE_RESTAURANT = "DELETE FROM `restaurant` WHERE `id` = ?";
	private static final String SET_RESTAURANT_OWNER = "INSERT INTO `manager` (`restaurant_id`, `user`) VALUES (?, ?)";

	private static final String UPDATE_ORDER = """
        INSERT INTO `order` (`id`, `customer_id`, `order_time`, `status`)
        VALUES (?, @customer:=?, @order_time:=?, @status:=?)
        ON DUPLICATE KEY UPDATE `customer_id` = @customer, `order_time` = @order_time, `status` = @status
    """;
	private static final String DELETE_ORDER = "DELETE FROM `order` WHERE `id` = ?";

	private static final String UPDATE_RESERVATION = """
        INSERT INTO `reservation` (`table_number`, `customer_id`, `reserve_time`, `customers`, `status`)
        VALUES (?, ?, ?, ?, ?)
    """;
	private static final String DELETE_RESERVATION = "DELETE FROM `reservation` WHERE `id` = ?";

	private static final String CHANGE_STATUS_RESERVATION_REQUESTS = "UPDATE `reservation` SET `status` = ? WHERE `id` = ?";

	private static final String GET_CUSTOMER = """
		SELECT * FROM `customer` WHERE `id` = ? AND `restaurant_id` = ?
		ORDER BY `id`
	""";
	private static final String GET_CUSTOMERS = "SELECT * FROM `customer` WHERE `restaurant_id` = ? ORDER BY `id`";
	private static final String ADD_CUSTOMER = """
        INSERT INTO `customer` (`name`, `dob`, `gender`, `phone`, `restaurant_id`)
        VALUES (@name:=?, @dob:=?, @gender:=?, @phone:=?, @restaurant_id:=?)
        ON DUPLICATE KEY UPDATE `name` = @name, `dob` = @dob, `gender` = @gender, `phone` = @phone, `restaurant_id` = @restaurant_id
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

	public void viewRestaurantDetails(int id) {
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(VIEW_RESTAURANT_DETAILS);
			statement.setInt(1, id);
			result = statement.executeQuery();
			int size = result.getFetchSize();
			if (size == 0) {
				System.out.println("No restaurant found with ID " + id);
				return;
			}
			while (result.next()) {
				System.out.println("Restaurant ID: " + result.getInt("id"));
				System.out.println("   Name: " + result.getString("name"));
				System.out.println("   Restaurant Address: " + result.getString("address"));
				System.out.println("   Restaurant Phone: " + result.getString("phone_number"));
				System.out.println("   Restaurant Email: " + result.getString("email"));
				System.out.println("   Restaurant Description: " + result.getString("description"));
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
	}

	public void updateRestaurantDetails(int id, String name, String address, String phone, String email, String description) {
		PreparedStatement statement = null;
		try {
			statement = sql.prepareStatement(UPDATE_RESTAURANT);
			statement.setInt(1, id);
			statement.setString(2, name);
			statement.setString(3, address);
			statement.setString(4, phone);
			statement.setString(5, email);
			statement.setString(6, description);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(null, statement);
		}
	}

	public void deleteRestaurantDetails(String id) {
		PreparedStatement statement = null;
		try {
			statement = sql.prepareStatement(DELETE_RESTAURANT);
			statement.setString(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(null, statement);
		}
	}

	public void viewTable(int restaurantId) {
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(VIEW_TABLE_DETAILS);
			statement.setInt(1, restaurantId);
			result = statement.executeQuery();
			int size = result.getFetchSize();
			if (size == 0) {
				System.out.println("No tables found");
				return;
			}
			while (result.next()) {
				System.out.println("Table No.: " + result.getInt("number"));
				System.out.println("   Type: " + result.getString("type"));
				System.out.println("   Capacity: " + result.getInt("capacity"));
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
	}

	public void updateTable(int ResId, int number, int capacity, String type) {
		PreparedStatement statement = null;
		try {
			statement = sql.prepareStatement(UPDATE_TABLE);
			statement.setInt(1, number);
			statement.setInt(2, ResId);
			statement.setInt(3, capacity);
			statement.setString(4, type);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(null, statement);
		}
	}

	public void deleteTable(int resId, int number) {
		PreparedStatement statement = null;
		try {
			statement = sql.prepareStatement(DELETE_TABLE);
			statement.setInt(1, number);
			statement.setInt(2, resId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(null, statement);
		}
	}

	public void createReservationRequest(int tableNumber, int customerId, int customers, LocalDateTime time, int status) {
		PreparedStatement statement = null;
		try {
			statement = sql.prepareStatement(UPDATE_RESERVATION);
			statement.setInt(1, tableNumber);
			statement.setInt(2, customerId);
			statement.setTimestamp(3, Timestamp.valueOf(time));
			statement.setInt(4, customers);
			statement.setInt(5, status);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(null, statement);
		}
	}

	public void viewReservationRequests() {
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(VIEW_ALL_RESERVATION_REQUESTS);
			result = statement.executeQuery();
			while (result.next()) {
				System.out.println("Reservation ID: " + result.getInt("id"));
				System.out.println("  Number of People: " + result.getInt("customers"));
				System.out.println("  Table: " + result.getInt("table_number") + " ("+ result.getString("type") + ")");
				System.out.println("  Time: " + result.getTimestamp("reserve_time"));
				System.out.println("  Status: " + ReserveStatus.getById(result.getInt("status")));
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
	}

	public void confirmOrRejectReservationRequests(String id, String status) {
		PreparedStatement statement = null;
		try {
			statement = sql.prepareStatement(CHANGE_STATUS_RESERVATION_REQUESTS);
			statement.setString(1, status);
			statement.setString(2, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(null, statement);
		}
	}

	public boolean existRestaurant(int id, String user) {
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(EXIST_RESTAURANT);
			statement.setInt(1, id);
			statement.setString(2, user);
			result = statement.executeQuery();
			while (result.next()) {
				if (result.getInt("restaurant_id") == id) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
		return false;
	}

	public int addRestaurantDetails(String name, String address, String phone, String email, String description) {
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(ADD_RESTAURANT, RETURN_GENERATED_KEYS);
			statement.setString(1, name);
			statement.setString(2, address);
			statement.setString(3, phone);
			statement.setString(4, email);
			statement.setString(5, description);
			statement.executeUpdate();
			result = statement.getGeneratedKeys();
			result.next();
			return result.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
		return -1;
	}

	public void setRestaurantOwner(int id, String username) {
		PreparedStatement statement = null;
		try {
			statement = sql.prepareStatement(SET_RESTAURANT_OWNER);
			statement.setInt(1, id);
			statement.setString(2, username);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(null, statement);
		}
	}

	public void viewRestaurants(String username) {
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(VIEW_RESTAURANTS);
			statement.setString(1, username);
			result = statement.executeQuery();
			while (result.next()) {
				System.out.println("Restaurant ID: " + result.getInt("id"));
				System.out.println("   Name: " + result.getString("name"));
				System.out.println("   Address: " + result.getString("address"));
				System.out.println("   Phone: " + result.getString("phone_number"));
				System.out.println("   Email: " + result.getString("email"));
				System.out.println("   Description: " + result.getString("description"));
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
	}

	public int viewTables(int restaurantId, int noOfPeople) {
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(VIEW_TABLES_BY_CAPACITY);
			statement.setInt(1, noOfPeople);
			statement.setInt(2, restaurantId);
			result = statement.executeQuery();
			System.out.println("Available tables:");
			int c = 0;
			while (result.next()) {
				System.out.println("- " + result.getInt("number"));
				c++;
			}
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
		return 0;
	}

	public int getOrAddCustomer(String customerName, String gender, String customerPhone, String dobStr, int restaurantId) {
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			Date dob = null;
			if (!dobStr.isBlank()) {
				dob = Date.valueOf(dobStr);
			}
			statement = sql.prepareStatement(ADD_CUSTOMER, RETURN_GENERATED_KEYS);
			statement.setString(1, customerName);
			statement.setDate(2, dob);
			statement.setString(3, gender);
			statement.setString(4, customerPhone);
			statement.setInt(5, restaurantId);
			statement.executeUpdate();
			result = statement.getGeneratedKeys();
			result.next();
			return result.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
		return -1;
	}

	public void getCustomers(int restaurantId) {
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(GET_CUSTOMERS);
			statement.setInt(1, restaurantId);
			result = statement.executeQuery();
			int c = 0;
			while (result.next()) {
				c++;
				System.out.println("Customer ID " + result.getInt("id") + ": "
						+ result.getString("name")
						+ " | " + result.getString("phone"));
			}
			if (c == 0) {
				System.out.println("No customers found.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
	}

	public void getCustomer(int id, int restaurantId) {
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = sql.prepareStatement(GET_CUSTOMER);
			statement.setInt(1, id);
			statement.setInt(2, restaurantId);
			result = statement.executeQuery();
			while (result.next()) {
				Date dob = result.getDate("dob");
				System.out.println("Customer ID " + result.getInt("id") + ": " + result.getString("name"));
				System.out.println("   Phone: " + result.getString("phone"));
				System.out.println("   Gender: " + result.getString("gender"));
				System.out.println("   DOB: " + (dob == null ? "N/A" : dob.toString()));
				System.out.println("   Joined: " + result.getTimestamp("create_time").toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			cleanup(result, statement);
		}
	}
}
