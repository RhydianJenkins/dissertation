/*
 * Canvas containing all bots. This class is responsible for handling mouse operations 
 * on the canvas, ticking / rendering the bots, and giving overall control (for testing) 
 * to the system (e.g. place all bots in teh center of the canvas). 
 */

package gfx;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import bots.Bot;
import listeners.CustomListener;
import target.LineTarget;
import target.TargetInterface;

public class BotCanvas extends Canvas {
	
	public ArrayList<Bot> bots;
	public ArrayList<TargetInterface> targets;
	private int width, height, botSize;
	private CustomListener listener;
	
	public BotCanvas(int width, int height, int noOfBots) {
		bots = new ArrayList<Bot>();
		targets = new ArrayList<TargetInterface>();
		
		this.botSize = 20;
		this.width = width;	//note, adjusted for scale
		this.height = height;
		
		// add bots to canvas
		for (int i = 0; i < noOfBots; i ++) {
			addBot((int)(Math.random()*width), (int)(Math.random()*height));
		}
		
		// set some vars for the canvas
		this.setBackground(new Color(200, 200, 200));
		this.setBounds(0, 0, width, height);
		
		// add listener
		listener = new CustomListener(this);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		this.addKeyListener(listener);
	}
	
	public void tick() {
		for (int i = 0; i < bots.size(); i ++) {
			bots.get(i).tick();
		}
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs== null) {	// If no buffer strategy has been created, create one.
			//System.out.println("Null bs, creating one");
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		//clear screen
		g.clearRect(0, 0, width, height);
		
		// Render bots
		for (int i = 0; i < bots.size(); i ++) {
			bots.get(i).render(g);
		}
		
		// Render targets
		for (int i = 0; i < targets.size(); i ++) {
			targets.get(i).render(g);
		}
		
		// clean up
		g.dispose();
		bs.show();
	}
	
	/* ADDS A BOT AT POSITION X, Y. RANDOM DIR */
	public void addBot(int x, int y) {
		bots.add(new Bot(x, y, width, height, botSize, bots));
	}
	
	/* ADDS A LINETARGET AT POSITION X, Y */
	public void addLineTarget(int x, int y) {
		targets.add(new LineTarget(x, y));
	}
	
	/* PLACES ALL BOTS IN THE CENTER OF THE CANVAS */
	public void centerAll() {
		for (int i = 0; i < bots.size(); i++) {
			bots.get(i).setPos(width / 2, height / 2);
		}
	}
	
	/* GIVES EACH BOT ON CANVAS A RANDOM X, Y POSITION */
	public void scatterAll() {
		for (int i = 0; i < bots.size(); i++) {
			double xi, yi;
			xi = Math.random() * width;
			yi = Math.random() * height;
			bots.get(i).setPos(xi, yi);
		}
	}
	
	/* TELLS EACH BOT ON CANVAS TO RANDOMISE ITS DIRECTION */
	public void randomiseBotDirections() {
		for (int i = 0; i < bots.size(); i++) {
			bots.get(i).setDirection(Math.random() * 360);
		}
	}
	
	/* REMOVES A SPECIFIED BOT FROM THE ARRAYLIST AND THEREFOR THE SIMULATOR */
	public void killBot(Bot bot) {
		for (int i = 0; i < bots.size(); i++) {
			if (bots.get(i).equals(bot)) {
				bots.remove(i);
			}
		}
	}
	
	/* IF NO BOT IS GIVEN, THE SELECTED BOT (ML) IS KILLED */
	public void killSelectedBot() {
		Bot theBot = listener.getSelectedBot();
		if (theBot != null) {
			killBot(theBot);
		}
	}
	
	/* TURNS ON A RANOM LIGHT ON SELECTED BOT */
	public void switchSelectedBotLED() {
		Bot theBot = listener.getSelectedBot();
		if (theBot != null) {
			theBot.requestBotBehind();
		}
	}
	
	/* GETTERS AND SETTERS */
	public int getBotSize() { return botSize; }
}
