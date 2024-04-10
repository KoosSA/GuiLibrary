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
	private List<float[]> verticesToRender = new ArrayList<float[]>();
	private List<float[]> textureCoordsToRender = new ArrayList<float[]>();
	private List<List<Float>> guiColours = new ArrayList<List<Float>>();
	private List<float[]> coloursToRender = new ArrayList<float[]>();

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
		updateRenderData();
	}

	public void hide(String id) {
		Element root = shownElements.getOrDefault(id, null);
		if (root == null) {
			return;
		}
		shownElements.remove(id);
		updateRenderData();
	}

	public void hideAll() {
		shownElements.clear();
		updateRenderData();
	}

	protected List<float[]> getVerticesToRender() {
		return verticesToRender;
	}
	
	protected List<float[]> getTextureCoordsToRender() {
		return textureCoordsToRender;
	}
	
	protected List<float[]> getColoursToRender() {
		return coloursToRender;
	}

	private void updateRenderData() {
		guiVerts.clear();
		verticesToRender.clear();
		textureCoordsToRender.clear();
		guiColours.clear();
		coloursToRender.clear();
		
		shownElements.forEach((id, elem) -> {
			List<Float> verts = new ArrayList<Float>();
			elem.addVertices(verts);
			guiVerts.add(verts);
			List<Float> colours = new ArrayList<Float>();
			elem.addColour(colours);
			guiColours.add(colours);
		});
		guiVerts.forEach(rt -> {
			float[] vertsArr = ArrayUtils.toPrimitive(rt.toArray(new Float[0]));
			verticesToRender.add(vertsArr);
			int numQuad = vertsArr.length / 12;
			float[] texCoordArr = new float[vertsArr.length];
			for (int i = 0; i < numQuad; i++) {
				texCoordArr[i*12] = 0;
				texCoordArr[i*12+1] = 1;
				texCoordArr[i*12+2] = 0;
				texCoordArr[i*12+3] = 0;
				texCoordArr[i*12+4] = 1;
				texCoordArr[i*12+5] = 1;
				texCoordArr[i*12+6] = 1;
				texCoordArr[i*12+7] = 1;
				texCoordArr[i*12+8] = 0;
				texCoordArr[i*12+9] = 0;
				texCoordArr[i*12+10] = 1;
				texCoordArr[i*12+11] = 0;
			}
			textureCoordsToRender.add(texCoordArr);
		});
		guiColours.forEach(col -> {
			float[] colArr = ArrayUtils.toPrimitive(col.toArray(new Float[0]));
			coloursToRender.add(colArr);
		});
	}

	public void resize(int width, int height) {
		rootElements.forEach((id, elem) -> {
			elem.autoLayout();
		});
		updateRenderData();
	}

}
