/*
 * Each and every bot owns a sensor object. The object is responsible 
 * for simulating sensors that will be mounted on each bot. These sensors 
 * can do operations such as see what bots are close, and calculate the position of 
 * the bot depending on the canvas.
 */

package bots;

import java.util.ArrayList;

public class Sensors {
	private double xPos, yPos, direction;	//current x, y, direction of bot
	private int xMax, yMax, botSize;	//Maximum x, y position on canvas
	private ArrayList<Bot> bots;	//All bots on canvas
	private ArrayList<Bot> closeBots;	//Bot that are within a certain radius of the bot
	private boolean isConnectedToBot;
	private Bot currentBot;	//the bot that the sensors belong to, used to remove from close list
	
	
	public Sensors (double xPos, double yPos, int xMax, int yMax, int botSize, ArrayList<Bot> bots, double direction, Bot currentBot) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.xMax = xMax;
		this.yMax = yMax;
		this.botSize = botSize;
		this.bots = bots;
		this.closeBots = new ArrayList<Bot>();
		this.direction = direction;
		this.isConnectedToBot = false;
		this.currentBot = currentBot;
	}
	
	/* CALCULATES AND RETURNS A LIST OF ALL BOTS CLOSE THAN MINDIST */
	private ArrayList<Bot> calcAndGetCloseBots(double minDist) {
		closeBots.clear();
		double distToBot;
		for (int i = 0; i < bots.size(); i++) {
			distToBot = getDistance(xPos, yPos, bots.get(i).getX(), bots.get(i).getY());
			if (minDist >= distToBot && bots.get(i).isActive() && bots.get(i) != currentBot) {
				closeBots.add(bots.get(i));
			}
		}
		return closeBots;
	}
	
	/* GET DIST BETWEEN TWO X, Y POINTS */
	protected double getDistance(double x1, double y1, double x2, double y2) { 
		double xDiff = x1 - x2;         
		double yDiff = y1 - y2;         
		double dist = Math.sqrt(xDiff*xDiff + yDiff*yDiff); 
		return dist;
	}
	
	/* GETTERS */
	protected double getXPos() { return xPos; }
	protected double getYPos() { return yPos; }
	protected double getXMax() { return xMax; }
	protected double getYMax() { return yMax; }
	protected double getDirection() { return direction; }
	protected int getBotSize() { return botSize; }
	protected ArrayList<Bot> getCloseBots(double minDist) { return calcAndGetCloseBots(minDist); }
	protected boolean isConnectedToBot() { return isConnectedToBot; }
	/* SETTERS */
	protected void setXPos(double val) { xPos = val; }
	protected void setYPos(double val) { yPos = val; }
	protected void setXMax(double val) { xMax = (int) val; }
	protected void setYMax(double val) { yMax = (int) val; }
	protected void setXMax(int val) { xMax = val; }
	protected void setYMax(int val) { yMax = val; }
	protected void setBotSize(int val) { botSize = val; }
	protected void setDirection(double val) { 
		direction = val; 
		if (direction > 360.0) {
			direction -= 360.0;
		} else if (direction < 1) {
			direction += 360.0;
		}
	}
	protected void setIsConnectedToBot(boolean val) { isConnectedToBot = val; }
}
