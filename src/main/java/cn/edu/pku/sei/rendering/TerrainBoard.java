package cn.edu.pku.sei.rendering;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import cn.edu.pku.sei.mapStructures.ConnType;
import cn.edu.pku.sei.mapStructures.Coordinate;
import cn.edu.pku.sei.mapStructures.HexDir;
import cn.edu.pku.sei.mapStructures.Path;
import cn.edu.pku.sei.mapStructures.TerrainType;
import cn.edu.pku.sei.mapStructures.Coordinate.Pair;
import cn.edu.pku.sei.shared.Graph;
import cn.edu.pku.sei.shared.Graph.Vertex;

public class TerrainBoard extends JPanel {
    Image background = null;

    Image cellOutline;
    Image brushBg;
    Image waterBg;
    Image wall0;
    Image wall1;

    int boardx = 15;
    int boardy = 15;

    int x = 72;
    int y = -10;
    int dx = 72 + 36;
    int dy = 63;
    
    TerrainGraph graph;
    
    public TerrainBoard( TerrainGraph g){
        graph = g;
        boardx = g.getWidth();
        boardy = g.getHeight();
        Coordinate.checkGeometry(boardx, boardy);
        Toolkit t = Toolkit.getDefaultToolkit();
        cellOutline = ImageCache.getInstance().getHexTile();
    }
    
    public void paint(Graphics g) {

        drawBackground(g);
        drawGridLayer(g);
        drawLinksLayer(g);
        drawDecorations(g);
        drawLabels(g);
        System.out.println(graph);
    }
    
    private void drawDecorations(Graphics g) {
        ImageCache ic = ImageCache.getInstance();
        for (Vertex<TerrainCell> v : graph.getVertices()){
            for (Graph.Edge<TerrainCell> item: v.getEdges()){
                if (item instanceof HexGraph.HexEdge){
                    HexGraph.HexEdge<TerrainCell> eItem = (HexGraph.HexEdge<TerrainCell>) item;
                    if (eItem.from != v) continue;
                    if (eItem.type != ConnType.path ) continue;
//                    System.out.format("DrawLinks: Edge at %d indexes from=%d, to=%d%n", 
//                            v.getIndex(),
//                            eItem.from.getIndex(),
//                            eItem.to.getIndex());
                    Coordinate cFrom = new Coordinate(eItem.from.getIndex());
                    Coordinate cTo = new Coordinate(eItem.to.getIndex());
                    Pair f = cFrom.getBoard();
                    Pair t = cTo.getBoard();
                    Pair dx = new Pair(t.x - f.x, t.y -f.y, 'B');
                    int angle = HexDir.inferAngle(dx);
                    BufferedImage img = ic.getConn(eItem.type, angle) ;
                    Pair scrnLoc = cFrom.getScreen();
                    g.drawImage(img, scrnLoc.x, scrnLoc.y, this);
                 
                }
             }
        }
        Path solution = graph.getPath();
        if (solution == null){
            return;
        }
        Coordinate seg1 = null; 
        Coordinate seg2 = null;
        for (Coordinate c: solution){
            if (seg1 == null && seg2 == null){
                seg2 = c;
                continue;
            }
            // compute direction of each segment
            // then apply it to half segments 
            seg1 = seg2;
            seg2 = c;
            Pair dt = seg2.getDelta(seg1);
           
            HexDir srcDir = HexDir.inferDir(dt);
            int srcAngle = srcDir.getAngle();
            HexDir nxtDir = srcDir.reverse();
            int nxtAngle = nxtDir.getAngle();
            //draw first half
            BufferedImage img1 = ic.getConn(ConnType.path, srcAngle) ;
            Pair scrnLoc = seg1.getScreen();
            g.drawImage(img1, scrnLoc.x, scrnLoc.y, this);
            //draw second half
            BufferedImage img2 = ic.getConn(ConnType.path, nxtAngle) ;
            Pair scrnLoc2 = seg2.getScreen();
            g.drawImage(img2, scrnLoc2.x, scrnLoc2.y, this);
            
            

        }
    }

    private void drawLinksLayer(Graphics g){
        ImageCache ic = ImageCache.getInstance();
        for (Vertex<TerrainCell> v : graph.getVertices()){
            for (Graph.Edge<TerrainCell> item: v.getEdges()){
                if (item instanceof HexGraph.HexEdge){
                    HexGraph.HexEdge<TerrainCell> eItem = (HexGraph.HexEdge<TerrainCell>) item;
                    if (eItem.from != v) continue;
                    if (eItem.type == ConnType.path ) continue;
//                    System.out.format("DrawLinks: Edge at %d indexes from=%d, to=%d%n", 
//                            v.getIndex(),
//                            eItem.from.getIndex(),
//                            eItem.to.getIndex());
                    Coordinate cFrom = new Coordinate(eItem.from.getIndex());
                    Coordinate cTo = new Coordinate(eItem.to.getIndex());
                    Pair f = cFrom.getBoard();
                    Pair t = cTo.getBoard();
                    Pair dx = new Pair(t.x - f.x, t.y -f.y, 'B');
                    int angle = HexDir.inferAngle(dx);
                    try{
                    BufferedImage img = ic.getConn(eItem.type, angle) ;
                    Pair scrnLoc = cFrom.getScreen();
                    g.drawImage(img, scrnLoc.x, scrnLoc.y, this);
                    } catch (Exception e){
                        System.err.format("No image in cache for %s edge %s -> %s%n",
                           eItem.type, f, t);
                    }
                 
                }
             }
        } 
    }

    private void drawBackground(Graphics g) {
        ImageCache ic = ImageCache.getInstance();
        for (int row = 0; row < boardy; row++) {
            for (int col = 0; col < boardx; col++) {
                Vertex<TerrainCell> v = graph.getVertexRect(col,row);
                if (v == null){
                    throw new NullPointerException("Null vertex at "+col+", "+row);
                }
                int locx = x + (col * dx);
                int locy = y + ((row * 2 + (col % 2)) * dy);
                background = null; 
                if (v.getData()!= null && v.getData().getTerrain() != null){
                    background = ic.getTerrain(v.getData().getTerrain());
                }    
                if (background != null) {
                    g.drawImage(background, locx, locy, this);
                }
            }
        }
    }
    
    private void drawLabels(Graphics g){
        for (int row = 0; row < boardy; row++) {
            for (int col = 0; col < boardx; col++) {
                int locx = x + (col * dx);
                int locy = y + ((row * 2 + (col % 2)) * dy);

                g.drawString("(" + (col) + "," + (row + (col + 1) / 2) + ")", locx + 62, locy + 72);
                g.drawString("UP", locx + 68, locy + 30);
                g.drawString("UR", locx + 110, locy + 50);
                g.drawString("DR", locx + 110, locy + 100);
                g.drawString("DN", locx + 68, locy + 127);
                g.drawString("UL", locx + 27, locy + 50);
                g.drawString("DL", locx + 27, locy + 100);
            }
        }
    }

    private void drawGridLayer(Graphics g) {
        for (int row = 0; row < boardy; row++) {
            for (int col = 0; col < boardx; col++) {
                int locx = x + (col * dx);
                int locy = y + ((row * 2 + (col % 2)) * dy);
                
                g.drawImage(cellOutline, locx, locy, this);
            }}
    }

    public static void main(String[] args) throws Exception {
        int h=15;
        int w=20;
        Coordinate.setGeometry(w,h);
        TerrainGraph g = new TerrainGraph(Coordinate.getWidth(), Coordinate.getHeight());
        g.setEdge(0, 0, 1, 1, ConnType.wall);
//        g.setEdge(0,0, 0,1, ConnType.wall);
//        System.out.println("AFTER SETEDGE\n"+g.toString());
        
        g.setEdge(0, 1, 1, 2, ConnType.wall);
        g.setEdge(1,1, 1, 2, ConnType.wall);
        g.setEdge(2, 2, 1, 2, ConnType.wall);
        g.setEdge(2, 3, 1, 2, ConnType.wall);
        g.setEdge(1, 3, 1, 2, ConnType.wall);
        g.setEdge(0, 2, 1, 2, ConnType.wall);
        
        g.setEdge(0, 0, 1, 1, ConnType.dirt);
        g.setEdge(1, 1, 2, 1, ConnType.dirt);
        
        g.setEdge(0, 3, 1, 4, ConnType.hwy4);
        g.setEdge(1, 4, 2, 4, ConnType.hwy4);
        g.setEdge(2, 4, 2, 3, ConnType.hwy4);

        g.setEdge(1,1, 2,2, ConnType.hwy2);
        g.setEdge(2,2, 3,3, ConnType.hwy2);
        g.setEdge(3,3, 4,4, ConnType.hwy2);
        
        g.setEdge(2, 1, 3, 2, ConnType.river);
        g.setEdge(3, 2, 4, 2, ConnType.river);

        g.setEdge(0, 2,  1,  3, ConnType.river);
        g.setEdge(1, 3, 1, 4, ConnType.river);
        
        g.setTerrain(2, 2, TerrainType.kBrush);
        g.setTerrain(3, 3, TerrainType.kWater);
        g.setTerrain(2, 3, TerrainType.kForest);
        g.setTerrain(1, 3, TerrainType.kForest);
        g.setTerrain(0, 0, TerrainType.kFlag1);
        g.setTerrain(3, 5, TerrainType.kFlag2);
        
        Path solution = new Path(new Coordinate(0,0),
                new Coordinate(0,1));
        solution.add(new Coordinate(0,2));
        solution.add(new Coordinate(1,3));
        solution.add(new Coordinate(2,4));
        solution.add(new Coordinate(3,5));
        g.setSolution(solution);
        
//        System.out.println(g);
        TerrainBoard m = new TerrainBoard(g);
        
        m.setPreferredSize(new Dimension(w*144,h*144));
        JFrame f = new JFrame();
        f.setSize(500, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane jsp = new JScrollPane( m );
        f.setContentPane(jsp);
        f.setVisible(true);
    }

}