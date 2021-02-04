package Data;

/**
 * A material is neither a Noun, an operator or a property.
 */
public interface Material {
    /**
     * @return true if the material is a noun and false otherwise
     */
    boolean isNoun();

    /**
     * @return the name of the material
     */
    String getName();
}
