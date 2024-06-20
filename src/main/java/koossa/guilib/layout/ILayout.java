package koossa.guilib.layout;

import java.util.List;

import koossa.guilib.elements.GuiElement;

public interface ILayout {
	
	//void applyLayout();

	void applyLayout(GuiElement element, List<GuiElement> children, int scrollYOffset);

}
