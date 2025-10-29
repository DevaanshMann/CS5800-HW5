package Flyweight;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FlyweightTest {

    @Test
    void styleFactory_returnsSameInstance_forSameTriplet() {
        CharacterStyle s1 = StyleFactory.get("Arial", 12, "Red");
        CharacterStyle s2 = StyleFactory.get("Arial", 12, "Red");
        CharacterStyle s3 = StyleFactory.get("Arial", 13, "Black");
        CharacterStyle s4 = StyleFactory.get("Arial", 13, "Black");

        assertSame(s1, s2, "Flyweight factory must return the same instance for equal (font,size,color)");
        assertSame(s3, s4, "Flyweight factory must return the same instance for equal (font,size,color)");
    }

    @Test
    void styleFactory_returnsDifferentInstances_forDifferentTriplets() {
        CharacterStyle a = StyleFactory.get("Arial", 12, "Red");
        CharacterStyle b = StyleFactory.get("Arial", 12, "Black");
        CharacterStyle c = StyleFactory.get("Calibri", 12, "Red");
        CharacterStyle d = StyleFactory.get("Arial", 14, "Red");

        assertNotSame(a, b);
        assertNotSame(a, c);
        assertNotSame(a, d);
    }

    @Test
    void document_append_buildsPlainText_andSharesStyles() {
        CharacterStyle arialRed12 = StyleFactory.get("Arial", 12, "Red");
        CharacterStyle calibriBlue14 = StyleFactory.get("Calibri", 14, "Blue");

        Document doc = new Document();
        for (char ch : "HelloWorld".toCharArray()) {
            doc.append(ch, arialRed12);
        }
        doc.setStyleAt(0, calibriBlue14);
        doc.setStyleAt(5, calibriBlue14);

        assertEquals("HelloWorld", doc.asPlainText());

        List<StyledCharacter> chars = doc.chars();
        CharacterStyle s0 = chars.get(0).style();
        CharacterStyle s5 = chars.get(5).style();
        assertSame(s0, s5, "Same style should be shared between multiple characters");

        CharacterStyle s1 = chars.get(1).style();
        CharacterStyle s2 = chars.get(2).style();
        assertSame(s1, s2, "Unchanged positions should share the original style instance");
        assertNotSame(s0, s1, "Different style selections should not be the same instance");
    }

    @Test
    void document_editHelpers_modifyContentCorrectly() {
        CharacterStyle a = StyleFactory.get("Arial", 12, "Red");
        CharacterStyle b = StyleFactory.get("Calibri", 14, "Blue");

        Document doc = new Document();
        for (char ch : "ABCDEF".toCharArray()) doc.append(ch, a);

        doc.setCharAt(1, 'Z');
        doc.setStyleAt(2, b);
        doc.insertAt(3, '_', b);
        doc.deleteAt(5);

        assertEquals("AZC_DF", doc.asPlainText());
        assertSame(b, doc.chars().get(2).style(), "Index 2 should now use style b");
    }

    @Test
    void document_saveAndLoad_roundTripPreservesText_andSharesFlyweights(@TempDir Path tmp) throws Exception {
        CharacterStyle s1 = StyleFactory.get("Arial",   12, "Red");
        CharacterStyle s2 = StyleFactory.get("Calibri", 14, "Blue");
        CharacterStyle s3 = StyleFactory.get("Verdana", 16, "Black");
        CharacterStyle s4 = StyleFactory.get("Arial",   12, "Black");

        Document doc = new Document();
        String text = "HelloWorldCS5800";
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            CharacterStyle style = switch (i % 4) {
                case 0 -> s1;
                case 1 -> s2;
                case 2 -> s3;
                default -> s4;
            };
            doc.append(c, style);
        }

        File out = tmp.resolve("roundtrip.txt").toFile();
        doc.saveTo(out);

        Document loaded = Document.loadFrom(out);

        assertEquals(text, loaded.asPlainText(), "Round-trip should preserve the plain text");

        List<StyledCharacter> chars = loaded.chars();
        CharacterStyle base = chars.get(0).style();
        for (int i = 4; i < chars.size(); i += 4) {
            assertSame(base, chars.get(i).style(),
                    "Positions spaced by 4 should share the same flyweight after load");
        }
    }

    @Test
    void document_load_ignoresMalformedLines(@TempDir Path tmp) throws Exception {
        File f = tmp.resolve("bad.txt").toFile();
        java.nio.file.Files.writeString(
                f.toPath(),
                "A|Arial|12|Red\n" + "B|Arial|12\n" + "C|Calibri|14|Blue\n"
        );

        Document loaded = Document.loadFrom(f);
        assertEquals("AC", loaded.asPlainText(), "Malformed lines should be skipped, not crash loading");
    }
}
