package main;

import java.awt.Graphics;

public class Bot {

	private double xPos, yPos, direction;
	private int xMax, yMax, arrayNumber;
	private int tickCount = 0;
	
	public Bot(int x, int y, int xMax, int yMax, int arrayNumber) {
		this.xPos = x;			//Current x
		this.yPos = y;			//Current y
		this.xMax = xMax;		//X boundary
		this.yMax = yMax;		//Y boundary
		this.arrayNumber = arrayNumber;
		this.direction = Math.random() * 360;		//Direction facing (0 = right, rotates clockwise)
	}
	
	public void tick() {
		checkIsInCanvas();

		moveForward(2.0);

		tickCount++;
	}
	
	public void render(Graphics g) {
		//System.out.println("BOT RENDER @ (X "+this.getX()+", Y "+this.getY()+")");
		g.drawOval((int) this.xPos, (int) this.yPos, 10, 10);
	}
	
	private void moveForward(double length) {
		//System.out.println((Math.cos(Math.toRadians(direction)) * length) + " X");
		//System.out.println((Math.sin(Math.toRadians(direction)) * length) + " Y\n");
		this.xPos += Math.cos(Math.toRadians(direction)) * length;
		this.yPos += Math.sin(Math.toRadians(direction)) * length;
	}
	
	private void turn(int amount) {
		this.direction += amount;
		if (this.direction + amount > 360) {
			direction -= 360;
		} else if (this.direction + amount < 0) {
			direction += 360;
		}
	}
	
	private void turnAround() {
		direction += 180;
		if (direction > 360) { direction -= 360; }	// Rotate back around to keep it tidy
	}
	
	private void goRandomDirection() {
		direction = Math.random() * 360;
	}
	
	/*
	 * If the bot is about to leave the canvas, its direction is inverted to seem like it 'bounced' on the boundary
	 */
	private void checkIsInCanvas() {
		if ((xPos >= xMax) || (yPos >= yMax) || (xPos < 0) || (yPos < 0)) {
			
		}
	}
	
	/*	GETTERS AND SETTERS	*/
	public double getX() { return xPos; }
	public double getY() { return yPos; }	
	public double getDirection() { return direction; }
	public void setPos(int x, int y) {
		this.xPos = (double) x;
		this.yPos = (double) y;
	}
	public void setPos(double x, double y) {
		this.xPos = x;
		this.yPos = y;
	}
	public void setDirection(double d) {
		direction = d;
	}
}
