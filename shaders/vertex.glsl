
// The position of each vertex in the quad.
attribute vec2 position;

// The position of each corner of the screen in 3D space.
attribute vec3 screen_Position;

// The direction between the camera and each pixel of the screen.
varying vec3 direction;

// The position of the camera.
uniform vec3 camera;


// Main method.
void main(void) {
    gl_Position = vec4(position, 0.0, 1.0);
    direction = normalize(screen_Position - camera);
}
