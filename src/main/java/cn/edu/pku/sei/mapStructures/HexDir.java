package cn.edu.pku.sei.mapStructures;

import cn.edu.pku.sei.mapStructures.Coordinate.Pair;
import cn.edu.pku.sei.rendering.Delta;

public enum HexDir {

    up,
    upRight,
    downRight,
    down,
    downLeft,
    upLeft   
    ;
    
  /**
 * @return the HexDir for 180 degrees from this. 
 */
public HexDir reverse() {
      switch(this){
      case up: return HexDir.down;
      case down: return HexDir.up;
      case upRight: return HexDir.downLeft;
      case upLeft: return HexDir.downRight;
      case downRight: return HexDir.upLeft;
      case downLeft: return HexDir.upRight;
      default: return null;
      }
  }
  
  private final static Delta dUp = new Delta(0, -1);
  private final static Delta dUpRight = new Delta(1,0);
  private final static Delta dDownRight = new Delta(1,1);
  private final static Delta dDown = new Delta(0,1);
  private final static Delta dDownLeft = new Delta(-1,0);
  private final static Delta dUpLeft = new Delta(-1,-1);
  
  private final static Delta dUpRightE = new Delta(1,-1);
  private final static Delta dDownLeftE = new Delta(-1,0);
  private final static Delta dDownLeftO = new Delta(-1,1);
  
  public static Delta getRectDelta(HexDir dir, boolean even) {
      Delta rval = null; 
      switch (dir) {
      case up : 
          return dUp;
      case upRight:
          return (even)? dUpRightE : dUpRight;
      case downRight:
          return (even)? dUpRight : dDownRight;
      case down: 
          return dDown;
      case upLeft:
          return (even)? dUpLeft: dDownLeft;
      case downLeft:
          return (even)? dDownLeftE: dDownLeft;
      }
      
      // TODO Auto-generated method stub
      return null;
  }

  public Delta getDelta(){
      switch(this){
      case up: return dUp;
      case down: return dDown;
      case upRight: return dUpRight;
      case upLeft: return dUpLeft;
      case downRight: return dDownRight;
      case downLeft: return dDownLeft;
      default: return null;
      }      
  }

  public int getAngle(){
      switch(this){
      case up: return 0;
      case down: return 180;
      case upRight: return 60;
      case upLeft: return -60;
      case downRight: return 120;
      case downLeft: return -120;
      default: return 0;
      }      
  }


    /**
     * The input xdy is a vector (in board coordinates) reflecting a link.
     * 
     * @param xdy
     * @return
     */

    public static HexDir inferDir(Pair xdy) {
        HexDir rval = null;
        switch (xdy.x) {
        case -1:
            rval = (xdy.y < 0) ? upLeft : downLeft;
            break;
        case 0:
            rval = (xdy.y < 0) ? up : down;
            break;
        case 1:
            rval = (xdy.y > 0) ? downRight : upRight;
            break;
        }
        return rval;
    }

    public static int inferAngle(Pair xdy) {
        HexDir dir = inferDir(xdy);
        
        return (dir == null) ? 0: dir.getAngle();
    }
    
    /**
     * Given a two character abbreviation (dn, up, ur, ul, dr, and dl) in 
     * While this function is case insensitive, other consumers of the
     * abbreviations are not, so developers should only use lower case versions. 
     * @param abrv
     * @return the corresponding HexDir value. 
     */
    public static HexDir fromAbbrev(String abrv){
        switch(abrv.toLowerCase()){
        case "dn": return HexDir.down;
        case "up": return HexDir.up;
        case "ur": return HexDir.upRight;
        case "ul": return HexDir.upLeft;
        case "dr": return HexDir.downRight;
        case "dl": return HexDir.downLeft;
        default: return null;
        }
    }

}

