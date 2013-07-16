package parameters.geometry;

import java.awt.Graphics;
import java.awt.Polygon;

/**
 * 
 * @author Philippe de Sève
 */
public class Pentagram extends AbstractShape{
    
    public Pentagram(){
        super();
    }
    
    public Pentagram(Pentagram pentagramToCopy){
        super(pentagramToCopy);
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(m_shapeColor);
 
        g.fillPolygon(getPolygon());
    }
    
    @Override
    public AbstractShape clone() {
        return new Pentagram(this);
    }

    @Override
    public Polygon getPolygon() {
        Polygon p = new Polygon();
        for (int i = 0; i < 5; i++){
            p.addPoint((int)(getWidth()/2 + getWidth()/2 * Math.cos((i * 2 * Math.PI / 5 )-Math.PI/2)),
                    (int) (getHeight()/2 + getHeight()/2 * Math.sin((i * 2 * Math.PI / 5 )-Math.PI/2)));
            p.addPoint((int)(getWidth()/2 + getWidth()/4 * Math.cos((i * 2 * Math.PI / 5 )-(Math.PI/2) + (Math.PI/5))),
                    (int)(getHeight()/2 + getHeight()/4 * Math.sin((i * 2 * Math.PI / 5 )-(Math.PI/2)+ ( Math.PI/5))));
        }
        return p;
    }

}