/*
 * This type of target gets the robots to form a line.
 */

package target;

import java.awt.Color;
import java.awt.Graphics;

public class LineTarget implements TargetInterface {

	int xPos;
	int yPos;	
	
	/* CONSTRUCTOR */
	public LineTarget(int x, int y) {
		this.xPos = x;
		this.yPos = y;
	}
	
	/* RENDERS THE TARGET */
	public void render(Graphics g) {
		int offset = TargetInterface.size / 2;
		g.setColor(new Color(255, 255, 0));
		g.drawRect(xPos-offset, yPos-offset, TargetInterface.size, TargetInterface.size);
	}

	/* PLACES THE TARGET AT X, Y */
	public void place(int x, int y) {
		setPos(x, y);
	}
	
	/* REMOVES THE TARGET FROM THE CANVAS */
	public void remove() {

	}

	/* GETTERS AND SETTERS */
	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
	
	public double getCharge() {
		return charge;
	}

	public void setPos(int x, int y) {
		this.xPos = x;
		this.yPos = y;
	}

	public void setXPos(int x) {
		this.xPos = x;
	}

	public void setYPos(int y) {
		this.yPos = y;
	}

}
