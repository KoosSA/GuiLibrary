package koossa.guilib.text;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL30;

public class TextManager {
	
	private static List<Text> torender = new ArrayList<Text>();
	private static int vao, vbo;
	
	public static void init() {
		vao = GL30.glGenVertexArrays();
		vbo = GL30.glGenBuffers();
		GL30.glBindVertexArray(vao);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
		GL30.glVertexAttribPointer(0, 2, GL30.GL_FLOAT, false, 0, 0);
		GL30.glEnableVertexAttribArray(0);
	}
	
	public static void addTextToRender(Text text) {
		torender.add(text);
	}
	
	private static int prepareRenderData() {
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
		//GL30.glClearBufferfi(GL30.GL_ARRAY_BUFFER, GL30.GL_STATIC_DRAW, 0, 0);
//		float[] v = { 	0, 0,
//						0, 100,
//						100, 0,
//						100, 0,
//						0, 100,
//						100, 100
//					};
		float[] v = torender.get(0).getVertices();
		for (int i = 0; i < v.length; i++) {
			System.out.println(v[i]);
		}
		if (v == null) return 0;
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, v, GL30.GL_DYNAMIC_DRAW);
		return v.length / 2;
	}
	
	public static void render() {
		int size = prepareRenderData();
		GL30.glMatrixMode(GL30.GL_PROJECTION);
		GL30.glLoadIdentity();
		GL30.glOrtho(0, 800, 0, 600, -5, 5);
		GL30.glBindVertexArray(vao);
		
		GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, size);
		
		GL30.glBindVertexArray(0);
		
		
	}
	
	public static void dispose() {
		GL30.glDeleteVertexArrays(vao);
		GL30.glDeleteBuffers(vbo);
	}
	
	

}
