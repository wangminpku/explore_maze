package cn.edu.pku.sei.mapStructures;

public enum TerrainType {
    kDefault(165),
    kBrush(180),
    kForest(195),
    kFlag1(0),
    kWater(190),
    kFlag2(0);
    
    private int numVal;
    TerrainType(int val){
        this.numVal = val;
    }
    
    public int getNumVal() {
        return numVal;
    }

    public int getWeight() {
        return numVal;
    }    
}
