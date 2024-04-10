package koossa.guilib.utils;

public class Bounds {
	
	private float x, y, width, height;
	private float[] vertices = new float[12];
	private float[] texCoords = {
			0,1,
			0,0,
			1,1,
			1,1,
			0,0,
			1,0
	};
	
	public Bounds(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public void calculateVertices() {
		vertices[0] = x;
		vertices[1] = y;
		
		vertices[2] = x;
		vertices[3] = y - height;
		
		vertices[4] = x + width;
		vertices[5] = y;
		
		vertices[6] = x + width;
		vertices[7] = y;
		
		vertices[8] = x;
		vertices[9] = y - height;
		
		vertices[10] = x + width;
		vertices[11] = y - height;
	}
	
	public float[] getTexCoords() {
		return texCoords;
	}
	
	public float[] getVertices() {
		return vertices;
	}

}
