#version 330

in vec2 passTexCoord;

out vec4 colour;

uniform sampler2D tex;

uniform vec4 uni_colour;

const float width = 0.5f;
const float edge = 0.1f;

void main() {

	float distanceFromCenter = 1.0f - texture(tex, passTexCoord).a;
	float newAlpha = 1.0f - smoothstep(width, width + edge, distanceFromCenter);
	
	//vec4 col = texture(tex, passTexCoord);
	if (newAlpha == 0.0f) {
		discard;
	}
	
	colour = vec4(uni_colour.rgb, newAlpha);

}
