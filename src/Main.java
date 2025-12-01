import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;
import java.io.FileInputStream;

public class Main {

    public static void main(String[] args) {
        try (Connection conn = getConnection();
             Scanner sc = new Scanner(System.in)) {

            if (conn == null) {
                System.out.println("DB connection failed.");
                return;
            }

            System.out.println("Connected to DB successfully!");

            runMenu(conn, sc);
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }

    /* ---------------------------------------------------
       1. JDBC CONNECTION
       --------------------------------------------------- */
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

    /* ---------------------------------------------------
       2. MAIN MENU (Scanner I/O)
       --------------------------------------------------- */
    private static void runMenu(Connection conn, Scanner sc) {
        while (true) {
            System.out.println("\n===== MUSIC DB MAIN MENU =====");
            System.out.println("1. View Tables");
            System.out.println("2. Insert Data");
            System.out.println("3. Update Data");
            System.out.println("4. Delete Data");
            System.out.println("5. Transaction Demo");
            System.out.println("6. Call View / Stored Procedure");
            System.out.println("999. Reset Database");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1": viewMenu(conn, sc); break;
                case "2": insertMenu(conn, sc); break;
                case "3": updateMenu(conn, sc); break;
                case "4": deleteMenu(conn, sc); break;
                case "5": transactionDemoMenu(conn, sc); break;
                case "6": runViewProcedureMenu(conn, sc); break;
                case "999": resetDatabase(conn); break;
                case "0": return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    /* ---------------------------------------------------
       3. VIEW OPERATIONS
       --------------------------------------------------- */
    private static void viewMenu(Connection conn, Scanner sc) {
        while (true) {
            System.out.println("\n===== MUSIC DB VIEW OPTIONS MENU =====");
            System.out.println("1. View Users");
            System.out.println("2. View Artists");
            System.out.println("3. View Songs");
            System.out.println("0. Return To Main Menu");
            System.out.print("Choose: ");

            String c = sc.nextLine();

            switch (c) {
                case "1": viewUsers(conn); break;
                case "2": viewArtists(conn); break;
                case "3": viewSongs(conn); break;
                case "0": return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void viewUsers(Connection conn) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM User");
            ResultSet rs = ps.executeQuery();
            printResultSetTable(rs);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void viewArtists(Connection conn) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Artist");
            ResultSet rs = ps.executeQuery();
            printResultSetTable(rs);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void viewSongs(Connection conn) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Song");
            ResultSet rs = ps.executeQuery();
            printResultSetTable(rs);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /* ---------------------------------------------------
       4. INSERT OPERATIONS
       --------------------------------------------------- */
    private static void insertMenu(Connection conn, Scanner sc) {
        while (true) {
            System.out.println("\n===== MUSIC DB INSERT OPTIONS MENU =====");
            System.out.println("1. Insert User");
            System.out.println("2. Insert Artist");
            System.out.println("3. Insert Song");
            System.out.println("0. Return To Main Menu");
            System.out.print("Choose: ");

            String c = sc.nextLine();

            switch (c) {
                case "1": insertUser(conn, sc); break;
                case "2": insertArtist(conn, sc); break;
                case "3": insertSong(conn, sc); break;
                case "0": return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void insertUser(Connection conn, Scanner sc) {
        while (true) {
            try {
                System.out.println("\n--- Insert New User ---");
                String username = readRequiredString(sc, "Username: ");
                String password = readRequiredString(sc, "Password: ");

                String insertUserSQL = "INSERT INTO User (Username, Password) VALUES (?, ?)";

                try (PreparedStatement ps = conn.prepareStatement(insertUserSQL)) {
                    ps.setString(1, username);
                    ps.setString(2, password);
                    ps.executeUpdate();
                    System.out.println("User added successfully!");
                }

                String selectSQL = "SELECT UserID, Username, Password FROM User WHERE Username = ?";
                try (PreparedStatement ps = conn.prepareStatement(selectSQL)) {
                    ps.setString(1, username);
                    ResultSet rs = ps.executeQuery();
                    printResultSetTable(rs);
                }

                return; // success → back to menu

            } catch (SQLException e) {
                System.out.println("Error inserting user: " + e.getMessage());
                if (askRetry(sc)) return;
            }
        }
    }
    private static void insertArtist(Connection conn, Scanner sc) {
        while (true) {
            try {
                System.out.println("\n--- Insert New Artist ---");
                String name = readRequiredString(sc, "Artist Name: ");
                System.out.print("Genre (optional): ");
                String genre = sc.nextLine().trim();
                System.out.print("Country (optional): ");
                String country = sc.nextLine().trim();
                Integer userId = readOptionalInt(sc, "UserID (optional): ");

                String insertArtistSQL = "INSERT INTO Artist (Name, Genre, Country, UserID) VALUES (?, ?, ?, ?)";

                try (PreparedStatement ps = conn.prepareStatement(insertArtistSQL)) {
                    ps.setString(1, name);
                    ps.setString(2, genre.isEmpty() ? null : genre);
                    ps.setString(3, country.isEmpty() ? null : country);

                    if (userId == null)
                        ps.setNull(4, java.sql.Types.INTEGER);
                    else
                        ps.setInt(4, userId);

                    ps.executeUpdate();
                    System.out.println("Artist added successfully!");
                }

                String selectSQL = "SELECT ArtistID, Name, Genre, Country, UserID FROM Artist WHERE Name = ?";
                try (PreparedStatement ps = conn.prepareStatement(selectSQL)) {
                    ps.setString(1, name);
                    ResultSet rs = ps.executeQuery();
                    printResultSetTable(rs);
                }

                return;

            } catch (SQLException e) {
                System.out.println("Error inserting artist: " + e.getMessage());
                if (askRetry(sc)) return;
            }
        }
    }
    private static void insertSong(Connection conn, Scanner sc) {
        while (true) {
            try {
                System.out.println("\n--- Insert New Song ---");
                String title = readRequiredString(sc, "Song Title: ");
                int duration = readPositiveInt(sc, "Duration (seconds > 0): ");
                int albumId = readPositiveInt(sc, "AlbumID: ");

                String insertSongSQL = "INSERT INTO Song (Title, Duration, AlbumID) VALUES (?, ?, ?)";

                try (PreparedStatement ps = conn.prepareStatement(insertSongSQL)) {
                    ps.setString(1, title);
                    ps.setInt(2, duration);
                    ps.setInt(3, albumId);
                    ps.executeUpdate();
                    System.out.println("Song added successfully!");
                }

                String selectSQL = "SELECT SongID, Title, Duration, AlbumID FROM Song WHERE Title = ?";
                try (PreparedStatement ps = conn.prepareStatement(selectSQL)) {
                    ps.setString(1, title);
                    ResultSet rs = ps.executeQuery();
                    printResultSetTable(rs);
                }

                return;

            } catch (SQLException e) {
                System.out.println("Error inserting song: " + e.getMessage());
                if (askRetry(sc)) return;
            }
        }
    }


    /* ---------------------------------------------------
       5. UPDATE OPERATIONS
       --------------------------------------------------- */
    private static void updateMenu(Connection conn, Scanner sc) {
        while (true) {
            System.out.println("\n===== MUSIC DB UPDATE OPTIONS MENU =====");
            System.out.println("1. Update User");
            System.out.println("2. Update Artist");
            System.out.println("3. Update Song");
            System.out.println("0. Return To Main Menu");
            System.out.print("Choose: ");

            String c = sc.nextLine();

            switch (c) {
                case "1": updateUser(conn, sc); break;
                case "2": updateArtist(conn, sc); break;
                case "3": updateSong(conn, sc); break;
                case "0": return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void updateUser(Connection conn, Scanner sc) {
        viewUsers(conn);
        System.out.println("\n--- Update User ---");

        int id = readInt(sc, "Enter UserID: ");

        // Load old data
        String sqlOld = "SELECT Username, Password FROM User WHERE UserID = ?";
        String oldUser, oldPass;

        try (PreparedStatement ps = conn.prepareStatement(sqlOld)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("No user found with that ID.");
                    return;
                }
                oldUser = rs.getString("Username");
                oldPass = rs.getString("Password");
            }
        } catch (SQLException e) {
            System.out.println("Error loading user: " + e.getMessage());
            return;
        }

        // Prefill update
        System.out.println("Press ENTER to keep current value.");
        String newUser = chooseNewValue(sc, "Username", oldUser);
        String newPass = chooseNewValue(sc, "Password", oldPass);

        // UPDATE
        String updateUserSQL = "UPDATE User SET Username = ?, Password = ? WHERE UserID = ?";

        try (PreparedStatement ps = conn.prepareStatement(updateUserSQL)) {
            ps.setString(1, newUser);
            ps.setString(2, newPass);
            ps.setInt(3, id);
            ps.executeUpdate();
            System.out.println("User updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }

        String selectSQL = "SELECT UserID, Username, Password FROM User WHERE Username = ?";
        try (PreparedStatement ps = conn.prepareStatement(selectSQL)) {
            ps.setString(1, newUser);
            ResultSet rs = ps.executeQuery();
            printResultSetTable(rs);
        } catch (SQLException e) {
            // None
        }

    }
    private static void updateArtist(Connection conn, Scanner sc) {
        viewArtists(conn);
        System.out.println("\n--- Update Artist ---");

        int id = readInt(sc, "Enter ArtistID: ");

        // Load existing row
        String sqlOld = "SELECT Name, Genre, Country, UserID FROM Artist WHERE ArtistID = ?";
        String oldName, oldGenre, oldCountry;
        Integer oldUserID;

        try (PreparedStatement ps = conn.prepareStatement(sqlOld)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("No artist found with that ID.");
                    return;
                }
                oldName = rs.getString("Name");
                oldGenre = rs.getString("Genre");
                oldCountry = rs.getString("Country");
                oldUserID = (Integer) rs.getObject("UserID");
            }
        } catch (SQLException e) {
            System.out.println("Error loading artist: " + e.getMessage());
            return;
        }

        // Prefill prompt
        System.out.println("Press ENTER to keep current value.");

        String name = chooseNewValue(sc, "Name", oldName);
        String genre = chooseNewValue(sc, "Genre", oldGenre == null ? "" : oldGenre);
        String country = chooseNewValue(sc, "Country", oldCountry == null ? "" : oldCountry);
        Integer userId = chooseNewInt(sc, "UserID", oldUserID);

        // UPDATE
        String updateArtistSQL = "UPDATE Artist SET Name=?, Genre=?, Country=?, UserID=? WHERE ArtistID=?";

        try (PreparedStatement ps = conn.prepareStatement(updateArtistSQL)) {
            ps.setString(1, name);
            ps.setString(2, genre.isEmpty() ? null : genre);
            ps.setString(3, country.isEmpty() ? null : country);

            if (userId == null)
                ps.setNull(4, java.sql.Types.INTEGER);
            else
                ps.setInt(4, userId);

            ps.setInt(5, id);

            ps.executeUpdate();
            System.out.println("Artist updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating artist: " + e.getMessage());
        }

        String selectSQL = "SELECT ArtistID, Name, Genre, Country, UserID FROM Artist WHERE Name = ?";
        try (PreparedStatement ps = conn.prepareStatement(selectSQL)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            printResultSetTable(rs);
        } catch (SQLException e) {
            // None
        }
    }
    private static void updateSong(Connection conn, Scanner sc) {
        viewSongs(conn);
        System.out.println("\n--- Update Song ---");

        int id = readInt(sc, "Enter SongID: ");

        // Load existing row
        String sqlOld = "SELECT Title, Duration, AlbumID FROM Song WHERE SongID = ?";
        String oldTitle;
        int oldDuration, oldAlbumID;

        try (PreparedStatement ps = conn.prepareStatement(sqlOld)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("No song found with that ID.");
                    return;
                }
                oldTitle = rs.getString("Title");
                oldDuration = rs.getInt("Duration");
                oldAlbumID = rs.getInt("AlbumID");
            }
        } catch (SQLException e) {
            System.out.println("Error loading song: " + e.getMessage());
            return;
        }

        // Prefill
        System.out.println("Press ENTER to keep current value.");

        String newTitle = chooseNewValue(sc, "Title", oldTitle);

        System.out.print("Duration [" + oldDuration + "]: ");
        String durStr = sc.nextLine().trim();
        int newDuration = durStr.isEmpty() ? oldDuration : Integer.parseInt(durStr);

        System.out.print("AlbumID [" + oldAlbumID + "]: ");
        String albStr = sc.nextLine().trim();
        int newAlbumID = albStr.isEmpty() ? oldAlbumID : Integer.parseInt(albStr);

        // UPDATE
        String updateSongSQL = "UPDATE Song SET Title=?, Duration=?, AlbumID=? WHERE SongID=?";

        try (PreparedStatement ps = conn.prepareStatement(updateSongSQL)) {
            ps.setString(1, newTitle);
            ps.setInt(2, newDuration);
            ps.setInt(3, newAlbumID);
            ps.setInt(4, id);

            ps.executeUpdate();
            System.out.println("Song updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating song: " + e.getMessage());
        }

        String selectSQL = "SELECT SongID, Title, Duration, AlbumID FROM Song WHERE Title = ?";
        try (PreparedStatement ps = conn.prepareStatement(selectSQL)) {
            ps.setString(1, newTitle);
            ResultSet rs = ps.executeQuery();
            printResultSetTable(rs);
        } catch (SQLException e) {
            // None
        }

    }

    /* ---------------------------------------------------
       6. DELETE OPERATIONS
       --------------------------------------------------- */
    private static void deleteMenu(Connection conn, Scanner sc) {
        while (true) {
            System.out.println("\n===== MUSIC DB DELETE OPTIONS MENU =====");
            System.out.println("1. Delete User");
            System.out.println("2. Delete Artist");
            System.out.println("3. Delete Song");
            System.out.println("0. Return To Main Menu");
            System.out.print("Choose: ");

            String c = sc.nextLine();

            switch (c) {
                case "1": deleteUser(conn, sc); break;
                case "2": deleteArtist(conn, sc); break;
                case "3": deleteSong(conn, sc); break;
                case "0": return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void deleteUser(Connection conn, Scanner sc) {
        viewUsers(conn);
        int userId = readInt(sc, "Enter UserID to delete: ");

        String deleteUserSQL = "DELETE FROM User WHERE UserID = ?";

        try (PreparedStatement ps = conn.prepareStatement(deleteUserSQL)) {
            ps.setInt(1, userId);

            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("User deleted.");
            else
                System.out.println("No user found with that ID.");
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }
    private static void deleteArtist(Connection conn, Scanner sc) {
        viewArtists(conn);
        int artistId = readInt(sc, "Enter ArtistID to delete: ");

        String deleteArtistSQL = "DELETE FROM Artist WHERE ArtistID = ?";

        try (PreparedStatement ps = conn.prepareStatement(deleteArtistSQL)) {
            ps.setInt(1, artistId);

            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("Artist deleted.");
            else
                System.out.println("No artist found with that ID.");
        } catch (SQLException e) {
            System.out.println("Error deleting artist: " + e.getMessage());
        }
    }
    private static void deleteSong(Connection conn, Scanner sc) {
        viewSongs(conn);
        int songId = readInt(sc, "Enter SongID to delete: ");

        String deleteSongSQL = "DELETE FROM Song WHERE SongID = ?";

        try (PreparedStatement ps = conn.prepareStatement(deleteSongSQL)) {
            ps.setInt(1, songId);

            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("Song deleted.");
            else
                System.out.println("No song found with that ID.");
        } catch (SQLException e) {
            System.out.println("Error deleting song: " + e.getMessage());
        }
    }

    /* ---------------------------------------------------
       7. TRANSACTION WORKFLOW (commit + rollback)
       --------------------------------------------------- */
    private static void transactionDemoMenu(Connection conn, Scanner sc) {
        while (true) {
            System.out.println("\n===== TRANSACTION DEMO MENU =====");
            System.out.println("1. Run Transaction and COMMIT");
            System.out.println("2. Run Transaction and ROLLBACK");
            System.out.println("0. Return To Main Menu");
            System.out.print("Choose: ");

            String c = sc.nextLine();

            switch (c) {
                case "1":
                    System.out.println("\n--- Starting Transaction and Committing ---");
                    try {
                        conn.setAutoCommit(false);
                        runTransactionDemo(conn);

                    } catch (Exception e) {
                        System.out.println("Error during commit workflow: " + e.getMessage());
                        try { conn.rollback(); } catch (SQLException ignored) {}
                    } finally {
                        try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
                    }
                    break;

                case "2":
                    System.out.println("\n--- Starting Transaction and Rolling Back ---");
                    try {
                        conn.setAutoCommit(false);
                        runTransactionDemo(conn);

                        System.out.println("Forcing rollback...");
                        conn.rollback();
                        System.out.println("Transaction rolled back successfully.");
                    } catch (Exception e) {
                        System.out.println("Error during rollback workflow: " + e.getMessage());
                        try { conn.rollback(); } catch (SQLException ignored) {}
                    } finally {
                        try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void runTransactionDemo(Connection conn) {
        System.out.println("Running transactional workflow...");
        try {
            conn.setAutoCommit(false);

            // TODO - Transaction demo
            // Example: Insert User, Insert Artist, Insert Song OR Add Song, Link to Artist? What ever you want.
            // placeholder for multi-step operations

            conn.commit();
            System.out.println("Transaction committed.");
        } catch (Exception e) {
            try { conn.rollback(); } catch (SQLException ignored) {}
            System.out.println("Transaction rolled back: " + e.getMessage());
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    /* ---------------------------------------------------
       8. VIEW / STORED PROCEDURE CALLS (SQL)
       --------------------------------------------------- */
    private static void runViewProcedureMenu(Connection conn, Scanner sc) {
        while (true) {
            System.out.println("\n===== MUSIC DB VIEW PROCEDURES OPTIONS MENU =====");
            System.out.println("1. View (SELECT from VIEW)");
            System.out.println("2. Call Stored Procedure/Routine");
            System.out.println("0. Return To Main Menu");
            System.out.print("Choose: ");

            String c = sc.nextLine();

            switch (c) {
                case "1": callView(conn); break;
                case "2": callStoredProcedure(conn); break;
                case "0": return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void callView(Connection conn) {
        String filePath = "sql/view.sql";
        printSqlFileBeforeRunning(filePath);

        System.out.println("\nCalling view SQL...");

        try {
            runSqlFile(conn, filePath);
            System.out.println("View executed successfully.");
        } catch (Exception e) {
            System.out.println("Failed to execute view SQL: " + e.getMessage());
        }
    }
    private static void callStoredProcedure(Connection conn) {
        String filePath = "sql/routine.sql";
        printSqlFileBeforeRunning(filePath);

        System.out.println("\nCalling stored procedure SQL...");

        try {
            runSqlFile(conn, filePath);
            System.out.println("Stored procedure executed successfully.");
        } catch (Exception e) {
            System.out.println("Failed to execute stored procedure SQL: " + e.getMessage());
        }
    }


    /* ---------------------------------------------------
       9. Helper Functions
       --------------------------------------------------- */
    // SQL
    private static void resetDatabase(Connection conn) {
        System.out.println("Resetting database...");

        try {
            runSqlFile(conn, "sql/create_and_populate.sql");
            System.out.println("Database reset complete.");
        } catch (Exception e) {
            System.out.println("Failed to reset database: " + e.getMessage());
        }
    }
    private static void runSqlFile(Connection conn, String filePath) throws Exception {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                int commentIndex = line.indexOf("--");
                if (commentIndex != -1) {
                    line = line.substring(0, commentIndex).trim();
                }

                // Skip empty lines or comments
                if (line.isEmpty() || line.startsWith("--") || line.startsWith("#")) {
                    continue;
                }

                // Accumulate SQL text
                sb.append(line).append(" ");

                // If the line ends with semicolon → execute the statement
                if (line.endsWith(";")) {
                    String sql = sb.toString().trim();
                    sb.setLength(0); // reset buffer

                    // Remove the trailing semicolon
                    sql = sql.substring(0, sql.length() - 1);

                    try {
                        System.out.println("\nExecuting SQL:\n" + sql);

                        // SELECT behavior:
                        if (sql.toLowerCase().startsWith("select")) {
                            tryPrintSelectResult(conn, sql);
                        } else {
                            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                                ps.execute();
                                System.out.println("Statement executed successfully.");
                            }
                        }

                    } catch (SQLException e) {
                        System.out.println("\n--- SQL EXECUTION ERROR ---");
                        System.out.println("Error executing: " + sql);
                        System.out.println("Message: " + e.getMessage());
                    }
                }
            }
        }
    }
    private static void printSqlFileBeforeRunning(String filePath) {
        System.out.println("\n--- SQL FILE: " + filePath + " ---");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println("Failed to read SQL file: " + e.getMessage());
        }

        System.out.println("\n--- END OF SQL FILE ---\n");
    }
    private static void tryPrintSelectResult(Connection conn, String sql) {
        String trimmed = sql.trim().toLowerCase();

        if (!trimmed.startsWith("select")) return;  // not a SELECT statement

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- RESULT SET ---");
            printResultSetTable(rs);

        } catch (SQLException e) {
            System.out.println("Error printing SELECT result: " + e.getMessage());
        }
    }


    // UI
    private static void printResultSetTable(ResultSet rs) {
        try {
            ResultSetMetaData md = rs.getMetaData();
            int colCount = md.getColumnCount();

            // Determine column widths
            int[] widths = new int[colCount];

            // Initialize with column name lengths
            for (int i = 0; i < colCount; i++) {
                widths[i] = md.getColumnName(i + 1).length();
            }

            // Store rows and compute field widths
            java.util.List<String[]> rows = new java.util.ArrayList<>();

            while (rs.next()) {
                String[] row = new String[colCount];
                for (int i = 0; i < colCount; i++) {
                    String value = rs.getString(i + 1);
                    if (value == null) value = "NULL";
                    row[i] = value;
                    widths[i] = Math.max(widths[i], value.length());
                }
                rows.add(row);
            }

            // Print header
            System.out.println();
            for (int i = 0; i < colCount; i++) {
                System.out.printf("%-" + (widths[i] + 2) + "s", md.getColumnName(i + 1));
            }
            System.out.println();

            // Separator line
            for (int i = 0; i < colCount; i++) {
                System.out.print("-".repeat(widths[i] + 2));
            }
            System.out.println();

            // Print rows
            for (String[] row : rows) {
                for (int i = 0; i < colCount; i++) {
                    System.out.printf("%-" + (widths[i] + 2) + "s", row[i]);
                }
                System.out.println();
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error printing table: " + e.getMessage());
        }
    }
    public static boolean askRetry(Scanner sc) {
        while (true) {
            System.out.print("Try again? (y/n): ");
            String choice = sc.nextLine().trim().toLowerCase();

            if (choice.equals("y")) return false;
            if (choice.equals("n")) return true;

            System.out.println("Invalid option. Please enter 'y' or 'n'.");
        }
    }


    // Insert
    private static String readRequiredString(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) return input;

            System.out.println("This field is required. Please try again.");
        }
    }
    private static Integer readOptionalInt(Scanner sc, String prompt) {
        System.out.print(prompt);
        String input = sc.nextLine().trim();

        if (input.isEmpty()) return null;

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Treating as NULL.");
            return null;
        }
    }
    private static int readPositiveInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value > 0) return value;
                System.out.println("Value must be > 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }


    // Update
    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }
    private static String chooseNewValue(Scanner sc, String fieldName, String oldValue) {
        System.out.print(fieldName + " [" + oldValue + "]: ");
        String input = sc.nextLine().trim();
        return input.isEmpty() ? oldValue : input;
    }
    private static Integer chooseNewInt(Scanner sc, String fieldName, Integer oldValue) {
        System.out.print(fieldName + " [" + (oldValue == null ? "" : oldValue) + "]: ");
        String input = sc.nextLine().trim();

        if (input.isEmpty()) return oldValue;

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Keeping old value.");
            return oldValue;
        }
    }
}

