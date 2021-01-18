package management;
import data.*;
import deplacement.Coordinate;
import assets.PathAssets;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.nio.file.Paths;
import java.util.Arrays;

import static java.lang.System.exit;


/**
 * Class use to generate the map according to a file.txt. A file has a specific form to work :
 * First you have to specify the size first with the width and the height of the board (w and h) : size wXhY
 * Then you can add the element which will define the current level with the x and y position of the element :
 * Baba x10y8 -> a baba will be appear at the coordinate x = 10 and y = 8.
 * You can specify multiple position for one element :
 * Is x5y10 x7y19 ...
 */
public class MapGenerator {


    /**
     * Extract the height and the width of the level if there is a line on the file which begin with "size"
     * @param file's level
     */
    static Board readSize(String[] file){
        if(file[0].equalsIgnoreCase("size")){
            for (String elem : file) {
                if (!elem.equals("size")) {
                    String extractX = elem.substring(elem.lastIndexOf("w") + 1, elem.lastIndexOf("h"));
                    int width = Integer.parseInt(extractX);

                    String extractY = elem.substring(elem.lastIndexOf("h") + 1, elem.lastIndexOf(""));
                    int height = Integer.parseInt(extractY);

                    return new Board(width, height);

                }
            }
        }
        return null;
    }


    /**
     * Collect the coordinates of the text bloc
     * @param buffer the text of the file
     * @param name of the bloc
     */
    static void parseXAndYText(String[] buffer, String name, Element element){
        Arrays.stream(buffer).filter(elem -> !elem.equalsIgnoreCase(name)).forEach(elem -> {

            String extractX = elem.substring(elem.lastIndexOf("x") + 1, elem.lastIndexOf("y"));

            int x = Integer.parseInt(extractX);

            String extractY = elem.substring(elem.lastIndexOf("y") + 1, elem.lastIndexOf(""));
            int y = Integer.parseInt(extractY);

            element.addLocation(new Coordinate(x, y));

        });
    }

    /**
     * Put the coordinates about operators' text in the gameBoard
     * @param buffer the text of the file
     */
    static void setOperator(String[] buffer, Operator op, Board board){
        PathAssets assets = new PathAssets();
        BufferedImage image = assets.initImage(PathAssets.pathTextElement(op));
        Text element = new Text(board.getWidthBoard(), board.getHeightBoard(), op, image);
        if(buffer[0].equals(op.getName())){
            parseXAndYText(buffer, op.name(), element);
            board.add(element);
        }
    }

    /**
     * Save the coordinates about nouns
     * @param buffer the text of the file
     */
    static void choice(String[] buffer, Noun noun, Board board){
        PathAssets assets = new PathAssets();
        BufferedImage image = assets.initImage(PathAssets.pathElement(noun));
        BufferedImage imageText = assets.initImage(PathAssets.pathTextElement(noun));
        if (buffer[0].equalsIgnoreCase(noun.name())) {
            Bloc element = new Bloc(board.getWidthBoard(), board.getHeightBoard(), noun, image);
            parseXAndYText(buffer, noun.getName(), element);
            board.add(element);
        }
        if ((noun.getName() +"Text").equalsIgnoreCase(buffer[0])) {
            Text element = new Text(board.getWidthBoard(), board.getHeightBoard(), noun, imageText);
            parseXAndYText(buffer, noun.getName() +"Text", element);
            board.add(element);
        }
    }

    /**
     * Save the coordinates about the property's text
     * @param buffer the text of the file
     */
    static void setProp(String[] buffer, Property property, Board board){
        PathAssets assets = new PathAssets();
        BufferedImage image = assets.initImage(PathAssets.pathTextElement(property));
        if(buffer[0].equals(property.getName())){
            Text element = new Text(board.getWidthBoard(), board.getHeightBoard(), property, image);
            parseXAndYText(buffer, property.name(), element);
            board.add(element);
        }
    }
    /**
     * Save all the data about the file ( for a level)
     * @param fileName for the level
     * @return the data
     */
    public static Board setLevel(String fileName){
        BufferedReader fluxEntree;
        try {
            fluxEntree = new BufferedReader(new FileReader(Paths.get(System.getProperty("user.dir")).toString() + "/src/assets/"+fileName));
            String actualLine = fluxEntree.readLine();
            String[] buffer = actualLine.split(" ");
            Board board = readSize(buffer);
            actualLine = fluxEntree.readLine();

            while(actualLine!=null){
                buffer = actualLine.split(" ");
                setNouns(buffer, board);
                setProperties(buffer, board);
                setOperators(buffer, board);
                actualLine = fluxEntree.readLine();
            }
            return board;
        }
        catch(IOException exc){
            System.out.println(exc.getMessage());
            exit(1);
        }
        return null;
    }

    /**
     * Insert the operators in the file in the board.
     * @param buffer the text of the file
     * @param board of the game
     */
    static void setOperators(String[] buffer, Board board){
        Arrays.stream(Operator.values()).filter(operator -> buffer[0].equalsIgnoreCase(operator.name()) && board != null).findFirst().ifPresent(operator -> setOperator(buffer, operator, board));
    }
    /**
     * Insert the nouns in the file in the board.
     * @param buffer the text of the file
     * @param board of the game
     */
    static void setNouns(String[] buffer, Board board){
        Arrays.stream(Noun.values()).filter(noun1 -> buffer[0].equalsIgnoreCase(noun1.name()) || buffer[0].equalsIgnoreCase(noun1.name() + "Text")).findFirst().ifPresent(noun1 -> choice(buffer, noun1, board));
    }
    /**
     * Insert the properties in the file in the board.
     * @param buffer the text of the file
     * @param board of the game
     */
    static void setProperties(String[] buffer, Board board){
        Arrays.stream(Property.values()).filter(property -> buffer[0].equalsIgnoreCase(property.name())).findFirst().ifPresent(property -> setProp(buffer, property, board));
    }
}
