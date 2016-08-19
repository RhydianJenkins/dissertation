/*
 * This is the main Bot class. Contains various other classes such as one 
 * for the sensors, and will contain multiple classes for the operations in the 
 * future.
 */

package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import gfx.BotCanvas;

public class Bot {

	//private double xPos, yPos, direction;
	//private int xMax, yMax;
	private int tickCount;
	private boolean isActive;
	private ArrayList<Bot> closeBots;	//NOTE: close bots array always contains itself
	private Sensors sens;	//simulates the sensors on the bots
	
	public Bot(int x, int y, int xMax, int yMax, int botSize, ArrayList<Bot> bots) {
		sens = new Sensors(x, y, xMax, yMax, botSize, bots);
		closeBots = new ArrayList<Bot>();
		tickCount = (int) (Math.random() * 20);
		isActive = true;
	}
	
	public void tick() {	
		if (!isActive) { return; }
		
		if (!isInCanvas()) {
			lookAt(sens.getXMax() / 2, sens.getYMax() / 2);
		}
		closeBots = sens.getCloseBots(30.0);
		
		moveForward(2.0);
		
		if (tickCount % 30 == 0) {
			goRandomDirection();
		}
		
		tickCount++;
	}
	
	public void render(Graphics g) {
		if (closeBots.size() > 1) {
			g.setColor(new Color(255, 255, 255));
		} else {
			g.setColor(new Color(255, 0, 0));
		}
		g.drawOval((int) sens.getXPos(), (int) sens.getYPos(), sens.getBotSize(), sens.getBotSize());
	}
	
	private void moveForward(double length) {
		sens.setXPos(sens.getXPos() + (Math.cos(Math.toRadians(sens.getDirection())) * length));
		sens.setYPos(sens.getYPos() + (Math.sin(Math.toRadians(sens.getDirection())) * length));
	}
	
	private void turn(int amount) {
		double dir = sens.getDirection();
		dir += amount;
		if (dir > 360.0) {
			dir -= 360;
		} else if (dir < 0) {
			dir += 360;
		}
		sens.setDirection(dir);
	}
	
	private void turnAround() {
		sens.setDirection(sens.getDirection() + 180.0);
		if (sens.getDirection() > 360) { sens.setDirection(sens.getDirection() - 360.0); }	// Rotate back around to keep it tidy
	}
	
	private void goRandomDirection() {
		sens.setDirection(Math.random() * 360);
	}
	
	/*
	 * If the bot is about to leave the canvas, its direction is inverted to seem like it 'bounced' on the boundary
	 */
	private boolean isInCanvas() {
		if ((sens.getXPos() >= sens.getXMax()) || (sens.getYPos() >= sens.getYMax()) || (sens.getXPos() < 0) || (sens.getYPos() < 0)) {
			return false;
		}
		return true;
	}
	
	private void lookAt(double x, double y) {
		// http://www.gamefromscratch.com/post/2012/11/18/GameDev-math-recipes-Rotating-to-face-a-point.aspx
		double desiredDirection = Math.atan2(y - sens.getYPos(), x - sens.getXPos()) * (180 / Math.PI);
		sens.setDirection(desiredDirection);
	}
	
	/*	GETTERS AND SETTERS	*/
	public double getX() { return sens.getXPos(); }
	public double getY() { return sens.getYPos(); }	
	public double getDirection() { return sens.getDirection(); }
	public void setPos(int x, int y) {
		sens.setXPos((double) x);
		sens.setYPos((double) y);
	}
	public void setPos(double x, double y) {
		sens.setXPos(x);
		sens.setYPos(y);
	}
	public void setXPos(double x) {
		sens.setXPos(x);
	}
	public void setYPos(double y) {
		sens.setXPos(y);
	}
	public void setDirection(double d) {
		sens.setDirection(d);
	}
	public void setActive(boolean b) {
		isActive = b;
	}
	public boolean isActive() {
		return isActive;
	}

}
