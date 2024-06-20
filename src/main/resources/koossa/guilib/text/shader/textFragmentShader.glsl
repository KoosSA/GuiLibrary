#version 330

in vec2 passTexCoord;

out vec4 colour;

uniform sampler2D tex;

uniform vec4 uni_colour;

void main() {

	vec4 col = texture(tex, passTexCoord);
	if (col.a < 0.47f) {
		discard;
	}
	
	colour = vec4(uni_colour.rgb, 1);

}
