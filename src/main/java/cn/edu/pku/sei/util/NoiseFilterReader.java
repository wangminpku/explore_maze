package cn.edu.pku.sei.util;

import java.io.FilterInputStream;
import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.zip.InflaterInputStream;

public class NoiseFilterReader extends FilterReader {
    
    public NoiseFilterReader(Reader in) {
        super(in);
    }
    
    @Override
    public boolean markSupported(){
        return false;
    }
    
    @Override
    public void mark(int limit) throws IOException{
        throw new IOException("Operation not supported");
    }
    
    
    @Override
    public int read() throws IOException{
        char[] lbuf = new char[1];
        int rval = read(lbuf, 0, 1);
        Character c = lbuf[0];
        return (rval>0) ? c.getNumericValue(c) : rval;
    }
    
    @Override
    public int read(char[] cbuf, int off, int len) throws IOException{
        int rval = super.read(cbuf, off, len);
        for (int i = off; i < rval; i++){
            Character c = cbuf[i];
            if (c == '\n' || c=='\r') cbuf[i]=' ';
            if (c.isLetterOrDigit(c) || c.isWhitespace(c) ){
                continue;
            } else {
                cbuf[i]=' ';
            }
        }
        return rval;
    }
    
}
