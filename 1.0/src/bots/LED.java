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
	private Color color;
	
	/* CONSTRUCTOR, TAKES IN PARENT BOT AND DIR TO PARENT BOT */
	public LED(Bot parentBot, int dirToParentBot) {
		this.parentBot = parentBot;
		this.dirToParentBot = dirToParentBot;
		this.color = null; // off
	}
	
	/* RENDERS THE LIGHT IN APPROPRIATE PLACE IF SWITCHED ON WITH DEFAULT COLOR */
	public void render(Graphics g) {
		if (isOff()) { return; }
		render(g, color);
	}
	
	/* SAME AS RENDER BUT WITH A SPECIFIC COLOR */
	public void render(Graphics g, Color c) {		
		if (this.getColorString() == null) { return; }
	
		g.setColor(c);
		
		int LEDSize     = 5;
		int botCenterX  = (int)(parentBot.getX());
		int botCenterY  = (int)(parentBot.getY());
		int LEDXCenter  = (int)(botCenterX+Math.cos(Math.toRadians(this.dirToParentBot+parentBot.getDirection()))*parentBot.getSize()/2);
		int LEDYCenter  = (int)(botCenterY+Math.sin(Math.toRadians(this.dirToParentBot+parentBot.getDirection()))*parentBot.getSize()/2);
		int LEDRadius = (int)LEDSize / 2;
		
		g.fillOval(LEDXCenter-LEDRadius, LEDYCenter-LEDRadius, LEDSize, LEDSize);
	}
	
	/* SETS COLOR OF LED DEPENDING ON STRING VALUE */
	protected void setColor(String c) { 
		switch (c.toString().toLowerCase()) {
		case "red":
			setColor(Color.RED);
			break;
		case "white":
			setColor(Color.WHITE);
			break;
		case "green":
			setColor(Color.GREEN);
			break;
		case "blue":
			setColor(Color.BLUE);
			break;
		default:
			// LED is set to 'off'
			color = null;
			break;
		}
	}
	
	/* SETS COLOR OF LED TO COLOR VALUE GIVEN, NO ERROR HANDLING. CALLED IN SETCOLOR(STRING) */
	protected void setColor(Color c) { color = c; }
	
	/* RETURNS STRING VALUE OF COLOR */
	public String getColorString() {
		if (Color.RED.equals(color)) {
		  return "red";
		} else if (Color.WHITE.equals(color)) {
		  return "white";
		} else if (Color.GREEN.equals(color)) {
		  return "green";
		} else if (Color.BLUE.equals(color)) {
		  return "blue";
		}
		// LED is off
		return null;
	}
	
	/* GETS CHARGE OF LED DEPENDING ON COLOR */
	public double getCharge() { 
		// if LED is switched off, return 0.0
		if (this.getColorString() == null) { return 0.0D; }
		
		switch (getColorString()) {
		case "red":
			return 0.0D;
		case "white":
			return 2.0D;
		case "green":
			return 0.0D;
		case "blue":
			return 0.0D;
		default:
			// LED is off
			return 0.0D;
		} 
	}
	
	/* RETURNS TRUE IF LED IS SWITCHED OFF (COLOR = NULL) */
	public boolean  isOff() {
		if (this.getColorString() == "") {
			return true;
		}
		return false;
	}

	/* GETTERS AND SETTERS */
	public boolean isSwitchedOn() { return this.getColorString() == null ? false : true; }
	public double getPosX() {
		double botCenterX = parentBot.getX();// + parentBot.getSize()/4;
		return botCenterX+Math.cos(Math.toRadians(this.dirToParentBot+parentBot.getDirection()))*parentBot.getSize()/2;
	}
	public double getPosY() {
		double botCenterY = parentBot.getY();// + parentBot.getSize()/4;
		return botCenterY+Math.sin(Math.toRadians(this.dirToParentBot+parentBot.getDirection()))*parentBot.getSize()/2;
	}
	public int getDirToParentBot() { return dirToParentBot; }
	public Bot getOwner() { return this.parentBot; }
}
