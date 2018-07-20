package cn.edu.pku.sei.rendering;

public interface BoardCell extends HexGridCell, Cloneable {
    public int getHexX();
    public int getHexY();
    public void setHexX(int x);
    public void setHexY(int y);
    public Object clone();
}
