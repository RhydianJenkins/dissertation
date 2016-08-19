/*
 * Each and every bot owns a sensor object. The object is responsible 
 * for simulating sensors that will be mounted on each bot. These sensors 
 * can do operations such as see what bots are close, and calculate the position of 
 * the bot relative to itself.
 */

package bots;

import java.awt.Color;
import java.util.ArrayList;

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
	
	/* GETS DIST BETWEEN TWO X, Y POS */
	protected double getDistanceBetween(double x1, double y1, double x2, double y2) { 
		double xDiff = x1 - x2;         
		double yDiff = y1 - y2;
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

	/* GET DIRECTION TO AN X, Y POINT */
	protected double getDirectionTo(double xTarget, double yTarget) {
		double dir = Math.atan2(yTarget-getYPos(), xTarget-getXPos()) / Math.PI * 180;
		return fixDirection(dir);
	}
	
	/* GET DIRECTION TO A BOT */
	protected double getDirectionTo(Bot bot) {
		double dir = Math.atan2(bot.getY()-getYPos(), bot.getX()-getXPos()) / Math.PI * 180;
		return fixDirection(dir);
	}
	
	/* GET RELATIVE DIRECTION TO A BOT FROM THE BOT */
	protected double getRelativeDirectionTo(Bot bot) {
		double dir = getDirectionTo(bot) - getDirection();
		dir = fixDirection(dir);
		return dir;
	}
	
	/* GET RELATIVE DIRECTION TO AN X, Y POINT FROM THE BOT */ 
	protected double getRelativeDirectionTo(double xTarget, double yTarget) {
		double dir = Math.abs(getDirectionTo(xTarget, yTarget) + getDirection());
		dir = fixDirection(dir);
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
	
	/* RETURNS CLOSEST WHITE LED, RETURNS NULL IF NONE FOUND */
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
	
	/* RETURNS AN ARRAYLIST<LED> OF CLOSE BLUE LEDS, RETURNS NULL IF NONE FOUND */
	protected ArrayList<LED> getClosestBlueLEDs() {
		ArrayList<Bot> closeBots = calcAndGetCloseBots(100.0D);
		ArrayList<LED> closeBlueLEDs = new ArrayList<LED>();
		
		// go through all bots seen
		for (int i = 0; i < closeBots.size(); i++) {
			
			// go through each of those bots' LEDs
			for (int j = 0; j < currentBot.getNoOfLEDs(); j ++) {
				
				// if it's BLUE
				if (closeBots.get(i).LEDs.get(j).getColor() == Color.BLUE) {
					
					// save the closest BLUE LED to the closeLEDs array
					closeBlueLEDs.add(closeBots.get(i).LEDs.get(j));
				}
			}
		}
		return closeBlueLEDs; 
	}
	
	/* TURNS OFF ANY WHITE LEDS THAT HAVE A BLUE BOT NEXT TO THEM (MEANING THEY HAVE BEEN CONNECTED) */
	protected void turnOffWhiteLEDsIfBlueBotConnected() {
		// if no WHITE LEDs are on, return
		if (!this.currentBot.LEDManager.hasLEDSetTo(Color.WHITE)) { return; }
		
		// if no BLUE LEDs found, return
		ArrayList<LED> closestBlueLEDs = getClosestBlueLEDs();
		if (closestBlueLEDs.size() < 1) { return; }
		
		/* WE HAVE A POTENTIAL CONNECTION NOW */
		
		// go through each white LED on our bot and set off if looking at closestBlueLED
		for (int i = 0; i < currentBot.LEDs.size(); i++) {
			
			// for each white LED that we have on...
			if (currentBot.LEDs.get(i).getColorString() == "white") {		
				
				// go through all close blue LEDs seen
				for (int j = 0; j < closestBlueLEDs.size(); j++) {
					
					// if that white LED is in the direction of any of the closestBlueLEDs' owners...
					if (isInDirection(closestBlueLEDs.get(j).getOwner(), currentBot.LEDs.get(i).getDirToParentBot())) {
						
						// turn that white LED red, as there is a bot there thats blue
						currentBot.LEDs.get(i).setColor(Color.RED);
					}
				}
			}
		}
	}
	
	/* RETURNS TRUE IF THE GIVEN X, Y POSITION IS WITHING THE MINDIST OF THE BOT */
	protected boolean isCloseTo(double x, double y, double minDist) {
		double dist = this.getDistanceTo(x, y);
		if (dist <= minDist) {
			return true;
		}
		return false;
	}
	
	/* RETURNS TRUE IF THE BOT IS IN THE GIVEN DIR (+- 2 DEGREES) */
	protected boolean isInDirection(Bot bot, int dir) {
		//dir = fixDirection(dir);
		int dirPlusAmount = dir + 45;
		int dirMinusAmount = dir - 45;
		if ((this.getRelativeDirectionTo(bot) < dirPlusAmount) && (this.getRelativeDirectionTo(bot) > dirMinusAmount)) {
			return true;
		}
		
		//System.out.println("Dir = "+dir+", relDirToBot = "+this.getRelativeDirectionTo(bot));
		return false;
	}
	
	/* RETURNS NORMALISED VALUE OF GIVEN DIRECTION. EG -90 WILL RETURN 270 */
	private int fixDirection(int dir) {
		while (dir < 0)   { dir += 360; }
		while (dir > 360) { dir -= 360; }
		return dir;
	}
	
	/* OVERLOADED TO TAKE DOUBLE PARAMETER, STILL RETURNS INT */
	private int fixDirection(double dir) {
		return fixDirection((int)dir);
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
