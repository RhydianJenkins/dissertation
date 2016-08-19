/*
 * Managers the LEDs to provide high level command functionaility to the bot.
 * Means the bot can just say commands like "confirm connection" without having 
 * to worry about all of the LEDs.
 */

package bots;

import java.awt.Color;
import java.util.ArrayList;

public class LEDManager {

	private ArrayList<LED> LEDs;
	
	public LEDManager(ArrayList<LED> LEDs) {
		this.LEDs = LEDs;
	}
	
	/* CONFIRMS THAT THE BOT HAS CONNECTED TO ANOTHER BOT */
	protected void confirmConnection() {
		setAllLEDs(Color.BLUE);
	}
	
	/* REQUEST A NEW BOT CONNECTION TO IT ON THE GIVEN LED */
	protected void requestConnection(int ConnectionLEDNo) {
		LEDs.get(ConnectionLEDNo).setColor(Color.WHITE);
		
		// now set all off LEDs to red
		for (int i = 0; i < LEDs.size(); i++) {
			if (LEDs.get(i).getColorString() != "white") {
				LEDs.get(i).setColor(Color.RED);
			}
		}
	}
	
	/* REQUEST A NEW BOT CONNECTION FROM STRING COMMAND */
	protected void requestConnection(String ConnectionLEDNo) {
		/*
		 * fr = front-right  (0)
		 * r  = right        (1)
		 * br = bottom-right (2)
		 * b  = bottom       (3)
		 * bl = bottom-left  (4)
		 * l  = left         (5)
		 * fl = front-left   (6) 
		 */
		
		switch (ConnectionLEDNo.toLowerCase()) {
		case "fr": requestConnection(0); break;
		case "r" : requestConnection(1); break;
		case "br": requestConnection(2); break;
		case "b" : requestConnection(3); break;
		case "bl": requestConnection(4); break;
		case "l" : requestConnection(5); break;
		case "fl": requestConnection(6); break;
		default: 
			System.err.println("ERROR: invalid string argument in LEDManager.RequestConnection("+ConnectionLEDNo+");");
			break;
		}
	}
	
	/* TURNS OFF ALL LEDS */
	protected void turnOffAllLEDs() {
		setAllLEDs(null);
	}
	
	/* SETS ALL LEDS TO SPECIFIED COLOR */
	protected void setAllLEDs(Color c) {
		for (int i = 0; i < LEDs.size(); i++) {
			LEDs.get(i).setColor(c);
		}
	}
	
	/* SWITCHES LED OF INDEX LEDNO TO VAL */
	public void switchLED(int LEDNo, String val) {
		if (LEDs.size() < LEDNo) {
			throw new IndexOutOfBoundsException();
		} else {
			LEDs.get(LEDNo).setColor(val);
		}
	}
	
	/* RETURNS TRUE IF AN LED IS LIT UP */
	public boolean hasLEDOn() {
		for (int i = 0; i < LEDs.size(); i++) {
			if (LEDs.get(i).isSwitchedOn()) { return true; }
		}
		return false;
	}
	
	/* RETURNS TRUE IF ANY OF THE LEDS ARE SET TO THE GIVEN COLOUR */
	public boolean hasLEDSetTo(Color c) {
		for (int i = 0; i < LEDs.size(); i++) {
			if (LEDs.get(i).getColor() == c) {
				return true;
			}
		}
		return false;
	}
	
	/* RETURNS TRUE IF ALL LEDS ARE OFF */
	public boolean hasAllLEDsSetOff() {
		for (int i = 0; i < LEDs.size(); i++) {
			if (LEDs.get(i).isOn()) {
				return false;
			}
		}
		return true;
	}
	
	/* SETS LEDS TO THE SAME AS THE GIVEN ARRAYLIST<LED> */
	public void copy(ArrayList<LED> LEDsToCopy) {
		for (int i = 0; i < LEDs.size(); i++) {
			try {
				this.LEDs.get(i).setColor(LEDsToCopy.get(i).getColor());
			} catch (Exception e) {
				System.err.println("ERROR: Something went wrong when trying to copy a bots LEDs");
				e.printStackTrace();
			}
		}
	}
	
}
