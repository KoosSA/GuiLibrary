package koossa.guilib;

import com.koossa.logger.Log;

import koossa.guilib.text.TextManager;

public class Gui extends Thread {
	
	private static boolean running = false;
	private static float updatesPerSecond = 30.000f;
	private static float trueDeltaTime = 0;
	
	public static void init() {
		Gui gui = new Gui();
		gui.setName("guiLibrary");
		TextManager.init();
		gui.start();
	}
	
	public static float stopGui() {
		running = false;
		TextManager.dispose();
		return trueDeltaTime;
	}
	
	@Override
	public void run() {
		Log.debug(this, "Starting gui library");
		running = true;
		startLoop();
		dispose();
		Log.debug(this, "Gui library stopped");
	}
	
	private void dispose() {
		Log.debug(this, "Disposing gui library");
		
		running = false;
	}

	private void startLoop() {
		long prevTime = System.nanoTime();
		float targetTime = 1.000f / updatesPerSecond;
		float deltaTime = 0;
		while (running) {
			deltaTime = (float) (System.nanoTime() - prevTime) / 1000000000.00000f;
			if (deltaTime >= targetTime) {
				trueDeltaTime = deltaTime;
				prevTime = System.nanoTime();
			} else {
				try {
					Thread.sleep((long) ((targetTime - deltaTime) * 1000));
				} catch (InterruptedException e) {
					Log.error(this, "Thread failed to sleep.");
					e.printStackTrace();
				}
			}
		}
	}

}
