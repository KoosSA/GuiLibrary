package koossa.guilib.text;

public class Glyph {
	
	private final int x, y, width, height, x_Advance, x_Offset, y_Offset;

	public Glyph(int x, int y, int width, int height, int x_Advance, int x_Offset, int y_Offset) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.x_Advance = x_Advance;
		this.x_Offset = x_Offset;
		this.y_Offset = y_Offset;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getX_Advance() {
		return x_Advance;
	}

	public int getX_Offset() {
		return x_Offset;
	}

	public int getY_Offset() {
		return y_Offset;
	}
	
	@Override
	public String toString() {
		return "{x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", Xoffset=" + x_Offset + ", yOffset=" + y_Offset + ", xAdvance=" + x_Advance + "}";
	}
}
