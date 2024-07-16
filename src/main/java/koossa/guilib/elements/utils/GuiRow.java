package koossa.guilib.elements.utils;

public class GuiRow {

	private int x = 0;
	private int y = 0;
	private int height = 0;
	
	public GuiRow(int x, int y, int height) {
		this.x = x;
		this.y = y;
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	@Override
	public String toString() {
		return "( Y:" + getY() + " Height:" + getHeight() +" )";
	}

}
