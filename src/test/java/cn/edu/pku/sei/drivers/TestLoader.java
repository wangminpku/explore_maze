package cn.edu.pku.sei.drivers;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.junit.Test;

import cn.edu.pku.sei.TerrainLoader;
import cn.edu.pku.sei.mapStructures.Coordinate;
import cn.edu.pku.sei.mapStructures.ParseErrorException;
import cn.edu.pku.sei.rendering.TerrainBoard;
import cn.edu.pku.sei.rendering.TerrainGraph;
import cn.edu.pku.sei.util.NoiseFilterReader;

public class TestLoader {

    String eol = System.lineSeparator();
    String minMap = 
            "geometry   5 6 "+eol+eol+
            "flags 2 3 5 5"+eol+eol
            ;
    
    String terrain = 
            "water "+eol
            + "1 3 2 4"+eol+eol+
            "brush "+eol
            + "0 4 0 5 1 5 1 6 2 6 3 7"+eol+eol+
            "forest "+eol
            + "3 3 3 4 3 5 4 4 4 5 4 6"+eol+eol
            ;
    
    String paths = 
            "hwy 2 3 3 3 4 3"+eol+eol+
            "unpaved 3 5 4 5 4 4"+eol+eol+
            "dividedhwy 0 3 1 4 1 5 2 6 3 7"+eol+eol+
            "river 2 1 2 2 1 2 1 3"+eol+eol;
    
    String borders = 
            "barrierwall 2 3 ul dl dn "
            + "3 4 dl dn" +eol+eol;
    
    String solution= 
            "testpath 2 3 2 2 1 2 0 2 0 3"+eol+eol;
    
    
    @Test
    public void testFromFile() throws ParseErrorException, FileNotFoundException {
        FileReader fRdr = new FileReader(new File("terrain a 5 6 .txt"));
        Reader rdr = new NoiseFilterReader(fRdr);
        TerrainLoader loader = new TerrainLoader(rdr);
        TerrainGraph tGraph = loader.load();
        display(tGraph);
        System.out.println(tGraph.toString());
    }


    @Test
    public void testSolution() throws ParseErrorException {
        Reader rdr = new NoiseFilterReader(
            new StringReader(
             minMap+terrain+paths+borders+solution));       
        TerrainLoader loader = new TerrainLoader(rdr);
        TerrainGraph tGraph = loader.load();
        display(tGraph);
    }
    
    @Test 
    public void testBorders() throws ParseErrorException {
        Reader rdr = new NoiseFilterReader(
            new StringReader(minMap+terrain+paths+borders));       
        TerrainLoader loader = new TerrainLoader(rdr);
        TerrainGraph tGraph = loader.load();
        display(tGraph);
    }
    
    @Test
    public void testPaths() throws ParseErrorException {
        Reader rdr = new NoiseFilterReader(new StringReader(minMap+terrain+paths));       
        TerrainLoader loader = new TerrainLoader(rdr);
        TerrainGraph tGraph = loader.load();
        display(tGraph);
    }
    
    @Test
    public void testTerrain() throws ParseErrorException {
        Reader rdr = new NoiseFilterReader(new StringReader(minMap+terrain));       
        TerrainLoader loader = new TerrainLoader(rdr);
        TerrainGraph tGraph = loader.load();
        display(tGraph);
    }
    
    @Test
    public void testMin() throws ParseErrorException {
        Reader rdr = new NoiseFilterReader(new StringReader(minMap));
        TerrainLoader loader = new TerrainLoader(rdr);
        TerrainGraph tGraph = loader.load();
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
        scan.nextLine();
    }

}
