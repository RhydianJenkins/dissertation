/*
 * Class that listenes for all mouse and key actions on the BotCanvas. 
 * Instructs the canvas on what to do when certain actions are fired. 
 */

package listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gfx.BotCanvas;
import main.Bot;

public class CustomListener implements MouseListener, MouseMotionListener, KeyListener {
	
	private Bot selectedBot;
	private BotCanvas botCanvas;
	private int mouseX, mouseY;
	
	public CustomListener(BotCanvas botCanvas) {
		this.botCanvas = botCanvas;
	}
	
	/* FINDS CLOSEST BOT AND SAVES IN SELECTEDBOT VAR, SAVES NULL IF NO BOT FOUND */
	public void mousePressed(MouseEvent e) {
    	double smallestDist = 50.0;
    	Bot closestBot = null;
    	for (int i = 0; i < botCanvas.bots.size(); i++) {
    		double dist = getDistanceToBot(e.getX(), e.getY(), botCanvas.bots.get(i));  
    		if (dist < smallestDist) {
    			smallestDist = dist;
    			closestBot = botCanvas.bots.get(i);
    		}	
    	}
    	if (closestBot != null) {
    		selectedBot = closestBot;
    		selectedBot.setActive(false);
    		selectedBot.setPos(e.getX(), e.getY());
    	}
    }
	
	/* CHECKS WHAT KEY IS PRESSED AND ACTS ACCORDINGLY */
	public void keyTyped(KeyEvent e) {
		char keyChar = e.getKeyChar();
		switch (keyChar) {
		case '1':
			//delete the selected bot (if any)
			botCanvas.killSelectedBot();
			break;
		case '2':
			//add a bot at mouse position
			botCanvas.addBot(mouseX, mouseY);
			break;
		}
	}
	
	/* PLACE THE SELECTED BOT (IF ANY) BACK ON THE SCREEN */
	public void mouseReleased(MouseEvent e) {
		if (selectedBot == null) { return; }
		
		selectedBot.setActive(true);
		selectedBot = null;
	}
	
	/* GET DIST FROM MOUSE TO CLOSEST BOT */
	private double getDistanceToBot(int mouseX, int mouseY, Bot bot) { 
		double xDiff = mouseX - bot.getX();         
		double yDiff = mouseY - bot.getY();         
		double dist = Math.sqrt(xDiff*xDiff + yDiff*yDiff); 
		return dist;
	}
	
	/* UPDATE THE SELECTED BOT'S POSITION AS THE MOUSE MOVES */
	public void mouseDragged(MouseEvent e) {
		if (selectedBot == null) { return; }
		selectedBot.setPos(e.getX(), e.getY());
	}
	
	/* IF THE MOUSE LEAVES THE SCREEN, DROP THE SELECTED BOT (IF ANY) */
	public void mouseExited(MouseEvent e) {
		if (selectedBot == null) { return; }
		
		selectedBot.setActive(true);
		selectedBot = null;
	}
	
	/* RETURNS NULL IF NO BOT SELECTED */
	public Bot getSelectedBot() {
		return selectedBot;
	}

	/* UPDATE MOUSEX AND MOUSEY WHEN MOUSE MOVES */
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	public int getMouseX() { return mouseX; }
	public int getMouseY() { return mouseY; }
	
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	
	
}
