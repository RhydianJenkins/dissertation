/*
 * Each and every bot owns a sensor object. The object is responsible 
 * for simulating sensors that will be mounted on each bot. These sensors 
 * can do operations such as see what bots are close, and calculate the position of 
 * the bot relative to itself.
 */

package bots;

import java.awt.Color;
import java.util.ArrayList;

import target.TargetInterface;

public class Sensors {
	private double xPos, yPos, direction;	//current x, y, direction of bot
	private int xMax, yMax, botSize;	//Maximum x, y position on canvas
	private ArrayList<Bot> bots;	//All bots on canvas
	private Bot currentBot;	//the bot that the sensors belong to, used to remove from close list
	
	public Sensors (double xPos, double yPos, int xMax, int yMax, int botSize, ArrayList<Bot> bots, double direction, Bot currentBot) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.xMax = xMax;
		this.yMax = yMax;
		this.botSize = botSize;
		this.bots = bots;
		this.direction = direction;
		this.currentBot = currentBot;
	}
	
	/* CALCULATES AND RETURNS A LIST OF ALL BOTS CLOSE THAN MINDIST */
	private ArrayList<Bot> calcAndGetCloseBots(double minDist) {
		ArrayList<Bot> closeBots = new ArrayList<Bot>();
		double distToBot;
		for (int i = 0; i < bots.size(); i++) {
			distToBot = getDistance(xPos, yPos, bots.get(i).getX(), bots.get(i).getY());
			if (minDist >= distToBot && bots.get(i) != currentBot) {
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
	
	/* GETS DIST TO X, Y POS */
	protected double getDistanceTo(double x, double y) { 
		double xDiff = this.getXPos() - x;         
		double yDiff = this.getYPos() - y;         
		double dist = Math.sqrt(xDiff*xDiff + yDiff*yDiff); 
		return dist;
	}
	
	/* GETS DIST FROM BOT TO OTHER BOT */
	protected double getDistanceTo(Bot bot) { 
		double xDiff = this.getXPos() - bot.getX();         
		double yDiff = this.getYPos() - bot.getY();         
		double dist = Math.sqrt(xDiff*xDiff + yDiff*yDiff); 
		return dist;
	}
	
	/* GETS DIST FROM BOT TO LEDS TARGET POSITION */
	protected double getDistanceTo(LED led) {
		double xDiff = this.getXPos() - led.getTargetX();         
		double yDiff = this.getYPos() - led.getTargetY();     
		double dist = Math.sqrt(xDiff*xDiff + yDiff*yDiff); 
		return dist;
	}
	
	/* GETS DIST FROM BOT TO A TARGET */
	protected double getDistanceTo(TargetInterface target) {
		double xDiff = this.getXPos() - target.getXPos();         
		double yDiff = this.getYPos() - target.getYPos();     
		double dist = Math.sqrt(xDiff*xDiff + yDiff*yDiff); 
		return dist;
	}
	
	/* GET DIRECTION TO AN X, Y POINT */
	protected double getDirectionTo(double xTarget, double yTarget) {
		double dir = Math.atan2(yTarget-getYPos(), xTarget-getXPos()) / Math.PI * 180;
		return dir;
	}
	
	/* GET DIRECTION TO A BOT */
	protected double getDirectionTo(Bot bot) {
		double dir = Math.atan2(bot.getY()-getYPos(), bot.getX()-getXPos()) / Math.PI * 180;
		return dir;
	}
	
	/* RETURNS CLOSEST BOT (NOT CURRENT BOT), RETURNS NULL IF NO BOTS IN RANGE */
	protected Bot getClosestBot() {
		ArrayList<Bot> closeBots = calcAndGetCloseBots(100.0D);
		Bot closestBotSoFar = null;
		for (int i = 0; i < closeBots.size(); i++) {
			if (closestBotSoFar == null || (getDistanceTo(closeBots.get(i)) < getDistanceTo(closestBotSoFar))) {
				closestBotSoFar = closeBots.get(i);
			}
		}
		return closestBotSoFar; 
	}
	
	/* FINDS THE CLOSEST WHITE LED AND SAVES IT TO THE BOTS VARIABLE */
	protected LED getClosestWhiteLED() {
		ArrayList<Bot> closeBots = calcAndGetCloseBots(100.0D);
		LED closestLEDSoFar = null;
		// go through all bots seen
		for (int i = 0; i < closeBots.size(); i++) {
			// go through each of those bots' LEDs
			for (int j = 0; j < currentBot.getNoOfLEDs(); j ++) {
				// if it's white
				if (closeBots.get(i).LEDs.get(j).getColor() == Color.WHITE) {
					// if the closestLEDSoFar == null, or is further away than that LED, set it to the closest so far
					if (closestLEDSoFar == null || (getDistanceTo(closeBots.get(i).LEDs.get(j)) < getDistanceTo(closestLEDSoFar))) {
						closestLEDSoFar = closeBots.get(i).LEDs.get(j);
					}
				}
			}
		}
		return closestLEDSoFar; 
	}
	
	/* RETURNS TRUE IF THE GIVEN X, Y POSITION IS WITHING THE MINDIST OF THE BOT */
	protected boolean isCloseTo(double x, double y, double minDist) {
		double dist = this.getDistanceTo(x, y);
		if (dist <= minDist) {
			return true;
		}
		return false;
	}
	
	//	UNTESTED
	/* RETURNS TRUE IF THE BOT IS LOOKING AT THE GIVEN BOT (+- 2 DEGREES)*/
	protected boolean isLookingAt(Bot bot) {
		if ((this.getDirectionTo(bot) < this.getDirection() + 2) && (this.getDirectionTo(bot) > this.getDirection() -2)) {
			return true;
		}
		
		return false;
	}
	
	/* GETTERS AND SETTERS */
	protected double getXPos() { return xPos;  }
	protected double getYPos() { return yPos;  }
	protected double getXMax() { return xMax;  }
	protected double getYMax() { return yMax;  }
	protected double getDirection() { return direction; }
	protected int getBotSize() { return botSize; }
	protected ArrayList<Bot> getCloseBots(double minDist) { return calcAndGetCloseBots(minDist); }
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

}
