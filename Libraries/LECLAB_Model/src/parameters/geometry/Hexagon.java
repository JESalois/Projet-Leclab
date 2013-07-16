package parameters.geometry;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 * Hexagon used to display visual distractor.
 * @author Philippe de Sève
 */
public class Hexagon extends AbstractShape{
    
    private final int NUMBER_OF_VERTEX = 6;
    
    public Hexagon(){
        super();
    }
    
    public Hexagon(Hexagon hexagonToCopy){
        super(hexagonToCopy);
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
        return new Hexagon(this);
    }

    @Override
    public Polygon getPolygon() {
        Polygon p = new Polygon();
        for (int i = 0; i < NUMBER_OF_VERTEX; i++){
             p.addPoint((int)(this.getWidth()/2  + this.getWidth()/2 * Math.cos(i * 2* Math.PI / NUMBER_OF_VERTEX)),
                    (int)(this.getHeight()/2 + this.getHeight()/2 * Math.sin(i * 2* Math.PI / NUMBER_OF_VERTEX)));
        }
        return p;
    }

}