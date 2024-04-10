package koossa.guilib.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.koossa.logger.Log;

import koossa.guilib.elements.Element;

public class GuiManager {
	
	private Map<String, Element> rootElements = new HashMap<String, Element>();
	private Map<String, Element> shownElements = new HashMap<String, Element>();
	private List<List<Float>> guiVerts = new ArrayList<List<Float>>();
	
	
	public void addGui(String id, Element rootElement) {
		rootElements.putIfAbsent(id, rootElement);
	}
	
	public void show(String id) {
		Element root = rootElements.getOrDefault(id, null);
		if (root == null) {
			Log.error(GuiManager.class, "Gui element does not exist when attempting to show: " + id);
			return;
		}
		shownElements.putIfAbsent(id, root);
		updateVerticesList();
	}

	public void hide(String id) {
		Element root = shownElements.getOrDefault(id, null);
		if (root == null) {
			return;
		}
		shownElements.remove(id);
		updateVerticesList();
	}

	public void hideAll() {
		shownElements.clear();
		updateVerticesList();
	}
	
	protected List<float[]> getVerticesToRender() {
		List<float[]> l = new ArrayList<float[]>();
		guiVerts.forEach(rt -> {
			float[] arr = ArrayUtils.toPrimitive(rt.toArray(new Float[0]));
			l.add(arr);
		});
		return l;
	}
	
	private void updateVerticesList() {
		guiVerts.clear();
		shownElements.forEach((id, elem) -> {
			List<Float> verts = new ArrayList<Float>();
			elem.addVertices(verts);
			guiVerts.add(verts);
		});
	}

	public void resize(int width, int height) {
		rootElements.forEach((id,elem) -> { elem.autoLayout(); });
		updateVerticesList();
	}

}
