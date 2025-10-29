package Proxy;

import java.util.*;
import java.util.stream.Collectors;

public class RealSongService implements SongService {
    private final Map<Integer, Song> db =  new HashMap<>();

    public RealSongService(List<Song> songs ) {
        for (Song song : songs) {
            db.put(song.getId(), song);
        }
    }

    private static void slow(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Song searchByID(Integer songID){
        slow();
        return db.get(songID);
    }

    @Override
    public List<Song> searchByTitle(String title) {
        slow();
        String s = title.toLowerCase();
        return db.values().stream().filter(song -> song.getTitle().toLowerCase().contains(s)).collect(Collectors.toList());
    }

    @Override
    public List<Song> searchByAlbum(String album) {
        slow();
        String l = album.toLowerCase();
        return db.values().stream().filter(song -> song.getAlbum().toLowerCase().contains(l)).collect(Collectors.toList());
    }
}
