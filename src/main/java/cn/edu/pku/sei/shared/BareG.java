package cn.edu.pku.sei.shared;

import cn.edu.pku.sei.shared.BareV;

public interface BareG{

    // does vertex exist in the graph? 
    boolean checkVertex(BareV vertex);

    /* retrieve a vertex using its index
     * (or linear identity). Returns null
     * if the index is not the identifier
     * of an existing vertex.  
     */
    BareV getVertex(int index);    
}
