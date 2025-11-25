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
                case "5": runTransactionDemo(conn); break;
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

    private static void insertUser(Connection conn, Scanner sc) { }
    private static void insertArtist(Connection conn, Scanner sc) { }
    private static void insertSong(Connection conn, Scanner sc) { }

    /* ---------------------------------------------------
       5. UPDATE OPERATIONS
       --------------------------------------------------- */
    private static void updateMenu(Connection conn, Scanner sc) {
        while (true) {
            System.out.println("\n===== MUSIC DB UPDATE OPTIONS MENU =====");
            System.out.println("1. Update User");
            System.out.println("2. Update Artist");
            System.out.println("3. Update Song TimesPlayed");
            System.out.println("0. Return To Main Menu");
            System.out.print("Choose: ");

            String c = sc.nextLine();

            switch (c) {
                case "1": updateUser(conn, sc); break;
                case "2": updateArtist(conn, sc); break;
                case "3": updateSongTimesPlayed(conn, sc); break;
                case "0": return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void updateUser(Connection conn, Scanner sc) { }
    private static void updateArtist(Connection conn, Scanner sc) { }
    private static void updateSongTimesPlayed(Connection conn, Scanner sc) { }

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

    private static void deleteUser(Connection conn, Scanner sc) { }
    private static void deleteArtist(Connection conn, Scanner sc) { }
    private static void deleteSong(Connection conn, Scanner sc) { }

    /* ---------------------------------------------------
       7. TRANSACTION WORKFLOW (commit + rollback)
       --------------------------------------------------- */
    private static void runTransactionDemo(Connection conn) {
        System.out.println("Running transactional workflow...");
        try {
            conn.setAutoCommit(false);

            // Example: Insert Artist, Insert Album, Insert Song
            // If any fail → rollback

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
            System.out.println("2. Call Stored Procedure");
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

    private static void callView(Connection conn) { }
    private static void callStoredProcedure(Connection conn) { }


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
}
