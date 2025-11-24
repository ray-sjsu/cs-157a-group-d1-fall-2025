USE data;

DELIMITER //
CREATE TRIGGER trigger_no_future_album_insert
BEFORE INSERT ON Album
FOR EACH ROW
BEGIN
  IF NEW.ReleaseDate > CURRENT_DATE THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT='ReleaseDate cannot be in the future';
  END IF;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER trigger_no_future_album_update
BEFORE UPDATE ON Album
FOR EACH ROW
BEGIN
  IF NEW.ReleaseDate > CURRENT_DATE THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT ='ReleaseDate cannot be in the future';
  END IF;
END//
DELIMITER ;

