package koossa.guilib.elements;

import java.util.List;

import koossa.guilib.elements.utils.ITextElement;
import koossa.guilib.layout.Layouts;
import koossa.guilib.layout.SizeFormat;
import koossa.guilib.text.FontLibrary;
import koossa.guilib.text.Text;

public class Label extends GuiElement implements ITextElement {
	
	protected Text text;
	private boolean overrideTextSize = false;

	public Label(SizeFormat sizeFormat, float width, float height, String text) {
		super(sizeFormat, width, height, Layouts.FLOW_LAYOUT);
		this.text = new Text(text, FontLibrary.getFont("DEFAULT_FONT"), getPosX(), getPosY(), width, getHeight());
		this.text.setMultiline(false);
		this.setBackgroundColor(1, 1, 1, 0);
	}
	
	@Override
	public void recalculateLayout() {
		super.recalculateLayout();
		text.setPosition(getPosX(), getPosY());
		if (!overrideTextSize) {
			text.setSize(getHeight());
		}
	}

	@Override
	public List<Float> getTextVertices() {
		return text.getVerticesList();
	}

	@Override
	public List<Float> getTextTextureCoords() {
		return text.getTexCoordList();
	}

	@Override
	public List<Float> getTextColourList() {
		return text.getColourList();
	}

	@Override
	public List<Float> getDataList() {
		return text.getDataList();
	}

	public void setTextSize(int textSize) {
		if (textSize != 0) {
			text.setSize(textSize);
			overrideTextSize = true;
		} else {
			text.setSize(getHeight() - getPadding());
			overrideTextSize = false;
		}
		
	}

	public void setTextColour(float r, float g, float b, float a) {
		text.setColour(r, g, b, a);
	}
	
	public void setText(String text) {
		this.text.setText(text);
		setDirty(true);
	}
	
	
	
	

}
