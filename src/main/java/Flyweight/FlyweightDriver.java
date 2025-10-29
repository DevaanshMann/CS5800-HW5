package Flyweight;

import java.io.File;

public class FlyweightDriver {
    private static final String PATH = "HW5-String.txt";

    public static void main(String[] args) throws Exception {
        File file = new File(args.length > 0 ? args[0] : PATH);
        if (!file.exists()) {
            System.err.println("File not found: " + file.getAbsolutePath());
            System.err.println("Create the file first (one line per char: char|font|size|color) and run again.");
            return; // exit
        }

        Document doc = Document.loadFrom(file);

        System.out.println("Loaded from: " + file.getAbsolutePath());
        System.out.println("Plain text: " + doc.asPlainText());
        System.out.println("Unique styles in factory: " + StyleFactory.styleCount());

        for (StyledCharacter sc : doc.chars()) {
            System.out.printf("'%c' -> %s%n", sc.ch(), sc.style());
        }

    }
}
