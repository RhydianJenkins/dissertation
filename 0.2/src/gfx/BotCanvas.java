/*
 * Canvas containing all bots. This class is responsible for handling mouse operations 
 * on the canvas, ticking / rendering the bots, and giving overall control (for testing) 
 * to the system (e.g. place all bots in teh center of the canvas). 
 */

package gfx;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JPanel;

import main.Bot;

public class BotCanvas extends Canvas implements MouseListener, MouseMotionListener {
	
	public ArrayList<Bot> bots;
	private int width, height, direction, botSize;
	
	public BotCanvas(ArrayList<Bot> bots, int width, int height, int noOfBots, int botSize) {
		this.bots = bots;
		this.width = width;	//note, addujsted for scale
		this.height = height;
		this.direction = 90;
		this.botSize = botSize;
		
		for (int i = 0; i < noOfBots; i ++) {
			addBot(width / 2, height / 2);
		}
		
		this.setBackground(new Color(0, 0, 0));
		this.setBounds(0, 0, width, height);
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
	
	/* MOUSE LISTENER STUFF */
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	
}
