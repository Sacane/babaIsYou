package Data;

import java.util.Arrays;
import java.util.Objects;

/**
 * A property is a material that make the rules to the noun.
 */
public enum Property implements Material{
    PUSH("Push"),
    SINK("Sink"),
    DEFEAT("Defeat"),
    YOU("You"),
    STOP("Stop"),
    WIN("Win"),
    MELT("Melt"),
    HOT("Hot"),
    SPREAD("Spread"),
    IMMUNE("Immune");


    private final String name;

    /**
     * Property's Constructor
     * @param name of the property
     */
    Property(String name){
        Objects.requireNonNull(name);
        this.name = name;
    }

    /**
     * Redefinition of the isNoun method
     * @return false because it's not a noun
     */
    @Override
    public boolean isNoun() {
        return false;
    }

    /**
     * Checks if the string is a valid property name
     * @param toCompare the string to compare
     * @return True if the string is a valid property's name and false otherwise
     */
    public static boolean isSimilarProp(String toCompare){
        return Arrays.stream(Property.values()).anyMatch(property -> toCompare.equalsIgnoreCase(property.toString()));
    }

    /**
     * The redefinition of the getName method
     * @return the property's name
     */
    @Override
    public String getName() {
        return name;
    }
}
