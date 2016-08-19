/*
 * This class represents the LED lights attached to each bot. The bots will 
 * have an ArrayList<LED> attached to them containing multiple LED's. It is the 
 * Bots duty to tell each LED to switch itself on and off, but the LEDs responsability 
 * to render itself in the correct position.
 */

package bots;

import java.awt.Color;
import java.awt.Graphics;

public class LED {
	private Bot parentBot;
	private int dirToParentBot;
	private boolean isSwitchedOn;
	private Color defaultColor;
	
	/* CONSTRUCTOR, TAKES IN PARENT BOT AND DIR TO PARENT BOT */
	public LED(Bot parentBot, int dirToParentBot) {
		this.parentBot = parentBot;
		this.dirToParentBot = dirToParentBot;
		this.isSwitchedOn = true;	//TESTING, WILL BE TURNED OFF BY DEFAULT
		this.defaultColor = new Color(255, 255, 255); // WHITE
	}
	
	/* RENDERS THE LIGHT IN APPROPRIATE PLACE IF SWITCHED ON WITH DEFAULT COLOR */
	public void render(Graphics g) {
		if (!isSwitchedOn) { return; }
		
		g.setColor(defaultColor);
		int LEDSize = (int)parentBot.getSize() / 3;
		int botCenterX = (int)(parentBot.getX() + parentBot.getSize()/4)-LEDSize/2;
		int botCenterY = (int)(parentBot.getY() + parentBot.getSize()/4)-LEDSize/2;
		int LEDX = (int)(botCenterX+Math.cos(Math.toRadians(this.dirToParentBot+parentBot.getDirection()))*parentBot.getSize()/2);
		int LEDY = (int)(botCenterY+Math.sin(Math.toRadians(this.dirToParentBot+parentBot.getDirection()))*parentBot.getSize()/2);
		g.fillOval(LEDX, LEDY, LEDSize, LEDSize);
	}
	
	/* SAME AS RENDER BUT WITH A SPECIFIC COLOR */
	public void render(Graphics g, Color c) {}
	
	/* GETTERS AND SETTERS */
	public boolean isSwitchedOn() { return isSwitchedOn; }
	public int getDirToParentBot() { return dirToParentBot; }
	public void switchLED(boolean val) { isSwitchedOn = val; }
}
