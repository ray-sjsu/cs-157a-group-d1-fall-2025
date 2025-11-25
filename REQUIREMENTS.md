# Final Project — JDBC Console Database Application

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

* ✅ Create `app.properties` file that stores:

    * ✅ Database URL
    * ✅ Username
    * ✅ Password
* ✅ Load `app.properties` from Java.
* ✅ Use JDBC to connect to the MySQL database.
* ✅ Verify the JDBC driver loads correctly (no ClassNotFound / driver errors).
* ✅ Gracefully handle and print clear errors if connection fails.

---

## 🧾 Step 2 — Console Menu with Scanner I/O

* ✅ Build a **text-based menu** with at least these options:

    * ✅ View data
    * ⚠️ Insert
    * ⚠️ Update
    * ⚠️ Delete
    * ⚠️ Run transaction
    * ✅ Exit
* ✅ Use `Scanner` for all user input.
* ✅ Operate on **at least 3 key tables** from your schema:

    * ✅ Each of the 3 tables has at least one **SELECT (view)** option.
* ✅ For **INSERT/UPDATE/DELETE**, choose appropriate tables that make sense.
    * ✅ User
    * ✅ Artist
    * ✅ Song
* ✅ Show the menu looping until the user chooses **Exit**.

---

## 💾 Step 3 — Implement PreparedStatements

* ☐ Use **PreparedStatement** for **all** SQL operations (no raw string concatenation).
* ✅ Implement **SELECT** operations using PreparedStatements.
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
