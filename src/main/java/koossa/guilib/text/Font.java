package koossa.guilib.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.stb.STBImage;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

public class Font {
	
	private Map<Integer, Glyph> glyphs;
//	private int textureId;
	private float texWidth = 0;
	private float lineHeight = 0;
	private float padding = 0;
	private float size = 0;
	private String name;
	private String textureName;
	private static final Glyph DEFAULT_GLYPH = new Glyph(0, 0, 0, 0, 0, 0, 0);
	
	public Font(String fontName) {
		glyphs = new HashMap<Integer, Glyph>();
		name = fontName;
		textureName = fontName.split(".fnt")[0] + ".png";

		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(Files.getCommonFolder(CommonFolders.Fonts), fontName)));
			String line;
			try {
				while ( (line = reader.readLine()) != null) {
					parseLine(line);
				}
			} catch (IOException e) {
				Log.error(this, "Error while reading font file: " + fontName);
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			Log.error(this, "Font file not found: " + fontName);
			Log.error(this, e.getMessage());
			e.printStackTrace();
		}
		Log.debug(this, "Font loaded: " + fontName + " with texture: " + textureName);
	}
	
	public Map<Integer, Glyph> getGlyphs() {
		return glyphs;
	}
	
//	public int getTextureId() {
//		return textureId;
//	}
	
	public Glyph getGlyph(int id) {
		return glyphs.getOrDefault(id, DEFAULT_GLYPH);
	}

	private void parseLine(String line) {
		line = StringUtils.normalizeSpace(line);
		if (line.startsWith("info")) {
			String[] arr = line.split(" ");
			for (int i = 0; i < arr.length; i++) {
				if (arr[i].startsWith("padding=")) {
					padding = Float.parseFloat(arr[i].split("=")[1].split(",")[0]) ;
					continue;
				} else if (arr[i].startsWith("size=")) {
					size = Float.parseFloat(arr[i].split("=")[1]) ;
					continue;
				}
			}
		}
		else if (line.startsWith("common")) {
			String[] arr = line.split(" ");
			for (int i = 0; i < arr.length; i++) {
				if (arr[i].startsWith("scaleW=")) {
					texWidth = Float.parseFloat(arr[i].split("=")[1]) ;
					continue;
				} else if (arr[i].startsWith("lineHeight=")) {
					lineHeight = Float.parseFloat(arr[i].split("=")[1]) ;
					continue;
				}
			}
		} else if (line.startsWith("page")) {
			String[] arr = line.split(" ");
			String textureName = null;
			for (int i = 0; i < arr.length; i++) {
				if (arr[i].startsWith("file=")) {
					textureName = arr[i].split("\"")[1];
					continue;
				}
			}
			if (textureName != null) {
				loadTextureInfo(textureName);
			}
		} else if (line.startsWith("char ")) {
			String[] arr = line.split(" ");
			int[] val = new int[8];
			for (int i = 1; i < 9; i++) {
				val[i-1] = Integer.parseInt(arr[i].split("=")[1]);
			}
			Glyph glyph = new Glyph(val[1], val[2], val[3], val[4], val[7], val[5], val[6]);
			glyphs.put(val[0], glyph);
		}
	}

	private void loadTextureInfo(String textureName2) {
		int[] x = new int[1], y = new int[1], c = new int[1];
		STBImage.stbi_info(Files.getCommonFolderPath(CommonFolders.Fonts) + "/" + textureName, x, y, c);
		texWidth = x[0];
	}

//	private void loadTexture(String textureName) {
//		textureId = GL30.glGenTextures();
//		GL30.glActiveTexture(GL30.GL_TEXTURE0);
//		GL30.glBindTexture(GL30.GL_TEXTURE_2D, textureId);
//		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR_MIPMAP_LINEAR);
//		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
//		int[] x = new int[1], y = new int[1], c = new int[1];
//		STBImage.stbi_set_flip_vertically_on_load(true);
//		ByteBuffer buff = STBImage.stbi_load(Files.getCommonFolderPath(CommonFolders.Fonts) + "/" + textureName, x, y, c, STBImage.STBI_rgb_alpha);
//		GL30.glPixelStorei(GL30.GL_UNPACK_ALIGNMENT, 1);
//		GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, x[0], y[0], 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, buff);
//		GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);
//		STBImage.stbi_image_free(buff);
//		int result = GL30.GL_NO_ERROR;
//		while ((result = GL30.glGetError()) != GL30.GL_NO_ERROR) {
//			Log.error(this, "Opengl Error: " + result);
//			Log.error(this, "This error probably has nothing to do with the texture currently being loaded to opengl");
//		}
//		texWidth = x[0];
//	}
	
	public float getTexWidth() {
		return texWidth;
	}
	
	public float getLineHeight() {
		return lineHeight;
	}

//	public static void disposeAll() {
//		allFonts.forEach(f -> {
//			GL30.glDeleteTextures(f.getTextureId());
//		});
//	}
	
	public float getPadding() {
		return padding;
	}
	
	public float getSize() {
		return size;
	}
	
	public String getTextureName() {
		return textureName;
	}
	
	@Override
	public String toString() {
		return "Fontname=" + name + ", size=" + size + ", padding=" + padding + ", lineHeight=" + lineHeight;
	}
	

}
