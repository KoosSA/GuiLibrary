package koossa.guilib.elements;

import koossa.guilib.elements.utils.IGuiEvent;
import koossa.guilib.layout.Layouts;
import koossa.guilib.layout.SizeFormat;
import koossa.inputlib.IInputHandler;
import koossa.inputlib.Input;
import koossa.inputlib.InputManager;

public class Button extends GuiElement implements IInputHandler {
	
	private IGuiEvent onHoverStart, onHoverEnd, onInteract;
	private boolean hovering = false;

	public Button(SizeFormat sizeFormat, float width, float height, Layouts layout) {
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
	}
	
	public void setOnHoverEnd(IGuiEvent onHoverEnd) {
		this.onHoverEnd = onHoverEnd;
	}
	
	public void setOnHoverStart(IGuiEvent onHoverStart) {
		this.onHoverStart = onHoverStart;
	}
	
	public void setOnInteract(IGuiEvent onInteract) {
		this.onInteract = onInteract;
	}

}
