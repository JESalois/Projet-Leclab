/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package view.icons;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import javax.swing.Icon;
import parameters.geometry.AbstractShape;

/**
 * Insert Description of class
 * @author Philippe
 */
public class ShapeListIcon implements Icon{
    
    private AbstractShape [] m_shapeList;
    
    private int m_hgap;
    
    private Dimension m_size;
    
    public ShapeListIcon(AbstractShape [] shapeList, int hgap){
        m_shapeList = shapeList;
        m_hgap = hgap;
        int totalwidth = 0;
        for(int i = 0; i < m_shapeList.length; i++){
            if(i != 0 && i !=  m_shapeList.length -1){
                totalwidth += m_hgap;
            }
            totalwidth += m_shapeList[i].getWidth();
        }
        m_size = new Dimension(totalwidth, m_shapeList[0].getHeight());
        
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        
        Point position = new Point(x, y);
        for(int i = 0; i < m_shapeList.length; i++){
            Polygon shapePolygon = m_shapeList[i].getPolygon();
            for(int j = 0; j < shapePolygon.npoints; j++){
                shapePolygon.xpoints[j] += position.x;
                shapePolygon.ypoints[j] += position.y;
            }
            position.x += m_shapeList[i].getSize().width + m_hgap;
            g2.setColor(m_shapeList[i].getShapeColor());
            g2.fillPolygon(shapePolygon);
        }
        
    }

    @Override
    public int getIconWidth() {
        return m_size.width;
    }

    @Override
    public int getIconHeight() {
        return m_size.height;
    }
}
