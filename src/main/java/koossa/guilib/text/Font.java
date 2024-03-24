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
		System.out.println(glyphs);
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
			Glyph glyph = new Glyph(val[1], val[2], val[3], val[4], val[5], val[6], val[7]);
			glyphs.put(val[0], glyph);
		}
	}

}
