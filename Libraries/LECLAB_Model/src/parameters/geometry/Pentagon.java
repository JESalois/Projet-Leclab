package parameters.geometry;

import java.awt.Graphics;
import java.awt.Polygon;

/**
 *
 * @author Philippe de Sève
 */
public class Pentagon extends AbstractShape{
    
    private final int NUMBER_OF_VERTEX = 5;
    
    public Pentagon(){
        super();
    }
    
    public Pentagon(Pentagon pentagonToCopy){
        super(pentagonToCopy);
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
        return new Pentagon(this);
    }

    @Override
    public Polygon getPolygon() {
        Polygon p = new Polygon();
        for (int i = 0; i < NUMBER_OF_VERTEX; i++){
             p.addPoint((int)(this.getWidth()/2  + this.getWidth()/2 * Math.cos(i * 2* Math.PI / NUMBER_OF_VERTEX + 3*Math.PI/2)),
                    (int)(this.getHeight()/2 + this.getHeight()/2 * Math.sin(i * 2* Math.PI / NUMBER_OF_VERTEX + 3*Math.PI/2)));
        }
        return p;
    }
}