package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * An openGL Texture object.
 *
 * @author Seph Pace
 */
public class Texture {

    // The ID of the texture
    private int id;

    /**
     * Constructor.  Loads a texture from a png file located in the "res" folder
     *
     * @param fileName The name of the png file to load as a texture
     */
    public Texture(String fileName) {
        try {
            // Load the buffered image
            BufferedImage textureImage = ImageIO.read(new File("res/" + fileName));

            // Put the RGBA info from the buffered image into a byte buffer
            int i = 0;
            ByteBuffer pixels = ByteBuffer.allocateDirect(textureImage.getHeight() * textureImage.getHeight() * 4);
            for(int t = 0; t < textureImage.getWidth(); t++) {
                for(int s = 0; s < textureImage.getWidth(); s++) {
                    pixels.put(i++, (byte)(((textureImage.getRGB(s, t) >> 16) & 0xFF))); // Red
                    pixels.put(i++, (byte)((textureImage.getRGB(s, t) >> 8) & 0xFF));    // Green
                    pixels.put(i++, (byte)(textureImage.getRGB(s, t) & 0xFF));           // Blue
                    pixels.put(i++, (byte)1);                                            // Alpha
                }
            }
            pixels.flip();

            // Create the texture object and bind it
            this.id = GL11.glGenTextures();
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL13.glBindTexture(GL11.GL_TEXTURE_2D, this.id);

            // Set up the pixel storage alignment
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

            // Upload the texture data and generate mip maps
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, textureImage.getWidth(),
                    textureImage.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

            // Setup the ST coordinate system
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

            // Setup what to do when the texture has to be scaled
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the texture.
     */
    public void cleanup() {GL11.glDeleteTextures(this.id);}

    /**
     * Returns the ID of the texture.
     *
     * @return The ID of the texture
     */
    public int getID() {return this.id;}
}
