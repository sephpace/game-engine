#version 150 core

// VERTEX SHADER


// The distance between each pixel of the screen
float PIXEL_GAP = 0.005;

// The position of each vertex in the quad.
in vec4 vertex_Position;

// The position of each pixel of the screen.
out vec2 uv;

// The width of the screen
uniform float screen_Width;

// The height of the screen
uniform float screen_Height;


// Main method
void main(void) {
    gl_Position = vertex_Position;
    uv = vec2(screen_Width * PIXEL_GAP * vertex_Position.x, screen_Height * PIXEL_GAP * vertex_Position.y);
}