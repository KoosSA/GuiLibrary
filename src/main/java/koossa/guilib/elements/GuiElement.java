package koossa.guilib.elements;

import java.util.ArrayList;
import java.util.List;

import org.joml.Math;
import org.joml.Vector4f;

import koossa.guilib.Gui;
import koossa.guilib.layout.ILayout;

public class GuiElement {
	
	private ILayout layout;
	private int width, height, posX, posY;
	private float relativeWidth, relativeHeight;
	private GuiElement parent;
	private List<GuiElement> children = new ArrayList<GuiElement>();
	private Vector4f backgroundColor;
	private int padding = 0, spacing = 0;
	
	public GuiElement(float width, float height, ILayout layout) {
		this.layout = layout;
		this.relativeHeight = Math.clamp(0, 1, height);
		this.relativeWidth = Math.clamp(0, 1, width);
		this.width = (int) (Gui.getScreenWidth() * relativeWidth);
		this.height = (int) (Gui.getScreenHeight() * relativeHeight);
		this.backgroundColor = new Vector4f(1);
	}
	
	public void addChild(GuiElement child) {
		child.setParent(this);
		children.add(child);
		child.width = (int) (width * child.relativeWidth);
		child.height = (int) (height * child.relativeHeight);
		layout.applyLayout(this, children);
	}
	
	public void update() {
		
	}
	
	public void onResize() {
		if (parent == null)	{
			this.width = (int) (Gui.getScreenWidth() * relativeWidth);
			this.height = (int) (Gui.getScreenHeight() * relativeHeight);
		} else {
			this.width = (int) (parent.getWidth() * relativeWidth);
			this.height = (int) (parent.getHeight() * relativeHeight);
		}
		children.forEach(c -> c.onResize());
		layout.applyLayout(this, children);
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
	}
	
	public int getPadding() {
		return padding;
	}
	
	public void setPadding(int padding) {
		this.padding = padding;
		layout.applyLayout(this, children);
	}
	
	public int getSpacing() {
		return spacing;
	}
	
	public void setSpacing(int spacing) {
		this.spacing = spacing;
		layout.applyLayout(this, children);
	}
	
	
}
