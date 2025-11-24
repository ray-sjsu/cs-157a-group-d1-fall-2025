USE data;

CREATE INDEX idx_album_artist ON Album(ArtistID);
CREATE INDEX idx_song_album ON Song(AlbumID);
CREATE INDEX idx_playlist_user ON Playlist(UserID);
CREATE INDEX idx_plsong_playlist ON PlaylistSong(PlaylistID);
CREATE INDEX idx_plsong_song ON PlaylistSong(SongID);

