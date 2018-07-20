package cn.edu.pku.sei.rendering;

public class Delta {

    public int x = 0;
    public int y = 0; 
    public Delta(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String toString(){
        return ""+x+","+y+" ";
    }
}
