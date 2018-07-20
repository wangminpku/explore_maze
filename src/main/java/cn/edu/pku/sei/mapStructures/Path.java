package cn.edu.pku.sei.mapStructures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Path implements Iterable<Coordinate> {
    private List<Coordinate> path = new ArrayList<Coordinate>();
    
    public Path(Coordinate start, Coordinate next){
        if (start == null || next == null || !start.isNeighbor(next)){
            throw new IllegalArgumentException(
                    "Path requires two valid, neighboring (adjacent) coordinates to identify"
                    + "a valid first segment");
        }
        path.add(start); // bypass check for neighborliness
        add(next);
        
    }
    
    public void add(Coordinate next){
        if (next == null || !next.isNeighbor(path.get(path.size()-1))){
            throw new IllegalArgumentException(
                  String.format("Cannot add %sPath can only be extended with"
                  + " a valid neighboring (adjacent) coordinate.",
                  (next == null)?"null": next.getBoard()
                  )
             );
        }
        path.add(next);
    } 
    
    public Iterator<Coordinate> iterator(){
        return path.iterator();
    }
}
