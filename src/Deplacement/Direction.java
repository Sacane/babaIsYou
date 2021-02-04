package Deplacement;

/**
 * Enumeration of the possible direction in the game.
 * The field x and y represent the coordinate by the direction.
 */
public enum Direction {
    NORTH(0, -1),
    SOUTH(0, 1),
    EAST(1, 0),
    WEST(-1, 0);

    private final int x;
    private final int y;

    /**
     * Direction's constructor
     * @param x the x-coordinate associated with the direction
     * @param y the y-coordinate associated with the direction
     */
    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * To obtain the x-coordinate of the direction
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * To obtain the y-coordinate of the direction
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }
}