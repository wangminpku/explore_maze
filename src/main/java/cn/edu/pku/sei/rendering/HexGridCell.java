package cn.edu.pku.sei.rendering;

import cn.edu.pku.sei.mapStructures.Coordinate;

public interface HexGridCell extends Cloneable {
    public Coordinate getCoordinates();
    public void setCoordinate(Coordinate ycx);
    public void setCoordinate(int index);
    public Object clone();

}
