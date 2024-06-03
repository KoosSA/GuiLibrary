package koossa.guilib;

import com.koossa.logger.Log;

import koossa.guilib.elements.GuiElement;
import koossa.guilib.gui.GuiManager;
import koossa.guilib.text.TextManager;

public class Gui {
	
	private static float screenWidth, screenHeight;
	private static GuiManager guiManager;
	
	public static void init(float screenWidth, float screenHeight) {
		Gui.screenHeight = screenHeight;
		Gui.screenWidth = screenWidth;
		guiManager = new GuiManager();
		TextManager.init();
	}
	
	public static void dispose() {
		TextManager.dispose();
		guiManager.dispose();
		Log.debug(Gui.class, "Disposing gui library");
	}
	
	public static void resize(int width, int height) {
		Gui.screenHeight = (float) height;
		Gui.screenWidth = (float) width;
		TextManager.onResize(width, height);
		guiManager.resize(width, height);
	}
	
	public static void render() {
		guiManager.render();
		TextManager.render();
	}
	
	public static float getScreenHeight() {
		return screenHeight;
	}
	
	public static float getScreenWidth() {
		return screenWidth;
	}
	
	public static void showGui(String id) {
		guiManager.show(id);
	}
	
	public static void hideGui(String id) {
		guiManager.hide(id);
	}
	
	public static void hideAllGui() {
		guiManager.hideAll();
	}
	
	public static void addGui(String id, GuiElement rootElement) {
		guiManager.addGui(id, rootElement);
	}
	
	public static void update(float delta) {
		guiManager.update(delta);
	}

}
