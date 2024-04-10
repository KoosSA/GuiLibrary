package test;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;

import com.koossa.logger.Log;

import koossa.guilib.Gui;
import koossa.guilib.elements.Panel;
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
		
		Gui.init(800, 600);
		TextManager.addTextToRender(new Text("abcdefghijklmnopqrstuvwxyz . 0123456789 /*-+", new Font("a.fnt"), 0, 0, 250,0,0.5f).setColour(0,0,0,1));
		Gui.addGui("TestPanel", new Panel(0f, 0f, 0.2f, 0.2f, true));
		Gui.showGui("TestPanel");
		Gui.addGui("TestPanel2", new Panel(0.3f, 0.3f, 0.2f, 0.2f, true));
		Gui.showGui("TestPanel2");
		
		GLFW.glfwSetWindowSizeCallback(winId, new GLFWWindowSizeCallbackI() {
			
			@Override
			public void invoke(long window, int width, int height) {
				GL30.glViewport(0, 0, width, height);
				Gui.resize(width, height);
			}
		});
		
		startLoop();
		
		
	}
	
	
	private static void render() {
		GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
		GL30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		
		Gui.render();
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
		Gui.dispose();
	}

	

}
