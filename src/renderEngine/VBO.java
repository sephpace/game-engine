package renderEngine;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.*;

/**
 * An openGL Vertex Buffer Object.
 *
 * @author Seph Pace
 */
public class VBO {

    // The type of data in the VBO
    private int dataType;

    // The ID of the VBO
    private int id;

    // The size of each vector (either 2, 3, or 4)
    private int size;

    // The type of VBO ("position", "color", etc)
    private String type;

    // The amount of vertices in the VBO
    private int vertexCount;

    /**
     * Constructor.  Creates a VBO of bytes.
     *
     * @param type        The type of VBO ("position", "color", etc)
     * @param bufferData  The data to be stored in the VBO
     * @param size        The size of each vector (either 2, 3, or 4)
     * @param vertexCount The amount of vertices in the VBO
     */
    public VBO(String type, byte[] bufferData, int size, int vertexCount) {
        this.dataType = GL11.GL_BYTE;
        this.size = size;
        this.type = type;
        this.vertexCount = vertexCount;

        // Create and bind the VBO
        this.id = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.id);

        // Create an IntBuffer from the given buffer data
        ByteBuffer buffer = BufferUtils.createByteBuffer(bufferData.length);
        buffer.put(bufferData);
        buffer.flip();

        // Put the IntBuffer into the VBO
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

        // Unbind the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Constructor.  Creates a VBO of doubles.
     *
     * @param type        The type of VBO ("position", "color", etc)
     * @param bufferData  The data to be stored in the VBO
     * @param size        The size of each vector (either 2, 3, or 4)
     * @param vertexCount The amount of vertices in the VBO
     */
    public VBO(String type, double[] bufferData, int size, int vertexCount) {
        this.dataType = GL11.GL_DOUBLE;
        this.size = size;
        this.type = type;
        this.vertexCount = vertexCount;

        // Create and bind the VBO
        this.id = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.id);

        // Create an IntBuffer from the given buffer data
        DoubleBuffer buffer = BufferUtils.createDoubleBuffer(bufferData.length);
        buffer.put(bufferData);
        buffer.flip();

        // Put the IntBuffer into the VBO
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

        // Unbind the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Constructor.  Creates a VBO of floats.
     *
     * @param type        The type of VBO ("position", "color", etc)
     * @param bufferData  The data to be stored in the VBO
     * @param size        The size of each vector (either 2, 3, or 4)
     * @param vertexCount The amount of vertices in the VBO
     */
    public VBO(String type, float[] bufferData, int size, int vertexCount) {
        this.dataType = GL11.GL_FLOAT;
        this.size = size;
        this.type = type;
        this.vertexCount = vertexCount;

        // Create and bind the VBO
        this.id = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.id);

        // Create an IntBuffer from the given buffer data
        FloatBuffer buffer = BufferUtils.createFloatBuffer(bufferData.length);
        buffer.put(bufferData);
        buffer.flip();

        // Put the IntBuffer into the VBO
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

        // Unbind the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Constructor.  Creates a VBO of ints.
     *
     * @param type        The type of VBO ("position", "color", etc)
     * @param bufferData  The data to be stored in the VBO
     * @param size        The size of each vector (either 2, 3, or 4)
     * @param vertexCount The amount of vertices in the VBO
     */
    public VBO(String type, int[] bufferData, int size, int vertexCount) {
        this.dataType = GL11.GL_INT;
        this.size = size;
        this.type = type;
        this.vertexCount = vertexCount;

        // Create and bind the VBO
        this.id = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.id);

        // Create an IntBuffer from the given buffer data
        IntBuffer buffer = BufferUtils.createIntBuffer(bufferData.length);
        buffer.put(bufferData);
        buffer.flip();

        // Put the IntBuffer into the VBO
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

        // Unbind the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Adds the VBO to a VAO at the given index.
     *
     * @param index The VAO index to add the VBO to
     */
    public void addAt(int index) {
        // Bind the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.id);

        // Add the VBO to the VAO
        GL20.glVertexAttribPointer(index, this.size, this.dataType, false, 0, 0);

        // Unbind the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Delete the VBO.
     */
    public void cleanup() {
        // Unbind the VBO
        GL15.glBindBuffer(GL15.GL_VERTEX_ARRAY, 0);

        // Delete the VBO
        GL15.glDeleteBuffers(this.id);
    }

    /**
     * Returns the data type of the data in the VBO.
     *
     * @return The data type of the data in the VBO
     */
    public int getDataType() {return this.dataType;}

    /**
     * Returns the ID of the VBO.
     *
     * @return The ID of the VBO
     */
    public int getID() {return this.id;}

    /**
     * Returns the size of each vector in the VBO.
     *
     * @return The size of each vector in the VBO
     */
    public int getSize() {return this.size;}

    /**
     * Returns the type of the VBO.
     *
     * @return The type of the VBO
     */
    public String getType() {return this.type;}

    /**
     * Returns the amount of vertices in the VBO.
     *
     * @return The amount of vertices in the VBO
     */
    public int getVertexCount() {return this.vertexCount;}
}
