package deplacement;

import java.util.Objects;


/**
 * Coordinate in 2 dimension with an int x and an int y to specify the coordinate of an element.
 */
public class Coordinate {
    private int x;
    private int y;

    /**
     * Construct a new Coordinate by initializing an x and y.
     * @param x coordinate in the board
     * @param y coordinate in the board
     */
    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Change the coordinate according the direction
     * @param direction to move the coordinate
     */
    public void move(Direction direction){
        x += direction.getX();
        y += direction.getY();
    }

    /**
     * Redefinition of the equals method
     * @param o the other coordinate
     * @return true if two coordinates are equal and false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    /**
     * Redefinition of the hashcode method
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Redefinition of the method toString
     * @return the x-coordinate and the the y-coordinate
     */
    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    /**
     * To obtain the x-coordinate
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * To obtain the y-coordinate
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }
}
