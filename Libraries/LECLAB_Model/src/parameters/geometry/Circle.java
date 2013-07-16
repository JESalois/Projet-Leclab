package parameters.geometry;

import java.awt.Graphics;
import java.awt.Polygon;

/**
 * Circle used to display visual distractor.
 * @author Jean-Etienne Salois
 */
public class Circle extends AbstractShape{
    
    /**
     * Number of vertex. Used to specify the quality of the circle approximation.
     */
    private final int NUMBER_OF_VERTEX = 100;
    
    /**
     * Default constructor.
     */
    public Circle(){
        super();
    }
    
    /**
     * Copy constructor.
     * @param circleToCopy 
     *          the circle to copy
     */
    public Circle(Circle circleToCopy){
        super(circleToCopy);
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
        Polygon p = getPolygon();
        g.drawPolygon(p);
        g.fillPolygon(p);
    }

    /**
     * Redefinition of clone.
     * @return the cloned circle
     */
    @Override
    public AbstractShape clone() {
        return new Circle(this);
    }

    /**
     * Returns the polygon approximation of a circle based on his size.
     * @return the circle polygon
     */
    @Override
    public Polygon getPolygon() {
        Polygon circle = new Polygon();
        for (int i = 0; i < NUMBER_OF_VERTEX; i++){
             circle.addPoint((int)(this.getWidth()/2  + this.getWidth()/2 * Math.cos(i * 2* Math.PI / NUMBER_OF_VERTEX)),
                    (int)(this.getHeight()/2 + this.getHeight()/2 * Math.sin(i * 2* Math.PI / NUMBER_OF_VERTEX)));
        }
        return circle;
    }
}