package parameters.geometry;
import java.awt.Color;
import java.awt.Polygon;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Abstract class representing shapes to be shown at screen.
 * @author Philippe de Sève, Jean-Etienne Salois
 */
public abstract class AbstractShape extends JPanel{
    
    /**
     * The color of the shape.
     */
    protected Color m_shapeColor;
    
    /**
     * Default constructor. Color is set to black.
     */
    public AbstractShape(){
        super();
        m_shapeColor = Color.BLACK;
    }
    
    /**
     * Copy constructor
     * @param shapeToCopy
     *          shape to be copied
     */
    public AbstractShape(AbstractShape shapeToCopy){
        super();
        m_shapeColor = shapeToCopy.m_shapeColor;
    }
    
    /**
     * Setter or m_shapeColor
     * @param color
     *          the color of the shape
     * @see #m_shapeColor
     */
    public void setShapeColor(Color color){
        m_shapeColor = color;
    }
    
    /**
     * Sets the size of height and width in pixel.
     * @param size
     *          size of the shape in pixel.
     */
    public void setSize(int size){
        this.setSize(size, size);
    }
    
    /**
     * Getter for m_shapeColor.
     * @return the colors of the shape
     * @see #m_shapeColor
     */
    public Color getShapeColor(){
        return m_shapeColor;
    }
    
    /**
    * HashCode redefinition
    * @return hashCode
    */
    @Override
    public int hashCode(){
        return Objects.hash(super.hashCode(), m_shapeColor);
    }

    /**
    * Tells if the object passed in parameters is equal to this one
    * @param obj
    *           the object to compare to
    * @Return if they are equals
    */
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final AbstractShape shape = (AbstractShape)obj;
        
        return Objects.equals(m_shapeColor, shape.m_shapeColor) 
                && Objects.equals(getSize(), shape.getSize());        
    } 
    
    /**
     * Subclass must redefined clone function.
     * @return the cloned shape
     */
    @Override
    public abstract AbstractShape clone();
    
    /**
     * Subclass must implement a function that returns the polygon to draw.
     * @return the polygon to draw      
     */
    public abstract Polygon getPolygon();
}
