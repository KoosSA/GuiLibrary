#version 330

in vec3 passTexCoord;
in vec4 passColor;

out vec4 color;

uniform sampler2DArray tex;

void main() {
	
	if (passTexCoord.b == -1) {
		color = passColor;
	} else {
		color = mix(texture(tex, passTexCoord), passColor, passColor.a);
	}

}
