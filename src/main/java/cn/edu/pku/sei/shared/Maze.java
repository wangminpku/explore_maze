package cn.edu.pku.sei.shared;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


/**
 * @author Robert Ward
 */

/**
 * This class constitutes the "entry point" for the application. The static methods 
 * (especially main()) should be responsible for examining the command line arguments 
 * (are there any? are there the right number? do they need to be converted from 
 * String to some other type?). If the args are appropriate, main() should create an 
 * instance (i.e., non-static function) of the main application logic by calling 
 * the Maze() constructor (saving a reference to the instance), and then 
 * invoke the main business logic with the appropriate input information by 
 * making a call to run. 
 * 
 * Requirements: 
 * if main() is called without arguments, it should print this warning:
 * "Warning: no file parameter, proceeding with default file ./mazes/maze.10.10.50.txt";
 * it should then continue and call run with the default file name. 
 * 
 * if main() is called with exactly one argument, it should proceed and forward that
 * argument (as a string) to the business logic (call run). 
 * 
 * if main() is called with more than one argument, it should simply print a usage
 * message and exit: "USAGE: Maze <file path>"
 * 
 * each invocation of run() should produce an output message of the form: 
 * 
 * Path length: 3
 * Path cost:  17
 * Path [3, 6, 9]
 * 
 * followed by a listing of the graph. The listing should be created with 
 * System.out.println(g). 
 *
 */
public class Maze {
    private static String maze10_50 = 
            "./mazes/maze.10.10.50.txt";
    private static Maze app = null;

    static public void main(String[] args ){
        String fname = maze10_50;
        // the following just allows command-line users to get correct
        // default behavior on first install without changing anything. 
        if (args.length < 1 && ! (new File("./mazes")).exists()){
            if ((new File("../mazes")).exists()){
                fname = "."+fname;
            }
        }
        if (args.length > 1){
            System.out.println("USAGE: Maze <file path>\n");
        }
        if (args.length < 1){
            System.out.format("Warning: no "
                    + "file parameter, proceeding with default file%s%n", fname);
        }
        if (args.length == 1){
            fname = args[0];
        }
        try {
        app = new Maze();
        app.run(fname);
        } catch (IOException e) { 
            System.out.println(e.getMessage());
        }
    };
    
    public void run(String fName) throws FileNotFoundException{
        MazeBuilder builder = new MazeBuilder();
        BareG g = builder.buildGraph(fName);

        try { 
        BareV source = g.getVertex(0);
        BareV last = g.getVertex(9);

        List<Integer> path = PathFinder.findPath(g, source, last);
        
         System.out.format("Path length: %s%n", 
                 (path == null)? "No Path": path.size() );
         System.out.format("Path cost:   %s%n",
                 (path == null)? "No Path": PathFinder.lastCost );
         System.out.format("Path %s%n",
                 (path == null)? "No Path": path.toString());
         System.out.println(g);
        } catch (Exception e){
            System.out.format("Failed to build usable graph: %s%n", e.getMessage());
        }
    }
}
