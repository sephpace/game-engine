
from abc import ABC

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

    def __init__(self, position=np.zeros(3), velocity=np.zeros(3), rotation=np.zeros(3), zoom=ZOOM):
        """
        Constructor.

        Parameters:
        position (ndarray): The position of the entity in 3D space.
        velocity (ndarray): The velocity of the entity in 3D space.
        rotation (ndarray): A vector containing angles (radians) of rotation for the x, y, and z axes.
        """
        super(Camera, self).__init__(position, velocity, rotation)
        self.zoom = zoom
        self.__screen = np.array([[-SCREEN_WIDTH / 2, -SCREEN_HEIGHT / 2, self.zoom],
                                  [-SCREEN_WIDTH / 2, +SCREEN_HEIGHT / 2, self.zoom],
                                  [+SCREEN_WIDTH / 2, -SCREEN_HEIGHT / 2, self.zoom],
                                  [+SCREEN_WIDTH / 2, +SCREEN_HEIGHT / 2, self.zoom]])

    def get_screen(self):
        screen = self.__screen.copy()
        rm = rot_mat(self.rotation)
        for i in range(len(screen)):
            screen[i] = np.matmul(rm, screen[i])
        screen += self.position
        return screen


def rot_mat(rot):
    a, b, c = rot
    return np.matmul(np.matmul(rz(c), ry(b)), rx(a))


def rx(theta):
    sin, cos = np.sin(theta), np.cos(theta)
    return np.array([[1, 0, 0], [0, cos, -sin], [0, sin, cos]])


def ry(theta):
    sin, cos = np.sin(theta), np.cos(theta)
    return np.array([[cos, 0, sin], [0, 1, 0], [-sin, 0, cos]])


def rz(theta):
    sin, cos = np.sin(theta), np.cos(theta)
    return np.array([[cos, -sin, 0], [sin, cos, 0], [0, 0, 1]])
