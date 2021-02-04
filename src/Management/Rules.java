package Management;

import Data.*;
import Deplacement.Coordinate;

import fr.umlv.zen5.ApplicationContext;

import fr.umlv.zen5.KeyboardKey;


/**
 * Class use to manage the rules define in the game, this will manage the game Over and the conditions if the user lost,
 * this will manage how the element in a board can be push, or to manage the priority of the properties.
 */
public class Rules {
    /**
     * Manage all the rules in the current level. Precisely, manage when Elements are in contact :
     * check if an element can push another, or if an element has to be remove from the game, when
     * he is in contact with another element.
     * @param gameBoard Board of the game
     * @param action get by the user.
     * @param you Element which has the property "You".
     */
    public static void manageRules(Board gameBoard, KeyboardKey action, Element you) {
        if(you.canYouMove(gameBoard, action)) {
            you.moveElement(you.getDirectionByAction(action));
        }
        gameBoard.contactProperties(Property.MELT, Property.HOT);
        gameBoard.contactProperties(Property.YOU, Property.DEFEAT);
        Element tmp = gameBoard.elementPushElement(you, you);
        pushLoop(gameBoard, tmp, you);
    }

    /**
     * Make the element tmp move, tmp become the element pushed and make the element in contact move until
     * the location is empty.
     * @param gameBoard of the board
     * @param tmp temporary Element to be pushed and then being the pusher
     * @param you : Element which has the property You
     */
    public static void pushLoop(Board gameBoard, Element tmp, Element you){
        do{
            if(tmp == null || !gameBoard.isContact()){
                break;
            }
            tmp = gameBoard.elementPushElement(tmp, you);
        }while(true);
    }

    /**
     * Check if the elements with the property "You" can move even when he's pushing others blocs.
     * If he push a bloc with the property "Push" then the function check if this bloc can be move :
     * - if the next position of the board get a bloc without the property "Stop"
     * - if the next position is out of Board
     * @param gameBoard Board of the game
     * @param x of the current position of the Element which is pushing the others
     * @param y of the current position of the Element which is pushing the others
     * @param width of the board
     * @param height of the board
     * @param you Element which is pushing
     * @return true if the Element you can move, false if not
     */
    public static boolean checkMovable(Board gameBoard, int x, int y, int width, int height, Element you){
        int newX = x + you.getDirection().getX();
        int newY = y + you.getDirection().getY();
        var check = gameBoard.getElementByCoordinate(new Coordinate(x + you.getDirection().getX(), y
         + you.getDirection().getY()));
        if(check != null && check.hasProperty(Property.STOP)){
            return false;
        }
        while(gameBoard.isInTheBoard(new Coordinate(x, y))){
            var manageStop = gameBoard.getElementByCoordinate(new Coordinate(newX, newY));
            x+=you.getDirection().getX();
            y+=you.getDirection().getY();
            newX += you.getDirection().getX();
            newY += you.getDirection().getY();
            if(manageStop != null) {
                if(manageStop.hasProperty(Property.STOP)){
                    return false;
                }
                if(!manageStop.hasProperty(Property.PUSH)){
                    return true;
                }
            }
        }
        return x >= 0 && x < width && y >= 0 && y < height;
    }


    /**
     * Checks if the player lost or not
     * @param gameBoard of the level
     * @param context use to exit the application if the game is over
     * @return true if it's game Over, false elsewhere.
     */
    public static boolean isGameOver(Board gameBoard, ApplicationContext context){
        if(!gameBoard.isYouLeft()){
            context.pollOrWaitEvent(1);
            context.exit(1);
            return true;
        }
        return false;
    }

    /**
     * Manage the priority of the properties, when an element has more than 1 property, some elements has the priority on the
     * others.
     * @param element to manage.
     */
    public static void managePriority(Element element){
        if(element.hasProperty(Property.HOT) && element.hasProperty(Property.PUSH)){ //If an element have the properties PUSH and HOT
            element.removeProperty(Property.HOT); // We remove HOT

        }
        if(element.hasProperty(Property.PUSH) && element.hasProperty(Property.STOP)){ //If an element have the properties PUSH and STOP
            element.removeProperty(Property.PUSH); //We remove PUSH
        }
    }

}


