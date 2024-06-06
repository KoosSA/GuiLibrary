package test;

import org.joml.Random;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.filesystem.RootFileLocation;
import com.koossa.logger.Log;
import com.koossa.savelib.SaveSystem;

import koossa.guilib.Gui;
import koossa.guilib.elements.Button;
import koossa.guilib.elements.GuiElement;
import koossa.guilib.elements.utils.IGuiEvent;
import koossa.guilib.layout.FlowLayout;
import koossa.guilib.layout.SizeFormat;
import koossa.guilib.text.Font;
import koossa.guilib.text.FontLibrary;
import koossa.inputlib.IInputHandler;
import koossa.inputlib.Input;
import koossa.inputlib.InputManager;

public class TestMain {

	static long winId;

	public static void main(String[] args) {
		Files.init("Resources", RootFileLocation.LOCAL);
		Log.init(Files.getCommonFolder(CommonFolders.Logs), true);
		SaveSystem.init(Files.getFolder("Saves"), Files.getFolder("Data"));
		GLFW.glfwInit();
		GLFW.glfwDefaultWindowHints();
		winId = GLFW.glfwCreateWindow(800, 600, "TestWindow", 0, 0);
		GLFW.glfwMakeContextCurrent(winId);
		GL.createCapabilities();
		GL30.glViewport(0, 0, 800, 600);

		GLFW.glfwShowWindow(winId);

		GLFW.glfwSetKeyCallback(winId, new GLFWKeyCallbackI() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (action == GLFW.GLFW_PRESS)
					Input.addKeyPressEvent(key);
				if (action == GLFW.GLFW_RELEASE)
					Input.addKeyReleasedEvent(key);
			}
		});

		GLFW.glfwSetCursorPosCallback(winId, new GLFWCursorPosCallbackI() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				Input.addMouseMovementEvent((float) xpos, (float) ypos);
			}
		});
		GLFW.glfwSetMouseButtonCallback(winId, new GLFWMouseButtonCallbackI() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				if (action == GLFW.GLFW_PRESS)
					Input.addMouseButtonPressedEvent(button);
				if (action == GLFW.GLFW_RELEASE)
					Input.addMouseButtonReleasedEvent(button);
			}
		});

		Input.init(30);
		Input.registerNewInputManger("GUI_INPUT");
		Input.activateInputManager("GUI_INPUT");
		Input.setKeyBinding("OPEN_INVENTORY", GLFW.GLFW_KEY_I);
		Input.setMouseBinding("CLICK_PRIMARY", GLFW.GLFW_MOUSE_BUTTON_1);

		Gui.init(800, 600);
		FontLibrary.addFont("DEFAULT_FONT", new Font("a.fnt"));
//		TextManager.addTextToRender(
//				new Text("abcdefghijklmnopqrstuvwxyz . 0123456789 /*-+", FontLibrary.getFont("DEFAULT_FONT"), 0, 0, 250, 0, 0.5f)
//						.setColour(0, 0, 0, 1));

		IInputHandler ih = new IInputHandler() {
			@Override
			public void handleInput(InputManager input) {
				if (input.isFunctionKeyJustPressed("OPEN_INVENTORY")) {
					Gui.toggleGui("TestPanel");
				}
			}
		};
		Input.registerInputHandler("GUI_INPUT", ih);
		
		Random r = new Random();

		GuiElement base = new GuiElement(SizeFormat.ABSOLUTE, 230, 230, new FlowLayout());
		
		GuiElement c1 = new GuiElement(SizeFormat.ABSOLUTE, 100, 100, new FlowLayout());
		c1.setBackgroundColor(0, 1, 0, 1);
		base.addChild(c1);
		base.setBackgroundColor(1, 0, 0, 1);
		GuiElement c2 = new GuiElement(SizeFormat.ABSOLUTE, 100, 100, new FlowLayout());
		c2.setBackgroundColor(0, 0, 1, 1);
		base.addChild(c2);
		Button c3 = new Button(SizeFormat.ABSOLUTE, 100, 100, new FlowLayout());
		c3.setOnInteract(new IGuiEvent() {
			@Override
			public void handleGuiEvent(GuiElement element) {
				element.setBackgroundColor(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1);
			}
		});
		c3.setBackgroundColor(0.5f, 0.5f, 0, 0.7f);
		base.addChild(c3);
		base.setPadding(10);
		base.setSpacing(10);

		GuiElement c11 = new GuiElement(SizeFormat.RELATIVE, 0.4f, 0.4f, new FlowLayout());
		c1.addChild(c11);
		GuiElement c12 = new GuiElement(SizeFormat.RELATIVE, 0.4f, 0.4f, new FlowLayout());
		c1.addChild(c12);
		c12.setBackgroundColor(0, 0, 0, 1);
		c1.setPadding(10);
		c1.setSpacing(10);

		Gui.addGui("TestPanel", base);
		Gui.showGui("TestPanel");

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
		long prevTime = System.nanoTime();
		float targetTime = 1.000f / 60;
		float deltaTime = 0;
		while (!GLFW.glfwWindowShouldClose(winId)) {
			deltaTime = (float) (System.nanoTime() - prevTime) / 1000000000.00000f;
			if (deltaTime >= targetTime) {
				GLFW.glfwPollEvents();
				Gui.update(deltaTime);
				render();
				GLFW.glfwSwapBuffers(winId);
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
		Input.stop();
		Log.disposeAll();
	}

}
