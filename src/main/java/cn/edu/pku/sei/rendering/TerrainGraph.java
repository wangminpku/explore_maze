package cn.edu.pku.sei.rendering;

import java.util.Iterator;
import java.util.List;

import cn.edu.pku.sei.mapStructures.BorderSegment;
import cn.edu.pku.sei.mapStructures.ConnType;
import cn.edu.pku.sei.mapStructures.Coordinate;
import cn.edu.pku.sei.mapStructures.HexDir;
import cn.edu.pku.sei.mapStructures.ParseErrorException;
import cn.edu.pku.sei.mapStructures.Path;
import cn.edu.pku.sei.mapStructures.TerrainType;

public class TerrainGraph extends HexGraph<TerrainCell> {

    private Coordinate start;
    private Coordinate goal;
    
    public static TerrainCell template = new TerrainCell(TerrainType.kDefault);
    private Path path;
    
    public TerrainGraph(int width, int height){
        super(width, height, template, TerrainType.kDefault.getWeight());
    }
    
    public void setTerrain(int x, int y, TerrainType type) {
        Coordinate.checkGeometry(width, height);
        Coordinate coord = new Coordinate(x,y); //board
        Vertex<TerrainCell> v = getVertex(coord.getLinear());
        if (v != null && v.getData() != null){
            TerrainCell cellData = v.getData();
            cellData.setTerrain(type);
            updateEdgeWeights(v);
        }
    }

    public void updateEdgeWeights(Vertex<TerrainCell>v){
        for (Edge<TerrainCell> srcE: v.getEdges()){
            Vertex<TerrainCell> vTo = getVertex(srcE.getTo());
            Edge<TerrainCell> dstE = findMatchingEdge(vTo, srcE.from.getIndex());
            updateEdgeWeight(srcE, v, vTo);
            updateEdgeWeight(dstE, vTo, v);
        }
    }
    
    private void updateEdgeWeight(Edge<TerrainCell> e,
            Vertex<TerrainCell> vFrom,
            Vertex<TerrainCell> vTo) 
    {
        ConnType eType = ConnType.kDefault;
        if (e instanceof HexEdge){
            eType = ((HexEdge<TerrainCell>) e ).type;
        }
        e.setWeight(computeWeight(vFrom, vTo, eType));
    }

    private Edge<TerrainCell> findMatchingEdge(Vertex<TerrainCell> v, int dest){
        for (Edge<TerrainCell> e : v.getEdges()){
            if (e.getTo() == dest){
                return e;
            }
        }
        return null;
    }
        
    
    
    public void setEdge(int x, int y, int x1, int y1, ConnType type) throws Exception {
        Coordinate cFrom;
        Coordinate cTo;
        try {
            cFrom = new Coordinate(x, y);
            cTo = new Coordinate(x1, y1);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }
        setEdge(cFrom, cTo, type);
    }

    public void setEdge(Coordinate cFrom, Coordinate cTo, ConnType type) throws Exception {
        Vertex<TerrainCell> from;
        Vertex<TerrainCell> to;
        from = super.getVertex(cFrom.getLinear());
        to = super.getVertex(cTo.getLinear());
        if (from == null || to == null) {
            throw new Exception("SetEdge failed to find both vertices");
        }
//        System.out.format("SetEdge %s to %s %n", cFrom.getBoard(), cTo.getBoard());
        listSpecialEdges();
        super.removeEdge(cFrom.getLinear(), cTo.getLinear());
        super.removeEdge(cTo.getLinear(), cFrom.getLinear());
//        System.out.println("AFTER REMOVEEDGES\n" + toString());
        int computedWeight = computeWeight(from, to, type);
        super.addEdge(new HexEdge<TerrainCell>(from, to, type, computedWeight));
//        System.out.println("AFTER FirstAddEdgE\n" + toString());
        listSpecialEdges();
        super.addEdge(new HexEdge<TerrainCell>(to, from, type, computedWeight));
//        System.out.println("AFTER SecondAddEdgE\n" + toString());
        listSpecialEdges();
    }
    
    private int computeWeight(Vertex<TerrainCell> from,
            Vertex<TerrainCell> to, ConnType type) {
        if (type.isBarrier()){
            return type.getWeight();
        }
        int tw1, tw2, cw;
        tw1 = from.getData().getTerrain().getWeight();
        tw2 = to.getData().getTerrain().getWeight();
        cw = type.getWeight();
        int rval =  Math.min(cw, Math.max(tw1, tw2));
//        System.out.format("ComputedWt: %d,%d:%d%n",
//            from.getIndex(),to.getIndex(),rval);
        return rval;
    }

    public void checkCoordinates(int x, int y){
        if (! super.isValidVertex(x,y)){
            throw new IllegalArgumentException(
                String.format("(%d,%d) is not a valid vertex address %n",x,y)
            );
        }
        
    }

    public void setSolution(Path solution) {
        this.path = solution;        
    }
    
    public void setSolution(List<Integer> vDexes){
        if (vDexes.size() < 2){
            throw new IllegalArgumentException("A path must have at least two vertices.");
        }
        Path rval = new Path(new Coordinate(vDexes.get(0)), new Coordinate(vDexes.get(1)));
        for (int i = 2; i<vDexes.size();i++){
            rval.add(new Coordinate(vDexes.get(i)));
        }
        setSolution(rval); 
    }
    
    public Coordinate getStart(){
        return start;
    }
    
    public Coordinate getGoal(){
        return goal;
    }
    
    public Path getPath(){
        return path;
    }

    public void setFlags(Coordinate f1, Coordinate f2) {
        start = f1;
        goal = f2;
        setTerrain(f1.getHexX(), f1.getHexY(), TerrainType.kFlag1);
        setTerrain(f2.getHexX(), f2.getHexY(), TerrainType.kFlag2);
    }

    public void setTerrain(List<Coordinate> cells, TerrainType terrain) {
        for (Coordinate cell: cells){
            setTerrain(cell.getHexX(), cell.getHexY(), terrain);
        }
        
    }

    public void setConnectedPath(Path path, ConnType type) throws Exception {
       Coordinate head;
       Coordinate tail;
       Iterator<Coordinate> iter = path.iterator();
       //path is already tested for consistency
       tail = iter.next();
       while (iter.hasNext()){
           head = tail;
           tail=iter.next();
           setEdge(head, tail, type);
       }
    }

    public void setBorderEdges(List<BorderSegment> segs, ConnType type) throws Exception {
        for (BorderSegment seg: segs){
            Coordinate head = seg.getCoord();
            for (HexDir dir: seg.getDirs()){
                Coordinate tail = head.get(dir); 
                setEdge(head, tail, type);
            }
        }
        
    }
}
