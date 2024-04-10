#version 330

in vec2 passTexCoord;
in vec4 passColour;

out vec4 colour;

//uniform sampler2D tex;

void main() {
	
	colour = passColour;

}
