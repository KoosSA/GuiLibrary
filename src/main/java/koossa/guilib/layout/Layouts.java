package koossa.guilib.layout;

public enum Layouts {
	
	FLOW_LAYOUT(new FlowLayout());

	
	private final ILayout value;
	
	private Layouts(final ILayout newValue) {
        value = newValue;
    }

    public ILayout getValue() { return value; }

}
