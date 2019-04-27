package renderEngine.display;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import renderEngine.VAO;
import renderEngine.shading.FragmentShader;
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

    // The window handle
    private long window;

    private VAO displayQuad;

    // The main shader program for the display
    private ShaderProgram shaderProgram;

    /**
     * Constructor.
     *
     * @param width  The width of the display window
     * @param height The height of the display window
     */
    public Display(int width, int height) {
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

        // Set up the display vao
        // TODO: Replace this code with object oriented vao code at some point
        Buffer[] buffers = new Buffer[1];
        int[] sizes = new int[1];
        float[] vertices = {
                -1f, 1f, 0f, 1f,
                -1f, -1f, 0f, 1f,
                1f, -1f, 0f, 1f,
                1f, -1f, 0f, 1f,
                1f, 1f, 0f, 1f,
                -1f, 1f, 0f, 1f
        };
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices);
        verticesBuffer.flip();
        buffers[0] = verticesBuffer;
        sizes[0] = 4;

//        byte[] indices = {
//                0, 1, 2,
//                2, 3, 0
//        };
//        ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indices.length);
//        indicesBuffer.put(indices);
//        indicesBuffer.flip();
//        buffers[1] = indicesBuffer;

        displayQuad = new VAO(buffers, sizes, 6);

        // Set up the main shader program
        FragmentShader fragmentShader = new FragmentShader("fragment.glsl");
        String[] programInputs = {};
        shaderProgram = new ShaderProgram(fragmentShader, programInputs);
    }

    /**
     * Cleans up the display by terminating GLFW.
     */
    public void cleanup() {
        // Clean up the shader program
        shaderProgram.cleanup();

        // Clean up the display quad
        displayQuad.cleanup();

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
//        GL20.glUseProgram(shaderProgram.getID());

        // Draw the display quad on the screen
        displayQuad.render();

        // Return everything to default
//        GL20.glUseProgram(0);

        // Swap the color buffers
        glfwSwapBuffers(window);

        // Poll for events
        glfwPollEvents();
    }
}
