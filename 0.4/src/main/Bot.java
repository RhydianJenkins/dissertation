/*
 * This is the main Bot class. Contains various other classes such as one 
 * for the sensors, and will contain multiple classes for the operations in the 
 * future.
 */

package main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

enum state {
	ROAMING, 
	GOINGTOBOT, 
	CONNECTED, 
	IDLE;
}

public class Bot {

	//private double xPos, yPos, direction;
	//private int xMax, yMax;
	private int tickCount;
	private boolean isActive;
	private ArrayList<Bot> closeBots;	//NOTE: closeBots array always contains itself
	private Sensors sens;	//simulates the sensors on the bots
	private state state;
	private Color botColor = new Color(0, 255, 0);	//default green
	private double desiredDirection;
	
	/* CONSTRUCTOR, SETS STATE TO ROAMING */
	public Bot(int x, int y, int xMax, int yMax, int botSize, ArrayList<Bot> bots) {
		desiredDirection = Math.random() * 360.0;
		closeBots = new ArrayList<Bot>();
		tickCount = (int) (Math.random() * 20);
		isActive = true;
		this.state = state.ROAMING;
		sens = new Sensors(x, y, xMax, yMax, botSize, bots, desiredDirection, this);
	}
	
	/* TELLS THE BOT TO 'THINK' ABOUT WHAT IT DOES NEXT */
	public void tick() {	
		if (!isActive) { return; }
		
		closeBots = sens.getCloseBots(30.0);
		//if (closeBots.size() > 0) {
		//	setDesiredDirection(getAvgCloseBotDirToBot() + 180);
		//	//turnAround();
		//}
		
		
		switch(state) {
		case IDLE: 
			break;
		case CONNECTED: 
			break;
		case ROAMING: 
			roam();
			break;
		case GOINGTOBOT: 
			break;
		default: 
			state = state.ROAMING;
			break;
		}
		
	}
	
	/* RENDERS THE ENTIRE BOT IN ITS POSITION ON CANVAS */
	public void render(Graphics g) {
		//g.setColor(botColor);
		if (closeBots.size() > 0) {
			g.setColor(new Color(255, 255, 0));
		} else {
			g.setColor(new Color(0, 255, 0));
		}
		renderBot(g);
		renderLights(g);
	}
	
	/* ROAM RANDOMLY */
	private void roam() {
		if (!isInCanvas()) {
			lookAt(sens.getXMax() / 2, sens.getYMax() / 2);
		}
		
		if (needToTurn(5.0)) { turnToDesiredDirection(5.0); }
		else { moveForward(2.0); }
		
		if (tickCount % 50 == 0) {
			goRandomDirection();
		}
		
		tickCount++;
	}
	
	/* SETS THE BACKGROUND COLOR OF THE BOT DEPENDING ON THE CURRENT STATE */
	private void setColorOfBot() {
		switch(state) {
		case IDLE: 
			botColor = new Color(200, 200, 200);
			break;
		case CONNECTED: 
			botColor = new Color(0, 0, 255);
			break;
		case ROAMING: 
			botColor = new Color(255, 0, 0);
			break;
		case GOINGTOBOT: 
			botColor = new Color(0, 0, 255);
			break;
		default: 
			botColor = new Color(0, 255, 0);
			break;
		}
	}
	
	/* RENDERS THE BOT AND A LINE SHOWING THE DIRECTION IT IS FACING */
	private void renderBot(Graphics g) {
		int d = sens.getBotSize();				//diameter of bot
		double xPos = sens.getXPos();	//true x pos
		double yPos = sens.getYPos();	//true y pos
		int x = (int) (xPos-(d/4));	//x center
		int y = (int) (yPos-(d/4));	// y center
		g.fillOval(x, y, d, d);
		//g.drawOval(x, y, d, d);
		
		// draw direction
		g.setColor(new Color(255, 0, 0));
		int xLineStart = (int) xPos + d/4;
		int yLineStart = (int) yPos + d/4;
		int xLineEnd = (int)(xLineStart+Math.cos(Math.toRadians(sens.getDirection()))*d/2);
		int yLineEnd = (int)(yLineStart+Math.sin(Math.toRadians(sens.getDirection()))*d/2);
		g.drawLine(xLineStart, yLineStart, xLineEnd, yLineEnd);
		
		// draw desiredDirection
		g.setColor(new Color(255, 200, 200));
		xLineStart = (int) xPos + d/4;
		yLineStart = (int) yPos + d/4;
		xLineEnd = (int)(xLineStart+Math.cos(Math.toRadians(desiredDirection))*d/2);
		yLineEnd = (int)(yLineStart+Math.sin(Math.toRadians(desiredDirection))*d/2);
		g.drawLine(xLineStart, yLineStart, xLineEnd, yLineEnd);
	}
	
	/* RENDERS THE LIGHTS ON THE BOT TO COMMUNICATE WITH OTHER BOTS */
	private void renderLights(Graphics g) {}
	
	/* MOVES THE BOT FORWARDS IN DIR FACING BY GIVEN AMOUNT */
	private void moveForward(double length) {
		sens.setXPos(sens.getXPos() + (Math.cos(Math.toRadians(sens.getDirection())) * length));
		sens.setYPos(sens.getYPos() + (Math.sin(Math.toRadians(sens.getDirection())) * length));
	}
	
	/* RETURNS TRUE IF DESIRED ANGLE IS MORE THAN X DEGREES AWAY */
	private boolean needToTurn(double amount) {
		double curDir = sens.getDirection();
		double desDirPlusAmount = desiredDirection + amount;		
		double desDirMinusAmount = desiredDirection - amount;
		
		// if desired direction is <||> +_ x degrees
		if (curDir < desDirMinusAmount || curDir > desDirPlusAmount) { 
			return true; 
		}
		return false;
	}
	
	/* TURNS BOT TOWARDS DESIRED DIRECTION BY AMOUNT GIVEN */
	private void turnToDesiredDirection(double amount) {									// NOT FULLY TESTED
	
		double dir = sens.getDirection();
		
		if (dir < desiredDirection) {
		    if(Math.abs(dir - desiredDirection) < 180) {
		    	dir += amount;
		    } else {
		    	dir -= amount;
		    }
		} else {
		    if(Math.abs(dir - desiredDirection) < 180) {
		    	dir -= amount;
		    } else {
		    	dir += amount;
		    }
		}
		
		sens.setDirection(dir);
		
	}
	
	/* TURNS THE BOTS DESIRED DIRECTION 180 DEGREES */
	private void turnAround() {
		desiredDirection = sens.getDirection() + 180.0;
		fixDirections();
	}
	
	/* RANDOMISES THE BOTS' DIRECTION */
	private void goRandomDirection() {
		desiredDirection = Math.random() * 360.0;
		//sens.setDirection(Math.random() * 360);
	}
	
	/* CHECKS TO SEE IF THE BOT HAS LEFT THE CANVAS */
	private boolean isInCanvas() {
		if ((sens.getXPos() >= sens.getXMax()) || (sens.getYPos() >= sens.getYMax()) || (sens.getXPos() < 0) || (sens.getYPos() < 0)) {
			return false;
		}
		return true;
	}
	
	/* SETS THE DIRECTION OF THE BOT TO FACE THE GIVEN X, Y POINT */
	private void lookAt(double x, double y) {
		// http://www.gamefromscratch.com/post/2012/11/18/GameDev-math-recipes-Rotating-to-face-a-point.aspx
		desiredDirection = Math.atan2(y - sens.getYPos(), x - sens.getXPos()) * (180 / Math.PI);
		fixDirections();
	}
	
	/* GET DIRECTION TO AN X, Y POINT */
	protected double getDirectionTo(double xTarget, double yTarget) {
		return Math.atan2(yTarget,  xTarget) / Math.PI * 180;
	}
	
	/* GET DIRECTION TO A BOT */
	protected double getDirectionTo(Bot bot) {
		return Math.atan2(bot.getY(), bot.getX()) / Math.PI * 180;
	}
	
	/* CALCULATE AND RETURNS AVERAGE DIRECTION OF CLOSEBOTS RELATIVE TO BOT */
	protected double getAvgCloseBotDirToBot() {
		// http://www.pcreview.co.uk/threads/mean-direction-through-360-degrees.1746638/
		if (closeBots.size() == 0) { return sens.getDirection(); }
		
		double cosDir = 0.0;
		double sinDir = 0.0;
		ArrayList<Double> cosDirs = new ArrayList<Double>();
		ArrayList<Double> sinDirs = new ArrayList<Double>();
		double sumCosDirs = 0.0;
		double sumSinDirs = 0.0;
		
		// Calc cosDirs, sinDirs
		for (int i = 0; i < closeBots.size(); i++) {
			cosDir = Math.cos(getDirectionTo(closeBots.get(i))*2*Math.PI/360);
			sinDir = Math.sin(getDirectionTo(closeBots.get(i))*2*Math.PI/360);
			
			//cosDir = Math.cos(getDirectionTo(closeBots.get(i)));
			//sinDir = Math.sin(getDirectionTo(closeBots.get(i)));
			
			cosDirs.add(cosDir);
			sinDirs.add(sinDir);
		}
		
		// Calc sums of cosDirs, sinDirs
		for (int i = 0; i < cosDirs.size(); i++) { sumCosDirs = sumCosDirs + cosDirs.get(i); }
		for (int i = 0; i < sinDirs.size(); i++) { sumSinDirs = sumSinDirs + sinDirs.get(i); }
		
		// Return avg dir
		double returnVal = Math.atan(sumSinDirs/sumCosDirs)*360/(2/Math.PI);
		
		return returnVal;
	}
	
	/* FIXES THE DESIREDDIRECTION AND SENS.DIRECTION TO BE BETWEEN 1 AND 360 */
	private void fixDirections() {
		double sensDir = sens.getDirection();
		if      (sensDir  > 360) { sensDir -= 360; }	// Rotate back around to keep it tidy
		else if (sensDir  < 1)   { sensDir += 360; }
		sens.setDirection(sensDir);
		
		if      (desiredDirection  > 360) { desiredDirection -= 360; }	// Rotate back around to keep it tidy
		else if (desiredDirection  < 1)   { desiredDirection += 360; }
	}
	
	/*	GETTERS AND SETTERS	*/
	public double getX() { return sens.getXPos(); }
	public double getY() { return sens.getYPos(); }	
	public double getDirection() { return sens.getDirection(); }
	public double getDesiredDirection() { return desiredDirection; }
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
		fixDirections();
	}
	public void setDesiredDirection(double d) {
		desiredDirection = d;
		fixDirections();
		
	}
	public void setActive(boolean b) {
		isActive = b;
	}
	public boolean isActive() {
		return isActive;
	}

}
