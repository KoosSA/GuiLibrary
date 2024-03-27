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
	
	public Text(String text, Font font) {
		this.text = text;
		this.font = font;
		createQuads();
	}
	
	private void createQuads() {
		byte[] bytes = text.getBytes();
		vertices = new float[12 * bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			int b = bytes[i];
			Glyph g = font.getGlyph(b);
			
			vertices[ i * 12 + 0 ] = cursorPosX + g.getX_Offset();
			vertices[ i * 12 + 1 ] = cursorPosY + g.getY_Offset(); 
			
			vertices[ i * 12 + 2 ] = cursorPosX + g.getX_Offset();
			vertices[ i * 12 + 3 ] = cursorPosY;
			
			vertices[ i * 12 + 4 ] = vertices[ i * 12 + 0 ] + g.getX_Advance();
			vertices[ i * 12 + 5 ] = vertices[ i * 12 + 1 ];
			
			vertices[ i * 12 + 6 ] = vertices[ i * 12 + 4 ];
			vertices[ i * 12 + 7 ] = vertices[ i * 12 + 5 ];
			
			vertices[ i * 12 + 8 ] = vertices[ i * 12 + 2 ];
			vertices[ i * 12 + 9 ] = vertices[ i * 12 + 3 ];
			
			vertices[ i * 12 + 10 ] = vertices[ i * 12 + 2 ] + g.getX_Advance();
			vertices[ i * 12 + 11 ] = vertices[ i * 12 + 3 ];
			
			cursorPosX += g.getX_Advance();
		}
	}
	
	public float[] getVertices() {
		return vertices;
	}

}
