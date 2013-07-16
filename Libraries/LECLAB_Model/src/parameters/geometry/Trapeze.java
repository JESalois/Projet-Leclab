package parameters.geometry;

import java.awt.Graphics;
import java.awt.Polygon;

/**
 *
 * @author Philippe de Sève
 */
public class Trapeze extends AbstractShape{
    
    public Trapeze(){
        super();
    }
    
    public Trapeze(Trapeze trapezeToCopy){
        super(trapezeToCopy);
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        
        g.setColor(m_shapeColor);
        g.fillPolygon(this.getPolygon());  
    }

    @Override
    public AbstractShape clone() {
        return new Trapeze(this);
    }

    @Override
    public Polygon getPolygon() {
        Polygon p = new Polygon();
        p.addPoint(this.getWidth()/4, 0);
        p.addPoint(0, this.getHeight());
        p.addPoint(this.getWidth(), this.getHeight());
        p.addPoint(3*this.getWidth()/4, 0);
        return p;
    }
}
