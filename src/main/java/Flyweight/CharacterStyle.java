package Flyweight;

import java.util.Objects;
public class CharacterStyle {
    private final String font;
    private final int size;
    private final String color;

    public CharacterStyle(String font, int size, String color) {
        this.font = font;
        this.size = size;
        this.color = color;
    }

    public String getFont(){
        return font;
    }
    public int getSize(){
        return size;
    }
    public String getColor(){
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CharacterStyle)) return false;
        CharacterStyle that = (CharacterStyle) o;
        return size == that.size &&
                Objects.equals(font, that.font) &&
                Objects.equals(color, that.color);
    }
    @Override
    public int hashCode() {
        return Objects.hash(font, size, color);
    }
    @Override
    public String toString() {
        return "Style(" + font + ", " + size + ", " + color + ")";
    }
}
