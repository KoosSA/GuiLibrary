package koossa.guilib.elements;

import koossa.guilib.elements.utils.IGuiEvent;
import koossa.guilib.layout.Layouts;
import koossa.guilib.layout.SizeFormat;
import koossa.inputlib.IInputHandler;
import koossa.inputlib.Input;
import koossa.inputlib.InputManager;

public class ScrollPanel extends GuiElement implements IInputHandler {
	
	private IGuiEvent onHoverStart, onHoverEnd, onInteract;
	private boolean hovering = false;
	private int scrollYOffset = 0;
	private int scrollSpeed = 2;
	
	public ScrollPanel(SizeFormat sizeFormat, float width, float height, Layouts layout) {
		super(sizeFormat, width, height, layout);
		Input.registerInputHandler("GUI_INPUT", this);
	}

	@Override
	public void handleInput(InputManager input) {
		if (!isInBounds()) return;
		if (getPosX() < input.getCurrentMouseX() && getPosX() + getWidth() > input.getCurrentMouseX() && 
				getPosY() < input.getCurrentMouseY() && getPosY() + getHeight() > input.getCurrentMouseY()) {
			if (!hovering) {
				hovering = true;
				if (onHoverStart != null) {
					onHoverStart.handleGuiEvent(this);
				}
			}
		} else {
			if (hovering) {
				hovering = false;
				if (onHoverEnd != null) {
					onHoverEnd.handleGuiEvent(this);
				}
			}
		}
		if (hovering && onInteract != null && input.isFunctionMouseButtonJustPressed("CLICK_PRIMARY")) {
			onInteract.handleGuiEvent(this);
		}
		if (hovering && input.getScrollYOffset() != 0) {
			childYOffset += (input.getScrollYOffset() * scrollSpeed);
			recalculateLayout();
			setDirty(true);
		}
	}
	
	public void setScrollSpeed(int scrollSpeed) {
		this.scrollSpeed = scrollSpeed;
	}

}
