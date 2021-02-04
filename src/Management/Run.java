package Management;

import Data.Noun;
import Data.Operator;
import Data.Property;
import fr.umlv.zen5.*;
import fr.umlv.zen5.Event;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

/**
 * Class use to generate the levels and run the application and manage the way the player want it to run :
 * it can be run by an entire folder, run just a file or run a file by default.
 */
public class Run {
    /**
     * Run the game, Display the board and start the rules.
     * @param context context
     * @param gameBoard of the game
     */
    private static void runGame(ApplicationContext context, Board gameBoard){
        ScreenInfo screenInfo = context.getScreenInfo();
        float widthWin = screenInfo.getWidth();
        float heightWin = screenInfo.getHeight();
        context.renderFrame(graphics -> gameBoard.drawAllElement(graphics, (int)widthWin / gameBoard.getWidthBoard(),
                (int)heightWin / gameBoard.getHeightBoard()));
        while(!gameBoard.passNextLevel()){
            Event event = context.pollOrWaitEvent(100);
            if(event == null){
                continue;
            }
            if(Rules.isGameOver(gameBoard, context)){
                System.out.println("You lost !");
                return;
            }
            manageKey(context, event, gameBoard, widthWin, heightWin);
        }
        System.out.println("You passed !");
    }

    /**
     * Take care of managing the different keys of the game
     * @param context to renderFrame the game.
     * @param event to manipulate the action of the user.
     * @param gameBoard of the level.
     * @param widthWin width of the screen
     * @param heightWin height of the screen
     */
    public static void manageKey(ApplicationContext context, Event event, Board gameBoard, float widthWin, float heightWin){
        context.renderFrame(graphics -> {
            KeyboardKey action = event.getKey();
            fr.umlv.zen5.Event.Action key = event.getAction();
            if (key == Event.Action.KEY_PRESSED) {
                switch (action) {
                    case SPACE, M -> {
                        System.out.println("Quit manually");
                        context.exit(0);
                    }
                    case Z, UP, Q, LEFT, S, DOWN, D, RIGHT -> {
                        gameBoard.setRules();
                        gameBoard.actualiseBoard(graphics,
                                (int)widthWin,
                                (int)heightWin,
                                action);
                    }
                }
            }
        });
    }

    /**
     * Run the game according to all the files in the folder name by "nameFolder".
     * @param nameFolder name of the folder which contains all the level to play.
     * @param args command line entered by user
     */
    public static void runByFolder(String nameFolder, String[] args) {
        Application.run(Color.BLACK, context -> {
            var rect = new Rectangle2D.Float(0, 0, (int) context.getScreenInfo().getWidth(), (int) context.getScreenInfo().getHeight());
            int currentLevel = 1;
            Board gameBoard;
            //loop until the player hasn't win all the levels or until he loose.
            do {
                //Reset the visual screen between each levels
                context.renderFrame(graphics -> {
                    graphics.setColor(Color.BLACK);
                    graphics.fill(rect);
                });
                gameBoard = MapGenerator.setLevel(nameFolder+"/level" + currentLevel + ".txt");
                assert gameBoard != null;
                gameBoard.setRules();
                generateRulesByCommandLine(args, gameBoard);
                runGame(context, gameBoard);
                currentLevel++;
            } while (currentLevel < 10);
            context.exit(0);
        });
    }

    /**
     * To allow the user to set rules using commands
     * @param args : command line entered by user
     * @param board : board of the game
     */
    private static void generateRulesByCommandLine(String[] args, Board board){
        for(int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("--execute")) {
                try {
                    if(Noun.isSimilarNoun(args[i+1]) && Operator.isSimilarOp(args[i+2]) && Property.isSimilarProp(args[i+3])) {
                            var noun = Noun.valueOf(args[i + 1]);
                            var operator = Operator.valueOf(args[i + 2]);
                            var property = Property.valueOf(args[i + 3]);
                            var element = board.getElementByMaterial(noun);
                            if (element != null) {
                                element.addPropertyStatic(property);
                            }
                    }
                    else{
                        System.out.println("Error of arguments");
                        System.exit(1);
                    }
                }catch(ArrayIndexOutOfBoundsException e){
                    System.out.println(e.getMessage());
                    System.exit(1);
                }
            }
        }
    }
    /**
     * Run the game and fill the board according the name of the file in parameter.
     * @param nameFile : name of the file which contains the level to play.
     * @param args : command line entered by user
     */
    public static void runByFile(String nameFile, String[] args){
        Application.run(Color.BLACK, context -> {
            var rect = new Rectangle2D.Float(0, 0, (int) context.getScreenInfo().getWidth(), (int) context.getScreenInfo().getHeight());
            Board gameBoard;
            context.renderFrame(graphics -> {
                graphics.setColor(Color.BLACK);
                graphics.fill(rect);
            });
            gameBoard = MapGenerator.setLevel("listLevel/" + nameFile);

            assert gameBoard != null;
            gameBoard.setRules();
            generateRulesByCommandLine(args, gameBoard);
            Run.runGame(context, gameBoard);
            context.exit(0);
        });
    }

    /**
     * Run the level by default if the user doesn't put any parameter in the launcher.
     * @param args : command line entered by user
     */
    public static void runByDefault(String[] args){
        Application.run(Color.BLACK, context -> {
            var rect = new Rectangle2D.Float(0, 0, (int) context.getScreenInfo().getWidth(), (int) context.getScreenInfo().getHeight());
            Board gameBoard;
            context.renderFrame(graphics -> {
                graphics.setColor(Color.BLACK);
                graphics.fill(rect);
            });
            gameBoard = MapGenerator.setLevel("listLevel/default_level.txt");
            assert gameBoard != null;
            gameBoard.setRules();
            generateRulesByCommandLine(args, gameBoard);
            Run.runGame(context, gameBoard);
            context.exit(0);
        });
    }

    /**
     * Checks if we have a command specifying level
     * @param args command line entered by user
     * @return true if we have a command specifying level and false
     */
    public static boolean isLevelSpecified(String[] args){
        return Arrays.stream(args).anyMatch(str -> str.equals("--level") || str.equals("--levels"));
    }

}
