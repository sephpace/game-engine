#version 150 core

// VERTEX SHADER


// The position of each vertex in the quad.
in vec4 vertex_Position;

// The position of each pixel of the screen.
out vec4 uv;

// Main method
void main(void) {
    gl_Position = vertex_Position;
    uv = vertex_Position;
}