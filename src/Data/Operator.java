package Data;

import java.util.Arrays;
import java.util.Objects;

/**
 * An operator is a Material, it does the link between the noun and the property.
 */
public enum Operator implements Material{
    IS("Is");

    private final String name;

    /**
     * Operator's Constructor
     * @param name of the operator
     */
    Operator(String name){
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
     * The redefinition of the getName method
     * @return the operator's name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Checks if the string is a valid operator's name
     * @param toCompare the string to compare
     * @return True if the string is a valid operator's name and false otherwise
     */
    public static boolean isSimilarOp(String toCompare){
        return Arrays.stream(Operator.values()).anyMatch(op -> toCompare.equalsIgnoreCase(op.toString()));
    }
}
