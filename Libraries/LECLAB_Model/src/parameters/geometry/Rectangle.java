package parameters.geometry;

import java.awt.Graphics;
import java.awt.Polygon;

/**
 * Insert Description of class
 * @author Philippe de Sève
 */
public class Rectangle extends AbstractShape {
    
    private final int NUMBER_OF_VERTEX = 4;
    
    public Rectangle(){
        super();
    }
    
    public Rectangle(Rectangle rectangleToCopy){
        super(rectangleToCopy);
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(m_shapeColor);
        Polygon p = getPolygon();
        g.drawPolygon(p);
        g.fillPolygon(p);
    }

    @Override
    public AbstractShape clone() {
        return new Rectangle(this);
    }

   @Override
    public Polygon getPolygon() {
        Polygon p = new Polygon();
        p.addPoint(0, 0);
        p.addPoint(this.getWidth(), 0);
        p.addPoint(this.getWidth(), this.getHeight());
        p.addPoint(0 , this.getHeight());
        return p;
    }
}
