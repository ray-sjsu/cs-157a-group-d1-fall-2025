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