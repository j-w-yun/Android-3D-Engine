#version 300 es

in vec3 position;
in vec2 texCoord;
in vec3 normal;

uniform mat4 projection;
uniform mat4 modelview;
// uniform mat4 normalMat;

out vec3 vertPos;
out vec3 normalInterp;
out vec2 texCoord0;

void main()
{
    mat4 normalMat = transpose(inverse(modelview));

    gl_Position = projection * vec4(position, 1.0);
    vec4 vertPos4 = modelview * vec4(position, 1.0);

    vertPos = vertPos4.xyz / vertPos4.w;
    normalInterp = vec3(normalMat * vec4(normal, 0.0));
    texCoord0 = texCoord;
}


// in vec3 position;
// in vec2 texCoord;
// in vec3 normal;

// out vec4 forFragColor;
// out vec2 texCoord0;

// uniform mat4 projection, modelview, normalMat;
// uniform int mode;

// const vec3 lightPos = vec3(0.0, 0.0, 1.0);
// const vec3 diffuseColor = vec3(0.5, 0.0, 0.0);
// const vec3 specColor = vec3(1.0, 1.0, 1.0);

// void main(){
//   gl_Position = projection * modelview * vec4(position, 1.0);

//   // all following gemetric computations are performed in the
//   // camera coordinate system (aka eye coordinates)
//   vec3 normal0 = vec3(normalMat * vec4(normal, 0.0));
//   vec4 vertPos4 = modelview * vec4(position, 1.0);
//   vec3 vertPos = vec3(vertPos4) / vertPos4.w;
//   vec3 lightDir = normalize(lightPos - vertPos);
//   vec3 reflectDir = reflect(-lightDir, normal0);
//   vec3 viewDir = normalize(-vertPos);

//   float lambertian = max(dot(lightDir,normal0), 0.0);
//   float specular = 0.0;

//   if(lambertian > 0.0) {
//     float specAngle = max(dot(reflectDir, viewDir), 0.0);
//     specular = pow(specAngle, 4.0);

//     // the exponent controls the shininess (try mode 2)
//     if(mode == 2)  specular = pow(specAngle, 16.0);

//     // according to the rendering equation we would need to multiply
//     // with the the "lambertian", but this has little visual effect
//     if(mode == 3) specular *= lambertian;
//     // switch to mode 4 to turn off the specular component
//     if(mode == 4) specular *= 0.0;
//   }
//   forFragColor = vec4(lambertian*diffuseColor + specular*specColor, 1.0);
// }