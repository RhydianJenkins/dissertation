/*
 * Main class. Responsible for running the simulator. Contains the engine that 
 * controls the ticks and renders of the overall system.
 */

package main;

import java.util.ArrayList;

import gfx.GUIManager;

public class Simulator implements Runnable {
	
	public ArrayList<Bot> bots = new ArrayList<Bot>();
	public GUIManager gui = new GUIManager(this, bots);
	public boolean running = false;
	
	public Simulator() {}

	public void tick() {
		gui.tick();
	}
	
	public void render() {
		gui.render();
	}
	
	public synchronized void start() {
		//System.out.println("START");
		running = true;
		new Thread(this).start();
	}

	public synchronized void stop() {
		//System.out.println("STOP");
		running = false;
		
	}
	
	// CHANGE THIS RUN METHOD, PLAGERISED
	public void run() {
		long lastTime = System.nanoTime();
		long lastTimer = System.currentTimeMillis();
		double desiredTickrate = 60;
		double nsPerTick = 1000000000D / desiredTickrate;
		double delta = 0;
		int ticks = 0;
		int frames = 0;
		
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;

			while (delta >= 1) {
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}
			
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				gui.setTitle(ticks + " ticks, " + frames + " FPS");
				frames = 0;
				ticks = 0;
			}
		}
		
		System.exit(1);
	}
	
	public static void main(String[] args) {
		new Simulator().start();
	}

}
