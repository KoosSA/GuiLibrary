package koossa.guilib.layout;

import java.util.List;

import com.koossa.logger.Log;

import koossa.guilib.elements.GuiElement;

class FlowLayout implements ILayout {
	
	private int cursorX = 0, cursorY = 0;
	
	public FlowLayout() {
		
	}

	@Override
	public void applyLayout(GuiElement element, List<GuiElement> children) {
		int prevMaxHeight = 0;
		this.cursorX = element.getPadding() + element.getPosX();
		this.cursorY = element.getPadding() + element.getPosY();
		
		for (int i = 0; i < children.size(); i++) {
			GuiElement child = children.get(i);
			//Checks if a new row should be created and offsets the Y position to the new row, resets X position
			if (cursorX + child.getWidth() + (element.getPadding()) > element.getPosX() + element.getWidth()) {
				cursorX = element.getPadding() + element.getPosX();
				cursorY += (prevMaxHeight + element.getSpacing());
				prevMaxHeight = 0;
			}
			//Sets the child position
			child.setPosX(cursorX);
			child.setPosY(cursorY);
			if (! child.isInBounds()) {
				Log.debug(this, "Gui element: " + child + " is not in bounds of parent. Will not be displayed or updated.");
			}
			//Advances the X cursor
			cursorX  += child.getWidth() + element.getSpacing();
			prevMaxHeight = Math.max(prevMaxHeight, child.getHeight());
		}
		
	}
	
	

}
