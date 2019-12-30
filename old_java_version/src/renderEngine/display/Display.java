package renderEngine.display;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import renderEngine.Texture;
import renderEngine.VAO;
import renderEngine.VBO;
import renderEngine.shading.Shader;
import renderEngine.shading.ShaderProgram;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * The LWJGL display.
 *
 * @author Seph Pace
 * @since  2019-04-18
 */
public class Display {

    // The width of the display in pixels
    private float width;

    // The height of the display in pixels
    private float height;

    // The window handle
    private long window;

    // The VAO for displaying the ray marched pixels
    private VAO displayQuad;

    // The indices of the quad
    private VBO indicesVBO;

    // The main shader program for the display
    private ShaderProgram shaderProgram;

    /**
     * Constructor.
     *
     * @param width  The width of the display window
     * @param height The height of the display window
     */
    public Display(int width, int height) {
        this.width = width;
        this.height = height;

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if(!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation

        // Create the window
        this.window = glfwCreateWindow(width, height, "Game Engine", NULL, NULL);
        if(window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Get the thread stack and push a new frame
        try(MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Set up the VBO data
        float[] vertices = {
                -1f, 1f, 0f, 1f,
                -1f, -1f, 0f, 1f,
                1f, -1f, 0f, 1f,
                1f, 1f, 0f, 1f
        };

        byte[] indices = {
                0, 1, 2,
                2, 3, 0
        };

        // Set up the VBOs
        VBO[] vbos = {new VBO(vertices, 4, 4)};
        indicesVBO = new VBO(indices, 1, 6);

        // Create the display VAO
        displayQuad = new VAO(vbos);

        // Set up the main shader program
        Shader[] shaders = {new Shader("vertex.glsl", GL20.GL_VERTEX_SHADER),
                            new Shader("fragment.glsl", GL20.GL_FRAGMENT_SHADER)};
        String[] programInputs = {"vertex_Position", "screen_Position"};
        shaderProgram = new ShaderProgram(shaders, programInputs);
    }

    /**
     * Cleans up the display and all of the openGL objects associated with it.
     */
    public void cleanup() {
        // Clean up the shader program
        shaderProgram.cleanup();

        // Clean up the display quad
        displayQuad.cleanup();
        indicesVBO.cleanup();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    /**
     * Returns true if the window should close (i.e. the 'X' at the top corner has been clicked, etc).
     *
     * @return  True if the window should close and false otherwise
     */
    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    /**
     * Updates the display.  Should be called once per frame.
     */
    public void update() {
        // Clear the framebuffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Use the shader program
        GL20.glUseProgram(shaderProgram.getID());

        // Pass the uniforms to the shader program
        int widthLoc = GL20.glGetUniformLocation(shaderProgram.getID(), "screen_Width");
        GL20.glUniform1f(widthLoc, this.width);
        int heightLoc = GL20.glGetUniformLocation(shaderProgram.getID(), "screen_Height");
        GL20.glUniform1f(heightLoc, this.height);

        // Draw the display quad on the screen
        GL30.glBindVertexArray(displayQuad.getID());
        GL20.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesVBO.getID());
        GL11.glDrawElements(GL15.GL_TRIANGLES, indicesVBO.getVertexCount(), GL11.GL_UNSIGNED_BYTE, 0);

        // Return everything to default
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        GL20.glUseProgram(0);

        // Swap the color buffers
        glfwSwapBuffers(window);

        // Poll for events
        glfwPollEvents();
    }
}
