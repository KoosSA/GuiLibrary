package koossa.guilib.rendering.shader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import com.koossa.logger.Log;

public class GuiShader {
	
	private int programId;
	private static LinkedList<Integer> shaderIDS = new LinkedList<>();
	protected Map<String, Integer> uniforms = new HashMap<>();
	
	public GuiShader() {
		programId = createShaderProgram("guiVertexShader.glsl", "guiFragmentShader.glsl");
		
		while (GL30.glGetError() != GL30.GL_NO_ERROR) {
			Log.error(this, "Opengl Error: " + GL30.glGetError());
		}
	}
	
	private int createShaderProgram(String vertPath, String fragPath) {
		int id = GL30.glCreateProgram();
		programId = id;
		int vert = createShader(vertPath, GL30.GL_VERTEX_SHADER);
		int frag = createShader(fragPath, GL30.GL_FRAGMENT_SHADER);
		
		GL30.glAttachShader(id, vert);
		GL30.glAttachShader(id, frag);
		GL30.glLinkProgram(id);
		if (GL30.glGetProgrami(id, GL30.GL_LINK_STATUS) == GL30.GL_FALSE) {
			Log.error(this, "Shader program link failed: " + vertPath + " & " + fragPath);
			Log.error(this, GL30.glGetProgramInfoLog(id));
			return 0;
		}
		GL30.glValidateProgram(id);
		if (GL30.glGetProgrami(id, GL30.GL_VALIDATE_STATUS) == GL30.GL_FALSE) {
			Log.error(this, "Shader program validation failed: " + vertPath + " & " + fragPath);
			Log.error(this, GL30.glGetProgramInfoLog(id));
			return 0;
		}
		getUniformLocationsBase();
		return id;
	}

	private void getUniformLocationsBase() {
		getUniformLocations();
		while (GL30.glGetError() != GL30.GL_NO_ERROR) {
			Log.error(this, "Opengl Error: " + GL30.glGetError() + " Probably an invalid uniform location in a shader");
		}
	}

	private int createShader(String shaderName, int glVertexShader) {
		int id = GL30.glCreateShader(glVertexShader);
		BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(shaderName)));
		GL30.glShaderSource(id, reader.lines().collect(Collectors.joining("\n")));
		GL30.glCompileShader(id);
		if (GL30.glGetShaderi(id, GL30.GL_COMPILE_STATUS) == GL30.GL_FALSE) {
			Log.error(this, "Failed to compile shader: " + shaderName);
			Log.error(this, GL30.glGetShaderInfoLog(id));
		}
		shaderIDS.add(id);
		return id;
	}
	
	public void start() {
		GL30.glUseProgram(programId);
	}
	
	public void stop() {
		GL30.glUseProgram(0);
	}
	
	public void dispose() {
		GL30.glUseProgram(0);
		shaderIDS.forEach(id -> {
			GL30.glDetachShader(programId, id);
			GL30.glDeleteShader(id);
		});
		GL30.glDeleteProgram(programId);
	}
	
	protected void loadVec3f(Vector3f vec3, int location) {
		GL30.glUniform3f(location, vec3.x, vec3.y, vec3.z);
	}
	
	protected void loadVec3f(float x, float y, float z, int location) {
		GL30.glUniform3f(location, x, y, z);
	}
	
	protected void loadVec4f(Vector4f vec4, int location) {
		GL30.glUniform4f(location, vec4.x, vec4.y, vec4.z, vec4.w);
	}
	
	protected void loadMat4f(Matrix4f mat4, int location) {
		GL30.glUniformMatrix4fv(location, false, matrix4fToFloatBuffer(mat4));
	}
	
	protected void loadFloat(float value, int location) {
		GL30.glUniform1f(location, value);
	}
	
	protected void loadBoolean(boolean bool, int location) {
		loadInt(bool ? 1 : 0, location);
	}
	
	protected void loadInt(int value, int location) {
		GL30.glUniform1i(location, value);
	}
	
	protected void addUniform(String name) {
		int id = GL30.glGetUniformLocation(programId, name);
		while (GL30.glGetError() != GL30.GL_NO_ERROR) {
			Log.error(this, "Opengl Error: " + GL30.glGetError() + " Probably an invalid uniform location in a shader: " + name);
		}
		uniforms.putIfAbsent(name, id);
	}

	private int projection_loc;
	//private int transformation_loc;
	
	protected void getUniformLocations() {
		projection_loc = GL30.glGetUniformLocation(programId, "projection");
		//transformation_loc = GL30.glGetUniformLocation(programId, "transformation");
	}
	
	private FloatBuffer matrix4fBuffer = BufferUtils.createFloatBuffer(16);
	public FloatBuffer matrix4fToFloatBuffer(Matrix4f matrix4f) {
		matrix4f.get(matrix4fBuffer);
		return matrix4fBuffer;
	}

	public void loadProjection(Matrix4f projectionMat) {
		loadMat4f(projectionMat, projection_loc);
	}
	
//	public void loadTransformation(Matrix4f transformationMatrix) {
//		loadMat4f(transformationMatrix, transformation_loc);
//	}
//	
//	public void loadColour(Vector4f colour) {
//		loadVec4f(colour, uni_colour_loc);
//	}

}
