/*
 * Each and every bot owns a sensor object. The object is responsible 
 * for simulating sensors that will be mounted on each bot. These sensors 
 * can do operations such as see what bots are close, and calculate the position of 
 * the bot depending on the canvas.
 */

package main;

import java.util.ArrayList;

public class Sensors {
	private double xPos, yPos, direction;	//current x, y, direction of bot
	private int xMax, yMax, botSize;	//Maximum x, y position on canvas
	private ArrayList<Bot> bots;	//All bots on canvas
	private ArrayList<Bot> closeBots;	//Bot that are within a certain radius of the bot
	
	public Sensors (double xPos, double yPos, int xMax, int yMax, int botSize, ArrayList<Bot> bots) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.xMax = xMax;
		this.yMax = yMax;
		this.botSize = botSize;
		this.bots = bots;
		this.closeBots = new ArrayList<Bot>();
		this.direction = Math.random() * 360.0;
	}
	
	/* CALCULATES AND RETURNS A LIST OF ALL CLOSE BOTS */
	private ArrayList<Bot> calcAndGetCloseBots(double minDist) {
		closeBots.clear();
		double distToBot;
		for (int i = 0; i < bots.size(); i++) {
			distToBot = getDistance(xPos, yPos, bots.get(i).getX(), bots.get(i).getY());
			if (minDist >= distToBot) {
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
	
	/* GET DIRECTION TO AN X, Y POINT */
	protected double getDirectionTo(double xTarget, double yTarget) {
		return Math.atan2(yTarget,  xTarget) / Math.PI * 180;
	}
	
	/* GET DIRECTION TO A BOT */
	protected double getDirectionTo(Bot bot) {
		return Math.atan2(bot.getY(), bot.getX()) / Math.PI * 180;
	}
	
	/* RETURNS AN ARRAY OF DIRECTIONS TO ALL CLOSE BOTS */
	protected void closeBotDirections() {
		if (closeBots.size() < 2) { return; }	// only 1 bot = no close bots, returning
	}
	
	
	/* GETTERS */
	protected double getXPos() { return xPos; }
	protected double getYPos() { return yPos; }
	protected double getXMax() { return xMax; }
	protected double getYMax() { return yMax; }
	protected double getDirection() { return direction; }
	protected int getBotSize() { return botSize; }
	protected ArrayList<Bot> getCloseBots(double minDist) { return calcAndGetCloseBots(minDist); }
	/* SETTERS */
	protected void setXPos(double val) { xPos = val; }
	protected void setYPos(double val) { yPos = val; }
	protected void setXMax(double val) { xMax = (int) val; }
	protected void setYMax(double val) { yMax = (int) val; }
	protected void setXMax(int val) { xMax = val; }
	protected void setYMax(int val) { yMax = val; }
	protected void setBotSize(int val) { botSize = val; }
	protected void setDirection(double val) { direction = val; }
}
