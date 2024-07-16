package koossa.guilib.elements;

import org.apache.commons.lang3.StringUtils;

import koossa.guilib.elements.utils.IGuiEvent;
import koossa.guilib.gui.GuiManager;
import koossa.guilib.layout.SizeFormat;
import koossa.inputlib.IInputHandler;
import koossa.inputlib.Input;
import koossa.inputlib.InputManager;

public class TextField extends Label implements IInputHandler {
	
	private boolean selected = false;
	private IGuiEvent onHoverStart, onHoverEnd, onInteract;
	private boolean hovering = false;
	private String string = "";

	public TextField(SizeFormat sizeFormat, float width, float height, String text) {
		super(sizeFormat, width, height, text);
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
		if (hovering && input.isFunctionMouseButtonJustPressed("CLICK_PRIMARY")) {
			selected = true;
			GuiManager.setTextInputEnabled(true);
		}
		if (!hovering && selected && input.isFunctionMouseButtonJustPressed("CLICK_PRIMARY")) {
			selected = false;
			GuiManager.setTextInputEnabled(false);
		}
		if (selected && input.getCharactersInput().size() > 0) {
			byte[] arr = new byte[input.getCharactersInput().size()];
			for (int i = 0; i < arr.length; i++) {
				arr[i] = input.getCharactersInput().get(i).byteValue();
			}
			String nstr = new String(arr);
			string = string + nstr;
			text.setText(string);
			setDirty(true);
		} else if (selected && input.isFunctionKeyJustPressed("GUI_BACKSPACE")) {
			string = StringUtils.chop(string);
			text.setText(string);
			setDirty(true);
		}
	}
	

}
