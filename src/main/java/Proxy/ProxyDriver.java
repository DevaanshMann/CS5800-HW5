package Proxy;

import java.util.*;

public class ProxyDriver {
    public static void main(String[] args) {
        List<Song> seed = List.of(
                new Song(1, "Hells Bells",     "AC/DC","Black In Black",   212),
                new Song(2, "For A Reason", "Karan Aujla","P-Pop Culture",198),
                new Song(3, "Starlit",     "Aria Volt","Horizons.",   244),
                new Song(4, "Driftwood", "Calm Seas", "Harbor", 186),
                new Song(5, "Night Drive", "Neon Street", "Night Drive", 205),
                new Song(6, "Hello World", "CS5800", "Patterns", 180),
                new Song(4, "Charmer",    "Diljit Dosanjh","Aura",   190)
        );

        SongService real = new RealSongService(seed);
        SongService svc  = new CachedSongServiceProxy(real);

        System.out.println(svc.searchByID(3));
        System.out.println(svc.searchByID(1));

        System.out.println(svc.searchByTitle("Night"));
        System.out.println(svc.searchByTitle("Night"));

        System.out.println(svc.searchByAlbum("Horizons"));
        System.out.println(svc.searchByAlbum("P-Pop Culture"));
    }
}

