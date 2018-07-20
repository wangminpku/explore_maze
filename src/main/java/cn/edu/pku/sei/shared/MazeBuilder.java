package cn.edu.pku.sei.shared;

/*
 * @author
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import cn.edu.pku.sei.shared.Graph;

public class MazeBuilder
{
 
    public List<Integer> run(String fName) {
        List<Integer> rval=null;
        try {
            Graph<EE> g = buildGraph(fName);
            System.out.println("A graph representing a maze");
            System.out.println(g);

            Graph.Vertex source = getEndVertex(g, true);
            Graph.Vertex last = getEndVertex(g, false);
            rval = PathFinder.findPath(g, source, last);
            System.out.println("A shortest path in the maze");
            if (rval != null)
                System.out.println(rval);
            else
                System.out.println("No path exists");
         } catch (Exception e) {
            e.printStackTrace();
        }
        return rval;
    }

  /**
   * Constructs and returns a Graph object from the file with the given file name.
   * Each line in the file contains, in this order, a vertex, a colon (no space before the colon),
   * a number of vertex-cost pairs with a space before each pair.
   * Below is an example line:
   * 0: 2,3 4,1 6,2 8,4
   * This line specifies four directed edges from vertex 0 to each of
   * the four vertices: 2 (with a cost of 3), 4 (1), 6 (2), 8 (4).
   * If there is no edge from the vertex, then the line contains only
   * the vertex and a colon.
   *
   * Uses the g.add_edge() method to add each edge to the Graph object.
   *
   * @param fileName
   *            - The name of a file containing a directed graph
   * @throws FileNotFoundException
   *            - If the given file does not exist
   */
  public Graph<EE> buildGraph(String fileName) throws FileNotFoundException
  {
      FileInputStream f = new FileInputStream(fileName);
      Scanner l = new Scanner(f);
      Graph g = new Graph();
      
      int size = l.nextInt(); //let it throw exception if there is nothing.
      l.nextLine();
      for (int i=0; i < size; i++){
          g.vertices.add(new Graph.Vertex(i, null));
      }
      while (l.hasNextLine()) {
        String str = l.nextLine();
        Scanner s = new Scanner(str);
        s.useDelimiter(":");
        int from = s.nextInt();
        s.skip("[: ]*");
        s.useDelimiter(",");
        while (s.hasNextInt()) {
          int to = s.nextInt();
          s.useDelimiter(" ");
          s.skip(",");
          int weight = s.nextInt();
          s.useDelimiter(",");
          if (s.hasNext()) {
            s.skip(" ");
          }
          
          g.addEdge(from, to, weight);
        }
      }
     return g;
  }

  /**
   *  Returns the first vertex in the given graph if isFirst is true,
   *  otherwise, returns the last vertex in the graph.
   *  Uses the get_vertices() method to get an Iterable<Graph.Vertex> object
   *  for getting access to each vertex in the given graph.
   *  Note that this code assumes some Graph.Vertex references may be null.
   *  Other client applications might use null to fill in the empty slots
   *  in the ArrayList<Graph.Vertex> object when vertices are not sequentially
   *  numbered. (i.e, the graph might consist only of nodes 3, 5, 6 and 10.)
   *  
   *
   *  @param g
   *          - The Graph object
   *  @param isFirst
   *          - The flag to indicate whether the first or last vertex is returned
   *  @throws NullPointerException
   *          - If g is null
  */
  public Graph.Vertex getEndVertex(Graph g, boolean isFirst)
  {
      if ( g == null )
           throw new NullPointerException();
      Iterator<Graph.Vertex> v = g.getVertices().iterator();
      Graph.Vertex last = null;
      while ( v.hasNext() )
      {
        Graph.Vertex cur = v.next();
        if ( cur != null )
        {
          last = cur;
          if ( isFirst )
             return last;
        }
      }
     return last;
  }
}
