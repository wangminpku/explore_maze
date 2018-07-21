package cn.edu.pku.sei;

import static org.junit.Assert.*;

import org.junit.Test;

import cn.edu.pku.sei.mapStructures.HexDir;

public class TestHexDir {

    @Test
    public void test() {
        String result = "";
        String expected = "0:up 1:upRight 2:downRight 3:down 4:downLeft 5:upLeft";
        for (HexDir dir: HexDir.values()) {
            result += dir.ordinal()+":"+dir.name() + " ";
        };
        System.out.println(result);
        assertEquals(expected, result.trim());
    }
    
    @Test
    public void testDeltas(){
        String result = "";
        String expected = "0:0,-1  1:1,0  2:1,1  3:0,1  4:-1,0  5:-1,-1";
        for (HexDir dir: HexDir.values()) {
            result += dir.ordinal()+":"+dir.getDelta() + " ";
        };
        System.out.println(result);
        assertEquals(expected, result.trim());
    }

}
