package data;

import deplacement.Coordinate;
import deplacement.Direction;
import fr.umlv.zen5.KeyboardKey;
import management.Board;
import management.Rules;
import graphics.Renderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * An element in the game is a texture that can be manipulate (push, destroy, interact) in the game.
 * This can be a text or can be a Bloc that the player will see.
 */
abstract public class Element {
    HashSet<Property> setOfProperties = new HashSet<>(); // to manage the properties by the text in game.
    HashSet<Property> PropertyByExecute = new HashSet<>(); // To manage the properties by the command "--execute".
    private final HashMap<Coordinate, Integer> mapOfCoordinate; // To manage the coordinates in the game.
    private Direction direction;
    private final int width;
    private final int height;
    private BufferedImage image;

    /**
     * Element's Constructor
     * @param width of the element in the screen.
     * @param height of the element in the screen.
     * @param image to represent the element in game.
     */
    public Element(int width, int height, BufferedImage image){
        if(width < 0 || height < 0 || image == null){
            throw new IllegalArgumentException("Arguments are invalids");
        }
        this.image = image;
        this.width = width;
        this.height = height;
        this.mapOfCoordinate = new HashMap<>();
        direction = Direction.SOUTH;
    }

    /**
     * To obtain the direction accordind to the keys pressed on the keyboard
     * @param action to change the direction
     * @return the direction according to the action.
     */
    public Direction getDirectionByAction(KeyboardKey action) {
        switch (action) {
            case Z, UP ->{
                this.direction = Direction.NORTH;
                return Direction.NORTH;
            }
            case Q, LEFT -> {
                this.direction = Direction.WEST;
                return Direction.WEST;
            }
            case S, DOWN -> {
                this.direction = Direction.SOUTH;
                return Direction.SOUTH;
            }
            case D, RIGHT -> {
                this.direction = Direction.EAST;
                return Direction.EAST;
            }
        }
        return null;
    }

    /**
     * Redefinition of the isText method
     * @return true if the element is a text, else return false.
     */
    public abstract boolean isText();

    /**
     * Add a Property to the set "PropertyByExecute"
     * @param toAdd to add in the set
     */
    public void addPropertyStatic(Property toAdd){
        Objects.requireNonNull(toAdd, "the parameter to add is null");
        PropertyByExecute.add(toAdd);
    }

    /**
     * add a Property to the set "setOfProperties"
     * @param data to add in the set
     */
    public void addProperty(Property data){
        Objects.requireNonNull(data, "the data to add is null");
        setOfProperties.add(data);
    }


    /**
     * Check if the given data is in the set of properties.
     * @param data to check.
     * @return true is data is in the properties, else return false.
     */
    public boolean hasProperty(Property data){
        Objects.requireNonNull(data, "data is null");
        return setOfProperties.contains(data) || PropertyByExecute.contains(data);
    }


    /**
     * To know if you can move or not
     * @param gameBoard of the current level.
     * @param action get by the user.
     * @return true if the Element with "You" can move, else return false.
     */
    public boolean canYouMove(Board gameBoard, KeyboardKey action){
        Objects.requireNonNull(gameBoard, "board can't be null");
        for(var location : mapOfCoordinate.entrySet()){
            if(location.getKey().getX() + this.getDirectionByAction(action).getX() < 0 || (location.getKey().getY() +
                    this.getDirectionByAction(action).getY()< 0) ||
                    (location.getKey().getX() + this.getDirectionByAction(action).getX() >= width)
                    || location.getKey().getY() + this.getDirectionByAction(action).getY() >= height){
                return false;
            }
            if(!Rules.checkMovable(gameBoard, location.getKey().getX(), location.getKey().getY(), width, height, this)){
                return false;
            }
        }
        return true;
    }

    /**
     * Check if 2 coordinate are alike in the list and return the location where the conflict is.
     * @return the location in conflict.
     */
    public Coordinate getLocationAlike(){
        return mapOfCoordinate.entrySet().stream().filter(coordinate -> mapOfCoordinate.entrySet().stream().anyMatch(otherCoordinate -> coordinate.getKey().equals(otherCoordinate.getKey()) &&
                !coordinate.getValue().equals(otherCoordinate.getValue()))).findFirst().map(Map.Entry::getKey).orElse(null);
    }

    /**
     * add the new coordinate in the set of coordinate only if it's not already here.
     * @param newCoordinate to add in the set of coordinates.
     */
    public void addLocation(Coordinate newCoordinate){
        Objects.requireNonNull(newCoordinate, "the new coordinate to add is null");
        if(mapOfCoordinate.containsKey(newCoordinate)){
            return;
        }
        if(mapOfCoordinate.isEmpty()){
            mapOfCoordinate.put(newCoordinate, 1);
        }
        else{
            mapOfCoordinate.put(newCoordinate, mapOfCoordinate.size() + 1);
        }
    }

    /**
     *  Remove a property from the set of properties.
     * @param data to remove from the set of properties.
     */
    public void removeProperty(Property data){
        Objects.requireNonNull(data, "Careful the data to remove is null");
        setOfProperties.remove(data);
    }

    /**
     * To know the name of the image
     * @return the name of the image
     */
    public abstract String getExtensionImage();

    /**
     * To know the name of the material
     * @return the name of the material
     */
    public abstract Material getData();

    /**
     * To obtain the list of coordinates
     * @return the coordinates in the game
     */
    public HashMap<Coordinate, Integer> getMapOfCoordinate() {
        return this.mapOfCoordinate;
    }

    /**
     * Redefinition of the toString method
     * @return the name of the material
     */
    @Override
    public String toString() {
        return getData().getName();
    }


    /**
     *
     * @param toCompare : material to compare with the current one.
     * @return true if the element has the material toCompare, else return false.
     */
    public abstract boolean hasSameMaterial(Material toCompare);


    /**
     * move the element according to the direction.
     * @param direction of the next move.
     */
    public void moveElement(Direction direction){
        mapOfCoordinate.forEach((key, value) -> key.move(direction));
    }

    /**
     * Checks if the set of location is empty or not
     * @return true if the set of location is empty, else return false.
     */
    public boolean isCoordinateEmpty(){
        return mapOfCoordinate.isEmpty();
    }


    /**
     * Checks if the player won the current level and false otherwise
     * @param gameBoard of the level.
     * @return true if the player has win the current level, else return false.
     */
    public boolean isWin(Board gameBoard){
        return mapOfCoordinate.entrySet().stream().anyMatch(location -> gameBoard.contactWin(location.getKey()));
    }

    /**
     * Check if the element is in contact with the location.
     * @param location to check
     * @return true if there are in contact, else return false.
     */
    public boolean isElementInContactWithLocation(Coordinate location){
        return this.getMapOfCoordinate().entrySet().stream().anyMatch(coordinate -> location.equals(coordinate.getKey()));
    }

    /**
     * Compare the location of the first and second element and check if there are in conflict.
     * @param first Element to compare
     * @param second Element to compare
     * @return true if both element are in conflict each other, else return false.
     */
    public static boolean areElementInContact(Element first, Element second){
        return first.getMapOfCoordinate().entrySet().stream().anyMatch(elementOne -> second.getMapOfCoordinate().entrySet().stream().anyMatch(elementTwo -> !first.equals(second) && elementOne.getKey().equals(elementTwo.getKey())));
    }

    /**
     * Checks if the element one is in contact with the element two and return the location.
     * @param one Element in contact
     * @param two Element in contact
     * @return the location of the conflict with the both elements.
     */
    public static Coordinate getCoordinateByContact(Element one, Element two){
        if(one == null || two == null){
            throw new IllegalArgumentException("Arguments null");
        }
        return one.getMapOfCoordinate().keySet().stream().filter(oneLocation -> two.getMapOfCoordinate().keySet().stream().anyMatch(oneLocation::equals)).findFirst().orElse(null);
    }

    /**
     * Remove the of the current element if it is in contact with the element in contact
     * @param contact element in contact with the current object.
     */
    public void removeElementInContact(Element contact){
        Objects.requireNonNull(contact, "Argument is null");
        contact.getMapOfCoordinate().forEach((key, value) -> getMapOfCoordinate().keySet().removeIf(coordinate -> coordinate.equals(key)));
    }

    /**
     * Remove the location of the two Elements if they are in contact
     * @param first Element to remove if he is in contact with the second
     * @param second Element to remove if he is in contact with the first
     */
    public static void removeBothElementInContact(Element first, Element second){
        Objects.requireNonNull(first, "First element is null");
        Objects.requireNonNull(second, "Second element is null");
        Coordinate tmp = Element.getCoordinateByContact(first, second);
        if(tmp != null){
            first.getMapOfCoordinate().keySet().removeIf(coordinate -> coordinate.equals(tmp));
            second.getMapOfCoordinate().keySet().removeIf(coordinate -> coordinate.equals(tmp));
        }
    }

    /**
     * Change the current image for another image.
     * @param anotherImage to switch with the current one.
     */
    void switchImage(BufferedImage anotherImage){
        Objects.requireNonNull(anotherImage, "loading image failed or null");
        this.image = anotherImage;
    }

    /**
     * Transfer all the location of the current Element into element's list of coordinates to convert,
     * then clear all the coordinates in the list of the current Element.
     * @param toConvert : Element that the current one has to convert into.
     */
    public void convertElementToOther(Element toConvert){
        Objects.requireNonNull(toConvert, "Invalid arguments");
        mapOfCoordinate.keySet().forEach(toConvert::addLocation);
        mapOfCoordinate.clear();
    }


    /**
     * Take a material as parameter and check if the board has a sentences according to the the material.
     * @param board of the level
     * @param locationOfTextNoun coordinate where is located the noun's text.
     * @param toCheck Material to check.
     * @return true if the board has a sentences with the material to check, else return false.
     */
    public boolean isSentenceWithMaterial(Board board, Coordinate locationOfTextNoun, Material toCheck){
        if(board == null || locationOfTextNoun == null || toCheck == null){
            throw new IllegalArgumentException("Invalid arguments");
        }
        Element is;
        Element property;
        if((is = board.getElementByCoordinate(new Coordinate(locationOfTextNoun.getX()+1, locationOfTextNoun.getY())))!=null){
            if(is.isText() && is.hasSameMaterial(Operator.IS) &&
                    (property = board.getElementByCoordinate(new Coordinate(locationOfTextNoun.getX()+2,
                    locationOfTextNoun.getY())))!= null && property.getData() == toCheck && property.isText()){
                return true;
            }
        }
        if((is = board.getElementByCoordinate(new Coordinate(locationOfTextNoun.getX(), locationOfTextNoun.getY()+1)))!=null){
            return is.isText() && is.hasSameMaterial(Operator.IS) &&
                    (property = board.getElementByCoordinate(new Coordinate(locationOfTextNoun.getX(),
                            locationOfTextNoun.getY() + 2))) != null && property.getData() == toCheck && property.isText();
        }

        return false;
    }


    /**
     *  Check if there is a sentence like : Noun + Operator + Noun, and convert the element at the left
     *  with the element at the right by transferring the coordinates.
     * @param board of the level.
     */
    public void manageSentencesNoun(Board board){
        Objects.requireNonNull(board, "board can't be null");
        if(this.isText() && this.getData().isNoun()) {
            var element = board.getElementByMaterial(this.getData());
            for (var noun : Noun.values()) {
                for (var coordinate : mapOfCoordinate.keySet()) {
                    if (isSentenceWithMaterial(board, coordinate, noun)) {
                        var toConvert = board.getElementByMaterial(noun);
                        element.convertElementToOther(toConvert);
                    }
                }
            }
        }
    }

    /**
     *
     * @param locationYou position of the element You
     * @return true if the element you is in contact with the element Win
     */
    public boolean isElementInContactWin(Coordinate locationYou){
        Objects.requireNonNull(locationYou, "the location to check is null");
        if(this.hasProperty(Property.WIN)) {
            return this.mapOfCoordinate.entrySet().stream().anyMatch(location -> location.getKey().equals(locationYou));
        }
        return false;
    }

    /**
     * Checks if the properties make a sentence
     * @param board of the level.
     * @param property to check if it make a sentence.
     * @return true if the properties make a sentence, else return false.
     */
    public boolean checkProperties(Board board, Property property){
        if(this.isText() && this.getData().isNoun()) {
            return mapOfCoordinate.keySet().stream().anyMatch(coordinate -> isSentenceWithMaterial(board, coordinate, property));
        }
        return false;
    }

    /**
     * Draw the element in all his location according to the map Of Location,
     * the function will using the class Renderer to draw the element in the board for each
     * location in the map of Coordinate.
     * @param graphics to draw the element by using Renderer.
     * @param width of the board
     * @param height of the board
     */
    public void drawElementInGame(Graphics2D graphics, int width, int height){
        mapOfCoordinate.keySet().forEach(coordinate -> Renderer.drawElementByCoordinate(graphics, image, width, height, coordinate));
    }

    /**
     * Clear the element for each coordinate in his map of coordinates.
     * @param graphics to draw the element by using Renderer.
     * @param width of the board
     * @param height of the board
     */
    public void clearElementInGame(Graphics2D graphics, int width, int height){
        if(graphics == null || width < 0 || height < 0){
            throw new IllegalArgumentException("Arguments invalid");
        }
        mapOfCoordinate.keySet().forEach(coordinate -> Renderer.clearElementByCoordinate(graphics, width, height, coordinate));
    }

    /**
     * Redefinition of the equals method
     * @param o the other elements
     * @return true if two elements are equal and false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return width == element.width && height == element.height && Objects.equals(setOfProperties, element.setOfProperties) && Objects.equals(mapOfCoordinate, element.mapOfCoordinate) && direction == element.direction && Objects.equals(image, element.image);
    }

    /**
     * Redefinition of the hashcode method
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(setOfProperties, mapOfCoordinate, direction, width, height, image);
    }

    /**
     * Get the actual direction of the element.
     * @return the current direction of the element
     */
    public Direction getDirection() {
        return direction;
    }


    /**
     * Duplicate the element in 4 other part by adding a coordinate in his North, South, East and West location,
     * for each coordinate in his map of coordinates.
     * @param gameBoard of the level
     */
    public void duplicateElement(Board gameBoard){
        Objects.requireNonNull(gameBoard, "the board can't be null");
        var list = new ArrayList<Coordinate>();
        for(var coordinate : mapOfCoordinate.keySet()){
            Arrays.stream(Direction.values()).map(directions -> new Coordinate(coordinate.getX() + directions.getX(), coordinate.getY() + directions.getY())).filter(toAdd -> !gameBoard.isLocationCorrect(toAdd)).forEach(list::add);
        }
        list.forEach(element -> addIfCaseEmpty(gameBoard, element));
    }

    /**
     * Add a location in the map of coordinate only if this location is still empty in the board.
     * @param gameBoard of the level.
     * @param location to add.
     */
    public void addIfCaseEmpty(Board gameBoard, Coordinate location){
        if(gameBoard == null || location == null){
            throw new IllegalArgumentException("Invalid arguments");
        }
        if(!gameBoard.isInTheBoard(location)){
            addLocation(location);
        }
    }
}
