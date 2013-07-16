package parameters.geometry;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 * Diamond used to display visual distractor.
 * @author Philippe de Sève
 */
public class Diamond extends AbstractShape{
    
    /**
     * Default constructor
     */
    public Diamond(){
        super();
    }
    
    /**
     * Copy constructor
     * @param diamondToCopy
     *          diamond shape to copy
     */
    public Diamond(Diamond diamondToCopy){
       super(diamondToCopy);
    }
    
     /**
     * Redefinition of paintComponent. Draw the polygon.
     * @param g 
     *          the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(m_shapeColor);
        g.fillPolygon(this.getPolygon());  
    }
    
     /**
     * Redefinition of clone.
     * @return the cloned circle
     */
    @Override
    public AbstractShape clone() {
       return new Diamond(this);
    }
    
     /**
     * Returns the polygoned shape diamond based on his size.
     * @return the circle polygon
     */
    @Override
    public Polygon getPolygon() {
        Polygon diamond = new Polygon();
        diamond.addPoint(this.getWidth()/2, 0);
        diamond.addPoint(0, this.getHeight()/2);
        diamond.addPoint(this.getWidth()/2, this.getHeight());
        diamond.addPoint(this.getWidth(), this.getHeight()/2);
        return diamond;
    }

}