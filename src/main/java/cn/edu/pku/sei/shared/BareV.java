package cn.edu.pku.sei.shared;

public interface BareV {

    //returns an iterable collection of
    //edges corresponding to this node's neighbors. 
    Iterable<BareE> getBareEdges();
    
    //returns the index (or linear identity)
    //of this node. 
    int getIndex(); 
}