/*
 * Class that manages the window the simulator appears in. Extends JFrame 
 * that contains the BotCanvas and the btnPanel. Contains the size information 
 * of the window, as well as manages the buttons in the btnPanel.
 */

package gfx;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

public class GUIManager extends JFrame {

	public static final int WIDTH = 256;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 3;
	
	public BotCanvas botCanvas;
	
	public GUIManager() {
		Dimension d1 = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		setMinimumSize(d1);
		//setMaximumSize(d1);
		//setPreferredSize(d1);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		//this.setAlwaysOnTop(true);
		this.setFocusable(true);
		this.requestFocus();
		
		botCanvas = new BotCanvas(WIDTH*SCALE, HEIGHT*SCALE, 5);	// WIDTH, HEIGHT, NUMBER OF BOTS
		
		this.add(botCanvas);
		this.pack();
	}
	
	public void tick() {
		botCanvas.tick();
	}
	
	public void render() {
		botCanvas.render();
	}

}
