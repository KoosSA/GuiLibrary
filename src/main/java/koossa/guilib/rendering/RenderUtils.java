package koossa.guilib.rendering;

import java.util.ArrayList;
import java.util.List;

import koossa.guilib.elements.GuiElement;

public class RenderUtils {

	private static List<Float> tempFloatList = new ArrayList<Float>();
	private static List<Integer> tempIntList = new ArrayList<Integer>();

	public static void updateRenderBatch(GuiElement parentElement, RenderBatch batch) {
		tempFloatList.clear();
		addVertices(parentElement, tempFloatList);
//		parentElement.getChildren().forEach(elem -> {
//			addVertices(elem, tempFloatList);
//		});
		batch.updateVertices(tempFloatList);

		tempIntList.clear();
//		addIndices(parentElement.getChildren().size() + 1, tempIntList);
		addIndices(batch, tempIntList);
		batch.updateIndices(tempIntList);

		tempFloatList.clear();
		addTexCoords(parentElement, tempFloatList);
//		parentElement.getChildren().forEach(elem -> {
//			addTexCoords(parentElement, tempFloatList);
//		});
		batch.updateTexCoords(tempFloatList);

		tempFloatList.clear();
		addColor(parentElement, tempFloatList);
//		parentElement.getChildren().forEach(elem -> {
//			addColor(elem, tempFloatList);
//		});
		batch.updateColors(tempFloatList);
	}

	private static void addColor(GuiElement element, List<Float> colorList) {
		for (int i = 0; i < 4; i++) {
			colorList.add(element.getBackgroundColor().x());
			colorList.add(element.getBackgroundColor().y());
			colorList.add(element.getBackgroundColor().z());
			colorList.add(element.getBackgroundColor().w());
		}
		if (element.getChildren() != null) {
			element.getChildren().forEach(e -> {
				addColor(e, colorList);
			});
		}
	}

	private static void addVertices(GuiElement element, List<Float> verticesList) {
		verticesList.add(0.000f + element.getPosX());
		verticesList.add(0.000f + element.getPosY());
		verticesList.add(0.000f + element.getPosX());
		verticesList.add((float) element.getHeight() + element.getPosY());
		verticesList.add((float) element.getWidth() + element.getPosX());
		verticesList.add((float) element.getHeight() + element.getPosY());
		verticesList.add((float) element.getWidth() + element.getPosX());
		verticesList.add(0.000f + element.getPosY());
		if (element.getChildren() != null) {
			element.getChildren().forEach(e -> {
				addVertices(e, verticesList);
			});
		}
	}

	private static void addTexCoords(GuiElement element, List<Float> texCoordList) {
		texCoordList.add(0.000f);
		texCoordList.add(0.000f);
		texCoordList.add(0.000f);
		texCoordList.add(1.000f);
		texCoordList.add(1.000f);
		texCoordList.add(1.000f);
		texCoordList.add(1.000f);
		texCoordList.add(0.000f);
		if (element.getChildren() != null) {
			element.getChildren().forEach(e -> {
				addTexCoords(e, texCoordList);
			});
		}
	}

	private static void addIndices(int numElements, List<Integer> indicesList) {
		for (int i = 0; i < numElements; i++) {
			indicesList.add(0 + (i * 4));
			indicesList.add(1 + (i * 4));
			indicesList.add(2 + (i * 4));
			indicesList.add(0 + (i * 4));
			indicesList.add(2 + (i * 4));
			indicesList.add(3 + (i * 4));
		}
	}

	private static void addIndices(RenderBatch batch, List<Integer> indicesList) {
		for (int i = 0; i < (batch.getVertices().length / 8); i++) {
			indicesList.add(0 + (i * 4));
			indicesList.add(1 + (i * 4));
			indicesList.add(2 + (i * 4));
			indicesList.add(0 + (i * 4));
			indicesList.add(2 + (i * 4));
			indicesList.add(3 + (i * 4));
		}
	}

}
