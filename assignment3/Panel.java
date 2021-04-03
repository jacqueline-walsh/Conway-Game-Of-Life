package assignment3;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javax.swing.*;

public class Panel extends JPanel implements MouseListener, MouseMotionListener, ActionListener, Runnable {

	// member data 
	private static final long serialVersionUID = 1L;
	private final int PANEL_WIDTH = 800;
	private final int PANEL_HEIGHT = 800;
	private boolean gameState[][] = new boolean[40][40]; 
	private boolean playing = false;
	Random rand = new Random();	
	Buttons startBtn, randomBtn, loadBtn, saveBtn, stopBtn;


	// constructor
	public Panel() {
		this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		this.setBackground(Color.black);
		this.setVisible(true);

		// instantiate the buttons
		startBtn = new Buttons("START");
		randomBtn = new Buttons("RANDOM");
		loadBtn = new Buttons("LOAD");
		saveBtn = new Buttons("SAVE");	
		stopBtn = new Buttons("STOP");
		// add Action Listener to buttons
		startBtn.addActionListener(this);
		randomBtn.addActionListener(this);
		loadBtn.addActionListener(this);
		saveBtn.addActionListener(this);
		stopBtn.addActionListener(this);
		// add buttons to JPanel
		add(startBtn);
		add(randomBtn);
		// add buttons to JPanel but set buttons not to be visible
		add(loadBtn).setVisible(true);
		add(saveBtn).setVisible(false);
		add(stopBtn).setVisible(false);
		
		 // Initialize the game state.  Set screen state to dead all black
		 for (int x=0;x<40;x++) { 
			 for (int y=0;y<40;y++) { 
				 gameState[x][y]=false; 
			 }
		 }			
		 // register the JPanel itself to receive motion listener for drag events
		 addMouseMotionListener(this); 	
		// register the JPanel itself to receive mouse events  
		 addMouseListener((MouseListener) this);  
		// create and start our animation thread 
		Thread t = new Thread(this); 
		t.start();
	}
	
	// application's paint method 
	@Override
	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		// redraw all game objects 
		g.setColor(Color.WHITE); 
		for (int x=0;x<40;x++) { 
			for (int y=0;y<40;y++) { 
				if (gameState[x][y]) { 
					g.fillRect(x*20, y*20, 20, 20); 
				} 
			} 
		} 
	}

	@Override
	public void mousePressed(MouseEvent e) {
		toggleGameState(e);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {	
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {	
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		toggleGameState(e);
	}

	// method to toggle cells drawn
	public void toggleGameState(MouseEvent e) {
		 // determines which cell of the gameState array was clicked on  
		int x = e.getX()/20; 
		int y = e.getY()/20; 
		 // toggle the state of the cell 
		 gameState[x][y] = !gameState[x][y]; 
		 // request an extra repaint, to get immediate visual feedback  
		 this.repaint(); 		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// RUN RANDOM AUTOMATION
		if (e.getSource() == startBtn) {
			playing = true;
	
			loadBtn.setVisible(true);
			saveBtn.setVisible(false);	
			stopBtn.setVisible(true);
			startBtn.setVisible(true);
		}
		
		if (e.getSource() == stopBtn) {
			playing = false;
			loadBtn.setVisible(true);
			saveBtn.setVisible(true);
			startBtn.setVisible(true);
		}
		
		// RUN RANDOM AUTOMATION
		if (e.getSource() == randomBtn) {
			playing = false;
			for (int x=0;x<40;x++) { 
				for (int y=0;y<40;y++) { 			
					gameState[x][y] = rand.nextBoolean();
				}
			}
		}	
		
		// LOAD GAME FROM FILE
		if (e.getSource() == loadBtn) {
			playing = false;
			int[][] state = new int[40][40];
			File file = new File("ConwayFile.txt");
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				for (int i = 0; i < 40 && reader.ready(); i++) {
					String[] splitRow = reader.readLine().split(" ");
					for (int j = 0; j < 40; j++) {
						state[i][j] = Integer.parseInt(splitRow[j]);
					}
				}
				for (int x=0; x<40;x++) {
					for (int y=0;y<40;y++) { 
						gameState[x][y] = state[x][y] == 1 ? true : false;
					}
				}
				this.repaint();
				reader.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}	
		
		//  SAVE GAME TO FILE
		if (e.getSource() == saveBtn) {
			playing = false;
			int[][] state = new int[40][40];

			for (int x=0; x<40;x++) {
				for (int y=0;y<40;y++) { 
					state[x][y] = gameState[x][y] ? 1 : 0;
				}
			}
			
			StringBuilder buildString = new StringBuilder();
			for (int x = 0; x < state.length; x++) {
				for (int y = 0; y < state.length; y++) {
					buildString.append(state[x][y]+ " ");
				}
				buildString.append("\n");
			}
			
			try {
				File file = new File("ConwayFile.txt");
				BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(file));
				bufferedwriter.write(buildString.toString());
				bufferedwriter.close();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}	
	}	

	// thread's entry point 
	public void run() { 
		while ( true ) { 
			// 1: sleep for 1/5 second
			try { 
				Thread.sleep(200); 
			} catch (InterruptedException e) { 
				e.printStackTrace();
			} 
			// 2: animate game objects 
			if (playing) {
				boolean[][] gameStateCopy = new boolean[40][40];
				//  check count for all cells around current cell
				for (int x=0;x<40;x++) { 
					for (int y=0;y<40;y++) { 
						int count = this.countNeighbours(x, y);
						// if current cell is true
						if (gameState[x][y]) { 
							if (count < 2) {
								gameStateCopy[x][y] = false;
							} else if (count > 3) {
								gameStateCopy[x][y] = false;
							} else {
								gameStateCopy[x][y] = true;
							}
						}  else {
							if (count == 3) {
								gameStateCopy[x][y] = true;
							} else {
								gameStateCopy[x][y] = false;
							}
						}
					} 
				}
				// assign gameStateCopy back to gameState
				gameState = gameStateCopy;
			}
			// 3: force an application repaint 
			this.repaint(); 
		} 
	}
	
	public int countNeighbours(int cx, int cy) {
		int count = 0;
		// this take current cell and checks all 8 cells around the cell
		for (int x = cx-1; x <= cx+1; x++) {
			for (int y = cy-1; y <= cy+1; y++ ) {
				try {
					// if gameState cell is true count++
					if (gameState[x][y]) {
						count++;
					}
					// If index is out of bounds catch clause will continue program
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}
		if (gameState[cx][cy]) 
			return count -1;
		else 
			return count;
	}	
}

	