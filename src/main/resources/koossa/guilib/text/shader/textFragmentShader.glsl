#version 330

in vec2 passTexCoord;

out vec4 colour;

uniform sampler2D tex;

void main() {

	vec4 col = texture(tex, passTexCoord);
	if (col.a < 0.2f) {
		discard;
	}
	
	colour = col;

}
