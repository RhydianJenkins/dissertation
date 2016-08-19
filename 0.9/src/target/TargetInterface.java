package target;

import java.awt.Graphics;

public interface TargetInterface {
	// constants
	int size = 10;	// diameter
	double charge = 2.0D;	// used for vff
	
	/* RENDERS TO SCREEN BASED ON XPOS, YPOS */
	void render(Graphics g);
	
	/* SETS POSITION TO X, Y */
	void place(int x, int y);
	
	/* REMOVES FROM CANVAS */
	void remove();
	
	/* GETTERS AND SETTERS */
	int getXPos();
	int getYPos();
	double getCharge();
	void setPos(int x, int y);
	void setXPos(int x);
	void setYPos(int y);
}
