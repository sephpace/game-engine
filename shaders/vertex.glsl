
// The position of each vertex in the quad
attribute vec2 position;

// The position of each corner of the screen in 3D space
attribute vec3 corner;

// The direction between the camera and each pixel of the screen
varying vec3 direction;

// The position of the camera
uniform vec3 camera;

// The rotation of the camera
uniform vec3 rotation;


// Returns a rotation matrix of the given angle for the x-axis
mat3 rotx(float theta) {
    return mat3(vec3(1, 0, 0), vec3(0, cos(theta), -sin(theta)), vec3(0, sin(theta), cos(theta)));
}

// Returns a rotation matrix of the given angle for the y-axis
mat3 roty(float theta) {
    return mat3(vec3(cos(theta), 0, sin(theta)), vec3(0, 1, 0), vec3(-sin(theta), 0, cos(theta)));
}

// Returns a rotation matrix of the given angle for the z-axis
mat3 rotz(float theta) {
    return mat3(vec3(cos(theta), -sin(theta), 0), vec3(sin(theta), cos(theta), 0), vec3(0, 0, 1));
}

// Returns a general rotation matrix with the given angles a, b, c
mat3 rot(float a, float b, float c) {
    return rotz(c) * roty(b) * rotx(a);
}

// Main method.
void main(void) {
    gl_Position = vec4(position, 0.0, 1.0);
    mat3 rm = rot(rotation.x, rotation.y, rotation.z);
    direction = normalize(rm * corner);
}
