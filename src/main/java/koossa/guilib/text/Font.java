package koossa.guilib.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.koossa.logger.Log;

public class Font {
	
	private Map<Integer, Glyph> glyphs;
	private int textureId;
	private static final Glyph DEFAULT_GLYPH = new Glyph(0, 0, 0, 0, 0, 0, 0);
	
	public Font(String fontName) {
		glyphs = new HashMap<Integer, Glyph>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fontName)));
		String line;
		try {
			while ( (line = reader.readLine()) != null) {
				parseLine(line);
			}
		} catch (IOException e) {
			Log.error(this, "File not found in internal system: " + fontName);
			e.printStackTrace();
		}
	}
	
	public Map<Integer, Glyph> getGlyphs() {
		return glyphs;
	}
	
	public int getTextureId() {
		return textureId;
	}
	
	public Glyph getGlyph(int id) {
		return glyphs.getOrDefault(id, DEFAULT_GLYPH);
	}

	private void parseLine(String line) {
		line = StringUtils.normalizeSpace(line);
		if (line.startsWith("page")) {
			String[] arr = line.split(" ");
			String textureName = null;
			for (int i = 0; i < arr.length; i++) {
				if (arr[i].startsWith("file=")) {
					textureName = arr[i].split("\"")[1];
					continue;
				}
			}
			if (textureName != null) {
				//System.out.println(textureName);
				//TODO Create texture here
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

}
