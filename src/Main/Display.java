package Main;

import Management.Run;

public class Display {
    public static void main(String[] args) {
        if(!Run.isLevelSpecified(args)){
            Run.runByDefault(args);
            return;
        }

        for(int i = 0; i < args.length; i++){
            //If we have the command --levels or level ...
            if(args[i].equals("--levels") || args[i].equals("--level")){
                if(i == args.length-1 || i+1 == args.length-1){
                    throw new IllegalArgumentException("Arguments non-valid");
                }
                //And the name of the file (or the folder) who contains the level(s)
                if(args[i+1].equals("name:")){
                    // We load the file (or the folder)
                    if (args[i].equals("--levels"))
                        Run.runByFolder(args[i+2], args);
                    else
                        Run.runByFile(args[i+2], args);
                }
            }
        }
    }
}
