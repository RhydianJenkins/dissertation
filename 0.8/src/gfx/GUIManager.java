/*
 * Class that manages the window the simulator appears in. Extends JFrame 
 * that contains the BotCanvas and the btnPanel. Contains the size information 
 * of the window, as well as manages the buttons in the btnPanel.
 */

package gfx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import bots.Bot;
import main.Simulator;

public class GUIManager extends JFrame {
	
	public static final int WIDTH = 256;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 2;
	
	public Simulator sim;
	public ArrayList<Bot> bots;
	public BotCanvas botCanvas;
	public JPanel btnPanel;
	public JButton btnExit;
	public JButton btnCenter;
	public JButton btnScatterBots;
	
	public GUIManager(Simulator sim, ArrayList<Bot> bots, int botSize) {
		Dimension d1 = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		setMinimumSize(d1);
		//setMaximumSize(d1);
		//setPreferredSize(d1);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		
		this.sim = sim;
		this.bots = bots;
		
		botCanvas = new BotCanvas(bots, WIDTH*SCALE, HEIGHT*SCALE, 3, botSize);
		
		btnPanel = new JPanel();
		btnPanel.setBackground(new Color(150, 150, 150));
		Dimension d2 = new Dimension(WIDTH * SCALE, 75);
		btnPanel.setSize(d2);
		
		btnScatterBots = new JButton("Scatter");
		btnScatterBots.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("btnScatterBots pressed, scattering...");
				botCanvas.scatterAll();
			}
		});
		
		btnCenter = new JButton("Center");
		btnCenter.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//System.out.println("btnCenter pressed, centering...");
				botCanvas.centerAll();
			}
		});
		
		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//System.out.println("btnExit pressed, exiting...");
				sim.stop();
			}
		});
		
		this.add(botCanvas);
		btnPanel.add(btnCenter);
		btnPanel.add(btnScatterBots);
		btnPanel.add(btnExit);
		this.add(btnPanel, BorderLayout.PAGE_END);
		this.pack();
	}
	
	public void tick() {
		botCanvas.tick();
	}
	
	public void render() {
		botCanvas.render();
	}

}
