package com.tharindu.textEditor;

import java.util.*;

public class TextEditorApp {
    public static void main(String[] args) {
        // Create a factory for character formats (flyweights)
        CharacterFormatFactory formatFactory = new CharacterFormatFactory();

        // Create a document and add characters with formatting
        Document document = new Document();
        document.addCharacter('H', 1, 1, formatFactory.getFormat("Arial", 12, false, false, "Black"));
        document.addCharacter('e', 1, 2, formatFactory.getFormat("Arial", 12, false, false, "Black"));
        document.addCharacter('l', 1, 3, formatFactory.getFormat("Arial", 12, false, false, "Black"));
        document.addCharacter('l', 1, 4, formatFactory.getFormat("Arial", 12, false, false, "Black"));
        document.addCharacter('o', 1, 5, formatFactory.getFormat("Arial", 12, false, false, "Black"));

        document.addCharacter('W', 2, 1, formatFactory.getFormat("Times New Roman", 14, true, false, "Blue"));
        document.addCharacter('o', 2, 2, formatFactory.getFormat("Times New Roman", 14, true, false, "Blue"));
        document.addCharacter('r', 2, 3, formatFactory.getFormat("Times New Roman", 14, true, false, "Blue"));
        document.addCharacter('l', 2, 4, formatFactory.getFormat("Times New Roman", 14, true, false, "Blue"));
        document.addCharacter('d', 2, 5, formatFactory.getFormat("Times New Roman", 14, true, false, "Blue"));

        // Render the document
        document.render();
    }
}

// Flyweight: intrinsic shared state
class CharacterFormat {
    private final String fontFamily;
    private final int fontSize;
    private final boolean bold;
    private final boolean italic;
    private final String color;

    public CharacterFormat(String fontFamily, int fontSize,
                           boolean bold, boolean italic,
                           String color) {
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.bold = bold;
        this.italic = italic;
        this.color = color;
    }

    // Getters
    public String getFontFamily() { return fontFamily; }
    public int getFontSize() { return fontSize; }
    public boolean isBold() { return bold; }
    public boolean isItalic() { return italic; }
    public String getColor() { return color; }
}

// Flyweight Factory: manages shared CharacterFormat instances
class CharacterFormatFactory {
    private final Map<String, CharacterFormat> cache = new HashMap<>();

    public CharacterFormat getFormat(String fontFamily, int fontSize,
                                     boolean bold, boolean italic,
                                     String color) {
        String key = fontFamily + ":" + fontSize + ":" + bold + ":" + italic + ":" + color;
        if (!cache.containsKey(key)) {
            cache.put(key, new CharacterFormat(fontFamily, fontSize, bold, italic, color));
        }
        return cache.get(key);
    }

}

// The context holds an extrinsic state and a reference to the flyweight
class TextCharacter {
    private final char character;
    private final int line;
    private final int column;
    private final CharacterFormat format;

    public TextCharacter(char character, int line, int column, CharacterFormat format) {
        this.character = character;
        this.line = line;
        this.column = column;
        this.format = format;
    }

    public void render() {
        System.out.printf("Char='%c' at (%d,%d) with [%s, %dpt, bold=%b, italic=%b, color=%s]\n",
                character, line, column,
                format.getFontFamily(), format.getFontSize(),
                format.isBold(), format.isItalic(), format.getColor());
    }
}

// Document: aggregates characters
class Document {
    private final List<TextCharacter> characters = new ArrayList<>();

    public void addCharacter(char c, int line, int column, CharacterFormat format) {
        characters.add(new TextCharacter(c, line, column, format));
    }

    public void render() {
        for (TextCharacter tc : characters) {
            tc.render();
        }
    }
}