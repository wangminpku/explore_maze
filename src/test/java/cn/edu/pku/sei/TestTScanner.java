package cn.edu.pku.sei;

import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

import cn.edu.pku.sei.mapStructures.BorderSegment;
import cn.edu.pku.sei.mapStructures.Coordinate;
import cn.edu.pku.sei.mapStructures.HexDir;
import cn.edu.pku.sei.util.NoiseFilterReader;
import cn.edu.pku.sei.util.TerrainScanner;

public class TestTScanner {

    @Test
    public void testCoord() {
        Coordinate.setGeometry(4, 5);
        String line = "1 7 8";
        coordScanHelper(line);
        String padded = " 1 7 8  ";
        coordScanHelper(padded);
        String broken = " 1 "
                + "7 8 xx";
        coordScanHelper(broken);
        String newline = "\n 1 "
                + "7 8 xx";
        coordScanHelper(newline);
        String term = " 1 7 8  \n";
        coordScanHelper(term);

    }
    private void coordScanHelper(String data){ 
        Reader rdr = new NoiseFilterReader(new StringReader(data));
        TerrainScanner s = new TerrainScanner(rdr);
        assertTrue(s.hasNextCoord());
        Coordinate c = s.nextCoord();
        System.out.println(c.getBoard());
        assertNotNull(c);
        assertEquals(" (B:1,7)",c.getBoard().toString());
        assertFalse(s.hasNextCoord());
//        assertEquals(8, s.nextInt());
    }
  
    @Test
    public void testDir(){
        Coordinate.setGeometry(4,5);
        String line = " 1 7 up dn 2 4 up ur keyword";
        TerrainScanner s = new TerrainScanner(line);
        assertFalse(s.hasNextDir());
        assertTrue(s.hasNextCoord());
        Coordinate c = s.nextCoord();
        assertFalse(s.hasNextCoord());
        assertTrue(s.hasNextDir());
        HexDir dir = s.nextDir();
        assertNotNull(dir);
    }
    
}
