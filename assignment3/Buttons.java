package assignment3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

public class Buttons extends JButton {

	private static final long serialVersionUID = 1L;

	// member colors
    Font defaultFont = new Font("Arial", Font.BOLD, 14);
    Color textColor = Color.decode("#000000");
    Color backgroundColor = Color.decode("#ff0000");

	// Constructor
	public Buttons(String text) {
        this.setFocusPainted(false);
		this.setText(text);
		this.setPreferredSize(new Dimension(120, 30));
        this.setBorder(null);
		this.setForeground(textColor);
        this.setBackground(backgroundColor);
        this.setFont(defaultFont);
        this.setOpaque(true);
		this.setVisible(true);
	}
}
