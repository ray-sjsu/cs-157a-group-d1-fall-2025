USE data;

-- JOIN query (2 tables - User and Playlist) - Shows users and their playlist
SELECT
  u.UserID,
  u.Username,
  p.PlaylistID,
  p.Title AS PlaylistTitle,
  p.CreatedAt
FROM `User` u
JOIN Playlist p ON p.UserID = u.UserID
ORDER BY u.Username, p.CreatedAt;

-- JOIN query (3 tables - Song, Album, Artist) - Songs info
SELECT s.SongID, s.Title, r.Name AS Artist, a.Title AS Album, s.TimesPlayed
FROM Song s
JOIN Album a ON a.AlbumID = s.AlbumID
JOIN Artist r ON r.ArtistID = a.ArtistID
ORDER BY s.TimesPlayed DESC
LIMIT 25;

-- UPDATE query (where clause) - Increment one song’s play counter
UPDATE Song
SET TimesPlayed = TimesPlayed + 1
WHERE SongID = 4; -- Shape of You
-- View updated table
SELECT SongID, Title, TimesPlayed
FROM Song
WHERE SongID = 4;

-- SUBQUERY (Outer: Artist & Album | Inner: Album) - Get each artist’s most recent album
SELECT 
  ar.ArtistID,
  ar.Name AS ArtistName,
  a.AlbumID,
  a.Title AS AlbumTitle,
  a.ReleaseDate
FROM Artist ar
JOIN Album a ON a.ArtistID = ar.ArtistID
JOIN (
  SELECT ArtistID, MAX(ReleaseDate) AS LatestDate
  FROM Album
  GROUP BY ArtistID
) last ON last.ArtistID = a.ArtistID AND last.LatestDate = a.ReleaseDate
ORDER BY a.ReleaseDate DESC;
