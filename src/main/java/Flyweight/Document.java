package Flyweight;

import java.io.*;
import java.util.*;

public class Document {
    private final List<StyledCharacter> content = new ArrayList<>();

    public void append(char c, CharacterStyle style) {
        content.add(new StyledCharacter(c, style));
    }

    public List<StyledCharacter> chars() {
        return Collections.unmodifiableList(content);
    }

    public String asPlainText() {
        StringBuilder sb = new StringBuilder();
        for (StyledCharacter sc : content) sb.append(sc.ch());
        return sb.toString();
    }

    public void saveTo(File file) throws IOException {
        try(PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (StyledCharacter sc : content) {
                CharacterStyle cs = sc.style();
                out.printf("%s|%s|%d|%s%n",
                        escape(String.valueOf(sc.ch())),
                        escape(cs.getFont()),
                        cs.getSize(),
                        escape(cs.getColor()));
            }
        }
    }

    public static Document loadFrom(File file) throws IOException {

        Document doc = new Document();
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length != 4) {continue;}
                char ch = unescape(parts[0]).charAt(0);
                String font = unescape(parts[1]);
                int size = Integer.parseInt(parts[2]);
                String color = unescape(parts[3]);
                CharacterStyle style = StyleFactory.get(font, size, color);
                doc.append(ch, style);
            }
        }
        return doc;
    }

    public void setCharAt(int index, char c) {
        StyledCharacter old = content.get(index);
        content.set(index, new StyledCharacter(c, old.style()));
    }
    public void setStyleAt(int index, CharacterStyle style) {
        StyledCharacter old = content.get(index);
        content.set(index, new StyledCharacter(old.ch(), style));
    }
    public void insertAt(int index, char c, CharacterStyle style) {
        content.add(index, new StyledCharacter(c, style));
    }
    public void deleteAt(int index) {
        content.remove(index);
    }

    private static String escape(String s) {
        return s.replace("\\","\\\\").replace("|","\\|");
    }

    private static String unescape(String s) {
        StringBuilder sb = new StringBuilder();
        for(char c : s.toCharArray()) {
            sb.append(c);
        }
        return sb.toString();
    }

}
