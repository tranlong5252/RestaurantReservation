package tranlong5252.objects;

import java.sql.Timestamp;

public record User(String username, String password, String email, Timestamp created, Customer customer) {
}
