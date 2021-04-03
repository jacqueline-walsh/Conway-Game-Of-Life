package assignment3;

import java.awt.*; 
import java.awt.image.*;

import javax.swing.JFrame;

public class Frame extends JFrame {

	// member data 
	Panel p;
	private static final long serialVersionUID = 1L;
	private BufferStrategy strategy; 
	private Graphics offscreenBuffer; 
	private int x;
	private int y;
	
	// Constructor
	public Frame() {
		p = new Panel();
		// Ensure program exists correctly when closed
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set frame to center of screen
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		x = screensize.width / 2 - 400;
		y = screensize.height / 2 - 400;
		// set frame to 800 by 800 size
		this.setBounds(x, y, 800, 800);
		this.setTitle("Conway's game of life");
		// add panel to JFrame
		this.add(p);
		this.pack();
		this.setVisible(true); 
	
		 // Initialize double-buffering 
		 createBufferStrategy(2); 
		 strategy = getBufferStrategy(); 
		 offscreenBuffer = strategy.getDrawGraphics(); 
	}


	// application's paint method 
	public void paint(Graphics g) { 
		super.paint(g);
		g = offscreenBuffer; // draw to off screen buffer 
	}
}