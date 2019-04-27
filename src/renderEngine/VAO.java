package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.*;
import java.security.InvalidParameterException;

/**
 * An openGL Vertex Array Object.
 *
 * @author Seph Pace
 */
public class VAO {

    // The VAO's ID
    private int id;

    // The ids for each vbo
    private int[] vboIds;

    private int vertexCount;

    /**
     * Constructor.
     *
     * @param vbos  A list of buffers to be turned into vbos
     * @param sizes The size of the vectors for each vbo
     */
    public VAO(Buffer[] vbos, int[] sizes, int vertexCount) {
        this.vertexCount = vertexCount;

        // Create and bind the VAO
        this.id = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(this.id);

        // Create the VBOs and put them into the VAO
        vboIds = new int[vbos.length];
        for(int i = 0; i < vbos.length; i++) {
            // Create and bind the VBO
            vboIds[i] = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboIds[i]);

            // Put the correct data type into the VBO
            if(vbos[i] instanceof ByteBuffer) {
                ByteBuffer buffer = (ByteBuffer)vbos[i];
                GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
                GL20.glVertexAttribPointer(i, sizes[i], GL11.GL_BYTE, false, 0, 0);
            }
            else if(vbos[i] instanceof DoubleBuffer) {
                DoubleBuffer buffer = (DoubleBuffer)vbos[i];
                GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
                GL20.glVertexAttribPointer(i, sizes[i], GL11.GL_DOUBLE, false, 0, 0);
            }
            else if(vbos[i] instanceof FloatBuffer) {
                FloatBuffer buffer = (FloatBuffer)vbos[i];
                GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
                GL20.glVertexAttribPointer(i, sizes[i], GL11.GL_FLOAT, false, 0, 0);
            }
            else if(vbos[i] instanceof IntBuffer) {
                IntBuffer buffer = (IntBuffer)vbos[i];
                GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
                GL20.glVertexAttribPointer(i, sizes[i], GL11.GL_INT, false, 0, 0);
            }
            else {
                throw new InvalidParameterException("Invalid buffer type!");
            }

            // Unbind the VBO
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        }

        // Unbind the VAO
        GL30.glBindVertexArray(0);
    }

    /**
     * Delete the VAO and all of it's VBOs
     */
    public void cleanup() {
        // Delete each VBO
        for(int i = 0; i < this.vboIds.length; i++) {
            // Disable the VAO index for the vbo
            GL20.glDisableVertexAttribArray(i);

            // Unbind the VBO
            GL15.glBindBuffer(GL15.GL_VERTEX_ARRAY, 0);

            // Delete the VBO
            GL15.glDeleteBuffers(this.vboIds[i]);
        }

        // Unbind the VAO
        GL30.glBindVertexArray(0);

        // Delete the VAO
        GL30.glDeleteVertexArrays(this.id);
    }

    /**
     * Returns the amount of vertices in the VAO.
     *
     * @return The amount of vertices in the VAO
     */
    public int getVertexCount() {return this.vertexCount;}

    public void render() {
        // Bind the VAO
        GL30.glBindVertexArray(this.id);

        // TODO: Once there's a VBO class, get the type from it and only draw if it is of the type "vertex"

        // Enable the VAO index
        GL20.glEnableVertexAttribArray(0);

        // Draw the vertices
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, this.vertexCount);

        // Disable the VAO index
        GL20.glDisableVertexAttribArray(0);

        // Unbind the VAO
        GL30.glBindVertexArray(0);
    }
}
