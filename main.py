
from glumpy import app, gloo, gl
from pyglet.window import key

WINDOW_WIDTH = 700
WINDOW_HEIGHT = 500

SCREEN_WIDTH = WINDOW_WIDTH / 200
SCREEN_HEIGHT = WINDOW_HEIGHT / 200

ZOOM = 7

SPEED = 0.1

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

velocity = (0.0, 0.0, 0.0)


def set_pos(pos):
    quad['camera'] = pos
    x, y, z = pos
    quad['screen_Position'] = [(x - SCREEN_WIDTH, y - SCREEN_HEIGHT, z + ZOOM),
                               (x - SCREEN_WIDTH, y + SCREEN_HEIGHT, z + ZOOM),
                               (x + SCREEN_WIDTH, y - SCREEN_HEIGHT, z + ZOOM),
                               (x + SCREEN_WIDTH, y + SCREEN_HEIGHT, z + ZOOM)]


def set_volocity(v):
    global velocity
    velocity = v


set_pos((0, 0, -5))


@window.event
def on_draw(dt):
    global velocity
    x, y, z = quad['camera']
    vx, vy, vz = velocity
    set_pos((x + vx, y + vy, z + vz))

    window.clear()
    quad.draw(gl.GL_TRIANGLE_STRIP)


@window.event
def on_key_press(symbol, modifiers):
    global velocity
    x, y, z = velocity
    if symbol == key.W:
        velocity = (x, y, z + SPEED)  # Forward
    elif symbol == key.A:
        velocity = (x - SPEED, y, z)  # Left
    elif symbol == key.S:
        velocity = (x, y, z - SPEED)  # Backward
    elif symbol == key.D:
        velocity = (x + SPEED, y, z)  # Right
    elif symbol == key.SPACE:
        velocity = (x, y + SPEED, z)  # Up
    elif symbol == key.LSHIFT:
        velocity = (x, y - SPEED, z)  # Down


@window.event
def on_key_release(symbol, modifiers):
    global velocity
    x, y, z = velocity
    if symbol == key.W:
        velocity = (x, y, z - SPEED)  # Forward
    elif symbol == key.A:
        velocity = (x + SPEED, y, z)  # Left
    elif symbol == key.S:
        velocity = (x, y, z + SPEED)  # Backward
    elif symbol == key.D:
        velocity = (x - SPEED, y, z)  # Right
    elif symbol == key.SPACE:
        velocity = (x, y - SPEED, z)  # Up
    elif symbol == key.LSHIFT:
        velocity = (x, y + SPEED, z)  # Down


app.run()
