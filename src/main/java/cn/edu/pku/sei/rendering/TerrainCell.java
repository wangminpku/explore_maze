package cn.edu.pku.sei.rendering;

import cn.edu.pku.sei.mapStructures.Coordinate;
import cn.edu.pku.sei.mapStructures.TerrainType;

public class TerrainCell 
implements BoardCell, Cloneable {

    TerrainType terrain;
    Coordinate coord;
    
    public TerrainCell(TerrainType type){
        terrain = type;
    }

    public TerrainType getTerrain(){
        return terrain;
    }
    
    @Override
    public int getHexX() {
        return (coord == null)? -1: coord.getHexX();
    }

    @Override
    public int getHexY() {
        return (coord == null)? -1: coord.getHexY();
    }

    @Override
    public void setHexX(int x) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void setHexY(int y) {
        throw new UnsupportedOperationException("not implemented");
    }
    
    @Override
    public TerrainCell clone(){
        TerrainCell rval = null;
        try {
           Object temp =  super.clone(); 
           if (! (temp instanceof TerrainCell)){
               return null;
           }
           coord = null; 
           rval = (TerrainCell) temp;
        } catch(CloneNotSupportedException e){
            throw new Error();
        }
        return rval;     
    }

    public void setTerrain(TerrainType type) {
       terrain = type; 
    }

    @Override
    public Coordinate getCoordinates() {
        return coord;
    }

    @Override
    public void setCoordinate(Coordinate ycx) {
        coord = ycx;     
    }

    @Override
    public void setCoordinate(int index) {
        coord = new Coordinate(index);
    }


}
