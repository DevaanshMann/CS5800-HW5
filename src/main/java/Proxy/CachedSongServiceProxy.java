package Proxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachedSongServiceProxy implements SongService {
    private final SongService songService;

    // Caches
    private final Map<Integer, Song> byIdCache = new HashMap<>();
    private final Map<String, List<Song>> byTitleCache = new HashMap<>();
    private final Map<String, List<Song>> byAlbumCache = new HashMap<>();

    public CachedSongServiceProxy(SongService songService) {
        this.songService = songService;
    }

    @Override
    public Song searchByID(Integer songID) {
        if (songID == null) return null;

        Song cached = byIdCache.get(songID);
        if (cached != null) {
            System.out.println("[cache] id=" + songID);
            return cached;
        }

        System.out.println("[real ] id=" + songID);
        Song s = songService.searchByID(songID);
        if (s != null) byIdCache.put(songID, s);
        return s;
    }

    @Override
    public List<Song> searchByTitle(String title) {
        if (title == null) return List.of();

        String key = title.toLowerCase();
        List<Song> cached = byTitleCache.get(key);
        if (cached != null) {
            System.out.println("[cache] title='" + title + "'");
            return cached;
        }

        System.out.println("[real ] title='" + title + "'");
        List<Song> res = songService.searchByTitle(title);
        if (res == null) res = List.of();   // never cache null
        byTitleCache.put(key, res);         // <-- store List<Song>, not Song
        return res;
    }

    @Override
    public List<Song> searchByAlbum(String album) {
        if (album == null) return List.of();

        String key = album.toLowerCase();
        List<Song> cached = byAlbumCache.get(key);
        if (cached != null) {
            System.out.println("[cache] album='" + album + "'");
            return cached;
        }

        System.out.println("[real ] album='" + album + "'");
        List<Song> res = songService.searchByAlbum(album);
        if (res == null) res = List.of();   // never cache null
        byAlbumCache.put(key, res);         // <-- store List<Song>, not Song
        return res;
    }
}
