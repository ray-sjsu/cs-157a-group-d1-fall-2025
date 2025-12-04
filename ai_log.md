# AI collaboration record, prompts used, what you adopted or changed

## Creation of UI/UX menu with error handling
We asked ChatGPT to design boilerplate code for a reusable UI/UX menu. It suggested using try, catch and exception handling to automatically generate error messages without defining for each possible error scenario.

We adopted the boilerplate code and reusable functions, which formed the foundation of our UI/UX console based menu.

However, we had more experience in object-oriented programming principles. The AI made some questionable decisions:
- Recreating Scanner `sc` object for every menu and submenu. This is questionable because Scanner meant to be defined once for use in the entire program to improve performance and enforce DRY principle. We passed along `sc` for every menu and submenu through parameters.
- Example AI suggested function header: `private static void runMenu(Connection conn)`. Then, created new Scanner object in each function.

What we changed and revised is the function headers with Scanner `sc`: `private static void runMenu(Connection conn, Scanner sc)`. Every submenu now passes a Scanner and Connection object. You still need to pass the Connection object if you want to run PreparedStatements. This follows object-oriented programming principles.

### Example Prompt:
We mostly had discussions and debates on code structure and layout in `Main.java` like in this example:
> Should common menus (For each operation) like viewUsers, viewArtists, etc... share a common menu or layout? So, the programmer only needs to define the UI menu once, but each function has a different SQL operation?

### Result:
Short answer: Yes — you can reduce duplication by reusing a common menu layout, but only when the options are the same.
In your case, though, each main menu category (View, Insert, Update, Delete) already has its own menu, and within those menus, the options differ.

However — the VIEW functions themselves can share a common pattern, even if the SQL differs...

## SQL parser
Java does not natively support SQL parsing, and we did not want to use an external library. We wanted to translate/parse a `.sql` file for use in formatted output and troubleshooting. Since this is a SQL class and not a Java class, the SQL parser function was sufficient for our needs with some modifications in handling multi-line comments.

What we adopted is the SQL parser function, alongside some Utility functions. We made a few changes to the initial SQL parser function by also handling DELIMITERS.

### Example Prompt:
> Create SQL parser

### Result:
✔ Read SQL file

✔ Respect line breaks

✔ Build statements until you detect ;

✔ Execute each statement

✔ Skip comments (-- and /* */)

✔ Works perfectly for CREATE TABLE, INSERT, VIEW, even basic PROCEDURE

Here is the improved, stable parser that works for 99% of student projects:
```java
private static void runSqlFile(Connection conn, String filePath) throws Exception {
    StringBuilder sb = new StringBuilder();

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;

        while ((line = br.readLine()) != null) {
            line = line.trim();

            // Skip empty lines and comments
            if (line.isEmpty() || line.startsWith("--") || line.startsWith("#")) {
                continue;
            }

            sb.append(line).append(" ");

            // Statement ends at semicolon
            if (line.endsWith(";")) {
                String sql = sb.toString();
                sb.setLength(0); // reset buffer

                sql = sql.substring(0, sql.length() - 1).trim(); // remove trailing ;

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.execute();
                } catch (SQLException e) {
                    System.out.println("Error executing: " + sql);
                    System.out.println("Message: " + e.getMessage());
                }
            }
        }
    }
}
```

## View Creation Assist
In order to create the view, we asked AI to help format the way that our view should be created, giving us an idea of where to begin. After creating the view, we had AI make the code look cleaner and more concise, allowing for us to add clearer comments.

## Requirements checklist
We asked ChatGPT to create a simple-to-read checklist. An example format was provided before this prompt. The prompt below was the from the `Project Document Initial Instructions` google doc. We made sure to double check and modify the checklist if there were important details missing or reword. This checklist was mainly used for project management and progress tracking.

What we adopted is the checklist. However, we rewrote some entries to be inline with the `Project Document Initial Instructions` google doc. For example, we made sure `.sql` file into its own `sql` folder and `Main.java` into a `src` folder for organization.

### Example Prompt:

> Steps Task 1. JDBC Setup and Connection Test Create an app.properties file that stores database connection info (URL, user, password). Test the connection from Java using JDBC and verify that the driver loads correctly. 2. Build Console Menu with Scanner I/O Build a simple text-based menu (view data, insert, update, delete, run transaction, exit). Use Scanner for input and PreparedStatement for all SQL. Operate on at least 3 key tables in your schema. Each of the 3 tables must have at least one SELECT option. For INSERT/UPDATE/DELETE, choose whichever tables make sense. Implement one transactional workflow that touches more than one table and demonstrates both COMMIT and ROLLBACK. More instructions in steps 3, 4, 5 3. Implement PreparedStatements Write SQL operations (SELECT, INSERT, UPDATE, DELETE), using PreparedStatement objects only. Show this behavior clearly in your video demo. 4. Implement Transactional Workflow Create at least one workflow that uses both COMMIT and ROLLBACK to ensure atomicity. Example: multi-table insert/update that rolls back on failure. Show this behavior clearly in your video demo. 5. Add Input Validation and Error Handling Validate all user input and catch SQLException errors. Provide clear, helpful messages when invalid data or constraint violations occur. Show this behavior clearly in your video demo. 6. Add a View and at Least One Stored Procedure/Function and constraint In MySQL, create: One VIEW (for reporting or convenience) and At least one Stored Routine that enforces a rule or automates a task and a constraint. Test and demonstrate each feature. Show this behavior clearly in your video demo. 7. Test, Debug, and Record Video Demo Perform full testing of your console app and database features. Record a short video (≤ 6 minutes) showing the menu, operations(view, insert,update and delete), the transactional workflow (commit + rollback), and your view/procedure/trigger tests. 8. Documentation and Submission Package Put documents in: CS157A_FinalProject_TeamGroupName.zip: Main.java: your full Java code console app with menu, Scanner input, JDBC connection, PreparedStatements, commit & rollback) create_and_populate.sql : all CREATE TABLE statements, constraints, sample data, plus your view and trigger or stored procedure app.properties : text file with database URL, username, password etc README.md: Instructions for building DB and running your Java program; How was your application built step by step; mention MySQL version/connector info, also screenshots where applicable ai_log.md: AI collaboration record, prompts used, what you adopted or changed Team-roles.txt: Each member’s contribution + short reflection on teamwork video_demo.mp4: (≤6 minute screen recording showing your application as described above) . Make this into a readme checklist. Use the example below as a guideline. Due Date Dec 4

See the checklist below this section.

# CHECKLIST Final Project — JDBC Console Database Application

**CS 157A — Database Management**

**DUE:** December 4, 2025 (Midnight)

---

## 📘 Overview

You will build a **Java console application** that connects to a **MySQL** database using **JDBC**.
Your app must support **menu-driven CRUD operations**, **PreparedStatements only**, a **multi-table transactional workflow** with **COMMIT/ROLLBACK**, and additional **database objects** (view + stored routine + constraint).
You’ll also prepare a **video demo** and a **submission package** with all required files and documentation.

---

| Legend | Description                                  |
| ------ | -------------------------------------------- |
| ☐      | ☐ Not started or in progress                 |
| ✅      | ✅ Completed                                  |
| ❌      | ❌ Not possible                               |
| ⚠️     | ⚠️ Needs bug fixes or partial implementation |

---

## 🛠️ Step 1 — JDBC Setup and Connection Test

* ☐ Create `app.properties` file that stores:

    * ☐ Database URL
    * ☐ Username
    * ☐ Password
* ☐ Load `app.properties` from Java.
* ☐ Use JDBC to connect to the MySQL database.
* ☐ Verify the JDBC driver loads correctly (no ClassNotFound / driver errors).
* ☐ Gracefully handle and print clear errors if connection fails.

---

## 🧾 Step 2 — Console Menu with Scanner I/O

* ☐ Build a **text-based menu** with at least these options:

    * ☐ View data
    * ☐ Insert
    * ☐️ Update
    * ☐ Delete
    * ☐ Run transaction
    * ☐ Exit
* ☐ Use `Scanner` for all user input.
* ☐ Operate on **at least 3 key tables** from your schema:

    * ☐ Each of the 3 tables has at least one **SELECT (view)** option.
* ☐ For **INSERT/UPDATE/DELETE**, choose appropriate tables that make sense.
    * ☐ User
    * ☐ Artist
    * ☐ Song
* ☐ Show the menu looping until the user chooses **Exit**.

---

## 💾 Step 3 — Implement PreparedStatements

* ☐ Use **PreparedStatement** for **all** SQL operations (no raw string concatenation).
* ☐ Implement **SELECT** operations using PreparedStatements.
* ☐ Implement **INSERT** operations using PreparedStatements.
* ☐ Implement **UPDATE** operations using PreparedStatements.
* ☐ Implement **DELETE** operations using PreparedStatements.
* ☐ Clearly show PreparedStatement usage in the video demo.

---

## 🔁 Step 4 — Transactional Workflow (COMMIT + ROLLBACK)

* ☐ Design at least **one transactional workflow** that:

    * ☐ Touches **more than one table** (multi-table insert/update/delete).
    * ☐ Demonstrates **atomicity** (all-or-nothing).
* ☐ Use **manual transaction control**:

    * ☐ Turn off auto-commit.
    * ☐ Use `COMMIT` on success.
    * ☐ Use `ROLLBACK` on failure or invalid input.
* ☐ Clearly demonstrate **both COMMIT and ROLLBACK** behavior in your video demo.

---

## ✅ Step 5 — Input Validation and Error Handling

* ☐ Validate all user inputs (types, required fields, ranges, formats, etc.).
* ☐ Handle `SQLException` properly:

    * ☐ Catch exceptions and print helpful, user-friendly messages.
    * ☐ Detect and display **constraint violations** clearly.
    * ☐ Prevent the app from crashing on bad input.
* ☐ Show input validation and error handling behavior in the video demo.

---

## 🧱 Step 6 — View, Stored Routine, and Constraint (MySQL)

* ☐ In **MySQL**, create at least **one VIEW**:

    * ☐ View is useful for reporting or convenience.
    * ☐ View is used or demonstrated from your Java app or via SQL.
* ☐ Create **at least one stored routine**:

    * ☐ Stored **procedure or function** that enforces a rule or automates a task.
    * ☐ Routine is called/tested and shown in your demo.
* ☐ Add at least **one constraint** (e.g., `PRIMARY KEY`, `FOREIGN KEY`, `UNIQUE`, `CHECK`, etc.) beyond trivial defaults.
* ☐ Test and demonstrate:

    * ☐ The VIEW.
    * ☐ The stored procedure/function.
    * ☐ The constraint (including what happens on violation).

---

## 🧪 Step 7 — Testing, Debugging, and Video Demo

* ☐ Perform **full testing** of:

    * ☐ Menu navigation.
    * ☐ View (SELECT) operations on all 3+ tables.
    * ☐ Insert, update, and delete operations.
    * ☐ Transactional workflow (COMMIT + ROLLBACK paths).
    * ☐ View + stored routine + constraint behavior.
* ☐ Record a **video demo** (`video_demo.mp4`, ≤ 6 minutes) that shows:

    * ☐ The console menu in action.
    * ☐ View, insert, update, and delete operations.
    * ☐ The transactional workflow showing **both** commit and rollback.
    * ☐ The view and stored procedure/function in use.
    * ☐ Constraint enforcement and any related error handling.

---

## 📦 Step 8 — Documentation and Submission Package

Create a zip file named:
**`CS157A_FinalProject_TeamGroupName.zip`**

Inside the zip, include:

* ☐ **`Main.java`**

    * ☐ Full Java console app code.
    * ☐ Menu + Scanner input.
    * ☐ JDBC connection setup.
    * ☐ PreparedStatements for all SQL.
    * ☐ Commit & rollback logic.

* ☐ **`create_and_populate.sql`**

    * ☐ All `CREATE TABLE` statements.
    * ☐ All constraints.
    * ☐ Sample data inserts.
    * ☐ Your **VIEW** definition.
    * ☐ Your **stored procedure/function** definition.

* ☐ **`app.properties`**

    * ☐ Database URL.
    * ☐ Username and password (or instructions if using placeholders).

* ☐ **`README.md`**

    * ☐ Steps to create/build the database.
    * ☐ Instructions to compile and run your Java program.
    * ☐ Mention **MySQL version** and **MySQL connector/J** version.
    * ☐ Any additional setup steps.
    * ☐ Screenshots where applicable.

* ☐ **`ai_log.md`**

    * ☐ List AI prompts or tools used.
    * ☐ What suggestions you adopted or changed.

* ☐ **`Team-roles.txt`**

    * ☐ Each member’s contribution.
    * ☐ Short reflection on teamwork from each member (or a combined reflection).

* ☐ **`video_demo.mp4`**

    * ☐ ≤ 6-minute screen recording showing all required behaviors.

---

## ✅ Final Sanity Check Before Submission

* ☐ App compiles and runs without errors.
* ☐ All menu options work and are clearly labeled.
* ☐ No raw SQL string concatenation with user input (PreparedStatements only).
* ☐ Transactions behave correctly (no partial updates).
* ☐ Zip file name and contents match the required structure.
