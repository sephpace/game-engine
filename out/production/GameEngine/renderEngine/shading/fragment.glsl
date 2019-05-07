#version 150 core

// FRAGMENT SHADER


// The maximum amount of steps the ray will march
float MAX_STEPS = 100;

// The maximum distance the ray will travel
float MAX_DISTANCE = 100.0;

// The minimum distance to a surface before it is considered a "hit"
float MIN_SURFACE_DISTANCE = 0.01;

// The positions of each pixel of the screen.
in vec4 uv;

// The color of each pixel of the screen
out vec4 color;

// Returns the minimum distance to the scene.
//
// point: The point to test the distance from
float getDistance(vec3 point) {
    vec4 sphere = vec4(0, 1, 2, 1);
    return length(point - sphere.xyz) - sphere.w;
}


// Returns the distance traveled by the ray.
//
// origin:    The point of origin for the ray
// direction: The direction the ray is travelling in
float rayMarch(vec3 origin, vec3 direction) {
    float distance = 0;

    for(int i = 0; i < MAX_STEPS; i++) {
        vec3 position = origin + direction * distance;
        float surface_Distance = getDistance(position);
        distance += surface_Distance;

        if(distance > MAX_DISTANCE || surface_Distance < MIN_SURFACE_DISTANCE) {
            break;
        }
    }

    return distance;
}


// Main method
void main(void) {
    vec3 origin = vec3(0, 1, 0);
    vec3 direction = normalize(vec3(uv.x, uv.y, 1));

    float distance = rayMarch(origin, direction) / 100;
    color = vec4(vec3(distance), 1);
}