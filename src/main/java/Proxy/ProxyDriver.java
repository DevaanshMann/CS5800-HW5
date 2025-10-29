package Proxy;

// ProxyDemo.java (Driver)
import java.util.*;

public class ProxyDriver {
    public static void main(String[] args) {
        List<Song> seed = List.of(
                new Song(1, "Skyline", "Aria Volt", "Horizons", 212),
                new Song(2, "Midnight Run", "Neon Street", "Night Drive", 198),
                new Song(3, "Starlit", "Aria Volt", "Horizons", 244),
                new Song(4, "Driftwood", "Calm Seas", "Harbor", 186),
                new Song(5, "Night Drive", "Neon Street", "Night Drive", 205),
                new Song(6, "Hello World", "CS5800", "Patterns", 180),
                new Song(7, "Observer", "GoF", "Patterns", 190)
        );

        SongService real = new RealSongService(seed);
        SongService svc  = new CachedSongServiceProxy(real);

        // 1) ID lookups (first = slow, second = fast)
        System.out.println(svc.searchByID(3));
        System.out.println(svc.searchByID(3));

        // 2) Title search (cache hit on second call)
        System.out.println(svc.searchByTitle("Night"));
        System.out.println(svc.searchByTitle("Night"));

        // 3) Album search (cache hit on second call)
        System.out.println(svc.searchByAlbum("Horizons"));
        System.out.println(svc.searchByAlbum("Horizons"));
    }
}

