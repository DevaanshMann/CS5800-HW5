package Flyweight;

public class StyledCharacter {
    private final char ch;
    private final CharacterStyle style;

    public StyledCharacter(char ch, CharacterStyle style) {
        this.ch = ch;
        this.style = style;
    }

    public char ch(){
        return ch;
    }
    public CharacterStyle style(){
        return style;
    }
}
