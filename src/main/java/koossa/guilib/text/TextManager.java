package koossa.guilib.text;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import koossa.guilib.text.shader.TextShader;

public class TextManager {
	
	private static List<Text> torender = new ArrayList<Text>();
	private static int vao, vbo, vbot;
	private static TextShader shader;
	private static Matrix4f projectionMat;
	
	public static void init() {
		vao = GL30.glGenVertexArrays();
		vbo = GL30.glGenBuffers();
		vbot = GL30.glGenBuffers();
		GL30.glBindVertexArray(vao);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
		GL30.glVertexAttribPointer(0, 2, GL30.GL_FLOAT, false, 0, 0);
		GL30.glEnableVertexAttribArray(0);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbot);
		GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 0, 0);
		GL30.glEnableVertexAttribArray(1);
		shader = new TextShader();
		projectionMat = new Matrix4f();
		projectionMat.ortho2D(0, 800, 0, 600);
		shader.start();
		shader.loadProjection(projectionMat);
		shader.stop();
	}
	
	public static void addTextToRender(Text text) {
		torender.add(text);
	}
	
	private static int prepareRenderData() {
		
		float[] v = torender.get(0).getVertices();
		float[] t = torender.get(0).getTexCoords();
		if (v == null || t == null) return 0;
		
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, v, GL30.GL_DYNAMIC_DRAW);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbot);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, t, GL30.GL_DYNAMIC_DRAW);
		
		
		GL30.glEnable(GL30.GL_BLEND);
		GL30.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		
		GL30.glEnable(GL30.GL_DEPTH_TEST);
		return v.length / 2;
	}
	
	public static void render() {
		shader.start();
		GL30.glBindVertexArray(vao);
		int size = prepareRenderData();
		
		GL30.glActiveTexture(GL30.GL_TEXTURE0);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, torender.get(0).font.getTextureId());
		GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, size);
		
		GL30.glBindVertexArray(0);
		shader.stop();
		
		
	}
	
	public static void dispose() {
		GL30.glDeleteVertexArrays(vao);
		GL30.glDeleteBuffers(vbo);
		GL30.glDeleteBuffers(vbot);
		Font.disposeAll();
	}
	
	

}
