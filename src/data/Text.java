package data;
import java.awt.image.BufferedImage;
import java.util.Objects;


/**
 * A Text is an Element of the game that can be materialize, the texts will bring the rules and make the property
 * do specifics things with the nouns.
 */
public class Text extends Element{

    private final Material material;

    /**
     * a Text is an Element
     * @param width of the bloc in the screen
     * @param height of the bloc in the screen
     * @param material which designed the bloc
     * @param image for the graphic representation of the bloc
     */
    public Text(int width, int height, Material material, BufferedImage image){
        super(width, height, image);
        this.material = Objects.requireNonNull(material);
    }

    /**
     * Redefinition of the getExtensionImage
     * @return the name of the image
     */
    @Override
    public String getExtensionImage() {
        if(material == Property.SPREAD || material == Property.IMMUNE){
            return "string" + material.getName() + ".png";
        }
        return "string" + material.getName() + ".gif";
    }

    /**
     * Redefinition of the getData method
     * @return the material (here the text)
     */
    @Override
    public Material getData() {
        return material;
    }

    /**
     * Redefinition of the method toString
     * @return image name
     */
    @Override
    public String toString() {
        return "Text : " + getExtensionImage();
    }

    /**
     * Redefinition of the hasSameMaterial
     * Check if these Materials are the same
     * @param toCompare : material to compare with the current one.
     * @return true if they are the same and false otherwise
     */
    @Override
    public boolean hasSameMaterial(Material toCompare) {
        return toCompare == material;
    }

    /**
     * Redefintion of the method isText
     * @return True because it's a text
     */
    @Override
    public boolean isText() {
        return true;
    }

    /**
     * Redefinition of the equals method
     * @param o the other text
     * @return true if two texts are equal and false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Text text = (Text) o;
        return Objects.equals(material, text.material);
    }

    /**
     * Redefinition of the hashcode method
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), material);
    }

}
