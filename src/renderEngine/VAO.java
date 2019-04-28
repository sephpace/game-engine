package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * An openGL Vertex Array Object.
 *
 * @author Seph Pace
 */
public class VAO {

    // The VAO's ID
    private int id;

    // The ids for each vbo
    private VBO[] vbos;

    /**
     * Constructor.
     *
     * @param vbos A list of Vertex Buffer Objects
     */
    public VAO(VBO[] vbos) {
        this.vbos = vbos;

        // Create and bind the VAO
        this.id = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(this.id);

        // Put the VBOs into the VAO
        for(int i = 0; i < this.vbos.length; i++) {
            vbos[i].addAt(i);
        }

        // Unbind the VAO
        GL30.glBindVertexArray(0);
    }

    /**
     * Delete the VAO and all of it's VBOs
     */
    public void cleanup() {
        // Delete each VBO
        for(int i = 0; i < this.vbos.length; i++) {
            // Disable the VAO index for the vbo
            GL20.glDisableVertexAttribArray(i);

            // Delete the VBO
            this.vbos[i].cleanup();
        }

        // Unbind the VAO
        GL30.glBindVertexArray(0);

        // Delete the VAO
        GL30.glDeleteVertexArrays(this.id);
    }

    /**
     * Returns the ID of the VAO.
     *
     * @return The ID of the VAO
     */
    public int getID() {return this.id;}

    /**
     * Returns the amount of VBOs contained in the VAO.
     *
     * @return The amount of VBOs contained in the VAO
     */
    public int getVBOCount() {return this.vbos.length;}

    public void render() {
        // TODO: Delete this method if I find out I don't need it later
        // Bind the VAO
        GL30.glBindVertexArray(this.id);

        // Render vertices contained in VBOs of type "position"
        for(int i = 0; i < this.vbos.length; i++) {
            if(this.vbos[i].getType().equals("position")) {
                // Enable the VAO index
                GL20.glEnableVertexAttribArray(i);

                // Draw the vertices
                GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, this.vbos[i].getVertexCount());

                // Disable the VAO index
                GL20.glDisableVertexAttribArray(i);
            }
        }

        // Unbind the VAO
        GL30.glBindVertexArray(0);
    }
}
