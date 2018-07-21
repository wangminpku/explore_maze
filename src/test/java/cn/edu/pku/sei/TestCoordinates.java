package cn.edu.pku.sei;

import static org.junit.Assert.*;

import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;

import cn.edu.pku.sei.mapStructures.Coordinate;
import cn.edu.pku.sei.mapStructures.HexDir;

public class TestCoordinates {


    
    /**
     * This particular coordinate was not mapping correctly. 
     */
    @Test 
    public void test5_8Link(){
        String expected = "5: (R:1,1) (B:1,2) (S:180,179) (C:252,251)";
        Coordinate.setGeometry(4,5);
        Coordinate c5 = new Coordinate(5);
        String capture = String.format("%d:%s%s%s%s%n",
                5, c5.getRect(), c5.getBoard(),
                c5.getScreen(), c5.getScreenCenter()
           );
        System.out.println(capture);
        assertEquals(expected, capture.trim());
    }
    
    
    String eol = System.lineSeparator();
    String dirExpected =
        "0  (B:0,0):  downRight 1 (B:1,1) down 4 (B:0,1)"+eol
        + "1  (B:1,1):  upRight 2 (B:2,1) downRight 6 (B:2,2) down 5 (B:1,2) downLeft 4 (B:0,1) upLeft 0 (B:0,0)"+eol
        + "2  (B:2,1):  downRight 3 (B:3,2) down 6 (B:2,2) downLeft 1 (B:1,1)"+eol
        + "3  (B:3,2):  down 7 (B:3,3) downLeft 6 (B:2,2) upLeft 2 (B:2,1)"+eol
        + "4  (B:0,1):  up 0 (B:0,0) upRight 1 (B:1,1) downRight 5 (B:1,2) down 8 (B:0,2)"+eol
        + "5  (B:1,2):  up 1 (B:1,1) upRight 6 (B:2,2) downRight 10 (B:2,3) down 9 (B:1,3) downLeft 8 (B:0,2) upLeft 4 (B:0,1)"+eol
        + "6  (B:2,2):  up 2 (B:2,1) upRight 3 (B:3,2) downRight 7 (B:3,3) down 10 (B:2,3) downLeft 5 (B:1,2) upLeft 1 (B:1,1)"+eol
        + "7  (B:3,3):  up 3 (B:3,2) down 11 (B:3,4) downLeft 10 (B:2,3) upLeft 6 (B:2,2)"+eol
        + "8  (B:0,2):  up 4 (B:0,1) upRight 5 (B:1,2) downRight 9 (B:1,3)"+eol
        + "9  (B:1,3):  up 5 (B:1,2) upRight 10 (B:2,3) upLeft 8 (B:0,2)"+eol
        + "10  (B:2,3):  up 6 (B:2,2) upRight 7 (B:3,3) downRight 11 (B:3,4) downLeft 9 (B:1,3) upLeft 5 (B:1,2)"+eol
        + "11  (B:3,4):  up 7 (B:3,3) upLeft 10 (B:2,3)"+eol;



    @Test
    public void testDirs() {
        int w=4;
        int h = 3;
        Coordinate.setGeometry(w,h);
        String result = "";
        for (int i = 0; i < w*h; i++){
            Coordinate c = new Coordinate(i);
            String neighbors = "";
            for (HexDir dir: HexDir.values()){
                Coordinate tx = c.get(dir);
                if (tx != null){
                    neighbors += " "+dir.name()+" "+tx.getLinear()+tx.getBoard();
                }
            }
            result += String.format("%d %s: %s%n", i, c.getBoard(), neighbors);
        }
//        System.out.println(dirExpected);
//        System.out.println(DatatypeConverter.printHexBinary(dirExpected.getBytes()));
//        System.out.println(DatatypeConverter.printHexBinary(result.getBytes()));
        System.out.println(result);
        assertEquals(dirExpected,result);
    }
    
    /**
     * A bug that should remain fixed.
     */
    @Test
    public void testDirDown() {
        String expected = "0  (B:0,0): down 2 (B:0,1)";
        int w=2;
        int h = 3;
        int linear = 0; 
        HexDir dir = HexDir.down;
        Coordinate.setGeometry(w,h);
        String result = "";
        Coordinate target = new Coordinate(linear);
        Coordinate c = target.get(dir);
        result += String.format("%d %s: %s %d%s%n", target.getLinear(),target.getBoard(), dir, c.getLinear(), c.getBoard());
        System.out.println(result);
        assertEquals(expected, result.trim());
        
    }   

}
