SELECT b.title, a.name AS author, c.name AS category
FROM Book b
JOIN Author a ON b.author_id = a.author_id
JOIN Category c ON b.category_id = c.category_id;

SELECT COUNT(*) AS borrowed_books FROM Borrow;

SELECT c.name AS category, COUNT(b.book_id) AS total_books
FROM Book b
JOIN Category c ON b.category_id = c.category_id
GROUP BY c.name;

SELECT title
FROM Book
WHERE book_id IN (SELECT book_id FROM Borrow WHERE return_date IS NULL);
