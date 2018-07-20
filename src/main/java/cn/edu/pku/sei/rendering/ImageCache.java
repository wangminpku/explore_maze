package cn.edu.pku.sei.rendering;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import cn.edu.pku.sei.mapStructures.ConnType;
import cn.edu.pku.sei.mapStructures.TerrainType;

public class ImageCache {
    
    private static ImageCache instance = null; 
    Map<Enum, Map<Integer,BufferedImage>> cache
        = new HashMap<Enum, Map<Integer, BufferedImage>>();

    Map<TerrainType, BufferedImage> tCache = new HashMap<TerrainType, BufferedImage>();
    private String imageFolder ="./images/";
    private String altImageLoc = "./src/images/";
    
    public BufferedImage hexTile;
    
    public static class FileAssn {
        
        private String fname;
        private Enum type;

        public FileAssn(String fname, TerrainType type){
           this.fname = fname;
           this.type  = type;
        }
        
        public FileAssn(String fname, ConnType type){
           this.fname = fname;
           this.type  = type;
        }
    }
    
    static FileAssn[] bgAssn = {
            // using weight for selector
            new FileAssn("bg_brush.png", TerrainType.kBrush),
            new FileAssn("bg_water.png", TerrainType.kWater),
            new FileAssn("bg_forrest.png", TerrainType.kForest),
            new FileAssn("bg_flag1.png", TerrainType.kFlag1),
            new FileAssn("bg_flag2.png", TerrainType.kFlag2)
    };
    
    static FileAssn[] txpAssn = {
            // using weight for transport cost
            new FileAssn("wall.png", ConnType.wall),
            new FileAssn("dirt.png", ConnType.dirt),
            new FileAssn("hwy2.png", ConnType.hwy2),
            new FileAssn("hwy4.png", ConnType.hwy4),
            new FileAssn("path.png", ConnType.path),
            new FileAssn("river.png", ConnType.river)
            
    };
       
    public ImageCache(){
        File cwd = new File(".");
        File dirProbed = new File(imageFolder);
        if (!dirProbed.exists()){
            // try the alternate
            dirProbed = new File(altImageLoc);
            if (dirProbed.exists()) imageFolder = altImageLoc;
        }
        System.out.format("Expecting image folder at %s%s%n",
                cwd.getAbsolutePath(),imageFolder);
        
        hexTile = get(imageFolder+"hexgrid_template.png");
        // complete the multimap
        for (int i = 0; i < bgAssn.length; i++){
            BufferedImage bgImg = get(imageFolder+bgAssn[i].fname);
            if (bgImg != null)
                tCache.put((TerrainType) bgAssn[i].type, bgImg);
        }
        for (int i = 0; i < txpAssn.length; i++){
            BufferedImage img = get(imageFolder+txpAssn[i].fname);
            if (img != null)
                cacheRotations( img, (ConnType) txpAssn[i].type);
        }    
    }
    
    public BufferedImage getHexTile(){
        return hexTile;
    }
    
    private void put(Enum type, Integer rAngle, BufferedImage img){
        Map<Integer, BufferedImage> group = null; 
        if ((group = cache.get(type)) == null){
            group = new HashMap<Integer, BufferedImage>();
            cache.put(type, group);
        }
        group.put(rAngle, img);
    }
    
    private void cacheRotations(BufferedImage img, ConnType type){
        put(type, 60, rotate(img, 60));
        put(type, 0, img);
        put(type, -60,rotate(img,-60));
        put(type, 120, rotate(img, 120));
        put(type, 180, rotate(img, 180));
        put(type, -120,rotate(img,-120));
    }
    
    private BufferedImage rotate(BufferedImage img, int angle){
        AffineTransform tx = AffineTransform.getRotateInstance(
                Math.toRadians(angle),
                71,71
                );
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(img, null);             
    }

    public BufferedImage getTerrain(TerrainType terrain){
        return tCache.get(terrain);
    }
    
    public BufferedImage getConn(ConnType conn, int angle){
        if (! this.isImageAvailable(conn, angle) ) {
            System.err.format("Failure to load image for connector type %s%n"+
               "Connector (i.e. a graph edge) will be ignored.%n", conn.name());
            return null; 
        }
        return cache.get(conn).get(angle);
    }
    
    public boolean isImageAvailable(ConnType conn, int angle){
        Map<Integer, BufferedImage> temp = cache.get(conn);
        if (temp == null) return false;
        BufferedImage tempImage = temp.get(angle);
        if (tempImage == null) return false;
        return true;
    }
    
    private BufferedImage get(String fname){
        BufferedImage rval = null; 
        try {
            rval = ImageIO.read(new File(fname));
        } catch (Exception e) {
            System.err.println("ImageCache.get Warning: "+e.getMessage()+ " "+fname);
            System.err.println("..cd is "+(new File(".")).getAbsolutePath());
        }
        return rval;
    }
    
    public static synchronized ImageCache getInstance(){
        if (instance == null){
            instance = new ImageCache();
        }
        return instance;
    }
}
