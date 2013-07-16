package parameters.geometry;

import java.awt.Graphics;
import java.awt.Polygon;

/**
 *
 * @author Philippe de Sève
 */
public class Triangle extends AbstractShape{
    
    private final int NUMBER_OF_VERTEX = 3;
    
    public Triangle(){
        super();
    }
    
    public Triangle(Triangle triangleToCopy){
        super(triangleToCopy);
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
        return new Triangle(this);
    }

    @Override
    public Polygon getPolygon() {
        Polygon p = new Polygon();
        p.addPoint(this.getWidth()/2, 0);
        p.addPoint(this.getWidth(), this.getHeight());
        p.addPoint(0 , this.getHeight());
        return p;
    }
}
