-- From 0-TableCreations.sql
DROP TABLE IF EXISTS PlaylistSong;
DROP TABLE IF EXISTS Playlist;
DROP TABLE IF EXISTS Song;
DROP TABLE IF EXISTS Album;
DROP TABLE IF EXISTS Artist;
DROP TABLE IF EXISTS `User`;

CREATE TABLE IF NOT EXISTS `User` (
  UserID INT auto_increment PRIMARY KEY,
  Username VARCHAR(50) NOT NULL,
  Password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Artist (
  ArtistID INT auto_increment PRIMARY KEY,
  Name VARCHAR(50) NOT NULL,
  Genre VARCHAR(50),
  Country VARCHAR(50),
  UserID INT,
  CONSTRAINT fk_artist_user
    FOREIGN KEY (UserID) REFERENCES User(UserID)
    ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS Album (
  AlbumID INT auto_increment PRIMARY KEY,
  Title VARCHAR(50) NOT NULL,
  ReleaseDate DATE NOT NULL,
  ArtistID INT NOT NULL,
  CONSTRAINT fk_album_artist
    FOREIGN KEY (ArtistID) REFERENCES Artist(ArtistID)
    ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Song (
  SongID INT auto_increment PRIMARY KEY,
  Title VARCHAR(50) NOT NULL,
  Duration INT NOT NULL CHECK (Duration > 0),
  AlbumID INT NOT NULL,
  TimesPlayed INT NOT NULL default 0 CHECK (TimesPlayed >= 0),
  CONSTRAINT fk_song_album
    FOREIGN KEY (AlbumID) REFERENCES Album(AlbumID)
    ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Playlist (
  PlaylistID INT auto_increment PRIMARY KEY,
  Title VARCHAR(50) NOT NULL,
  UserID INT NOT NULL,
  CreatedAt TIMESTAMP not null default CURRENT_TIMESTAMP,
  CONSTRAINT fk_playlist_user
    FOREIGN KEY (UserID) REFERENCES `User`(UserID)
    ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS PlaylistSong (
  PlaylistID INT NOT NULL,
  SongID INT NOT NULL,
  AddedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (PlaylistID, SongID),
  CONSTRAINT fk_plsong_playlist
    FOREIGN KEY (PlaylistID) REFERENCES Playlist(PlaylistID)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_plsong_song
    FOREIGN KEY (SongID) REFERENCES Song(SongID)
    ON UPDATE CASCADE ON DELETE CASCADE
);

SHOW TABLES;

-- From 1-PopulateDB.sql
-- =========================
-- USERS (40) — artists (1–20) + original people (21–40)
-- =========================
INSERT INTO `User` (UserID, Username, Password) VALUES
-- Artist accounts
(1, 'taylorswift', '$2y$10$hash_ts'),
(2, 'edsheeran', '$2y$10$hash_es'),
(3, 'adele', '$2y$10$hash_ad'),
(4, 'drake', '$2y$10$hash_dr'),
(5, 'coldplay', '$2y$10$hash_cp'),
(6, 'thebeatles', '$2y$10$hash_tb'),
(7, 'beyonce', '$2y$10$hash_by'),
(8, 'kanyewest', '$2y$10$hash_kw'),
(9, 'billieeilish', '$2y$10$hash_be'),
(10, 'imaginedragons', '$2y$10$hash_id'),
(11, 'maroon5', '$2y$10$hash_m5'),
(12, 'arianagrande', '$2y$10$hash_ag'),
(13, 'brunomars', '$2y$10$hash_bm'),
(14, 'rihanna', '$2y$10$hash_ri'),
(15, 'dualipa', '$2y$10$hash_dl'),
(16, 'theweeknd', '$2y$10$hash_tw'),
(17, 'ladygaga', '$2y$10$hash_lg'),
(18, 'postmalone', '$2y$10$hash_pm'),
(19, 'justinbieber', '$2y$10$hash_jb'),
(20, 'kendricklamar', '$2y$10$hash_kl'),
-- Original non-artist users
(21, 'alice', '$2y$10$hashalice'),
(22, 'bob', '$2y$10$hashbob'),
(23, 'charlie', '$2y$10$hashcharlie'),
(24, 'diana', '$2y$10$hashdiana'),
(25, 'eric', '$2y$10$hasheric'),
(26, 'fiona', '$2y$10$hashfiona'),
(27, 'george', '$2y$10$hashgeorge'),
(28, 'hannah', '$2y$10$hashhannah'),
(29, 'ian', '$2y$10$hashian'),
(30, 'julia', '$2y$10$hashjulia'),
(31, 'kevin', '$2y$10$hashkevin'),
(32, 'lisa', '$2y$10$hashlisa'),
(33, 'mike', '$2y$10$hashmike'),
(34, 'nora', '$2y$10$hashnora'),
(35, 'oliver', '$2y$10$hasholiver'),
(36, 'paula', '$2y$10$hashpaula'),
(37, 'quentin', '$2y$10$hashquentin'),
(38, 'rachel', '$2y$10$hashrachel'),
(39, 'steve', '$2y$10$hashsteve'),
(40, 'tina', '$2y$10$hashtina');

-- =========================
-- ARTISTS (20) — each linked to matching UserID (1–20)
-- =========================
INSERT INTO Artist (ArtistID, Name, Genre, Country, UserID) VALUES
(1, 'Taylor Swift', 'Pop', 'USA', 1),
(2, 'Ed Sheeran', 'Pop', 'UK', 2),
(3, 'Adele', 'Pop', 'UK', 3),
(4, 'Drake', 'Hip-Hop', 'Canada', 4),
(5, 'Coldplay', 'Alternative', 'UK', 5),
(6, 'The Beatles', 'Rock', 'UK', 6),
(7, 'Beyoncé', 'R&B', 'USA', 7),
(8, 'Kanye West', 'Hip-Hop', 'USA', 8),
(9, 'Billie Eilish', 'Pop', 'USA', 9),
(10, 'Imagine Dragons', 'Alternative', 'USA', 10),
(11, 'Maroon 5', 'Pop Rock', 'USA', 11),
(12, 'Ariana Grande', 'Pop', 'USA', 12),
(13, 'Bruno Mars', 'Pop', 'USA', 13),
(14, 'Rihanna', 'Pop', 'Barbados', 14),
(15, 'Dua Lipa', 'Pop', 'UK', 15),
(16, 'The Weeknd', 'R&B', 'Canada', 16),
(17, 'Lady Gaga', 'Pop', 'USA', 17),
(18, 'Post Malone', 'Hip-Hop', 'USA', 18),
(19, 'Justin Bieber', 'Pop', 'Canada', 19),
(20, 'Kendrick Lamar', 'Hip-Hop', 'USA', 20);

-- =========================
-- ALBUMS (21) — unchanged
-- =========================
INSERT INTO Album (AlbumID, Title, ReleaseDate, ArtistID) VALUES
(1, '1989', '2014-10-27', 1),
(2, '÷ (Divide)', '2017-03-03', 2),
(3, '21', '2011-01-24', 3),
(4, 'Scorpion', '2018-06-29', 4),
(5, 'Parachutes', '2000-07-10', 5),
(6, 'Abbey Road', '1969-09-26', 6),
(7, 'Lemonade', '2016-04-23', 7),
(8, 'Graduation', '2007-09-11', 8),
(9, 'Happier Than Ever', '2021-07-30', 9),
(10, 'Night Visions', '2012-09-04', 10),
(11, 'Songs About Jane', '2002-06-25', 11),
(12, 'Thank U, Next', '2019-02-08', 12),
(13, '24K Magic', '2016-11-18', 13),
(14, 'Anti', '2016-01-28', 14),
(15, 'Future Nostalgia', '2020-03-27', 15),
(16, 'After Hours', '2020-03-20', 16),
(17, 'The Fame', '2008-08-19', 17),
(18, 'Hollywood''s Bleeding', '2019-09-06', 18),
(19, 'Purpose', '2015-11-13', 19),
(20, 'DAMN.', '2017-04-14', 20),
(21, 'Red', '2012-10-22', 1);

-- =========================
-- SONGS (63) — unchanged
-- =========================
INSERT INTO Song (SongID, Title, Duration, AlbumID, TimesPlayed) VALUES
(1, 'Blank Space', 231, 1, 1250),
(2, 'Style', 231, 1, 980),
(3, 'Shake It Off', 219, 1, 2210),
(4, 'Shape of You', 233, 2, 3200),
(5, 'Castle on the Hill', 261, 2, 1500),
(6, 'Perfect', 263, 2, 2900),
(7, 'Rolling in the Deep', 228, 3, 2400),
(8, 'Someone Like You', 285, 3, 2600),
(9, 'Set Fire to the Rain', 242, 3, 1800),
(10, 'God''s Plan', 198, 4, 3300),
(11, 'In My Feelings', 217, 4, 2100),
(12, 'Nice for What', 210, 4, 1900),
(13, 'Yellow', 269, 5, 2200),
(14, 'Shiver', 299, 5, 600),
(15, 'Trouble', 260, 5, 700),
(16, 'Come Together', 259, 6, 2700),
(17, 'Something', 182, 6, 1300),
(18, 'Here Comes the Sun', 185, 6, 3100),
(19, 'Formation', 213, 7, 1600),
(20, 'Hold Up', 216, 7, 1200),
(21, 'Sorry', 220, 7, 1100),
(22, 'Stronger', 312, 8, 2400),
(23, 'Good Life', 207, 8, 1400),
(24, 'Flashing Lights', 232, 8, 1300),
(25, 'Happier Than Ever', 298, 9, 2000),
(26, 'Therefore I Am', 174, 9, 1500),
(27, 'NDA', 187, 9, 900),
(28, 'Radioactive', 186, 10, 2600),
(29, 'Demons', 177, 10, 2100),
(30, 'It''s Time', 240, 10, 1500),
(31, 'This Love', 204, 11, 2000),
(32, 'She Will Be Loved', 256, 11, 1900),
(33, 'Harder to Breathe', 173, 11, 1600),
(34, 'thank u, next', 207, 12, 2300),
(35, '7 rings', 179, 12, 2500),
(36, 'break up with your girlfriend, i''m bored', 191, 12, 1200),
(37, '24K Magic', 227, 13, 2000),
(38, 'That''s What I Like', 207, 13, 2700),
(39, 'Versace on the Floor', 273, 13, 1500),
(40, 'Work', 219, 14, 2400),
(41, 'Needed Me', 193, 14, 1700),
(42, 'Love on the Brain', 225, 14, 1500),
(43, 'Don''t Start Now', 183, 15, 3100),
(44, 'Levitating', 203, 15, 3300),
(45, 'Physical', 193, 15, 1600),
(46, 'Blinding Lights', 200, 16, 5200),
(47, 'Save Your Tears', 215, 16, 3700),
(48, 'In Your Eyes', 238, 16, 2000),
(49, 'Just Dance', 241, 17, 2200),
(50, 'Poker Face', 239, 17, 3500),
(51, 'Paparazzi', 215, 17, 1600),
(52, 'Circles', 215, 18, 3400),
(53, 'Wow.', 133, 18, 1700),
(54, 'Sunflower', 158, 18, 3600),
(55, 'Sorry', 200, 19, 3300),
(56, 'Love Yourself', 233, 19, 2800),
(57, 'What Do You Mean?', 207, 19, 2500),
(58, 'HUMBLE.', 177, 20, 3000),
(59, 'DNA.', 185, 20, 2100),
(60, 'LOYALTY.', 226, 20, 1600),
(61, 'Red', 223, 21, 1400),
(62, 'I Knew You Were Trouble', 220, 21, 1800),
(63, 'We Are Never Ever Getting Back Together', 193, 21, 1700);

-- =========================
-- PLAYLISTS (30) — 20 owned by artists + 10 owned by original users
-- =========================
INSERT INTO Playlist (PlaylistID, Title, UserID, CreatedAt) VALUES
-- Artist-owned (same as before)
(1, 'Road Trip', 1,  NOW()),
(2, 'Chill Vibes', 2, NOW()),
(3, 'Workout Mix', 3, NOW()),
(4, 'Focus Mode', 4, NOW()),
(5, 'Party Hits', 5, NOW()),
(6, 'Throwbacks', 6, NOW()),
(7, 'Pop Essentials', 7, NOW()),
(8, 'Rock Classics', 8, NOW()),
(9, 'Fresh Finds', 9, NOW()),
(10, 'Golden Hour', 10, NOW()),
(11, 'Hip-Hop Heat', 11, NOW()),
(12, 'Sunday Morning', 12, NOW()),
(13, 'Late Night', 13, NOW()),
(14, 'Top 2010s', 14, NOW()),
(15, 'New & Now', 15, NOW()),
(16, 'Feel Good', 16, NOW()),
(17, 'Study Time', 17, NOW()),
(18, 'All-Time Faves', 18, NOW()),
(19, 'Globetrotting', 19, NOW()),
(20, 'Weekend Mix', 20, NOW()),
-- Original-user-owned
(21, 'Alice Mix', 21, NOW()),
(22, 'Bob''s Bops', 22, NOW()),
(23, 'Study Session', 23, NOW()),
(24, 'Deep Focus', 24, NOW()),
(25, 'Morning Run', 25, NOW()),
(26, 'Indie Hour', 26, NOW()),
(27, 'George''s Jams', 27, NOW()),
(28, 'Hannah Chill', 28, NOW()),
(29, 'Ian''s Picks', 29, NOW()),
(30, 'Julia''s Faves', 30, NOW());

-- =========================
-- PLAYLIST ↔ SONG LINKS (90 total: original 60 + 30 new)
-- =========================
-- original 60
INSERT INTO PlaylistSong (PlaylistID, SongID, AddedAt) VALUES
(1, 28, NOW()), (1, 31, NOW()), (1, 1, NOW()),
(1, 37, NOW()), (1, 46, NOW()), (1, 52, NOW()),
(2, 43, NOW()), (2, 44, NOW()), (2, 25, NOW()),
(2, 26, NOW()), (2, 50, NOW()), (2, 49, NOW()),
(3, 22, NOW()), (3, 23, NOW()), (3, 24, NOW()),
(3, 10, NOW()), (3, 59, NOW()), (3, 58, NOW()),
(4, 13, NOW()), (4, 16, NOW()), (4, 18, NOW()),
(4, 29, NOW()), (4, 30, NOW()), (4, 47, NOW()),
(5, 37, NOW()), (5, 38, NOW()), (5, 40, NOW()),
(5, 55, NOW()), (5, 34, NOW()), (5, 35, NOW()),
(6, 31, NOW()), (6, 32, NOW()), (6, 33, NOW()),
(6, 49, NOW()), (6, 50, NOW()), (6, 17, NOW()),
(7, 4, NOW()), (7, 6, NOW()), (7, 1, NOW()),
(7, 43, NOW()), (7, 44, NOW()), (7, 55, NOW()),
(8, 16, NOW()), (8, 17, NOW()), (8, 18, NOW()),
(8, 13, NOW()), (8, 14, NOW()), (8, 15, NOW()),
(9, 45, NOW()), (9, 47, NOW()), (9, 25, NOW()),
(9, 28, NOW()), (9, 52, NOW()), (9, 61, NOW()),
(10, 3, NOW()), (10, 43, NOW()), (10, 47, NOW()),
(10, 38, NOW()), (10, 32, NOW()), (10, 41, NOW());

-- new links for playlists 21–30 (3 each)
INSERT INTO PlaylistSong (PlaylistID, SongID, AddedAt) VALUES
(21, 43, NOW()), (21, 1, NOW()),  (21, 28, NOW()),
(22, 4, NOW()),  (22, 55, NOW()), (22, 37, NOW()),
(23, 25, NOW()), (23, 28, NOW()), (23, 31, NOW()),
(24, 13, NOW()), (24, 16, NOW()), (24, 47, NOW()),
(25, 22, NOW()), (25, 23, NOW()), (25, 24, NOW()),
(26, 43, NOW()), (26, 45, NOW()), (26, 30, NOW()),
(27, 49, NOW()), (27, 50, NOW()), (27, 33, NOW()),
(28, 41, NOW()), (28, 40, NOW()), (28, 35, NOW()),
(29, 58, NOW()), (29, 59, NOW()), (29, 60, NOW()),
(30, 6, NOW()),  (30, 34, NOW()), (30, 44, NOW());

-- From 2-Indexes.sql
CREATE INDEX idx_album_artist ON Album(ArtistID);
CREATE INDEX idx_song_album ON Song(AlbumID);
CREATE INDEX idx_playlist_user ON Playlist(UserID);
CREATE INDEX idx_plsong_playlist ON PlaylistSong(PlaylistID);
CREATE INDEX idx_plsong_song ON PlaylistSong(SongID);

