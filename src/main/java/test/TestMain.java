package test;

import org.joml.Random;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;
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
import koossa.guilib.elements.Label;
import koossa.guilib.elements.ScrollPanel;
import koossa.guilib.elements.utils.IGuiEvent;
import koossa.guilib.layout.Layouts;
import koossa.guilib.layout.SizeFormat;
import koossa.guilib.text.FontLibrary;
import koossa.guilib.text.Text;
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
		
		GLFW.glfwSetScrollCallback(winId, new GLFWScrollCallbackI() {
			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				Input.addScrollOffset((float) xoffset, (float) yoffset);
			}
		});

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
		Input.setKeyBinding("UP", GLFW.GLFW_KEY_UP);
		Input.setKeyBinding("DOWN", GLFW.GLFW_KEY_DOWN);
		Input.setMouseBinding("CLICK_PRIMARY", GLFW.GLFW_MOUSE_BUTTON_1);

		Gui.initWithUnstitchedTextures(800, 600);
		
		Text text = new Text("abcdefghijklmnopqrstuvwxyz . 0123456789 /*-+", FontLibrary.getFont("DEFAULT_FONT"), 100, 100, 250, 100)
				.setColour(0, 0, 0, 1);
		//TextManager.addTextToRender(text);
		
		
		IInputHandler ih = new IInputHandler() {
			@Override
			public void handleInput(InputManager input) {
				if (input.isFunctionKeyJustPressed("OPEN_INVENTORY")) {
					Gui.toggleGui("TestPanel");
				}
				if (input.isFunctionKeyDown("DOWN")) {
					text.setSize(text.getSize() - 1);
				}
				if (input.isFunctionKeyDown("UP")) {
					text.setSize(text.getSize() + 1);
				}
			}
		};
		Input.registerInputHandler("GUI_INPUT", ih);
		
		Random r = new Random();

		ScrollPanel base = new ScrollPanel(SizeFormat.ABSOLUTE, 230, 230, Layouts.FLOW_LAYOUT);
		base.setPosition(100, 100);
		base.setScrollSpeed(5);
		
		Label l1 = new Label(SizeFormat.ABSOLUTE, 210, 30, "Test label 1."); 
		l1.setBackgroundColor(0.7f, 0.5f, 0.5f, 0.5f);
		base.addChild(l1);
		l1.setTextSize(0);
		l1.setTextColour(0,1,0,1);
		
		GuiElement c1 = new GuiElement(SizeFormat.ABSOLUTE, 100, 100, Layouts.FLOW_LAYOUT);
		c1.setBackgroundColor(0, 1, 0, 1);
		
		base.addChild(c1);
		base.setBackgroundColor(1, 0, 0, 1);
		GuiElement c2 = new GuiElement(SizeFormat.ABSOLUTE, 100, 100, Layouts.FLOW_LAYOUT);
		c2.setBackgroundColor(0, 0, 1, 1);
		c2.setTextureName("circle.png");
		base.addChild(c2);
		Button c3 = new Button(SizeFormat.ABSOLUTE, 100, 100, Layouts.FLOW_LAYOUT);
		c3.setPosition(0, 0);
		c3.setOnInteract(new IGuiEvent() {
			@Override
			public void handleGuiEvent(GuiElement element) {
				c2.setBackgroundColor(r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat());
//				c2.setTextureName(Files.getFolder("Gui/UnstitchedTextures").list(new FilenameFilter() {
//					
//					@Override
//					public boolean accept(File dir, String name) {
//						if (name.endsWith(".png")) return true;
//						return false;
//					}
//				})[r.nextInt(4)]);
			}
		});
		c3.setBackgroundColor(0.5f, 0.5f, 0, 0.7f);
		base.addChild(c3);
		base.setPadding(10);
		base.setSpacing(10);
		GuiElement c4 = new GuiElement(SizeFormat.ABSOLUTE, 100, 100, Layouts.FLOW_LAYOUT);
		c4.setTextureName("cross.png");
		base.addChild(c4);
		GuiElement c5 = new GuiElement(SizeFormat.ABSOLUTE, 100, 100, Layouts.FLOW_LAYOUT);
		c5.setTextureName("square.png");
		base.addChild(c5);

		GuiElement c11 = new GuiElement(SizeFormat.RELATIVE, 0.4f, 0.4f, Layouts.FLOW_LAYOUT);
		c1.addChild(c11);
		GuiElement c12 = new GuiElement(SizeFormat.RELATIVE, 0.4f, 0.4f, Layouts.FLOW_LAYOUT);
		c1.addChild(c12);
		c12.setTextureName("triangle.png");
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
