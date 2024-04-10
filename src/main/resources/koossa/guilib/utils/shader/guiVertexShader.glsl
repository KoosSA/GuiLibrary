#version 330

layout(location = 0) in vec2 position;
layout(location = 1) in vec2 textureCoord;
layout(location = 2) in vec4 colour;

out vec2 passTexCoord;
out vec4 passColour;

uniform mat4 projection;

void main() {

	gl_Position = projection * vec4(position, 0, 1);

	passTexCoord = textureCoord;
	passColour = colour;
	
}