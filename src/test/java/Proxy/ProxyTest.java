package Proxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ProxyTest {

    private FakeSongService fake;
    private CachedSongServiceProxy proxy;

    @BeforeEach
    void setUp() {
        // Seed some songs
        List<Song> seed = List.of(
                new Song(1, "Skyline",     "Aria Volt",   "Horizons",   212),
                new Song(2, "Night Drive", "Neon Street", "Night Drive",198),
                new Song(3, "Starlit",     "Aria Volt",   "Horizons",   244),
                new Song(4, "Observer",    "GoF",         "Patterns",   190)
        );
        fake  = new FakeSongService(seed);
        proxy = new CachedSongServiceProxy(fake);
    }

    // --- ID CACHING ---

    @Test
    void searchByID_caches_byId() {
        Song a1 = proxy.searchByID(3);
        Song a2 = proxy.searchByID(3);

        assertEquals(a1, a2);
        assertEquals(1, fake.byIdCalls.get(), "Delegate should be called once for the same id");
    }

    @Test
    void searchByID_differentIds_callDelegateEach() {
        proxy.searchByID(1);
        proxy.searchByID(2);
        proxy.searchByID(1);

        // id=1 should be called once (cached), id=2 once
        assertEquals(2, fake.byIdCalls.get(), "Two distinct ids -> two delegate hits total");
    }

    @Test
    void searchByID_unknownId_notCachedIfNull() {
        // Fake returns null for unknown id
        Song s1 = proxy.searchByID(999);
        Song s2 = proxy.searchByID(999);

        assertNull(s1);
        assertNull(s2);
        // Because nulls aren't cached, delegate is called twice
        assertEquals(2, fake.byIdCalls.get(), "Unknown id shouldn't be cached (two delegate calls)");
    }

    @Test
    void searchByID_nullInput_returnsNull_andNoDelegate() {
        Song s = proxy.searchByID(null);
        assertNull(s);
        assertEquals(0, fake.byIdCalls.get(), "Null id should not call delegate");
    }

    // --- TITLE CACHING ---

    @Test
    void searchByTitle_caches_listResults() {
        List<Song> t1 = proxy.searchByTitle("Night");
        List<Song> t2 = proxy.searchByTitle("Night");

        assertEquals(t1, t2);
        assertEquals(1, fake.byTitleCalls.get(), "Same title twice -> delegate once due to caching");
    }

    @Test
    void searchByTitle_caseInsensitive_key() {
        // First call is lowercase
        List<Song> a = proxy.searchByTitle("night");
        // Second call is different case (should hit cache if proxy normalizes)
        List<Song> b = proxy.searchByTitle("NiGhT");

        assertEquals(a, b);
        assertEquals(1, fake.byTitleCalls.get(),
                "Case variants should map to the same cache entry (delegate once)");
    }

    @Test
    void searchByTitle_null_returnsEmpty_andNoDelegate() {
        List<Song> res = proxy.searchByTitle(null);
        assertNotNull(res);
        assertTrue(res.isEmpty());
        assertEquals(0, fake.byTitleCalls.get());
    }

    // --- ALBUM CACHING ---

    @Test
    void searchByAlbum_caches_listResults() {
        List<Song> a1 = proxy.searchByAlbum("Horizons");
        List<Song> a2 = proxy.searchByAlbum("Horizons");

        assertEquals(a1, a2);
        assertEquals(1, fake.byAlbumCalls.get(), "Same album twice -> delegate once due to caching");
    }

    @Test
    void searchByAlbum_caseInsensitive_key() {
        List<Song> a = proxy.searchByAlbum("patterns");
        List<Song> b = proxy.searchByAlbum("PaTtErNs");

        assertEquals(a, b);
        assertEquals(1, fake.byAlbumCalls.get(),
                "Case variants should map to the same cache entry (delegate once)");
    }

    @Test
    void searchByAlbum_null_returnsEmpty_andNoDelegate() {
        List<Song> res = proxy.searchByAlbum(null);
        assertNotNull(res);
        assertTrue(res.isEmpty());
        assertEquals(0, fake.byAlbumCalls.get());
    }

    // --- FAKE IMPLEMENTATION FOR TESTING ---

    /**
     * Simple in-memory SongService that records how many times each method is called,
     * and performs contains() filtering for title/album.
     */
    private static class FakeSongService implements SongService {
        private final Map<Integer, Song> byId = new HashMap<>();
        private final List<Song> all;

        private final AtomicInteger byIdCalls    = new AtomicInteger();
        private final AtomicInteger byTitleCalls = new AtomicInteger();
        private final AtomicInteger byAlbumCalls = new AtomicInteger();

        FakeSongService(List<Song> seed) {
            this.all = new ArrayList<>(seed);
            for (Song s : seed) byId.put(s.getId(), s);
        }

        @Override
        public Song searchByID(Integer songID) {
            byIdCalls.incrementAndGet();
            return byId.get(songID);
        }

        @Override
        public List<Song> searchByTitle(String title) {
            byTitleCalls.incrementAndGet();
            if (title == null) return null;
            String t = title.toLowerCase();
            List<Song> out = new ArrayList<>();
            for (Song s : all) {
                if (s.getTitle().toLowerCase().contains(t)) out.add(s);
            }
            return out;
        }

        @Override
        public List<Song> searchByAlbum(String album) {
            byAlbumCalls.incrementAndGet();
            if (album == null) return null;
            String a = album.toLowerCase();
            List<Song> out = new ArrayList<>();
            for (Song s : all) {
                if (s.getAlbum().toLowerCase().contains(a)) out.add(s);
            }
            return out;
        }
    }
}

