package management;

import data.*;
import deplacement.Coordinate;
import fr.umlv.zen5.KeyboardKey;

import java.awt.*;
import java.util.HashSet;


/**
 * Class to manage the entire level, it stock all the element use in the enigma and manage the element each other.
 */
public class Board {

    private final int widthBoard;
    private final int heightBoard;
    private final HashSet<Element> listElement;

    /**
     * Set the constructor to create a board which contains his width and height, a board will contains a set, we can add
     * an element to this.
     * @param widthBoard width of the board
     * @param heightBoard height of the board
     */
    public Board(int widthBoard, int heightBoard){
        this.widthBoard = widthBoard;
        this.heightBoard = heightBoard;
        this.listElement = new HashSet<>();
    }

    /**
     * Add an element in the set of the board
     * @param element to add in the Set
     */
    public void add(Element element){
        listElement.add(element);
    }

    /**
     * To obtain the height's board
     * @return the height's board
     */
    int getHeightBoard() {
        return heightBoard;
    }

    /**
     * To obtain the width's board
     * @return the width's board
     */
    int getWidthBoard() {
        return widthBoard;
    }

    /**
     * Checks if an element is in contact with another and remove
     * the location depending on the property given in parameter.
     * @param first property to manage with the second
     * @param second property to manage with the first
     */
    public void contactProperties(Property first, Property second){
        for(var parser : listElement) {
            for (var secondParser : listElement) {
                if(secondParser.hasProperty(first) && parser.hasProperty(second)
                && Element.areElementInContact(parser, secondParser)){
                    if(second == Property.DEFEAT && !secondParser.hasProperty(Property.IMMUNE)) {
                        secondParser.removeElementInContact(parser);
                    }
                    if(second == Property.HOT && first == Property.MELT){
                        secondParser.removeElementInContact(parser);
                    }
                }
            }
        }
    }

    /**
     * Remove the location of two element if one element is in contact with an element which has "Sink" as property.
     */
    public void removeSink(){
        for(var parser : listElement) {
            listElement.stream().filter(secondParser -> (parser.hasProperty(Property.SINK) ||
                    secondParser.hasProperty(Property.SINK)) &&
                    Element.areElementInContact(parser, secondParser)).forEach(secondParser -> Element.removeBothElementInContact(parser, secondParser));
        }
    }

    /**
     * To know if there is a conflict
     * @return true if there is a conflict between two element or if an element is in conflict with itself else return false.
     */
    public boolean isContact(){
        for(var parser : listElement){
            if (listElement.stream().filter(secondParser -> !parser.equals(secondParser)).anyMatch(secondParser -> Element.areElementInContact(parser, secondParser)) || parser.getLocationAlike() != null)
                return true;
        }

        return false;
    }

    /**
     * To know if one ELement has the property "You" in the board
     * @return true if there still is an Element with the property "You" in the board, else return false.
     */
    public boolean isYouLeft(){
        return listElement.stream().filter(element -> element.hasProperty(Property.YOU)).anyMatch(element -> !element.isCoordinateEmpty());
    }


    /**
     * To recover the  element's list of the board
     * @return the element's list
     */
    HashSet<Element> getListElement(){
        return listElement;
    }

    /**
     * Get the element in the board which has "data" as material.
     * @param data of the wanted Element.
     * @return the element which has the material "data".
     */
    public Element getElementByMaterial(Material data){
        return listElement.stream().filter(element -> !element.isText()).filter(element -> element.hasSameMaterial(data)).findFirst().orElse(null);
    }

    /**
     * To check if the player won or not
     * @return true if the user won the level, false if he is still playing.
     */
    public boolean passNextLevel(){
        for(var element : listElement){
            if(element.hasProperty(Property.YOU)){
                if (element.isWin(this) || element.hasProperty(Property.WIN)) {
                    System.out.println("GG You won !");
                    return true;
                }
            }
            
        }
        return false;
    }

    /**
     * To check if the player is on an Element who have the property You
     * @param locationYou : location of the elements "you", to check if he is in contact with an element "Win"
     * @return true if there is an element you in contact with an element Win, else return false.
     */
    public boolean contactWin(Coordinate locationYou){
        return listElement.stream().anyMatch(element -> element.isElementInContactWin(locationYou));
    }

    /**
     * Set all the rules which make a sentences in the board, it also put the property "Push" to all the text in the board.
     */
    public void setRules(){
        for(var nouns : listElement){
            Rules.managePriority(nouns);
            if(nouns.isText()) {
                nouns.addProperty(Property.PUSH);
                if(nouns.getData().isNoun()) {
                    var element = getElementByMaterial(nouns.getData());
                    for (var property : Property.values()) {
                        if (nouns.checkProperties(this, property) && element != null) {
                            element.addProperty(property);

                        } else {
                            if (element != null) {
                                element.removeProperty(property);
                            }
                        }
                    }
                }

            }
        }
    }

    /**
     * Parse the gameBoard and return the Element in the "coordinate" taken as a parameter.
     * @param coordinate to check the Element in this position.
     * @return the Element in the position of the target coordinate
     */
    public Element getElementByCoordinate(Coordinate coordinate){
        return this.getListElement().stream().filter(element -> element.isElementInContactWithLocation(coordinate)).findFirst().orElse(null);
    }


    /**
     * Check if there is an Element at the given location
     * @param location where the element is supposed to be.
     * @return true if there is an Element at the location, else return false.
     */
    public boolean isInTheBoard(Coordinate location){
        if(isLocationCorrect(location)){
            return false;
        }
        return listElement.stream().anyMatch(element -> element.isElementInContactWithLocation(location));
    }

    /**
     * Checks if the given location is off of the board or not
     * @param location the given location
     * @return true if the given location is off the board
     */
    public boolean isLocationCorrect(Coordinate location){
        return (location.getX() >= widthBoard || location.getX() < 0 || location.getY() >= heightBoard || location.getY() < 0);
    }

    /**
     * Parse the list of the element and check if an element is in contact with the element pusher,
     * if yes, return the element which is being push.
     * @param pusher which is in contact of the Element of the push
     * @param you : Element which has the property You in the game
     * @return the Element which is pushed by the Element pusher
     */
    public Element elementPushElement(Element pusher, Element you){
        Coordinate coordinate;
        for(var parser : listElement){
            if(!pusher.equals(parser)){
                if(Element.areElementInContact(parser, pusher) && parser.hasProperty(Property.PUSH)){
                    Coordinate contact = Element.getCoordinateByContact(parser, pusher);
                    assert contact != null;
                    contact.move(you.getDirection());
                    return parser;
                }
            }
            if((coordinate = parser.getLocationAlike()) != null){
                coordinate.move(you.getDirection());
                return pusher;
            }
        }
        return null;
    }

    /**
     * Draw all elements of the listElement in the screen
     * @param graphics to draw the Element
     * @param width of the screen
     * @param height of the screen
     */
    public void drawAllElement(Graphics2D graphics, int width, int height){
        listElement.forEach(element -> element.drawElementInGame(graphics, width, height));
    }

    /**
     * clear the previous position of the element of the board.
     * @param graphics2D to hide the elements at their previous position
     * @param width of the screen
     * @param height of the screen
     */
    public void clearAllElement(Graphics2D graphics2D, int width, int height){
        listElement.forEach(element -> element.clearElementInGame(graphics2D, width, height));
    }

    /**
     * Redefinition of the toString's method
     * @return the board parameters
     */
    @Override
    public String toString() {
        return "Board{" +
                "widthBoard=" + widthBoard +
                ", heightBoard=" + heightBoard +
                ", listElement=" + listElement +
                '}';
    }


    /**
     * Actualise the position of all the element in the board. Check if all the rules are respected.
     * @param graphics to actualise the entire screen
     * @param width of the screen
     * @param height of the screen
     * @param action detected to change the direction of baba he is in the board
     */
    public void actualiseBoard(Graphics2D graphics, int width, int height, KeyboardKey action){
        drawAllElement(graphics, width / widthBoard, height / heightBoard);
        for(var element : listElement){
            if(element.hasProperty(Property.SPREAD)){
                element.duplicateElement(this);
            }
            element.manageSentencesNoun(this);
            Rules.managePriority(element);
            if(!element.isText()){
                ((Bloc) element).changeDirectionBaba(action);
            }
            this.clearAllElement(graphics, width / widthBoard, height / heightBoard);
            if(!element.isText() && element.hasProperty(Property.YOU)){
                Rules.manageRules(this, action, element);
            }
            Rules.managePriority(element);
        }
        this.removeSink();
        this.drawAllElement(graphics, width / widthBoard, height / heightBoard);
    }

}
