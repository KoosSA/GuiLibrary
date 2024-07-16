package koossa.guilib.text;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;

import koossa.guilib.gui.InternalGuiTextureManager;
import koossa.texturepacker.TextureInfo;

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
	private List<Float> colourList = new ArrayList<Float>();
	private List<Float> dataList = new ArrayList<Float>();
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
	
	public void setText(String text) {
		this.text = text;
		bytes = text.getBytes();
		createQuads();
	}
	
	public void setTextBytes(byte[] bytes) {
		String s = new String(bytes);
		setText(s);
	}

	private void createQuads() {
		verticesList.clear();
		texCoordList.clear();
		colourList.clear();
		dataList.clear();
		
		cursorPosX = posX;
		cursorPosY = posY;
		for (int index = 0; index < bytes.length; index++) {
			int b = bytes[index];
			Glyph glyph = font.getGlyph(b);
			if (cursorPosX + glyph.getX_Advance() * scale >= maxLineWidth + posX) {
				if (!multiline) {
					break;
				}
				cursorPosY -= ((font.getLineHeight() * scale) + (font.getPadding() * scale * 2));
				cursorPosX = posX;
			}

			generateVertices(index, glyph);
			generateTexCoords(index, glyph);

			if (!(cursorPosX == 0 && b == 32)) {
				cursorPosX += (glyph.getX_Advance() * scale - (font.getPadding() * scale * 2));
			}
		}
	}

	private void generateColoursAndData() {
		colourList.add(colour.x);
		colourList.add(colour.y);
		colourList.add(colour.z);
		colourList.add(colour.w);
		dataList.add(1.0f);
	}

	private void generateTexCoords(int index, Glyph glyph) {
		TextureInfo info = InternalGuiTextureManager.getTexInfo(font.getTextureName());
		float layer = (info == null) ? -1.0f : InternalGuiTextureManager.getAtlasLayer(info.getAtlasName());
		
		texCoordList.add((float) glyph.getX() / font.getTexWidth());
		texCoordList.add((font.getTexWidth() - (float) glyph.getY()) / font.getTexWidth());
		texCoordList.add(layer);
		generateColoursAndData();
		
		texCoordList.add((float) glyph.getX() / font.getTexWidth());
		texCoordList.add((font.getTexWidth() - ((float) glyph.getY() + (float) glyph.getHeight())) / font.getTexWidth());
		texCoordList.add(layer);
		generateColoursAndData();
		
		texCoordList.add(((float) glyph.getX() + (float) glyph.getWidth()) / font.getTexWidth());
		texCoordList.add((font.getTexWidth() - ((float) glyph.getY() + (float) glyph.getHeight())) / font.getTexWidth());
		texCoordList.add(layer);
		generateColoursAndData();
		
		texCoordList.add(((float) glyph.getX() + (float) glyph.getWidth()) / font.getTexWidth());
		texCoordList.add((font.getTexWidth() - (float) glyph.getY()) / font.getTexWidth());
		texCoordList.add(layer);
		generateColoursAndData();
	}

	private void generateVertices(int i, Glyph g) {
		verticesList.add(cursorPosX - g.getX_Offset() * scale);
		verticesList.add(cursorPosY + g.getY_Offset() * scale);
		
		verticesList.add(cursorPosX - g.getX_Offset() * scale);
		verticesList.add(cursorPosY + g.getHeight() * scale + g.getY_Offset() * scale);
		
		verticesList.add(cursorPosX + g.getX_Offset() * scale + g.getWidth() * scale);
		verticesList.add(cursorPosY + g.getHeight() * scale + g.getY_Offset() * scale);
		
		verticesList.add(cursorPosX + g.getX_Offset() * scale + g.getWidth() * scale);
		verticesList.add(cursorPosY + g.getY_Offset() * scale);
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

	public List<Float> getVerticesList() {
		return verticesList;
	}

	public List<Float> getTexCoordList() {
		return texCoordList;
	}
	
	public Font getFont() {
		return font;
	}

	public List<Float> getColourList() {
		return colourList;
	}

	public List<Float> getDataList() {
		return dataList;
		
	}

}
