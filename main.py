
from glumpy import app, gl
from pyglet.window import key
import numpy as np

from entity import Camera
from constants import WINDOW_WIDTH, WINDOW_HEIGHT, SPEED

window = app.Window(WINDOW_WIDTH, WINDOW_HEIGHT)

camera = Camera(position=np.array([0.0, 0.0, -5.0]))


@window.event
def on_draw(dt):
    window.clear()
    camera.update(dt)


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
