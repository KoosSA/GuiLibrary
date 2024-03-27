package koossa.guilib.text;

public class Text {
	
	String text;
	Font font;
	float currentLineWidth;
	float maxLineWidth = 100;
	float startPosX = 0;
	float startPosY = 0;
	float cursorPosX = 0;
	float cursorPosY = 0;
	
	float[] vertices;
	float[] texCoords;
	
	public Text(String text, Font font) {
		this.text = text;
		this.font = font;
		createQuads();
	}
	
	private void createQuads() {
		byte[] bytes = text.getBytes();
		vertices = new float[12 * bytes.length];
		texCoords = new float[12 * bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			int b = bytes[i];
			Glyph g = font.getGlyph(b);
			
			generateVertices(i, g);
			generateTexCoords(i, g);
			
			cursorPosX += g.getX_Advance();
		}
		
	}
	
	private void generateTexCoords(int i, Glyph g) {
		texCoords[ i * 12 + 0 ] = (float) g.getX() / font.getTexWidth();
		texCoords[ i * 12 + 1 ] = (font.getTexWidth() - (float) g.getY()) / font.getTexWidth(); 
		
		texCoords[ i * 12 + 2 ] = texCoords[ i * 12 + 0];
		texCoords[ i * 12 + 3 ] = (font.getTexWidth() - ((float) g.getY() + (float) g.getHeight())) / font.getTexWidth();
		
		texCoords[ i * 12 + 4 ] = ((float) g.getX() + (float) g.getWidth()) / font.getTexWidth();
		texCoords[ i * 12 + 5 ] = texCoords[ i * 12 + 1 ];
		                 
		texCoords[ i * 12 + 6 ] = texCoords[ i * 12 + 4 ];
		texCoords[ i * 12 + 7 ] = texCoords[ i * 12 + 1 ];
		                 
		texCoords[ i * 12 + 8 ] = texCoords[ i * 12 + 2 ];
		texCoords[ i * 12 + 9 ] = texCoords[ i * 12 + 3 ];
		               
		texCoords[ i * 12 + 10 ] = texCoords[ i * 12 + 4 ];
		texCoords[ i * 12 + 11 ] = texCoords[ i * 12 + 3 ];
	}

	private void generateVertices(int i, Glyph g) {
		vertices[ i * 12 + 0 ] = cursorPosX ;
		vertices[ i * 12 + 1 ] = cursorPosY + g.getHeight(); 
		
		vertices[ i * 12 + 2 ] = cursorPosX;
		vertices[ i * 12 + 3 ] = cursorPosY;
		
		vertices[ i * 12 + 4 ] = vertices[ i * 12 + 0 ] + g.getWidth();
		vertices[ i * 12 + 5 ] = vertices[ i * 12 + 1 ];
		
		vertices[ i * 12 + 6 ] = vertices[ i * 12 + 4 ];
		vertices[ i * 12 + 7 ] = vertices[ i * 12 + 5 ];
		
		vertices[ i * 12 + 8 ] = vertices[ i * 12 + 2 ];
		vertices[ i * 12 + 9 ] = vertices[ i * 12 + 3 ];
		
		vertices[ i * 12 + 10 ] = vertices[ i * 12 + 2 ] + g.getWidth();
		vertices[ i * 12 + 11 ] = vertices[ i * 12 + 3 ];
	}
	
	public float[] getVertices() {
		return vertices;
	}
	
	public float[] getTexCoords() {
		return texCoords;
	}

}
