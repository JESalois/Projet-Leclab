package parameters.geometry;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 * Insert Description of class
 * @author Philippe de Sève
 */
public class Square extends AbstractShape{

    private final int NUMBER_OF_VERTEX = 4;
    
    public Square(){
        super();
    }
    
    public Square(Square rectangleToCopy){
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
        return new Square(this);
    }

   @Override
    public Polygon getPolygon() {
        int length = this.getWidth() < this.getHeight() ? this.getWidth() : this.getHeight();
        Polygon p = new Polygon();
        p.addPoint(0, 0);
        p.addPoint(length, 0);
        p.addPoint(length, length);
        p.addPoint(0 , length);
        return p;
    }
}
