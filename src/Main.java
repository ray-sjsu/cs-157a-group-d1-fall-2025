import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Connected to DB successfully!");
            }
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }

    private static Connection getConnection() {
        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream("src/app.properties")) {
            props.load(fis);
        } catch (Exception e) {
            System.out.println("Could not load app.properties: " + e.getMessage());
            return null;
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.password");

        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.out.println("Failed to connect: " + e.getMessage());
            return null;
        }
    }
}
