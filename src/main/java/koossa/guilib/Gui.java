package koossa.guilib;

import java.io.File;

import org.lwjgl.opengl.GL30;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

import koossa.guilib.elements.GuiElement;
import koossa.guilib.gui.GuiManager;
import koossa.guilib.text.Font;
import koossa.guilib.text.FontLibrary;
import koossa.texturepacker.AtlasSizes;
import koossa.texturepacker.TexturePacker;

public class Gui {
	
	private static float screenWidth, screenHeight;
	private static GuiManager guiManager;
	private static boolean initialised = false;
	
	public static void init(float screenWidth, float screenHeight) {
		Gui.screenHeight = screenHeight;
		Gui.screenWidth = screenWidth;
		guiManager = new GuiManager();
		Log.info(Gui.class, "Max number of layers of textures supported: " + GL30.glGetInteger(GL30.GL_MAX_ARRAY_TEXTURE_LAYERS));
		FontLibrary.addFont("DEFAULT_FONT", new Font("c.fnt"));
		initialised = true;
	}
	
	public static void initWithUnstitchedTextures(float screenWidth, float screenHeight) {
		if (!initialised) {
			Gui.init(screenWidth, screenHeight);
		}
		TexturePacker.setTargetTextureSize(AtlasSizes._1024);
		File atlasFolder = new File(Files.getCommonFolder(CommonFolders.Gui), "Atlases");
		TexturePacker.packTextures(new File(Files.getCommonFolder(CommonFolders.Gui), "UnstitchedTextures"), atlasFolder, "gui");
		guiManager.loadTextureAtlases(atlasFolder, "gui");
	}
	
	public static void dispose() {
		guiManager.dispose();
		Log.debug(Gui.class, "Disposing gui library");
		initialised = false;
	}
	
	public static void resize(int width, int height) {
		Gui.screenHeight = (float) height;
		Gui.screenWidth = (float) width;
		guiManager.resize(width, height);
	}
	
	public static void render() {
		guiManager.render();
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
	
	public static int hideGui(String id) {
		return guiManager.hide(id);
	}
	
	public static int toggleGui(String id) {
		return guiManager.toggleGui(id);
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
