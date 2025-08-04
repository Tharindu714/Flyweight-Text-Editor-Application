package com.tharindu.textEditor;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class TextEditorUI extends JFrame {
    private final JTextPane textPane;
    private final JComboBox<String> fontFamilyBox;
    private final JSpinner fontSizeSpinner;
    private final JButton boldButton;
    private final JButton italicButton;
    private final JButton colorButton;

    // Underlying flyweight components
    private final CharacterFormatFactory formatFactory;
    private final Document model;

    public TextEditorUI() {
        super("Professional Text Editor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        formatFactory = new CharacterFormatFactory();
        model = new Document();

        // Toolbar setup
        JToolBar toolBar = new JToolBar();
        fontFamilyBox = new JComboBox<>(new String[]{"Arial", "Times New Roman", "Courier New", "Verdana"});
        fontSizeSpinner = new JSpinner(new SpinnerNumberModel(12, 8, 72, 1));
        boldButton = new JButton("B"); boldButton.setFont(boldButton.getFont().deriveFont(Font.BOLD));
        italicButton = new JButton("I"); italicButton.setFont(italicButton.getFont().deriveFont(Font.ITALIC));
        colorButton = new JButton("Color");

        toolBar.add(new JLabel("Font: ")); toolBar.add(fontFamilyBox);
        toolBar.add(new JLabel("Size: ")); toolBar.add(fontSizeSpinner);
        toolBar.add(boldButton); toolBar.add(italicButton); toolBar.add(colorButton);

        // Text pane
        textPane = new JTextPane();
        textPane.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textPane);

        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel footerLabel = new JLabel("© 2025 Tharindu Chanaka | github.com/Tharindu714");
        footerLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        footerLabel.setForeground(Color.DARK_GRAY);
        footerPanel.add(footerLabel);

        getContentPane().add(footerPanel, BorderLayout.SOUTH);
        // Action listeners
        setupFormattingActions();
        setupToolbarListeners();
        setupDocumentSync();

        // Load sample styled paragraphs
        loadSampleText();
    }

    private void setupFormattingActions() {
        boldButton.addActionListener(e -> toggleStyle(StyleConstants.Bold));
        italicButton.addActionListener(e -> toggleStyle(StyleConstants.Italic));
        colorButton.addActionListener(e -> {
            Color selected = JColorChooser.showDialog(this, "Choose Text Color", Color.BLACK);
            if (selected != null) {
                SimpleAttributeSet attr = new SimpleAttributeSet();
                StyleConstants.setForeground(attr, selected);
                textPane.setCharacterAttributes(attr, false);
            }
        });
    }

    private void setupToolbarListeners() {
        fontFamilyBox.addActionListener(e -> {
            String fontFamily = (String) fontFamilyBox.getSelectedItem();
            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setFontFamily(attr, fontFamily);
            textPane.setCharacterAttributes(attr, false);
        });

        fontSizeSpinner.addChangeListener(e -> {
            int fontSize = (Integer) fontSizeSpinner.getValue();
            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setFontSize(attr, fontSize);
            textPane.setCharacterAttributes(attr, false);
        });
    }

    private void toggleStyle(Object style) {
        StyledEditorKit kit = (StyledEditorKit) textPane.getEditorKit();
        MutableAttributeSet attr = kit.getInputAttributes();
        boolean current = Boolean.TRUE.equals(attr.getAttribute(style));
        SimpleAttributeSet sas = new SimpleAttributeSet();
        sas.addAttribute(style, !current);
        textPane.setCharacterAttributes(sas, false);
    }

    private void setupDocumentSync() {
        textPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                int pos = textPane.getCaretPosition();
                try {
                    StyledDocument doc = textPane.getStyledDocument();
                    Element elem = doc.getCharacterElement(pos - 1);
                    AttributeSet as = elem.getAttributes();

                    String fontFamily = StyleConstants.getFontFamily(as);
                    int fontSize = StyleConstants.getFontSize(as);
                    boolean isBold = StyleConstants.isBold(as);
                    boolean isItalic = StyleConstants.isItalic(as);
                    Color col = StyleConstants.getForeground(as);
                    String colorName = String.format("#%06x", col.getRGB() & 0xFFFFFF);

                    int line = doc.getDefaultRootElement().getElementIndex(pos) + 1;
                    int column = pos - doc.getDefaultRootElement()
                            .getElement(line - 1).getStartOffset() + 1;

                    CharacterFormat format = formatFactory.getFormat(
                            fontFamily, fontSize, isBold, isItalic, colorName);
                    model.addCharacter(ch, line, column, format);
                } catch (Exception ex) {
                    throw new RuntimeException("Error while adding character to model", ex);
                }
            }
        });
    }

    private void loadSampleText() {
        StyledDocument doc = textPane.getStyledDocument();
        try {
            // Header paragraph
            SimpleAttributeSet headerStyle = new SimpleAttributeSet();
            StyleConstants.setFontFamily(headerStyle, "Verdana");
            StyleConstants.setFontSize(headerStyle, 20);
            StyleConstants.setBold(headerStyle, true);
            doc.insertString(doc.getLength(), "Welcome to Flyweight Text Editor Dev By Tharindu Chanaka\n\n", headerStyle);

            // Body paragraph 1
            SimpleAttributeSet body1 = new SimpleAttributeSet();
            StyleConstants.setFontFamily(body1, "Arial");
            StyleConstants.setFontSize(body1, 14);
            doc.insertString(doc.getLength(), "This is a sample paragraph in Arial, size 14. You can edit this text, apply bold or italic, change the color, and more.\n\n", body1);

            // Body paragraph 2
            SimpleAttributeSet body2 = new SimpleAttributeSet();
            StyleConstants.setFontFamily(body2, "Courier New");
            StyleConstants.setFontSize(body2, 12);
            StyleConstants.setItalic(body2, true);
            doc.insertString(doc.getLength(), "Here is another paragraph in Courier New, size 12, italicized. Try selecting it and toggling the bold button!\n\n", body2);

            // Footer paragraph
            SimpleAttributeSet footer = new SimpleAttributeSet();
            StyleConstants.setFontFamily(footer, "Times New Roman");
            StyleConstants.setFontSize(footer, 16);
            StyleConstants.setForeground(footer, Color.BLUE);
            doc.insertString(doc.getLength(), "Enjoy experimenting with different fonts, sizes, and styles!", footer);
        } catch (BadLocationException e) {
            throw new RuntimeException("Error while loading sample text", e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TextEditorUI ui = new TextEditorUI();
            ui.setVisible(true);
        });
    }
}