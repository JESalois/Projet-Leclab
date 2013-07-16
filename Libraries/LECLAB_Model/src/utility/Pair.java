package utility;

import java.io.Serializable;

/**
 * A pair contains 2 separate objects accessible directly
 * @author Jean-Etienne
 */
public class Pair<X, Y> implements Serializable{

    /**
     *First element
     */
    public X x;
    /**
     *Second element
     */
    public Y y;
    
    /**
     * Constructor
     * @param x
     * @param y 
     */
    public Pair(X _x, Y _y){
        x = _x;
        y = _y;
    }
}
