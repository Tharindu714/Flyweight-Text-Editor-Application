# Flyweight Text Editor Application 🚀

**TC Text Editor** is a sleek, high-performance Java Swing application showcasing the **Flyweight Design Pattern**. Built with clean code and Maven, it delivers an intuitive UI and extreme memory efficiency—ideal for editing large documents with thousands of styled characters.

---

## 🌟 Key Features

* **Rich Text Editing**: Bold, italic, color, font family & size controls
* **Sample Content**: Preloaded header, body, and footer showcasing multiple fonts
* **Footer Branding**: Customizable footer with your details
* **Full-Screen Mode**: Maximized window on startup for distraction-free writing
* **Flyweight Efficiency**: Shared formatting flyweights minimize memory overhead

---

## 📦 Installation & Usage

1. **Clone the repo**

   ```bash
   git clone https://github.com/Tharindu714/Flyweight-Text-Editor-Application.git
   cd Flyweight-Text-Editor-Application
   ```

2. **Build the JAR**

   ```bash
   mvn clean package
   ```
---

## 🧩 Flyweight Pattern Explained

In a large document, thousands of characters share identical styling. Without Flyweight, each character would carry its own formatting object, skyrocketing memory usage.

1. **Intrinsic State (Flyweight)**

   * Defined in `CharacterFormat`:

     * `fontFamily`, `fontSize`, `bold`, `italic`, `color`
   * **Shared**: Only one instance per unique style combination.

2. **Extrinsic State (Context)**

   * Managed by `TextCharacter`:

     * `character` glyph, `line`, `column` position
   * **Provided at runtime**, kept separate from formatting.

3. **Flyweight Factory**

   * `CharacterFormatFactory` maintains a cache `Map<String,CharacterFormat>`.
   * On `getFormat(...)`, it returns existing flyweight or creates a new one if needed.

4. **Document Structure**

   * `Document` aggregates `TextCharacter` contexts.
   * `TextEditorUI` captures keystrokes, retrieves the current `StyledDocument` attributes, and adds characters to the model using shared formats.

By reusing `CharacterFormat` objects, the application uses **orders of magnitude less memory** and reduces garbage collection overhead—perfect for real-world text editors handling enormous documents.

---

## 🛠️ Code Structure

```
src/main/java/
  ├─ TextEditorUI.java      # Swing GUI & user interaction
  ├─ TextEditorApp.java     # (Optional CLI demo)
  ├─ CharacterFormat.java   # Flyweight class (intrinsic state)
  ├─ CharacterFormatFactory.java  # Flyweight factory & cache
  ├─ TextCharacter.java     # Context class (extrinsic state)
  └─ Document.java          # Aggregator of TextCharacters
```

---

## 🎉 Get Started

1. Launch the editor and explore sample text.
2. Select text to change fonts, sizes, colors, and styles.
3. Observe memory savings by printing `formatFactory.getTotalFormats()` in your code.

Embrace the power of Flyweight—write more, waste less!

---

© 2025 Tharindu Chanaka | [github.com/Tharindu714](https://github.com/Tharindu714)

