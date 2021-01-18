package data;
import fr.umlv.zen5.KeyboardKey;
import assets.PathAssets;

import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * This class manage the information of the elements use in a board,
 * this is also a way to dissociate a bloc from a text.
 */
public class Bloc extends Element{

    private final Noun noun;

    /**
     * Create an element with his width, height, identified with the his given noun and associate with an image.
     * @param width of the bloc in the screen
     * @param height of the bloc in the screen
     * @param noun which designed the bloc
     * @param image for the graphic representation of the bloc
     */
    public Bloc(int width, int height, Noun noun, BufferedImage image){
        super(width, height, image);
        this.noun = Objects.requireNonNull(noun);

    }

    /**
     * Redefinition of the getExtensionImage
     * @return the name of the image
     */
    @Override
    public String getExtensionImage() {
        return noun == Noun.BABA ? "Baba" + getDirection().name() + ".jpg" : noun.getName() + ".png";
    }

    /**
     * Redefinition of the getData method
     * @return the material (here the noun)
     */
    @Override
    public Material getData() {
        return noun;
    }


    /**
     * Redefinition of the method toString
     * @return image name
     */
    @Override
    public String toString() {
        return "Bloc : " + getExtensionImage();
    }

    /**
     * Redefinition of the hasSameMaterial
     * Check if these Materials are the same
     * @param toCompare : material to compare with the current one.
     * @return true if they are the same and false otherwise
     */
    @Override
    public boolean hasSameMaterial(Material toCompare) {
        return toCompare == noun;
    }


    /**
     * Redefintion of the method isText
     * @return False because it's not a text
     */
    @Override
    public boolean isText() {
        return false;
    }

    /**
     * Redefinition of the equals method
     * @param o the other Bloc
     * @return true if two blocs are equal and false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Bloc bloc = (Bloc) o;
        return noun == bloc.noun;
    }

    /**
     * Redefinition of the hashcode method
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), noun);
    }


    /**
     * @param action to know which image the bloc "Baba" has to switch with.
     */
    public void changeDirectionBaba(KeyboardKey action){
        if (noun == Noun.BABA) {
            PathAssets assets = new PathAssets();
            var dir = getDirectionByAction(action);
            switchImage(assets.initImage(PathAssets.pathBabaDir(dir)));
        }
    }
}
