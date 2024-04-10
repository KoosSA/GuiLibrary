package koossa.guilib.utils;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import koossa.guilib.Gui;
import koossa.guilib.elements.Element;
import koossa.guilib.utils.shader.GuiShader;

public class GuiRenderer {
	
	private Matrix4f projectionMatrix = new Matrix4f();
	private GuiShader shader;
	private List<float[]> torender_Verts;
	private static int vao, vbo, vbot, vboc;
	private GuiManager guiManager;
	
	public GuiRenderer(GuiManager guiManager) {
		this.guiManager = guiManager;
		shader = new GuiShader();
		vao = GL30.glGenVertexArrays();
		vbo = GL30.glGenBuffers();
		vbot = GL30.glGenBuffers();
		vboc = GL30.glGenBuffers();
		
		GL30.glBindVertexArray(vao);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
		GL30.glVertexAttribPointer(0, 2, GL30.GL_FLOAT, false, 0, 0);
		GL30.glEnableVertexAttribArray(0);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbot);
		GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 0, 0);
		GL30.glEnableVertexAttribArray(1);
//		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboc);
//		GL30.glVertexAttribPointer(2, 4, GL30.GL_FLOAT, false, 0, 0);
//		GL30.glEnableVertexAttribArray(2);
		
		updateProjection((int) Gui.getScreenWidth(), (int) Gui.getScreenHeight());
	}
	
	private int prepare(float[] vertices) {
//		if (vertices == null || textureCoords == null || colours == null) return 0;
		
		
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertices, GL30.GL_DYNAMIC_DRAW);
//		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbot);
//		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, textureCoords, GL30.GL_DYNAMIC_DRAW);
//		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboc);
//		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, colours, GL30.GL_DYNAMIC_DRAW);
//		
		
		
		return vertices.length / 2;
	}
	
	public void render() {
		GL30.glEnable(GL30.GL_BLEND);
		GL30.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		GL30.glDisable(GL30.GL_DEPTH_TEST);
		shader.start();
		GL30.glBindVertexArray(vao);
		guiManager.getVerticesToRender().forEach(root -> {
			int count = prepare(root);
			GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, count);
		});
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void resize(int width, int height) {
		updateProjection(width, height);
	}
	
	public void dispose() {
		shader.stop();
		shader.dispose();
		GL30.glDeleteBuffers(vbo);
		GL30.glDeleteBuffers(vbot);
		GL30.glDeleteBuffers(vboc);
		GL30.glDeleteVertexArrays(vao);
		
	}
	
	private void updateProjection(int width, int height) {
		projectionMatrix.identity();
		projectionMatrix.ortho2D(0, width, -height, 0);
		shader.start();
		shader.loadProjection(projectionMatrix);
		shader.stop();
	}

}
