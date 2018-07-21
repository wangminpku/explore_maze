package cn.edu.pku.sei.drivers;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import cn.edu.pku.sei.shared.BareV;
import cn.edu.pku.sei.shared.EE;
import cn.edu.pku.sei.shared.Graph;
import cn.edu.pku.sei.shared.Maze;
import cn.edu.pku.sei.shared.MazeBuilder;
import cn.edu.pku.sei.shared.PathFinder;

/**
 * This driver (it doesn't evaluate results) is handy for launching tests
 * (especially when you want to work in debug mode) of the shortest path
 * algorithm. 
 * 
 * NOTE: see MazeTest for an automated test covering all these files. 
 * 
 * @author Robert Ward
 *
 */
public class MazeDriver {
    private static String[] maze = { 
            "mazes/maze.txt",    
            "mazes/maze.10.10.50.txt",
            "mazes/maze.20.10.90.txt",
            "mazes/maze.30.100.10.txt",
            "mazes/maze.30.50.75.txt"
    };

    // edit this line to run with a different one of the above input files. 
    //     //
    int selected = 0; 
    @Test
    public void test() throws FileNotFoundException {
        MazeBuilder builder = new MazeBuilder();
        Graph<EE> g = builder.buildGraph(maze[selected]);
        System.out.println("A graph representing a maze");
        System.out.println(g);

        BareV source = builder.getEndVertex(g, true);
        BareV last = builder.getEndVertex(g, false);
        List<Integer> path = PathFinder.findPath(g, source, last);
        System.out.println("A shortest path in the maze");
        if ( path != null )
          System.out.println( path );
        else
          System.out.println( "No path exists" );
   }
    
   @Test
   public void testMain() {
       String[] args = { };
       Maze.main(args );
   }
}
