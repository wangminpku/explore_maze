package cn.edu.pku.sei.rendering;

import cn.edu.pku.sei.mapStructures.ConnType;
import cn.edu.pku.sei.mapStructures.Coordinate;
import cn.edu.pku.sei.mapStructures.HexDir;
import cn.edu.pku.sei.shared.Graph;

public class HexGraph<E extends HexGridCell> extends Graph<E>  {
    public int width;
    public int height;
    
    public static class HexEdge<E> extends Graph.Edge<E>{
        public ConnType type = null; 
        
        public HexEdge(Vertex<E> from, Vertex<E> to, ConnType cType, int computedWeight){
            super(from, to, computedWeight);
            this.type=cType;
        }

    }
    
    public void listSpecialEdges(){
//        System.out.println("Special Edges\n");
        for (Vertex<E> v : getVertices()){
            for (Edge<E> e: v.getEdges()){
                if (e instanceof HexEdge<?>){
//                    System.out.format("%d Edge %d -> %d %n",
//                            v.getIndex(), e.from.getIndex(), e.to.getIndex());
                }
            }
        }
    }
    
    public HexGraph(int width, int height, E defaultCell, int defEdgeWeight){
        super();
        this.width = width;
        this.height = height;
        Coordinate.checkGeometry(width, height);
//        System.out.println("HexGraph Geometry is (HxW) "+height+","+width);
        //populate grid in row major order
        for (int y = 0; y < height; y ++){
            for (int x = 0; x < width; x++ ){
                 E cell = (E) defaultCell.clone();
                 int uid = super.addVertex(cell);
                 if (uid != getUid(x,y)){                     
                     String msg = String.format("Error in expected uid relation %d != %d x %d + %d",
                             uid, width, y, x);
                     throw new IllegalStateException(msg);
                 }
                 cell.setCoordinate(uid);
            }
        }
//        System.out.println("BEFORE LINKING\n"+toString());
        for (Vertex<E> v: getVertices()){
            linkNeighbors( v, defEdgeWeight );
        }
//        System.out.println("AFTER LINKING\n"+toString());
    }
    
    
    private void linkNeighbors(Vertex<E> from, int defWeight){
        Coordinate fromCoord = from.getData().getCoordinates();
        for (HexDir dir: HexDir.values()){
            
            Coordinate toCoord = fromCoord.get(dir);
            if (toCoord == null) continue;
//            System.out.format("Linking uids %s %d %d for %s,%s %n" , 
//                    dir.name(), fromCoord.getLinear(), toCoord.getLinear(),
//                    fromCoord.getBoard(), toCoord.getBoard());
            Vertex<E> to = super.getVertex(toCoord.getLinear());
//            System.out.format("Adding edge (%d,%d:%d)%n", 
//                    to.getIndex(), from.getIndex(), defWeight);
            super.addEdge(from, to, defWeight);
        }
    }
        
    private boolean checkInRectBounds(int toX, int toY) {
        return ((toX >=0 && toY >= 0 ) && toX < width && toY < height);        
    }

    private int getUid(int col, int row){
        return row*width+col;
    }

    Vertex<E> getVertexRect(int x, int y) {
        return super.vertices.get(getUid(x,y));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * selects a vertex using board hex coordinates (vs rectangular) 
     * @param x board x
     * @param y board y (diagonal down) 
     * @return
     */
    public Vertex<E> getVertex(int x, int y) {
        return getVertexRect(x, y-((x+1)/2 ));
    }

    public boolean isValidVertex(int x, int y) {
        int ty = y - ((x+1)/2);
//        System.out.format("Board coordinates %d, %d mapped to rect %d %d%n",
//                x, y, x, ty);
        return (checkInRectBounds(x,ty ) && super.checkUid(x + ty* width));      
    }

    /* Removes the edge link associated with the undirected
     * edge from -- to
     * @see cn.edu.pku.sei.rendering.Graph#removeEdge(int, int)
     */
    public void removeEdge(int from, int to) {
        super.removeEdge( from, to); 
    }

    public void addEdge(Edge<E> edge) {
        super.addEdge(edge); 
    }    
}
