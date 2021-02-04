package Data;

import java.util.Arrays;


/**
 * An Noun implements the interface "Material" and list all the Noun that the user can interact with.
 * A Noun is just a bloc in the game, this can't change the rules of the game.
 */
public enum Noun implements Material{
    BABA("Baba"),
    FLAG("Flag"),
    LAVA("Lava"),
    ROCK("Rock"),
    SKULL("Skull"),
    WALL("Wall"),
    WATER("Water"),
    VIRUS("Virus");

    private final String name;

    /**
     * Noun's Constructor
     * @param name of the Noun
     */
    Noun(String name){
        this.name = name;
    }

    /**
     * Redefinition of the isNoun method
     * @return true because it's a noun
     */
    @Override
    public boolean isNoun() {
        return true;
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
     * Checks if the string is a valid noun's name
     * @param toCompare the string to compare
     * @return True if the string is a valid noun's name and false otherwise
     */
    public static boolean isSimilarNoun(String toCompare){
        return Arrays.stream(Noun.values()).anyMatch(nouns -> toCompare.equalsIgnoreCase(nouns.toString()));
    }


}
