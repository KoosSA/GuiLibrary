package koossa.guilib.rendering;

import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import com.koossa.logger.Log;

import koossa.guilib.Gui;
import koossa.guilib.rendering.shader.GuiShader;

public class GuiRenderer {
	
	private Matrix4f projectionMatrix = new Matrix4f();
	private GuiShader shader = new GuiShader();
	private int vao;
	private Map<BufferLocations, Integer> vbos = new HashMap<BufferLocations, Integer>();
	
	
	public GuiRenderer() {
		Log.debug(this, "Creating Gui renderer.");
		createOpenGLData();
		updateProjectionMatrix(Gui.getScreenWidth(), Gui.getScreenHeight());
	}

	public void resize(int width, int height) {
		updateProjectionMatrix(width, height);
	}
	
	public void render(RenderBatch batch) {
		GL30.glDisable(GL30.GL_DEPTH_TEST);
		
		shader.start();
		GL30.glBindVertexArray(vao);
		prepareRenderData(batch);
		
		GL30.glDrawElements(GL30.GL_TRIANGLES, batch.getIndices().length, GL30.GL_UNSIGNED_INT, 0);
		
		GL30.glBindVertexArray(0);
		shader.stop();
		
		GL30.glEnable(GL30.GL_DEPTH_TEST);
	}
	
	public void dispose() {
		Log.debug(this, "Disposing Gui Renderer.");
		GL30.glBindVertexArray(0);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glDeleteVertexArrays(vao);
		vbos.forEach((k, v) -> GL30.glDeleteBuffers(v));
		shader.dispose();
	}
	
	private void prepareRenderData(RenderBatch batch) {
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbos.get(BufferLocations.VERTEX_BUFFER));
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, batch.getVertices(), GL30.GL_DYNAMIC_DRAW);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbos.get(BufferLocations.TEXTURECOORD_BUFFER));
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, batch.getTextureCoords(), GL30.GL_DYNAMIC_DRAW);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbos.get(BufferLocations.COLOR_BUFFER));
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, batch.getColors(), GL30.GL_DYNAMIC_DRAW);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, vbos.get(BufferLocations.INDEX_BUFFER));
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, batch.getIndices(), GL30.GL_DYNAMIC_DRAW);
	}
	
	private void updateProjectionMatrix(float width, float height) {
		projectionMatrix.identity();
		projectionMatrix.ortho2D(0, width, height, 0);
		shader.start();
		shader.loadProjection(projectionMatrix);
		shader.stop();
	}
	
	private void createOpenGLData() {
		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		createBuffer(BufferLocations.VERTEX_BUFFER, 2);
		createBuffer(BufferLocations.TEXTURECOORD_BUFFER, 2);
		createBuffer(BufferLocations.COLOR_BUFFER, 4);
		createIndicesBuffer(BufferLocations.INDEX_BUFFER);
		GL30.glBindVertexArray(0);
	}
	
	private void createBuffer(BufferLocations index, int size) {
		int vbo = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
		GL30.glVertexAttribPointer(index.ordinal(), size, GL30.GL_FLOAT, false, 0, 0);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, new float[0], GL30.GL_DYNAMIC_DRAW);
		GL30.glEnableVertexAttribArray(index.ordinal());
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
		vbos.put(index, vbo);
	}
	
	private void createIndicesBuffer(BufferLocations index) {
		int vbo = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, vbo);
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, new int[0], GL30.GL_DYNAMIC_DRAW);
		vbos.put(index, vbo);
	}

	

}
