# Music Application Database
CS-157A Fall 2025 Semester Project.

Group D1 team members: Jonathan, Raymund, Christopher, Daeren

Professor Ethel Tshukudu

---
See `ai_log.md` for AI usage and applications.

See `team_roles.md` for team roles and contributions.

For more details on how this application was built and a live demo, please view the `video_demo.mp4`.

---

## Installation Guide
### Install and run MySQL server
0. Download project `.zip`. Make sure all folders `sql`, `src`, and various `.md` files are under one project directory.
1. Install MySQL installer community version [8.0.43](https://downloads.mysql.com/archives/installer/). Then, open the MySQL installer `.exe`.
2. Open MySQL installer, then install MySQL server version 8.0.43.
3. In `Type and Networking`, select Config Type: `Development Computer`, check TCP/IP with a port of `3306`. Press `Next`.
4. In `Authentication Method`, select `Use Strong Password Encryption for Authentication (RECOMMENDED)`. Press `Next`.
5. In `Account and Roles`, create an account with the username `root` and password `admin`. Press `Next`.
   - You can change the login as long as you modify the `db.user` and `db.password` within `src/app.properties` file in the project directory.
6. In `Windows Service`, either select/unselect `Configure MySQL Server as a Windows Service`. Press `Next`.
7. In `Apply Configuration`, click `Execute` to run the MySQL server.
8. Click `Finish` to close mySQL installer.
### Install MySQL workbench and define schema
9. Open MySQL installer, install MySQL workbench version 8.0.43.
10. Open MySQL workbench and add a new `MySQL Connections` by pressing the `+` icon.
11. In the `Setup New Connection` window, define a `Connection Name` and put in `username` and `password`.
12. Click `Test Connection` to make sure you are connected to the database with correct login, then press `Ok`.
13. Open the newly created connection in the menu.
14. On the left, there is the `SCHEMAS` column. Right click inside the blank space and in the popup menu, click `Create Schema...`.
    - You can change the schema as long as you modify the `db.url` in `src/app.properties` file in the project directory.
15. A new tab will open. In the `Name`, name the new schema `data`. Then on the bottom right, click `Apply`.
16. A new popup `Apply SQL Script to Database` will open, click on `Apply`, followed by `Finish`.
### IDE and running Java program
17. Choose an IDE that supports Java of your choice. In this guide, we used [Intellj Community Edition](https://www.jetbrains.com/idea/download/?section=windows)
18. Open the project directory.
19. In Intellj, select the `src` folder as a `Sources Root`.
20. On the top right, click the green `Run` button or equivalent to start the program.
21. To create the tables and populate the database, enter `999` to reset the database.
---

## General Structure
`sql` directory - hosts all `.sql` files for creation, population, and indexing.
  - `create_and_populate.sql` - defines all tables for application, populates tables, and indexes.
  - `routine.sql` - stored routine that automates a task using function and constraints.
  - `view.sql` - defines two views for reporting statistics.

`src` directory - hosts Java code and database information.
  - `app.properties` - database url, user, and password.
  - `Main.java` - all Java code including JBDC, UI/UX menus with error checking, and `PreparedStatements`.