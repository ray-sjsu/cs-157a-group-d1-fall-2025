import java.sql.*;
import java.io.FileInputStream;
import java.util.Properties;

public class App {
    public static void main(String[] args) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("java/db.properties")) {
            props.load(fis);
        } catch (Exception e) {
            System.out.println("Could not load db.properties: " + e.getMessage());
            return;
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.password");

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            System.out.println("Connected to DB successfully!");
            String sql = "SELECT title, author_id FROM Book";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getString("title") + " | Author ID: " + rs.getInt("author_id"));
                }
            }
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }
}
