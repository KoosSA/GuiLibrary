package koossa.guilib.gui;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL45;
import org.lwjgl.stb.STBImage;

import com.koossa.logger.Log;

import koossa.texturepacker.AtlasData;
import koossa.texturepacker.TextureInfo;

public class InternalGuiTextureManager {

	private static Map<String, Integer> atlasLayer = new HashMap<String, Integer>();
	private static AtlasData data = new AtlasData();
	private int texId = 0;

	private FilenameFilter jsonFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			if (name.toLowerCase().endsWith(".json"))
				return true;
			return false;
		}
	};
	private FilenameFilter pngFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			if (name.toLowerCase().endsWith(".png"))
				return true;
			return false;
		}
	};
	
	protected void dispose() {
		GL30.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, 0);
		GL30.glDeleteTextures(texId);
		texId = 0;
	}

	protected int loadTextureAtlasses(File atlasFolder, String atlasPrefix) {
		String[] dataFiles = atlasFolder.list(jsonFilter);
		String[] imgFiles = atlasFolder.list(pngFilter);
		data = data.loadFrom(AtlasData.class, atlasFolder, dataFiles[0]);
		return loadTexturesToGL(atlasFolder.getPath(), imgFiles);
	}
	
	protected AtlasData getData() {
		return data;
	}

	private int loadTexturesToGL(String folder, String[] imgFiles) {
		texId = GL30.glGenTextures();
		GL30.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, texId);
		
		GL45.glTexStorage3D(GL30.GL_TEXTURE_2D_ARRAY, 1, GL30.GL_RGBA8, 1024, 1024, imgFiles.length);
		
		int[] w = new int[1], h = new int[1], c = new int[1];
		for (int i = 0; i < imgFiles.length; i++) {
			String name = folder + "/" + imgFiles[i];
			atlasLayer.put(imgFiles[i], i);
			STBImage.stbi_set_flip_vertically_on_load(true);
			ByteBuffer pixels = STBImage.stbi_load(name, w, h, c, STBImage.STBI_rgb_alpha);
			GL45.glTexSubImage3D(GL30.GL_TEXTURE_2D_ARRAY, 0, 0, 0, i, 1024, 1024, imgFiles.length, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, pixels);
			STBImage.stbi_image_free(pixels);
		}
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_NEAREST_MIPMAP_NEAREST);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_NEAREST);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL30.GL_TEXTURE_WRAP_S, GL30.GL_REPEAT);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL30.GL_TEXTURE_WRAP_T, GL30.GL_REPEAT);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, 0);
		int result = GL30.GL_NO_ERROR;
		while ((result = GL30.glGetError()) != GL30.GL_NO_ERROR) {
			Log.error(this, "Opengl Error: " + result);
			Log.error(this, "This error probably has nothing to do with the texture currently being loaded to opengl but this was the last action performed before checking for errors");
		}
		return texId;
	}
	
	public static int getAtlasLayer(String atlasName) {
		return atlasLayer.getOrDefault(atlasName, -1);
	}
	
	public static TextureInfo getTexInfo(String textureName) {
		if (textureName == null) {
			return null;
		}
		return data.getTextureInfo(textureName);
	}

}
