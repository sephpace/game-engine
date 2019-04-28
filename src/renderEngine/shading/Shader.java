package renderEngine.shading;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL20;

/**
 * An abstract shader.
 *
 * @author Seph Pace
 */
public class Shader {

    // The ID of the shader
    private int id;

    /**
     * Constructor.
     *
     * @param fileName The name of the GLSL shader file
     * @param type     The type of the shader
     */
    public Shader(String fileName, int type) {
        // Build the GLSL source from the given file
        StringBuilder source = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/renderEngine/shading/" + fileName));
            String line;
            while((line = reader.readLine()) != null) {
                source.append(line);
                source.append("\n");
            }
            reader.close();
        }
        catch(IOException e) {
            System.err.println("Could not read file");
            e.printStackTrace();
            System.exit(-1);
        }

        // Set up the shader on the GPU
        this.id = GL20.glCreateShader(type);
        GL20.glShaderSource(this.id, source);
        GL20.glCompileShader(this.id);
    }

    /**
     * Returns the ID of the shader.
     *
     * @return The ID of the shader
     */
    public int getID() {return this.id;}

    /**
     * Destroys the shader
     */
    public void cleanUp() {
        GL20.glDeleteShader(this.id);
    }
}
