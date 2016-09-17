#version 300 es

in vec2 texCoord0;
in vec3 normal0;

out vec4 fragColor;

uniform vec3 color;
uniform sampler2D sampler;

void main() {

	vec4 textureColor = texture(sampler, texCoord0);
	// if(textureColor == vec4(0, 0, 0, 0))
	// 	fragColor = vec4(color, 1.0);
	// else
	fragColor = vec4(color, 1.0);

}