package koossa.guilib.text;

import java.util.HashMap;
import java.util.Map;

import com.koossa.logger.Log;

public class FontLibrary {
	
	private static Map<String, Font> fonts = new HashMap<String, Font>();
	
	public static Font getFont(String font) {
		if (fonts.containsKey(font)) {
			return fonts.get(font);
		} else {
			Log.error(FontLibrary.class, "Non existing font selected: " + font);
		}
		return null;
	}
	
	public static void addFont(String id, Font font) {
		fonts.putIfAbsent(id, font);
	}

}
