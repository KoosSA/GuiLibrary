#version 330

layout(location = 0) in vec2 position;
layout(location = 1) in vec3 textureCoord;
layout(location = 2) in vec4 color;
layout(location = 3) in float data;

out vec3 passTexCoord;
out vec4 passColor;
out float passType;

uniform mat4 projection;

void main() {

	gl_Position = projection * vec4(position, 0, 1);

	passTexCoord = textureCoord;
	passColor = color;
	passType = data;
	
}