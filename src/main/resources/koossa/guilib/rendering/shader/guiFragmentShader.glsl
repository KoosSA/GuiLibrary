#version 330

in vec3 passTexCoord;
in vec4 passColor;
in float passType;

out vec4 color;

uniform sampler2DArray tex;

const float texWidth = 0.5f;
const float texEdge = 0.1f;

void main() {
	
	if (passType == 1.0f) {
		float distanceFromCenter = 1.0f - texture(tex, passTexCoord).a;
		float newAlpha = 1.0f - smoothstep(texWidth, texWidth + texEdge, distanceFromCenter);

		color = vec4(passColor.rgb, newAlpha);
	} else {
		if (passTexCoord.z == -1) {
			color = passColor;
		} else {
			color = mix(texture(tex, passTexCoord), passColor, passColor.a);
		}
	}

}
