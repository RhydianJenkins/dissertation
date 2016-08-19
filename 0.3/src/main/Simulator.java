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

	public void tick() { gui.tick(); }
	public void render() { gui.render(); }
	
	public synchronized void start() {
		//System.out.println("START");
		running = true;
		new Thread(this).start();
	}

	public synchronized void stop() { running = false; }
	
	public void run() {
		long lastTimeNs = System.nanoTime();
		long lastTimeMs = System.currentTimeMillis();
		long now = lastTimeNs;
		double renderIfMoreThan1 = 0;
		double desiredTicks = 24;
		double nsPerTick = 1000000000D / desiredTicks;
		int tickNo = 0, frameNo = 0;
		
		while (running) {
			now = System.nanoTime();
			renderIfMoreThan1 += (now - lastTimeNs) / nsPerTick;;
			lastTimeNs = now;
			boolean shouldRender = true;

			while (renderIfMoreThan1 >= 1) {
				tickNo++;
				tick();
				renderIfMoreThan1 -= 1;
				shouldRender = true;
			}
			
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (shouldRender) {
				render();
				frameNo++;
			}

			if (System.currentTimeMillis() - lastTimeMs >= 1000) {
				gui.setTitle(tickNo + " ticks, " + tickNo + " FPS");
				lastTimeMs += 1000;
				frameNo = 0;
				tickNo = 0;
			}
		}
		
		System.exit(1);
	}
	
	public static void main(String[] args) {
		new Simulator().start();
	}

}
