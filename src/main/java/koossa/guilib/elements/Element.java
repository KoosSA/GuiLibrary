package koossa.guilib.elements;

import java.util.List;

public abstract class Element {
	
	protected Element parent;
	protected List<Element> children;
	protected boolean visible;
	protected boolean receiveMouseInput;
	protected boolean reseiveKeyboardInput;
	protected int width;
	protected int heigth;
	protected int xPos;
	protected int yPos;

}
