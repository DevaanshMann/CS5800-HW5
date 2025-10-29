package Proxy;

import java.util.*;

public class Song {
    private final Integer songID;
    private final String title;
    private final String artist;
    private final String album;
    private final int duration;

    public Song(Integer id, String title, String artist, String album, Integer duration){
        this.songID = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
    }

    public Integer getId() {
        return songID;
    }
    public String getTitle() {
        return title;
    }
    public String getArtist() {
        return artist;
    }
    public String getAlbum() {
        return album;
    }
    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return String.format("Song{id = %d, title = '%s', artist = '%s', album='%s', duration = %ds}",
                songID, title, artist, album, duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;
        Song song = (Song) o;
        return Objects.equals(songID, song.songID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songID);
    }
}
