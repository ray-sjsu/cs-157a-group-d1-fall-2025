-- At least ONE view
-- For reporting or convenience
-- Tables used: User, Artist, Song
-- Include SELECT statements to show results

-- Safety: drop views if they already exist
DROP VIEW IF EXISTS v_artist_song_stats;
DROP VIEW IF EXISTS v_artist_top_song;

-- View 1: Per-artist song stats (ties User -> Artist -> Album -> Song)
-- Includes user info, artist info, song count, total plays, and average duration.
CREATE OR REPLACE VIEW v_artist_song_stats AS
SELECT
    u.UserID,
    u.Username,
    a.ArtistID,
    a.Name AS ArtistName,
    COUNT(s.SongID) AS SongCount,
    COALESCE(SUM(s.TimesPlayed), 0) AS TotalPlays,
    COALESCE(ROUND(AVG(s.Duration)), 0) AS AvgDurationSeconds
FROM `User` u
JOIN Artist a       ON a.UserID = u.UserID       -- Only users who are artists
LEFT JOIN Album al  ON al.ArtistID = a.ArtistID  -- Artists may have 0+ albums
LEFT JOIN Song  s   ON s.AlbumID   = al.AlbumID  -- Albums may have 0+ songs
GROUP BY
    u.UserID,
    u.Username,
    a.ArtistID,
    a.Name;

-- Show output for View 1
SELECT * FROM v_artist_song_stats
ORDER BY TotalPlays DESC, SongCount DESC, ArtistName ASC
LIMIT 10;

-- View 2: Each artist's top (most played) song
-- Uses a window function (MySQL 8+) to find the top TimesPlayed per Artist.
CREATE OR REPLACE VIEW v_artist_top_song AS
SELECT UserID, Username, ArtistID, ArtistName, SongID, SongTitle, TimesPlayed
FROM (
    SELECT
        u.UserID,
        u.Username,
        a.ArtistID,
        a.Name                 AS ArtistName,
        s.SongID,
        s.Title                AS SongTitle,
        s.TimesPlayed,
        ROW_NUMBER() OVER (
            PARTITION BY a.ArtistID
            ORDER BY s.TimesPlayed DESC, s.SongID ASC
        ) AS rn
    FROM `User` u
    JOIN Artist a      ON a.UserID   = u.UserID
    JOIN Album  al     ON al.ArtistID = a.ArtistID
    JOIN Song   s      ON s.AlbumID   = al.AlbumID
) ranked
WHERE rn = 1;

-- Show output for View 2
SELECT * FROM v_artist_top_song
ORDER BY TimesPlayed DESC, ArtistName ASC
LIMIT 10;