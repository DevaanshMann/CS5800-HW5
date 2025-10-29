package Flyweight;

import java.util.*;

public class StyleFactory {
    private static final Map<CharacterStyle, CharacterStyle> stylingData = new HashMap<>();

    public static CharacterStyle get(String font, int size, String color) {
        CharacterStyle style = new CharacterStyle(font, size, color);
        return stylingData.computeIfAbsent(style, k->style);
    }

    public static int styleCount(){
        return stylingData.size();
    }

    public static Collection<CharacterStyle> styles(){
        return Collections.unmodifiableCollection(stylingData.values());
    }
}
