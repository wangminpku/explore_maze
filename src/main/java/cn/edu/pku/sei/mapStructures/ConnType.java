package cn.edu.pku.sei.mapStructures;

public enum ConnType {
    
    hwy4(110),
    hwy2(130),
    dirt(143),
    river(185),
    wall(900), 
    path(0), 
    kDefault(195);
    private int weight = 0;
    
    private ConnType(int w){
        weight = w;
    }
    public int getWeight() {
        return weight;
    }
    public boolean isBarrier() {
        return this== wall;
    }  
}
