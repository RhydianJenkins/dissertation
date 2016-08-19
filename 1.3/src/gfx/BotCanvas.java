/*
 * Canvas containing all bots. This class is responsible for handling mouse operations 
 * on the canvas, ticking / rendering the bots, and giving overall control (for testing) 
 * to the system (e.g. drag and drop a bot, elect a seed bot). 
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
	
	public BotCanvas(int width, int height, int noOfBots) {
		bots = new ArrayList<Bot>();
		
		this.botSize = 20;
		this.width = width;	//note, adjusted for scale
		this.height = height;
		
		// add bots to canvas
		for (int i = 0; i < noOfBots; i ++) {
			// place randomised
			//addBot((int)(Math.random()*width), (int)(Math.random()*height));
			
			// place centered
			addBot(width/2, height/2);
		}
		
		// set some vars for the canvas
		this.setBackground(new Color(240, 240, 240));
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
		
		// clear screen
		g.clearRect(0, 0, width, height);
		
		// Render bots
		for (int i = 0; i < bots.size(); i ++) {
			bots.get(i).render(g);
		}
		
		// render instructional text
		renderText(g);
		
		// clean up
		g.dispose();
		bs.show();
	}
	
	/* RENDERS THE INSTRUCTIONAL TEXT IN THE TOP LEFT OF THE SCREEN */
	private void renderText(Graphics g) {
		int xStart = 20;
		int yStart = 20;
		
		g.setColor(new Color(0, 0, 0));
		g.drawString("Press 1 to add a new bot at mouse", xStart, yStart);
		
		if (listener.getSelectedBot() != null) {
			g.drawString("Press 2 to delete selected bot", xStart, yStart + 20);
			g.drawString("Press 3 to elect seed bot (line)", xStart, yStart + 40);
			g.drawString("Press 4 to elect seed bot (arrow)", xStart, yStart + 60);
			g.drawString("Press 5 to elect seed bot (cross)", xStart, yStart + 80);
		} else {
			g.drawString("Click and drag bots to select them", xStart, yStart + 20);
		}
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
	
	/* SETS SELECTED BOT TO LINE SEED (TURN LED ON BEHIND) */
	public void setSelectedBotLineSeed() {
		Bot theBot = listener.getSelectedBot();
		if (theBot == null) { return; }
		
		// set the LEDs
		theBot.requestBot("b");
	}
	
	/* SETS SELECTED BOT TO ARROW SEED (TURN LED ON LB + RB) */
	public void setSelectedBotArrowSeed() {
		Bot theBot = listener.getSelectedBot();
		if (theBot == null) { return; }
		
		// set the LEDs
		theBot.requestBot("bl");
		theBot.requestBot("br");
	}
	
	/* SETS SELECTED BOT TO CROSS SEED (TURN LED ON LF + RF, LB + RB) */
	public void setSelectedBotCrossSeed() {
		Bot theBot = listener.getSelectedBot();
		if (theBot == null) { return; }
		
		// set the LEDs
		theBot.requestBot("fr");
		theBot.requestBot("fl");
		theBot.requestBot("br");
		theBot.requestBot("bl");
	}
	
	/* GETTERS AND SETTERS */
	public int getBotSize() { return botSize; }
}
