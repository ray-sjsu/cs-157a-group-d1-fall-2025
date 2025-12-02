-- At least ONE stored routine
-- Either enforces a rule or automates a task
-- Includes constraint
-- Include SELECT statements to show results
DELIMITER $$

DROP FUNCTION IF EXISTS AlbumDuration $$
CREATE FUNCTION AlbumDuration(a_id INT)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE TotalDuration INT;

    -- Constraint: Album ID must be positive
    IF a_id <= 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Album ID must be a positive integer.';
    END IF;

    -- Constraint: Album must exist
    IF (SELECT COUNT(*) FROM Album WHERE AlbumID = a_id) = 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Album does not exist.';
    END IF;

    SELECT SUM(s.Duration)
    INTO TotalDuration
    FROM Song s
    JOIN Album a ON s.AlbumID = a.AlbumID
    WHERE a.AlbumID = a_id;

    RETURN TotalDuration;
END$$

DELIMITER ;

SELECT *, AlbumDuration(Album.AlbumID) AS TotalDuration FROM Album
ORDER BY Album.ReleaseDate;