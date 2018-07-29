package cn.edu.pku.sei;


import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import cn.edu.pku.sei.mapStructures.Coordinate;
import cn.edu.pku.sei.mapStructures.ParseErrorException;
import cn.edu.pku.sei.rendering.TerrainBoard;
import cn.edu.pku.sei.rendering.TerrainGraph;
import cn.edu.pku.sei.shared.BareG;
import cn.edu.pku.sei.shared.BareV;
import cn.edu.pku.sei.shared.PathFinder;
import cn.edu.pku.sei.util.DirectoryProbe;
import cn.edu.pku.sei.util.NoiseFilterReader;

public class Explore {

    private static Explore app;
    
    public static void main(String[] args) throws InterruptedException, IOException{
        String file = "terrain.txt";
        String mapsloc = DirectoryProbe.probe("maps", "src/maps");
        if (mapsloc == null){
            throw new FileNotFoundException("Cannot find maps directory.");
        }
        System.out.println("Using maps from "+(new File(".")).getCanonicalPath()+mapsloc);
        if (args.length < 1){
            System.out.println("Explore takes a filename as argument\n"
                    + "Using default file "+file);
        } else {
           file = args[0];
           if (file.startsWith(File.pathSeparator)){
               mapsloc = "";
           }
        }
        
        
        app = new Explore();
        try {
            FileReader fRdr = new FileReader(new File(mapsloc,file));
            NoiseFilterReader rdr = new NoiseFilterReader(fRdr);
            app.run(rdr);
            Thread.currentThread().sleep(5000);
        } catch (ParseErrorException e){
            
        } catch (IOException e){
            
        }
     }
    
    
    public void run(NoiseFilterReader rdr ) throws ParseErrorException {
        TerrainLoader loader = new TerrainLoader(rdr);
        TerrainGraph tGraph = loader.load();
        BareG g = tGraph;
        BareV start = g.getVertex(tGraph.getStart().getLinear());
        BareV goal  = g.getVertex(tGraph.getGoal().getLinear());

        List<Integer> result = PathFinder.findPath(g, start, goal);
        tGraph.setSolution(result);
        display(tGraph);
    }
    
    public void display(TerrainGraph g){
        TerrainBoard m = new TerrainBoard(g);
        int w = Coordinate.getWidth();
        int h = Coordinate.getHeight();
        m.setPreferredSize(new Dimension(w*144,h*144));
        JFrame f = new JFrame();
        f.setSize(500, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane jsp = new JScrollPane( m );
        f.setContentPane(jsp);
        f.setVisible(true);
        Scanner scan = new Scanner(System.in);
        //scan.nextLine();
    }

}
