package koossa.guilib.elements;

import org.joml.Math;

import koossa.guilib.elements.utils.IGuiEvent;
import koossa.guilib.layout.Layouts;
import koossa.guilib.layout.SizeFormat;
import koossa.inputlib.IInputHandler;
import koossa.inputlib.Input;
import koossa.inputlib.InputManager;

public class ScrollPanel extends GuiElement implements IInputHandler {
	
	private IGuiEvent onHoverStart, onHoverEnd, onInteract;
	private boolean hovering = false;
	private int scrollSpeed = 2;
	private boolean smoothScrolling = false;
	
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
			childYOffset = Math.clamp(-getChildren().getLast().getPosY() + getChildren().getLast().getHeight() + getPadding(), 0, childYOffset);
			recalculateLayout();
			setDirty(true);
		}
	}
	
	@Override
	public void recalculateLayout() {
		super.recalculateLayout();
		if (!smoothScrolling) {
			scrollSpeed = getChildMaxHeight() + getPadding();
		}
	}
	
	public void setScrollSpeed(int scrollSpeed) {
		this.scrollSpeed = scrollSpeed;
	}

	private int getChildMaxHeight() {
		int max = 0;
		for (int i = 0; i < getChildren().size(); i++ ) {
			if (getChildren().get(i).getHeight() > max) {
				max = getChildren().get(i).getHeight();
			}
		}
		return max;
	}

	
}
