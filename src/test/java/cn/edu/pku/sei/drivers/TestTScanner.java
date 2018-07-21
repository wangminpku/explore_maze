package cn.edu.pku.sei.drivers;

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
    
    //Note: this test is unstable because it depends on order of 
    //the edges in a collection. You must examine the result
    @Test
    public void testBSegment(){
        Coordinate.setGeometry(4, 5);
        String line = " 1 7 up dn 1 keyword";
        bScanHelper(line, "( (B:1,7): up down)");
        String simple = " 1 7 1 8 keyword";
        bScanHelper(simple,"( (B:1,7):)" );
        String all = " 1 7 ur dr ul dl up dn 1 8 keyword";
        bScanHelper(all,"( (B:1,7): downLeft up downRight down upRight upLeft)" );
    }

    private void bScanHelper(String data, String expected){
        TerrainScanner s = new TerrainScanner(data);
        assertTrue(s.hasNextBorderSegment());
        BorderSegment b1 = s.nextBorderSegment();
        System.out.println(b1);
        assertNotNull(b1);
        assertEquals(b1.toString(), expected.trim());
    }

}
