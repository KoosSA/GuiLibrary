package koossa.guilib.text;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.joml.Vector4f;

public class Text {

	String text;
	Font font;
	float currentLineWidth;
	float maxLineWidth = 600;
	float posX = 0;
	float posY = 0;
	float scale = 1;
	float rotZ = 0;
	float cursorPosX = 0;
	float cursorPosY = 0;
	Vector4f colour = new Vector4f(0, 0, 0, 1);
	float currentSize = 0;
	boolean multiline = true;
	
	private List<Float> verticesList = new ArrayList<Float>();
	private List<Float> texCoordList = new ArrayList<Float>();
	private byte[] bytes;

	float[] vertices;
	float[] texCoords;

	public Text(String text, Font font, float posX, float posY, float maxLineLength, float size) {
		this.text = text;
		this.font = font;
		this.posX = posX;
		this.posY = posY;
		this.maxLineWidth = maxLineLength;
		currentSize = size;
		scale = currentSize / font.getSize();
		bytes = text.getBytes();
		createQuads();
	}

//	public Text(String text, Font font, float posX, float posY, float maxLineLength, float rotZ, float scale) {
//		this(text, font, posX, posY, maxLineLength);
//		this.rotZ = rotZ;
//		this.scale = scale;
//	}
	
	public Text setSize(float size) {
		if (size == currentSize) {
			return this;
		}
		currentSize = size;
		scale = currentSize / font.getSize();
		createQuads();
		return this;
	}
	
	public Text setMultiline(boolean multiLine) {
		if (this.multiline != multiLine) {
			this.multiline = multiLine;
			createQuads();
		}
		return this;
	}
	
	public Text setPosition(float posX, float posY) {
		this.posX = posX;
		this.posY = posY;
		createQuads();
		return this;
	}
	
	private void createQuads() {
//		vertices = new float[12 * bytes.length];
//		texCoords = new float[12 * bytes.length];
		verticesList.clear();
		texCoordList.clear();
		cursorPosX = posX;
		cursorPosY = -posY;
		for (int index = 0; index < bytes.length; index++) {
			int b = bytes[index];
			Glyph glyph = font.getGlyph(b);
			if (cursorPosX + glyph.getX_Advance() * scale >= maxLineWidth + posX) {
				if (!multiline) {
					break;
				}
				cursorPosY -= ((font.getLineHeight() * scale) - (font.getPadding() * scale * 2));
				cursorPosX = posX;
			}

			generateVertices(index, glyph);
			generateTexCoords(index, glyph);

			if (!(cursorPosX == 0 && b == 32)) {
				cursorPosX += (glyph.getX_Advance() * scale - (font.getPadding() * scale * 2));
			}
		}
		
		vertices = ArrayUtils.toPrimitive(verticesList.toArray(new Float[0]));
		texCoords = ArrayUtils.toPrimitive(texCoordList.toArray(new Float[0]));
		
	}

	private void generateTexCoords(int index, Glyph glyph) {
		
		texCoordList.add((float) glyph.getX() / font.getTexWidth());
		texCoordList.add((font.getTexWidth() - (float) glyph.getY()) / font.getTexWidth());
		
		texCoordList.add(texCoordList.get(index * 12));
		texCoordList.add((font.getTexWidth() - ((float) glyph.getY() + (float) glyph.getHeight())) / font.getTexWidth());
		
		texCoordList.add(((float) glyph.getX() + (float) glyph.getWidth()) / font.getTexWidth());
		texCoordList.add(texCoordList.get(index * 12 + 1));
		
		texCoordList.add(texCoordList.get(index * 12 + 4));
		texCoordList.add(texCoordList.get(index * 12 + 1));
		
		texCoordList.add(texCoordList.get(index * 12 + 2));
		texCoordList.add(texCoordList.get(index * 12 + 3));
		
		texCoordList.add(texCoordList.get(index * 12 + 4));
		texCoordList.add(texCoordList.get(index * 12 + 3));
		
//		texCoords[index * 12 + 0] = (float) glyph.getX() / font.getTexWidth();
//		texCoords[index * 12 + 1] = (font.getTexWidth() - (float) glyph.getY()) / font.getTexWidth();
//
//		texCoords[index * 12 + 2] = texCoords[index * 12 + 0];
//		texCoords[index * 12 + 3] = (font.getTexWidth() - ((float) glyph.getY() + (float) glyph.getHeight())) / font.getTexWidth();
//
//		texCoords[index * 12 + 4] = ((float) glyph.getX() + (float) glyph.getWidth()) / font.getTexWidth();
//		texCoords[index * 12 + 5] = texCoords[index * 12 + 1];
//
//		texCoords[index * 12 + 6] = texCoords[index * 12 + 4];
//		texCoords[index * 12 + 7] = texCoords[index * 12 + 1];
//
//		texCoords[index * 12 + 8] = texCoords[index * 12 + 2];
//		texCoords[index * 12 + 9] = texCoords[index * 12 + 3];
//
//		texCoords[index * 12 + 10] = texCoords[index * 12 + 4];
//		texCoords[index * 12 + 11] = texCoords[index * 12 + 3];
	}

	private void generateVertices(int i, Glyph g) {
		
		verticesList.add(cursorPosX + g.getX_Offset() * scale);
		verticesList.add(cursorPosY - g.getY_Offset() * scale);
		
		verticesList.add(cursorPosX + g.getX_Offset() * scale);
		verticesList.add(cursorPosY - g.getHeight() * scale - g.getY_Offset() * scale);
		
		verticesList.add(verticesList.get(i * 12 + 0) + g.getWidth() * scale);
		verticesList.add(verticesList.get(i * 12 + 1));
		
		verticesList.add(verticesList.get(i * 12 + 4));
		verticesList.add(verticesList.get(i * 12 + 5));
		
		verticesList.add(verticesList.get(i * 12 + 2));
		verticesList.add(verticesList.get(i * 12 + 3));
		
		verticesList.add(verticesList.get(i * 12 + 4));
		verticesList.add(verticesList.get(i * 12 + 3));

//		vertices[i * 12 + 0] = cursorPosX + g.getX_Offset() * scale;
//		vertices[i * 12 + 1] = cursorPosY - g.getY_Offset() * scale;
//
//		vertices[i * 12 + 2] = cursorPosX + g.getX_Offset() * scale;
//		vertices[i * 12 + 3] = cursorPosY - g.getHeight() * scale - g.getY_Offset() * scale;
//
//		vertices[i * 12 + 4] = vertices[i * 12 + 0] + g.getWidth() * scale;
//		vertices[i * 12 + 5] = vertices[i * 12 + 1];
//
//		vertices[i * 12 + 6] = vertices[i * 12 + 4];
//		vertices[i * 12 + 7] = vertices[i * 12 + 5];
//
//		vertices[i * 12 + 8] = vertices[i * 12 + 2];
//		vertices[i * 12 + 9] = vertices[i * 12 + 3];
//
//		vertices[i * 12 + 10] = vertices[i * 12 + 4];
//		vertices[i * 12 + 11] = vertices[i * 12 + 3];

	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getTexCoords() {
		return texCoords;
	}

	public Text setColour(float r, float g, float b, float a) {
		colour.set(r, g, b, a);
		return this;
	}

	public Vector4f getColour() {
		return colour;
	}
	
	public float getSize() {
		return currentSize;
	}

}
