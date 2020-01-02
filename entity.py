
from abc import ABC

from glumpy import gl, gloo
import numpy as np

from constants import SCREEN_WIDTH, SCREEN_HEIGHT, ZOOM


class Entity(ABC):
    """
    An abstract entity class.

    An entity is an object that can move.

    Attributes:
    position (ndarray): The position of the entity in 3D space.
    velocity (ndarray): The velocity of the entity in 3D space.
    rotation (ndarray): A vector containing angles of rotation for the x, y, and z axes.
    speed (float):      The speed of the entity's movement.
    """

    def __init__(self, position=np.zeros(3), velocity=np.zeros(3), rotation=np.zeros(3), speed=0.1):
        """
        Constructor.

        Parameters:
        position (ndarray): The position of the entity in 3D space.
        velocity (ndarray): The velocity of the entity in 3D space.
        rotation (ndarray): A vector containing angles of rotation for the x, y, and z axes.
        speed (float):      The speed of the entity's movement.
        """
        self.position = position
        self.velocity = velocity
        self.rotation = rotation
        self.speed = speed

    def update(self, delta):
        """
        Code to be called every frame.
        """
        self.position += self.velocity * delta * 100


class Camera(Entity):
    """
    A camera used as a viewpoint into 3D space.

    Used as the origin point in ray marching.
    """

    def __init__(self, position=np.zeros(3), velocity=np.zeros(3), rotation=np.zeros(3), zoom=ZOOM, width=SCREEN_WIDTH, height=SCREEN_HEIGHT):
        """
        Constructor.

        Parameters:
        position (ndarray):  The position of the entity in 3D space.
        velocity (ndarray):  The velocity of the entity in 3D space.
        rotation (ndarray):  A vector containing angles (radians) of rotation for the x, y, and z axes.
        zoom (float):        The zoom of the camera.
        width (float):       The width of the screen in 3D space.
        height (float):      The height of the screen in 3D space.
        """
        super(Camera, self).__init__(position, velocity, rotation)
        self.zoom = zoom
        self.width = width
        self.height = height

        # Setup shader program
        with open('shaders/vertex.glsl') as file:
            vertex = file.read()
        with open('shaders/fragment.glsl') as file:
            fragment = file.read()
        self.screen = gloo.Program(vertex, fragment, count=4)
        self.screen['position'] = [(-1.0, -1.0),
                                   (-1.0, +1.0),
                                   (+1.0, -1.0),
                                   (+1.0, +1.0)]
        self.screen['corner'] = np.array([[-width / 2, -height / 2, self.zoom],
                                          [-width / 2, +height / 2, self.zoom],
                                          [+width / 2, -height / 2, self.zoom],
                                          [+width / 2, +height / 2, self.zoom]])

        # Movement booleans
        self.move_forward = False
        self.move_left = False
        self.move_backward = False
        self.move_right = False
        self.move_up = False
        self.move_down = False

    def update(self, delta):
        """
        Code to be called every frame.
        """
        super(Camera, self).update(delta)
        self.screen['camera'] = self.position
        self.screen['rotation'] = self.rotation
        self.screen.draw(gl.GL_TRIANGLE_STRIP)

        # Movement
        if self.move_forward:
            rot = self.rotation[1] + np.pi / 2
            self.position += np.array([np.cos(rot), 0.0, np.sin(rot)]) * self.speed
        if self.move_left:
            rot = self.rotation[1] - np.pi
            self.position += np.array([np.cos(rot), 0.0, np.sin(rot)]) * self.speed
        if self.move_backward:
            rot = self.rotation[1] - np.pi / 2
            self.position += np.array([np.cos(rot), 0.0, np.sin(rot)]) * self.speed
        if self.move_right:
            rot = self.rotation[1]
            self.position += np.array([np.cos(rot), 0.0, np.sin(rot)]) * self.speed
        if self.move_up:
            self.position += np.array([0.0, self.speed, 0.0])
        if self.move_down:
            self.position -= np.array([0.0, self.speed, 0.0])
