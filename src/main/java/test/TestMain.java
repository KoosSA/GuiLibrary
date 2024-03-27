package test;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;

import com.koossa.logger.Log;

import koossa.guilib.Gui;
import koossa.guilib.text.Font;
import koossa.guilib.text.Text;
import koossa.guilib.text.TextManager;

public class TestMain {
	
	static long winId;

	public static void main(String[] args) {
		
		GLFW.glfwInit();
		GLFW.glfwDefaultWindowHints();
		winId = GLFW.glfwCreateWindow(800, 600, "TestWindow", 0, 0);
		GLFW.glfwMakeContextCurrent(winId);
		GL.createCapabilities();
		GL30.glViewport(0, 0, 800, 600);
		
		GLFW.glfwShowWindow(winId);
		
		Gui.init();
		TextManager.addTextToRender(new Text("TesT", new Font("arial.fnt")));

		startLoop();
		
		
	}
	
	
	private static void render() {
		GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
		GL30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		
		TextManager.render();
	}
	
	private static void startLoop() {
		System.out.println("LoopStart");
		long prevTime = System.nanoTime();
		float targetTime = 1.000f / 60;
		float deltaTime = 0;
		while (!GLFW.glfwWindowShouldClose(winId)) {
			deltaTime = (float) (System.nanoTime() - prevTime) / 1000000000.00000f;
			if (deltaTime >= targetTime) {
				GLFW.glfwSwapBuffers(winId);
				GLFW.glfwPollEvents();
				render();
				prevTime = System.nanoTime();
			} else {
				try {
					Thread.sleep((long) ((targetTime - deltaTime) * 1000));
				} catch (InterruptedException e) {
					Log.error(TestMain.class, "Thread failed to sleep.");
					e.printStackTrace();
				}
			}
		}
		Gui.stopGui();
	}

	

}
