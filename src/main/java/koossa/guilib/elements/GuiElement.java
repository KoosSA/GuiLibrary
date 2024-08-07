package koossa.guilib.elements;

import java.util.ArrayList;
import java.util.List;

import org.joml.Math;
import org.joml.Vector4f;

import koossa.guilib.Gui;
import koossa.guilib.elements.utils.GuiRow;
import koossa.guilib.layout.ILayout;
import koossa.guilib.layout.Layouts;
import koossa.guilib.layout.SizeFormat;

public class GuiElement {
	
	protected ILayout layout;
	private int width, height, posX, posY;
	private float relativeWidth, relativeHeight;
	private GuiElement parent;
	private List<GuiElement> children = new ArrayList<GuiElement>();
	private Vector4f backgroundColor;
	private int padding = 0, spacing = 0;
	private SizeFormat sizeFormat = SizeFormat.RELATIVE;
	private boolean dirty = false;
	private String textureName;
	protected int childYOffset = 0;
	private List<GuiRow> rows = new ArrayList<GuiRow>();
	private List<GuiRow> rowsInBounds = new ArrayList<GuiRow>();
	
	public GuiElement(SizeFormat sizeFormat, float width, float height, Layouts layout) {
		this.layout = layout.getValue();
		this.sizeFormat = sizeFormat;
		this.relativeHeight = Math.clamp(0, 1, height);
		this.relativeWidth = Math.clamp(0, 1, width);
		this.width = (int) ((sizeFormat == SizeFormat.RELATIVE) ? (Gui.getScreenWidth() * relativeWidth) : width);
		this.height = (int) ((sizeFormat == SizeFormat.RELATIVE) ? (Gui.getScreenHeight() * relativeHeight) : height);
		this.backgroundColor = new Vector4f(1);
	}
	
	public void addChild(GuiElement child) {
		child.setParent(this);
		children.add(child);
		child.width = (int) ((child.sizeFormat == SizeFormat.RELATIVE) ? (width * child.relativeWidth) : child.width);
		child.height = (int) ((child.sizeFormat == SizeFormat.RELATIVE) ? (height * child.relativeHeight) : child.height);
//		child.width = (int) (width * child.relativeWidth);
//		child.height = (int) (height * child.relativeHeight);
		recalculateLayout();
	}
	
	public void update() {
		
	}
	
	public void onResize() {
		if (parent == null)	{
			this.width = (int) ((sizeFormat == SizeFormat.RELATIVE) ? (Gui.getScreenWidth() * relativeWidth) : width);
			this.height = (int) ((sizeFormat == SizeFormat.RELATIVE) ? (Gui.getScreenHeight() * relativeHeight) : height);
		} else {
			this.width = (int) ((sizeFormat == SizeFormat.RELATIVE) ? (parent.getWidth() * relativeWidth) : width);
			this.height = (int) ((sizeFormat == SizeFormat.RELATIVE) ? (parent.getHeight() * relativeHeight) : height);
		}
//		if (parent == null)	{
//			this.width = (int) (Gui.getScreenWidth() * relativeWidth);
//			this.height = (int) (Gui.getScreenHeight() * relativeHeight);
//		} else {
//			this.width = (int) (parent.getWidth() * relativeWidth);
//			this.height = (int) (parent.getHeight() * relativeHeight);
//		}
		children.forEach(c -> c.onResize());
		recalculateLayout();
	}

	public boolean isInBounds() {
		if (parent == null) {
			return true;
		}
		if (posX >= parent.posX && posY >= parent.posY + parent.padding && posX+width <= parent.posX+parent.width && posY+height <= parent.posY+parent.height-parent.padding) {
			return true;
		}
		return false;
	}
	
	private void updateRows() {
		if (children.size() <= 0) {
			return;
		}
		rows.clear();
		int prevY = -999, prevHeight = -999;
		int currentRowIndex = -1;
		for (int i = 0; i < children.size(); i++) {
			GuiElement child = children.get(i);
			if (child.getPosY() > prevY) {
				GuiRow row = new GuiRow(child.getPosX(), child.getPosY(), child.getHeight());
				rows.add(row);
				currentRowIndex++;
				prevY = child.getPosY();
				prevHeight = child.getHeight();
			} else {
				GuiRow row = rows.get(currentRowIndex);
				row.setHeight(Math.max(prevHeight, child.getHeight()));
			}
		}
	}
	
	private void updateRowsInBounds() {
		rowsInBounds.clear();
		for (int i = 0; i < rows.size(); i++) {
			GuiRow row = rows.get(i);
			if (row.getY() >= posY + padding && row.getY() + row.getHeight() <= posY + height) {
				rowsInBounds.add(row);
			}
		}
	}
	
	protected List<GuiRow> getRowsInBound(){
		return rowsInBounds;
	}
	
	
	
	
	
	
	
	
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getPosX() {
		return posX;
	}
	
	public void setPosX(int posX) {
		this.posX = posX;
	}
	
	public int getPosY() {
		return posY;
	}
	
	public GuiElement setPosition(int x, int y) {
		setPosX(x);
		setPosY(y);
		return this;
	}
	
	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	public GuiElement getParent() {
		return parent;
	}
	
	public void setParent(GuiElement parent) {
		this.parent = parent;
	}
	
	public List<GuiElement> getChildren() {
		return children;
	}
	
	public Vector4f getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(float r, float g, float b, float a) {
		this.backgroundColor.set(r, g, b, a);
		this.dirty = true;
	}
	
	public int getPadding() {
		return padding;
	}
	
	public void setPadding(int padding) {
		this.padding = padding;
		recalculateLayout();
		this.dirty = true;
	}
	
	public int getSpacing() {
		return spacing;
	}
	
	public void setSpacing(int spacing) {
		this.spacing = spacing;
		recalculateLayout();
		this.dirty = true;
	}

	public boolean isDirty() {
		boolean d = dirty;
		dirty = false;
		for (int i = 0; i < children.size(); i ++) {
			d = d || children.get(i).isDirty();
		}
		return d;
	}
	
	public String getTextureName() {
		return textureName;
	}
	
	public void setTextureName(String textureName) {
		if (this.textureName == null) {
			setBackgroundColor(1, 1, 1, 0);
		}
		this.textureName = textureName;
		this.dirty = true;
	}
	
	
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	public void recalculateLayout() {
		layout.applyLayout(this, children, childYOffset);
		children.forEach(c -> c.recalculateLayout());
		updateRows();
		updateRowsInBounds();
	}
	
}
