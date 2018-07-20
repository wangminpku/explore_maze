package cn.edu.pku.sei.mapStructures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BorderSegment {
    private Coordinate coord;
    private Set<HexDir> dirs = new HashSet<HexDir>();
    
    public BorderSegment(Coordinate c){
        coord = c;
    }
    
    public void add(HexDir dir){
        dirs.add(dir);
    }
    
    public Coordinate getCoord(){
        return coord;
    }
    
    public Set<HexDir> getDirs(){
        return dirs;
    }
    
    
    @Override
    public String toString(){
        String dStr = "";
        for (HexDir dir: dirs){
            dStr+= " "+dir.name();
        }
        return String.format("(%s:%s)", 
                coord.getBoard(), dStr);
    }
}
