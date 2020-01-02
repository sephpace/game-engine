
from glumpy import app
from glumpy.app.window import key
import numpy as np

from entity import Camera
from constants import WINDOW_WIDTH, WINDOW_HEIGHT

app.use('pyglet')
window = app.Window(WINDOW_WIDTH, WINDOW_HEIGHT)

camera = Camera(position=np.array([0.0, 0.0, 0.0]))


@window.event
def on_draw(dt):
    window.clear()
    camera.update(dt)


@window.event
def on_key_press(symbol, modifiers):
    if symbol == key.ESCAPE:
        window.close()
        exit()

    if symbol == key.W:
        camera.move_forward = True
    elif symbol == key.A:
        camera.move_left = True
    elif symbol == key.S:
        camera.move_backward = True
    elif symbol == key.D:
        camera.move_right = True
    elif symbol == key.SPACE:
        camera.move_up = True
    elif symbol == key.LSHIFT:
        camera.move_down = True


@window.event
def on_key_release(symbol, modifiers):
    if symbol == key.W:
        camera.move_forward = False
    elif symbol == key.A:
        camera.move_left = False
    elif symbol == key.S:
        camera.move_backward = False
    elif symbol == key.D:
        camera.move_right = False
    elif symbol == key.SPACE:
        camera.move_up = False
    elif symbol == key.LSHIFT:
        camera.move_down = False


@window.event
def on_mouse_motion(x, y, dx, dy):
    camera.rotation -= np.array([dy, dx, 0]) * np.pi / 500
    camera.rotation[1] = np.clip(camera.rotation[1], -np.pi, np.pi)  # TODO: Fix this so it actually clamps the values correctly


app.run()
