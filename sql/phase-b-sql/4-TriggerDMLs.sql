USE data;

-- TRIGGER DMLs

-- Release date in the future (fail)
INSERT INTO Album (AlbumID, ArtistID, Title, ReleaseDate)
VALUES (101, 5, 'Future Sounds', '2030-01-01');

-- Update release date into the future (fail)
UPDATE Album
SET ReleaseDate = '2035-12-01'
WHERE AlbumID = 7; -- Lemonade

-- Valid release date (success)
INSERT INTO Album (AlbumID, ArtistID, Title, ReleaseDate)
VALUES (102, 5, 'Past Echoes', '2024-08-10');

