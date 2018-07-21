package cn.edu.pku.sei;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

import cn.edu.pku.sei.util.NoiseFilterReader;
import cn.edu.pku.sei.util.TerrainScanner;

public class TestNoiseFilter {

    @Test
    public void test() throws IOException {
        String nop;
        if (System.lineSeparator().length() == 2) {
            nop =String.format( "no  punct    17  but 0 1  odd stuff  ");
        }
        else {
            nop =String.format( "no  punct   17  but 0 1  odd stuff ");
        }
        testHelper(new StringReader(nop), nop);
        String punc = String.format("no  punct!%n_17, but(0,1):odd;stuff%n");
        testHelper(new StringReader(punc), nop);
    }
    
    public void testHelper(Reader in, String expected) throws IOException{
       char[] buffer = new char[1024]; 
       Reader rdr = new NoiseFilterReader(in);
       int rval = rdr.read(buffer, 0, 1024);
       String result = new String(buffer,0,rval);
       System.out.println(result);
       assertEquals(expected.length(), rval);
       assertEquals(expected, result);
       
       String testData = " ..... ";
       NoiseFilterReader readdr = new NoiseFilterReader(new StringReader(testData));
       TerrainScanner scan = new TerrainScanner(readdr);
    }

}
