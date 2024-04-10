package koossa.guilib.elements;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;

import koossa.guilib.Gui;
import koossa.guilib.utils.Bounds;

public abstract class Element {
	
	protected Element parent = null;
	protected List<Element> children = new ArrayList<Element>();
	protected boolean visible = true;
	protected boolean receiveMouseInput = false;
	protected boolean reseiveKeyboardInput = false;
	protected Bounds bounds = new Bounds(0, 0, 100, 100);
	protected Vector4f backgroundColour = new Vector4f(0.8f, 0.8f, 0.8f, 1);
	private float[] ogData = new float[5];
	
	public Element(float posX, float posY, float width, float height, boolean isRatioValues) {
		onLayout(posX, -posY, width, height, isRatioValues);
		ogData[0] = posX;
		ogData[1] = -posY;
		ogData[2] = width;
		ogData[3] = height;
		ogData[4] = isRatioValues ? 1 : 0;
	}
	
	private void onLayout(float posX, float posY, float width, float height, boolean isRatioValues) {
		if (!isRatioValues) {
			bounds.setHeight(height);
			bounds.setWidth(width);
			bounds.setX(posX);
			bounds.setY(posY);
		} else {
			if (parent == null) {
				bounds.setHeight(Gui.getScreenHeight() * height);
				bounds.setWidth(Gui.getScreenWidth() * width);
				bounds.setX(Gui.getScreenWidth() * posX);
				bounds.setY(Gui.getScreenHeight() * posY);
			} else {
				bounds.setHeight(parent.bounds.getHeight() * height);
				bounds.setWidth(parent.bounds.getWidth() * width);
				bounds.setX(parent.bounds.getX() * posX);
				bounds.setY(parent.bounds.getY() * posY);
			}
		}
		bounds.calculateVertices();
	}
	
	public Bounds getBounds() {
		return bounds;
	}
	
	public Vector4f getBackgroundColour() {
		return backgroundColour;
	}

	public void addVertices(List<Float> verts) {
		for (int i = 0; i < bounds.getVertices().length; i++) {
			verts.add(bounds.getVertices()[i]);
		}
		children.forEach(child -> {
			child.addVertices(verts);
		});
	}

	public void autoLayout() {
		onLayout(ogData[0], ogData[1], ogData[2], ogData[3], (ogData[4] == 1));
		children.forEach(child -> child.autoLayout());
	}
}
