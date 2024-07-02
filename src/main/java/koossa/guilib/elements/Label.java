package koossa.guilib.elements;

import koossa.guilib.layout.Layouts;
import koossa.guilib.layout.SizeFormat;
import koossa.guilib.text.FontLibrary;
import koossa.guilib.text.Text;
import koossa.guilib.text.TextManager;

public class Label extends GuiElement {
	
	private Text text;

	public Label(SizeFormat sizeFormat, float width, float height, String text) {
		super(sizeFormat, width, height, Layouts.FLOW_LAYOUT);
		this.text = new Text(text, FontLibrary.getFont("DEFAULT_FONT"), getPosX(), getPosY(), width, getHeight());
		this.text.setMultiline(false);
		TextManager.addTextToRender(this.text);
	}
	
	@Override
	public void recalculateLayout() {
//		super.recalculateLayout();
		text.setPosition(getPosX(), getPosY());
		text.setSize(getHeight());
		
	}
	
	
	
	

}
