
from glumpy import app, gloo, gl
from pyglet.window import key
import numpy as np

from entity import Camera
from constants import WINDOW_WIDTH, WINDOW_HEIGHT, SPEED

window = app.Window(WINDOW_WIDTH, WINDOW_HEIGHT)

with open('shaders/vertex.glsl') as file:
    vertex = file.read()

with open('shaders/fragment.glsl') as file:
    fragment = file.read()

quad = gloo.Program(vertex, fragment, count=4)

quad['position'] = [(-1.0, -1.0),
                    (-1.0, +1.0),
                    (+1.0, -1.0),
                    (+1.0, +1.0)]

camera = Camera(position=np.array([0.0, 0.0, -5.0]))


@window.event
def on_draw(dt):
    camera.update(dt)
    quad['camera'] = camera.position
    quad['screen_Position'] = camera.get_screen()

    window.clear()
    quad.draw(gl.GL_TRIANGLE_STRIP)


@window.event
def on_key_press(symbol, modifiers):
    if symbol == key.W:
        camera.velocity += np.array([0.0, 0.0, +SPEED])  # Forward
    elif symbol == key.A:
        camera.velocity += np.array([-SPEED, 0.0, 0.0])  # Left
    elif symbol == key.S:
        camera.velocity += np.array([0.0, 0.0, -SPEED])  # Backward
    elif symbol == key.D:
        camera.velocity += np.array([+SPEED, 0.0, 0.0])  # Right
    elif symbol == key.SPACE:
        camera.velocity += np.array([0.0, +SPEED, 0.0])  # Up
    elif symbol == key.LSHIFT:
        camera.velocity += np.array([0.0, -SPEED, 0.0])  # Down


@window.event
def on_key_release(symbol, modifiers):
    if symbol == key.W:
        camera.velocity += np.array([0.0, 0.0, -SPEED])  # Forward
    elif symbol == key.A:
        camera.velocity += np.array([+SPEED, 0.0, 0.0])  # Left
    elif symbol == key.S:
        camera.velocity += np.array([0.0, 0.0, +SPEED])  # Backward
    elif symbol == key.D:
        camera.velocity += np.array([-SPEED, 0.0, 0.0])  # Right
    elif symbol == key.SPACE:
        camera.velocity += np.array([0.0, -SPEED, 0.0])  # Up
    elif symbol == key.LSHIFT:
        camera.velocity += np.array([0.0, +SPEED, 0.0])  # Down


app.run()
