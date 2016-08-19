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

public class BotCanvas extends Canvas {
	
	public ArrayList<Bot> bots;
	private int width, height, botSize;
	private CustomListener listener;
	
	public BotCanvas(ArrayList<Bot> bots, int width, int height, int noOfBots, int botSize) {
		this.bots = bots;
		this.width = width;	//note, adjusted for scale
		this.height = height;
		this.botSize = botSize;
		
		for (int i = 0; i < noOfBots; i ++) {
			addBot(width / 2, height / 2);
		}
		
		this.setBackground(new Color(0, 0, 0));
		this.setBounds(0, 0, width, height);
		
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
	
		// center dot
		//g.setColor(new Color(255, 255, 255));
		//g.fillOval(width/2, height/2, 5, 5);
		
		g.dispose();
		bs.show();
	}
	
	/* ADDS A BOT AT POSITION X, Y. RANDOM DIR */
	public void addBot(int x, int y) {
		bots.add(new Bot(x, y, width, height, botSize, bots));
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
			yi = Math.random() * width;
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

	/* GETTERS AND SETTERS */
	public int getBotSize() { return botSize; }
}
