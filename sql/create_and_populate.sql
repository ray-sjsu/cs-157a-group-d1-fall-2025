-- Drop old tables (for rebuilds)
DROP TABLE IF EXISTS Borrow;
DROP TABLE IF EXISTS Book;
DROP TABLE IF EXISTS Member;
DROP TABLE IF EXISTS Author;
DROP TABLE IF EXISTS Category;

-- Create base tables
CREATE TABLE Category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE Author (
    author_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE Member (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE Book (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    category_id INT,
    author_id INT,
    FOREIGN KEY (category_id) REFERENCES Category(category_id),
    FOREIGN KEY (author_id) REFERENCES Author(author_id)
);

CREATE TABLE Borrow (
    borrow_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT,
    book_id INT,
    borrow_date DATE,
    return_date DATE,
    FOREIGN KEY (member_id) REFERENCES Member(member_id),
    FOREIGN KEY (book_id) REFERENCES Book(book_id)
);

-- Sample data
INSERT INTO Category (name) VALUES ('Fiction'), ('Nonfiction'), ('Science');
INSERT INTO Author (name) VALUES ('Isaac Asimov'), ('Jane Austen'), ('Stephen Hawking');
INSERT INTO Member (name, email) VALUES
('Alice', 'alice@example.com'),
('Bob', 'bob@example.com');

INSERT INTO Book (title, category_id, author_id) VALUES
('Pride and Prejudice', 1, 2),
('Foundation', 3, 1),
('A Brief History of Time', 3, 3);

INSERT INTO Borrow (member_id, book_id, borrow_date, return_date)
VALUES (1, 2, '2024-09-01', '2024-09-15'),
       (2, 3, '2024-09-05', NULL);
