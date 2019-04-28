package renderEngine.shading;

import org.lwjgl.opengl.GL20;

/**
 * An openGL shader program that links one or more shaders together and executes the GLSL files.
 *
 * If there is more than one shader given, they are linked in the order they are found in the array.
 *
 * @author Seph Pace
 */
public class ShaderProgram {

    // The ID of the shader program
    private int id;

    // The shaders used in the shader program
    private Shader[] shaders;

    /**
     * Constructor.  Creates a program with a single shader.
     *
     * @param shader  The shader to use in the shader program
     * @param inputs  The names of the inputs of the shader
     */
    public ShaderProgram(Shader shader, String[] inputs) {
        this.shaders = new Shader[1];
        this.shaders[0] = shader;

        // Create the program
        this.id = GL20.glCreateProgram();

        // Attach the shader to the program
        GL20.glAttachShader(this.id, shader.getID());

        // Set up the inputs
        for(int i = 0; i < inputs.length; i++) {
            GL20.glBindAttribLocation(this.id, i, inputs[i]);
        }

        // Link and validate the shader program
        GL20.glLinkProgram(this.id);
        GL20.glValidateProgram(this.id);
    }

    /**
     * Constructor.  Creates a program with multiple shaders.
     *
     * @param shaders  The shaders to use in the shader program
     * @param inputs  The names of the inputs of the first shader in the array of shaders
     */
    public ShaderProgram(Shader[] shaders, String[] inputs) {
        this.shaders = shaders;

        // Create the program
        this.id = GL20.glCreateProgram();

        // Attach the shaders to the program
        for(Shader shader : shaders) {
            GL20.glAttachShader(this.id, shader.getID());
        }

        // Set up the inputs
        for(int i = 0; i < inputs.length; i++) {
            GL20.glBindAttribLocation(this.id, i, inputs[i]);
        }

        // Link and validate the shader program
        GL20.glLinkProgram(this.id);
        GL20.glValidateProgram(this.id);
    }

    /**
     * Returns the id of the shader program.
     *
     * @return The id of the shader program
     */
    public int getID(){return this.id;}

    public void cleanup() {
        // Unbind the shader program
        GL20.glUseProgram(0);

        // Detach and delete the shaders
        for(Shader shader : shaders) {
            GL20.glDetachShader(this.id, shader.getID());
            shader.cleanUp();
        }

        // Delete the shader program
        GL20.glDeleteProgram(this.id);
    }
}
