package koossa.guilib.rendering;

import java.util.ArrayList;
import java.util.List;

import koossa.guilib.elements.GuiElement;
import koossa.guilib.elements.utils.ITextElement;
import koossa.guilib.gui.InternalGuiTextureManager;
import koossa.texturepacker.TextureInfo;

public class RenderUtils {

	private static List<Float> tempFloatList = new ArrayList<Float>();
	private static List<Integer> tempIntList = new ArrayList<Integer>();

	public static void updateRenderBatch(GuiElement parentElement, RenderBatch batch) {
		tempFloatList.clear();
		addVertices(parentElement, tempFloatList);
		batch.updateVertices(tempFloatList);

		tempIntList.clear();
		addIndices(batch, tempIntList);
		batch.updateIndices(tempIntList);

		tempFloatList.clear();
		addTexCoords(parentElement, tempFloatList);
		batch.updateTexCoords(tempFloatList);

		tempFloatList.clear();
		addColor(parentElement, tempFloatList);
		batch.updateColors(tempFloatList);
		
		tempFloatList.clear();
		addData(parentElement, tempFloatList);
		batch.updateData(tempFloatList);
	}

	private static void addData(GuiElement element, List<Float> dataList) {
		for (int i = 0; i < 4; i++) {
			dataList.add(0.0f);
		}
		if (element instanceof ITextElement) {
			dataList.addAll(((ITextElement) element).getDataList());
		}
		if (element.getChildren() != null) {
			element.getChildren().forEach(e -> {
				if (e.isInBounds()) addData(e, dataList);
			});
		}
	}

	private static void addColor(GuiElement element, List<Float> colorList) {
		for (int i = 0; i < 4; i++) {
			colorList.add(element.getBackgroundColor().x());
			colorList.add(element.getBackgroundColor().y());
			colorList.add(element.getBackgroundColor().z());
			colorList.add(element.getBackgroundColor().w());
		}
		if (element instanceof ITextElement) {
			colorList.addAll(((ITextElement) element).getTextColourList());
		}
		if (element.getChildren() != null) {
			element.getChildren().forEach(e -> {
				if (e.isInBounds()) addColor(e, colorList);
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
		if (element instanceof ITextElement) {
			verticesList.addAll(((ITextElement) element).getTextVertices());
		}
		if (element.getChildren() != null) {
			element.getChildren().forEach(e -> {
				if (e.isInBounds()) addVertices(e, verticesList);
			});
		}
	}

	private static void addTexCoords(GuiElement element, List<Float> texCoordList) {
		TextureInfo info = InternalGuiTextureManager.getTexInfo(element.getTextureName());
		float layer = (info == null) ? -1.0f : InternalGuiTextureManager.getAtlasLayer(info.getAtlasName());
		texCoordList.add((info == null) ? 0.0f : info.getTexCoordX1());
		texCoordList.add((info == null) ? 0.0f : info.getTexCoordY1());
		texCoordList.add(layer);
		texCoordList.add((info == null) ? 0.0f : info.getTexCoordX1());
		texCoordList.add((info == null) ? 0.0f : info.getTexCoordY2());
		texCoordList.add(layer);
		texCoordList.add((info == null) ? 0.0f : info.getTexCoordX2());
		texCoordList.add((info == null) ? 0.0f : info.getTexCoordY2());
		texCoordList.add(layer);
		texCoordList.add((info == null) ? 0.0f : info.getTexCoordX2());
		texCoordList.add((info == null) ? 0.0f : info.getTexCoordY1());
		texCoordList.add(layer);
		if (element instanceof ITextElement) {
			texCoordList.addAll(((ITextElement) element).getTextTextureCoords());
		}
		if (element.getChildren() != null) {
			element.getChildren().forEach(e -> {
				if (e.isInBounds()) addTexCoords(e, texCoordList);
			});
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
