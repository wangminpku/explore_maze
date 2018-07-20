package cn.edu.pku.sei.util;

import java.io.File;

public class DirectoryProbe {

    public static String probe(String first, String second){
        File dirProbed = new File(".",first);
        if (dirProbed.exists()) 
            return first;

        // try the alternate
        dirProbed = new File(".",second);
        if (dirProbed.exists()) 
            return second;
       
        return(null);
    }
}
