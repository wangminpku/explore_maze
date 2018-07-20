package cn.edu.pku.sei.shared;



public interface BareE {

    /** returns the neighbor (to) vertex 
     * associated with this edge.      */
    BareV getToVertex();

    /** The weight associated with this edge. */
    int getWeight();        
}


