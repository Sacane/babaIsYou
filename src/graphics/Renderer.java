package graphics;
import deplacement.Coordinate;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


/**
 * Class to draw or clean the image according to its width, height and position in the screen.
 */
public class Renderer {
    /**
     * draw an Element in the Board according to the coordinate and the width ane height of the element.
     * @param graphics to draw the image in the screen.
     * @param image to draw in the screen.
     * @param width of the screen.
     * @param height of the screen.
     * @param location : Coordinate where we want to draw the image.
     */
    public static void drawElementByCoordinate(Graphics2D graphics, BufferedImage image, int width, int height, Coordinate location){
        graphics.drawImage(image,
                location.getX() * width,
                location.getY() *height,
                width,
                height,
                null);
    }

    /**
     * Clear the display of the image in his previous location according to his height and width.
     * @param graphics to clear the image in the screen.
     * @param width of the screen.
     * @param height of the screen.
     * @param location :  Coordinate where we want to clear the image.
     */
    public static void clearElementByCoordinate(Graphics2D graphics, int width, int height, Coordinate location){
        Rectangle2D screen = new Rectangle2D.Float(0, 0, 0, 0);
        graphics.setColor(Color.BLACK);
        screen.setRect(location.getX() * width, location.getY() * height, width, height);
        graphics.fill(screen);
    }

}
