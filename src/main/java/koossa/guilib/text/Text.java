package koossa.guilib.text;

import org.joml.Math;
import org.joml.Matrix4f;
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
	private static Matrix4f transformationMatrix = new Matrix4f();

	float[] vertices;
	float[] texCoords;

	public Text(String text, Font font, float posX, float posY, float maxLineLength) {
		this.text = text;
		this.font = font;
		this.posX = posX;
		this.posY = posY;
		this.maxLineWidth = maxLineLength;
		createQuads();
		updateTransformation();
	}

	public Text(String text, Font font, float posX, float posY, float maxLineLength, float rotZ, float scale) {
		this(text, font, posX, posY, maxLineLength);
		this.rotZ = rotZ;
		this.scale = scale;
		updateTransformation();
	}

	private void updateTransformation() {
		transformationMatrix.identity();
		transformationMatrix.translate(posX, posY, 0);
		transformationMatrix.rotateZ(Math.toRadians(rotZ));
		transformationMatrix.scale(scale);
	}

	private void createQuads() {
		byte[] bytes = text.getBytes();
		vertices = new float[12 * bytes.length];
		texCoords = new float[12 * bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			int b = bytes[i];
			Glyph g = font.getGlyph(b);

			if (cursorPosX + g.getX_Advance() >= maxLineWidth) {
				cursorPosY -= 52;
				cursorPosX = 0;
			}

			generateVertices(i, g);
			generateTexCoords(i, g);

			if (!(cursorPosX == 0 && b == 32))
				cursorPosX += g.getX_Advance();
		}

	}

	private void generateTexCoords(int i, Glyph g) {
		texCoords[i * 12 + 0] = (float) g.getX() / font.getTexWidth();
		texCoords[i * 12 + 1] = (font.getTexWidth() - (float) g.getY()) / font.getTexWidth();

		texCoords[i * 12 + 2] = texCoords[i * 12 + 0];
		texCoords[i * 12 + 3] = (font.getTexWidth() - ((float) g.getY() + (float) g.getHeight())) / font.getTexWidth();

		texCoords[i * 12 + 4] = ((float) g.getX() + (float) g.getWidth()) / font.getTexWidth();
		texCoords[i * 12 + 5] = texCoords[i * 12 + 1];

		texCoords[i * 12 + 6] = texCoords[i * 12 + 4];
		texCoords[i * 12 + 7] = texCoords[i * 12 + 1];

		texCoords[i * 12 + 8] = texCoords[i * 12 + 2];
		texCoords[i * 12 + 9] = texCoords[i * 12 + 3];

		texCoords[i * 12 + 10] = texCoords[i * 12 + 4];
		texCoords[i * 12 + 11] = texCoords[i * 12 + 3];
	}

	private void generateVertices(int i, Glyph g) {
//		System.out.println(g.getY_Offset());
		vertices[i * 12 + 0] = cursorPosX + g.getX_Offset();
		vertices[i * 12 + 1] = cursorPosY - g.getY_Offset();

		vertices[i * 12 + 2] = cursorPosX + g.getX_Offset();
		vertices[i * 12 + 3] = cursorPosY - g.getHeight() - g.getY_Offset();

		vertices[i * 12 + 4] = vertices[i * 12 + 0] + g.getWidth();
		vertices[i * 12 + 5] = vertices[i * 12 + 1];

		vertices[i * 12 + 6] = vertices[i * 12 + 4];
		vertices[i * 12 + 7] = vertices[i * 12 + 5];

		vertices[i * 12 + 8] = vertices[i * 12 + 2];
		vertices[i * 12 + 9] = vertices[i * 12 + 3];

		vertices[i * 12 + 10] = vertices[i * 12 + 4];
		vertices[i * 12 + 11] = vertices[i * 12 + 3];

	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getTexCoords() {
		return texCoords;
	}

	public Matrix4f getTransformationMatrix() {
		return transformationMatrix;
	}

	public Text setColour(float r, float g, float b, float a) {
		colour.set(r, g, b, a);
		return this;
	}

	public Vector4f getColour() {
		return colour;
	}

}
