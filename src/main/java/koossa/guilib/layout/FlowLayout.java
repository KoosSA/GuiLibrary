package koossa.guilib.layout;

import java.util.List;

import koossa.guilib.elements.GuiElement;

public class FlowLayout implements ILayout {
	
	private int currentRow = 0;
	private int cursorX = 0, cursorY = 0;
	private List<GuiElement> children;
	private float width, height;
	
	public FlowLayout() {
		
	}

	@Override
	public void applyLayout(GuiElement element, List<GuiElement> children) {
		this.children = children;
		this.width = element.getWidth();
		this.height = element.getHeight();
		int prevMaxHeight = 0;
		this.cursorX = element.getPadding() + element.getPosX();
		this.cursorY = element.getPadding() + element.getPosY();
		
		for (int i = 0; i < children.size(); i++) {
			GuiElement child = children.get(i);
			//Checks if a new row should be created and offsets the Y position to the new row, resets X position
			if (cursorX + child.getWidth() + (element.getPadding()) > width) {
				cursorX = element.getPadding() + element.getPosX();
				cursorY += (prevMaxHeight + element.getSpacing());
				prevMaxHeight = 0;
			}
			//Sets the child position
			child.setPosX(cursorX);
			child.setPosY(cursorY);
			//Advances the X cursor
			cursorX  += child.getWidth() + element.getSpacing();
			prevMaxHeight = Math.max(prevMaxHeight, child.getHeight());
		}
	}
	
	

}
