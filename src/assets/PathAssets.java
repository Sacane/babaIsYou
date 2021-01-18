package assets;
import data.Material;
import data.Noun;
import data.Property;
import deplacement.Direction;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * this Class is just a way to have an access to all the assets use in the game.
 */
public class PathAssets {



    /**
     * initialise the image of the element taken as a parameter.
     * @param path of the image.
     * @return the buffered image associate to the path.
     */
    public BufferedImage initImage(String path){
        BufferedImage image = null;
        try{
            image = ImageIO.read(new File(Paths.get(System.getProperty("user.dir") + "/src/assets/"+ path).toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * get the path of all the images.
     * @return the path of all the images
     */
    private static String pathImages(){
        return "images/";
    }

    /**
     *  to get the path of an element according to its material.
     * @param material of the image to represent in the board
     * @return the exact path of the images of the bloc in the directory
     */
    public static String pathElement(Material material){
        return pathImages() + "Nouns/" +material.getName() + ".png";
    }

    /**
     * to get a path of a text according to its material.
     * @param material of the image to represent in the board
     * @return the exact path of the images of the text to represent in the board
     */
    public static String pathTextElement(Material material){
        return pathImages() + "Text/string" + material.getName() + ".png";
    }
    
    /**
     * to get the path of the image of Baba according to its direction.
     * @param dir the direction of baba.
     * @return the path of the image represent the back part of Baba
     */
    public static String pathBabaDir(Direction dir){
        return pathImages()+"Babas/Baba"+dir.name()+".jpg";
    }


}
