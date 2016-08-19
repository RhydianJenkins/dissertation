/*
 * This is the main Bot class. Contains various other classes such as one 
 * for the sensors, and will contain multiple classes for the operations in the 
 * future.
 */

package bots;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import target.TargetInterface;

public class Bot {

	private double charge;
	private Bot botConnectedTo;
	private int tickCount;
	private boolean isActive;
	private TargetInterface target;
	private ArrayList<Bot> seenBots;
	private ArrayList<Bot> closeBots;
	private ArrayList<Object> seenCharges;	// contains bots' charges and LEDs' charges
	private ArrayList<LED> LEDs;
	private Sensors sens; // simulates the sensors on the bots
	private LEDManager LEDManager;	// handles which LEDs to turn on to provide high level command functionality to the bot
	private double desiredDirection;
	private static final double TURNSPEED = 5.0;
	private final int MINDIST = 5; // minimum dist bot can get to other bots
									// before it must move away

	/* CONSTRUCTOR, SETS STATE TO ROAMING */
	public Bot(int x, int y, int xMax, int yMax, int botSize, ArrayList<Bot> bots) {
		this.charge = -2.0D;
		this.botConnectedTo = null;
		desiredDirection = Math.random() * 360.0;
		target = null;
		seenBots = new ArrayList<Bot>();
		closeBots = new ArrayList<Bot>();
		seenCharges = new ArrayList<Object>();
		LEDs = new ArrayList<LED>();
		tickCount = (int) (Math.random() * 20);
		isActive = true;
		sens = new Sensors(x, y, xMax, yMax, botSize, bots, desiredDirection, this);
		
		// add the LEDs
		LEDs.add(new LED(this, 45));
		LEDs.add(new LED(this, 90));
		LEDs.add(new LED(this, 135));
		LEDs.add(new LED(this, 180));
		LEDs.add(new LED(this, 225));
		LEDs.add(new LED(this, 270));
		LEDs.add(new LED(this, 315));
		LEDManager = new LEDManager(this.LEDs);
		
	}

	/* TELLS THE BOT TO 'THINK' ABOUT WHAT IT DOES NEXT */
	public void tick() {
		
		if (!isActive) { return; }
		
		closeBots = sens.getCloseBots(sens.getBotSize() + MINDIST);
		seenBots = sens.getCloseBots(100.0);
		
		if (isWaitingForBotToConnect()) { return; }
		
		updateDirection();
		if (!hasHitWhiteLED()) {
			move();
		} else {
			this.lookAt(botConnectedTo);
		}
	}

	/* RENDERS THE ENTIRE BOT IN ITS POSITION ON CANVAS */
	public void render(Graphics g) {
		// decide what color to be
		if (seenBots.size() > 0) {
			if (closeBots.size() > 0) {
				g.setColor(new Color(255, 0, 0));
			} else {
				g.setColor(new Color(255, 255, 0));
			}
		} else {
			g.setColor(new Color(0, 255, 0));
		}

		// render
		renderBot(g);
		renderLights(g);
	}

	/* MOVE FORWARD OR TURN */
	private void move() {
		// look at center of canvas if out of bounds
		if (!isInCanvas()) {
			lookAt(sens.getXMax() / 2, sens.getYMax() / 2);
		}

		// if we're not looking at our desired direction, turn towards. Else, move forward
		if (needToTurn()) {
			turnToDesiredDirection();
		} else {
			moveForward(2.0);
		}

		// move in a random direction every 50 roaming ticks
		if (tickCount % 100 == 0) {
			goRandomDirection();
		}
		tickCount++;
	}

	/* RENDERS THE BOT AND A LINE SHOWING THE DIRECTION IT IS FACING */
	private void renderBot(Graphics g) {
		int d = sens.getBotSize(); // diameter of bot
		double xPos = sens.getXPos(); // true x pos (center)
		double yPos = sens.getYPos(); // true y pos (center)
		int x = (int) (xPos - (d / 2)); // x start rendering
		int y = (int) (yPos - (d / 2)); // y start rendering
		g.fillOval(x, y, d, d);

		int xLineStart = 0;
		int xLineEnd = 0;
		int yLineStart = 0;
		int yLineEnd = 0;
		
		// draw desiredDirection
		g.setColor(new Color(0, 0, 255));
		xLineStart = (int) xPos;
		yLineStart = (int) yPos;
		xLineEnd = (int) (xLineStart + Math.cos(Math.toRadians(desiredDirection)) * d / 2);
		yLineEnd = (int) (yLineStart + Math.sin(Math.toRadians(desiredDirection)) * d / 2);
		g.drawLine(xLineStart, yLineStart, xLineEnd, yLineEnd);
		
		// draw direction
		g.setColor(new Color(0, 0, 255));
		xLineStart = (int) xPos;
		yLineStart = (int) yPos;
		xLineEnd = (int) (xLineStart + Math.cos(Math.toRadians(sens.getDirection())) * d / 2);
		yLineEnd = (int) (yLineStart + Math.sin(Math.toRadians(sens.getDirection())) * d / 2);
		g.drawLine(xLineStart, yLineStart, xLineEnd, yLineEnd);

	}

	/* RENDERS THE LIGHTS ON THE BOT TO COMMUNICATE WITH OTHER BOTS */
	private void renderLights(Graphics g) {
		for (int i = 0; i < LEDs.size(); i++) {
			LEDs.get(i).render(g);
		}
	}

	/* MOVES THE BOT FORWARDS IN DIR FACING BY GIVEN AMOUNT */
	private void moveForward(double length) {
		sens.setXPos(sens.getXPos() + (Math.cos(Math.toRadians(sens.getDirection())) * length));
		sens.setYPos(sens.getYPos() + (Math.sin(Math.toRadians(sens.getDirection())) * length));
	}

	/* RETURNS TRUE IF DESIRED ANGLE IS MORE THAN X DEGREES AWAY */
	private boolean needToTurn() {
		double curDir = sens.getDirection();
		double desDirPlusAmount = desiredDirection + TURNSPEED;
		double desDirMinusAmount = desiredDirection - TURNSPEED;

		// if desired direction is <||> +_ x degrees
		if (curDir < desDirMinusAmount || curDir > desDirPlusAmount) {
			return true;
		}
		return false;
	}

	/* TURNS BOT TOWARDS DESIRED DIRECTION BY AMOUNT GIVEN */
	private void turnToDesiredDirection() { // NOT FULLY TESTED

		double dir = sens.getDirection();

		if (dir < desiredDirection) {
			if (Math.abs(dir - desiredDirection) < 180) {
				dir += TURNSPEED;
			} else {
				dir -= TURNSPEED;
			}
		} else {
			if (Math.abs(dir - desiredDirection) < 180) {
				dir -= TURNSPEED;
			} else {
				dir += TURNSPEED;
			}
		}

		sens.setDirection(dir);

	}

	/* RANDOMISES THE BOTS' DIRECTION */
	private void goRandomDirection() {
		desiredDirection = Math.random() * 360.0;
		// sens.setDirection(Math.random() * 360);
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
	
	/* SETS THE DESIRED DIRECTION OF THEB BOT TO FACE A IVEN BOT */
	private void lookAt(Bot bot) {
		// http://www.gamefromscratch.com/post/2012/11/18/GameDev-math-recipes-Rotating-to-face-a-point.aspx
		desiredDirection = Math.atan2(bot.getY() - sens.getYPos(), bot.getX() - sens.getXPos()) * (180 / Math.PI);
		fixDirections();
	}
	
	/*
	 * CALCULATE AND RETURNS AVERAGE DIRECTION OF CLOSEBOTS RELATIVE TO BOT,
	 * RETURNS OWN BOTS DIR IF NO CLOSE
	 */
	protected double getAvgCloseBotDirToBots() {
		// http://www.pcreview.co.uk/threads/mean-direction-through-360-degrees.1746638/
		if (closeBots.size() == 0) {
			return sens.getDirection();
		}

		double cosDir = 0.0;
		double sinDir = 0.0;
		ArrayList<Double> cosDirs = new ArrayList<Double>();
		ArrayList<Double> sinDirs = new ArrayList<Double>();
		double sumCosDirs = 0.0;
		double sumSinDirs = 0.0;

		// calculate cosDirs and sinDirs
		for (int i = 0; i < closeBots.size(); i++) {
			cosDir = Math.cos(sens.getDirectionTo(closeBots.get(i)) * 2 * Math.PI / 360);
			sinDir = Math.sin(sens.getDirectionTo(closeBots.get(i)) * 2 * Math.PI / 360);

			this.fixDirections();

			cosDirs.add(cosDir);
			sinDirs.add(sinDir);
		}

		// Calc sums of cosDirs, sinDirs
		for (int i = 0; i < cosDirs.size(); i++) {
			sumCosDirs += cosDirs.get(i);
		}
		for (int i = 0; i < sinDirs.size(); i++) {
			sumSinDirs += sinDirs.get(i);
		}

		// Return avg dir
		// double returnVal = Math.atan(sumSinDirs/sumCosDirs)*360/2/Math.PI;
		double returnVal = Math.atan2(sumSinDirs, sumCosDirs) * 360 / 2 / Math.PI;
		return returnVal;

	}

	/* FIXES THE DESIREDDIRECTION AND SENS.DIRECTION TO BE BETWEEN 1 AND 360 */
	private void fixDirections() {
		double sensDir = sens.getDirection();
		if (sensDir > 360) {
			sensDir -= 360;
		} // Rotate back around to keep it tidy
		else if (sensDir < 1) {
			sensDir += 360;
		}
		sens.setDirection(sensDir);

		if (desiredDirection > 360) {
			desiredDirection -= 360;
		} // Rotate back around to keep it tidy
		else if (desiredDirection < 1) {
			desiredDirection += 360;
		}
	}

	/* UPDATES THE DESIRED DIRECTION DEPENDING ON VFF RESULTS */
	private void updateDirection() {
		/* http://www.cs.mcgill.ca/~hsafad/robotics/, accessed 28/02/16 */

		// First, set all seen bots' charge to -VE, all on LEDs to +VE, all off LEDs to 0.0D
		populateCharges();
		
		// VARIABLES
		double dirX = 0.0D; // relative dirX and dirY that the bot will need to face
		double dirY = 0.0D; 
		double dx = 0.0D;	// used to calculate each push/pull force, added to dirX, dirY
		double dy = 0.0D;
		double targetCharge = 0.0D;	// charge of current target bot / led
		double minS = 200;
		double distSq = 0.0D;	// distance^2 to bot/led
		double safety = 0.0D;
		double norm = 0.0D;
		
		for (int i = 0; i < seenCharges.size(); i++) {
			try {
				// first, check if the bot has a target
				if (target != null) {
					targetCharge = target.getCharge();
					distSq = sens.getDistanceTo((TargetInterface) target);
					distSq = distSq * distSq;
					
					// calculated forces
					dx = targetCharge * (target.getXPos() - this.getX()) / distSq;
					dy = targetCharge * (target.getYPos() - this.getY()) / distSq;
					
					// add calculated forces to overall direction so far
					dirX += dx;
					dirY += dy;
					
					safety = distSq / ((dx*dirX + dy*dirY));
					if ((safety > 0) && (safety < minS)) { minS = safety; }
				}
				
				// now, update direction depending on charges
				if (seenCharges.get(i) instanceof Bot) {
						
					targetCharge = ((Bot) seenCharges.get(i)).getCharge();
					distSq = sens.getDistanceTo((Bot) seenCharges.get(i));
					distSq = distSq * distSq;
					
					// calculated forces
					dx = targetCharge * (((Bot) seenCharges.get(i)).getX() - this.getX()) / distSq;
					dy = targetCharge * (((Bot) seenCharges.get(i)).getY() - this.getY()) / distSq;

					// add calculated forces to overall direction so far
					dirX += dx;
					dirY += dy;
					
					safety = distSq / ((dx*dirX + dy*dirY));
					if ((safety > 0) && (safety < minS)) { minS = safety; }
						
				} else if ((seenCharges.get(i) instanceof LED)) {
				
					targetCharge = ((LED) seenCharges.get(i)).getCharge();
					
					if (targetCharge != 0.0D) { 
						
						distSq = sens.getDistanceTo((LED) seenCharges.get(i));
						distSq = distSq * distSq;

						// calculated forces
						dx = targetCharge * (((LED) seenCharges.get(i)).getPosX() - this.getX()) / distSq;
						dy = targetCharge * (((LED) seenCharges.get(i)).getPosY() - this.getY()) / distSq;
						
						// add calculated forces to overall direction so far
						dirX += dx;
						dirY += dy;
						
						safety = distSq / ((dx*dirX + dy*dirY));
						if ((safety > 0) && (safety < minS)) { minS = safety; }
						
						if (minS < 5) {
							targetCharge *= minS/5;	
						}
						 
						if (minS > 100) {
							targetCharge *= minS/100;
						}
						
					}
				
				} else {
					System.out.println("ERROR: unknown seenCharges item "+i);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// calculate vector normal, apply to dirX, dirY
		norm = Math.sqrt(dirX*dirX + dirY*dirY);
		dirX = dirX / norm;
		dirY = dirY / norm;
		
		// set desired direction if it calculates a number
		if (dirX == (double) dirX && dirY == (double) dirY) {
			this.setDesiredDirection(sens.getDirectionTo(dirX + sens.getXPos(), dirY + sens.getYPos()));
		}
		
	}
	
	/* POPULATES SEENCHARGES APPROPRIATELY */
	private void populateCharges() {
		seenCharges.clear();
		
		// add target if there is one
		if (target != null) { seenCharges.add(target); }
		
		// add bots and LEDs
		for (int i = 0; i < seenBots.size(); i++) {
			// add the bot
			seenCharges.add(seenBots.get(i));
			// go through LEDs of bot and add
			for (int j = 0; j < seenBots.get(i).getNoOfLEDs(); j++) {
				seenCharges.add(seenBots.get(i).LEDs.get(j));
			}
		}
	}
	
	/* RETURNS TRUE IF BOT HAS COLLIDED WITH A WHITE LED (CONNECTED TO A BOT) */
	public boolean hasHitWhiteLED() {
		// go through all bots close
		for (int i = 0; i < closeBots.size(); i++) {
			// go through all their LEDs
			for (int j = 0; j < closeBots.get(i).LEDs.size(); j++) {
				// if there is one that is white
				if (closeBots.get(i).LEDs.get(j).getColorString() == "white") {
					// and that white LED is close to it
					if (sens.getDistanceTo(closeBots.get(i).LEDs.get(j)) <= this.getSize()/2) {
						// CONTACT! Save bot and return true 
						botConnectedTo = closeBots.get(i);
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/* REQUESTS BOT TO CONNECT FROM BEHIND */
	public void requestBotBehind() {
		int halfWayNo = (int)(getNoOfLEDs()/2);
		LEDManager.requestConnection("b");
	}
	
	/* RETURNS TRUE IF THE BOT IS WAITING FOR ANOTHER BOT TO CONNECT TO IT */
	public boolean isWaitingForBotToConnect() {
		for (int i = 0; i < this.LEDs.size(); i++) {
			if (LEDs.get(i).getColorString() == "white") { return true; }
		}
		return false;
	}
	
	/* GETTERS AND SETTERS */
	public double getX() {
		return sens.getXPos();
	}

	public double getY() {
		return sens.getYPos();
	}
	
	public int getNoOfLEDs() {
		return LEDs.size();
	}

	public double getDirection() {
		return sens.getDirection();
	}

	public double getDesiredDirection() {
		return desiredDirection;
	}

	public int getSize() {
		return sens.getBotSize();
	}
	
	public double getCharge() {
		return charge;
	}

	public void setXPos(double x) {
		sens.setXPos(x);
	}

	public void setYPos(double y) {
		sens.setXPos(y);
	}

	public void setActive(boolean b) {
		isActive = b;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setPos(int x, int y) {
		sens.setXPos((double) x);
		sens.setYPos((double) y);
	}

	public void setPos(double x, double y) {
		sens.setXPos(x);
		sens.setYPos(y);
	}

	public void setDirection(double d) {
		sens.setDirection(d);
		fixDirections();
	}

	public void setDesiredDirection(double d) {
		desiredDirection = d;
		fixDirections();
	}

}
