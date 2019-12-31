
from abc import ABC

from glumpy import gl, gloo
import numpy as np

from constants import SCREEN_WIDTH, SCREEN_HEIGHT, ZOOM


class Entity(ABC):
    """
    An abstract entity class.

    An entity is an object that can move.

    Attributes:
    __position (ndarray): The position of the entity in 3D space.
    __velocity (ndarray): The velocity of the entity in 3D space.
    __rotation (ndarray): A vector containing angles of rotation for the x, y, and z axes.
    """

    def __init__(self, position=np.zeros(3), velocity=np.zeros(3), rotation=np.zeros(3)):
        """
        Constructor.

        Parameters:
        position (ndarray): The position of the entity in 3D space.
        velocity (ndarray): The velocity of the entity in 3D space.
        rotation (ndarray): A vector containing angles of rotation for the x, y, and z axes.
        """
        self.position = position
        self.velocity = velocity
        self.rotation = rotation

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

    def __init__(self, position=np.zeros(3), velocity=np.zeros(3), rotation=np.zeros(3), direction=np.zeros(2), zoom=ZOOM, width=SCREEN_WIDTH, height=SCREEN_HEIGHT):
        """
        Constructor.

        Parameters:
        position (ndarray):  The position of the entity in 3D space.
        velocity (ndarray):  The velocity of the entity in 3D space.
        rotation (ndarray):  A vector containing angles (radians) of rotation for the x, y, and z axes.
        direction (ndarray): The rotation and elevation angles denoting the direction the camera is facing.
        zoom (float):        The zoom of the camera.
        width (float):       The width of the screen in 3D space.
        height (float):      The height of the screen in 3D space.
        """
        super(Camera, self).__init__(position, velocity, rotation)
        self.direction = direction
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

    def update(self, delta):
        """
        Code to be called every frame.
        """
        super(Camera, self).update(delta)
        theta, phi = self.direction
        self.rotation = np.array([np.cos(theta) * np.sin(phi),
                                  np.sin(theta) * np.sin(phi),
                                  -np.cos(phi)])
        self.screen['camera'] = self.position
        self.screen['rotation'] = self.rotation
        self.screen.draw(gl.GL_TRIANGLE_STRIP)
