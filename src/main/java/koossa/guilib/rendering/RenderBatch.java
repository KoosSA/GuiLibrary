package koossa.guilib.rendering;

import java.util.List;

public class RenderBatch {
	
	/** 2 floats **/
	private float[] vertices;
	/** 1 int **/
	private int[] indices;
	/** 2 floats **/
	private float[] textureCoords;
	/** 4 floats **/
	private float[] colors;
	
//	public void updateData(List<Float> verticesList) {
//		vertices = new float[verticesList.size()];
//		for (int i = 0; i < verticesList.size(); i++) {
//			vertices[i] = verticesList.get(i);
//		}
//	}
	
	public void updateVertices(List<Float> verticesList) {
		vertices = new float[verticesList.size()];
		for (int i = 0; i < verticesList.size(); i++) {
			vertices[i] = verticesList.get(i);
		}
	}

	public void updateIndices(List<Integer> indicesList) {
		indices = new int[indicesList.size()];
		for (int i = 0; i < indicesList.size(); i++) {
			indices[i] = indicesList.get(i);
		}
	}

	public void updateTexCoords(List<Float> texCoordList) {
		textureCoords = new float[texCoordList.size()];
		for (int i = 0; i < texCoordList.size(); i++) {
			textureCoords[i] = texCoordList.get(i);
		}
	}
	
	public void updateColors(List<Float> colorList) {
		colors = new float[colorList.size()];
		for (int i = 0; i < colorList.size(); i++) {
			colors[i] = colorList.get(i);
		}
	}

	public float[] getColors() {
		return colors;
	}
	
	public int[] getIndices() {
		return indices;
	}
	
	public float[] getTextureCoords() {
		return textureCoords;
	}
	
	public float[] getVertices() {
		return vertices;
	}
}
