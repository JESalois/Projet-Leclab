package view.component.contentpane;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import parameters.VisualDistractorParameter;
import parameters.geometry.AbstractShape;
import sitting.SittingManager;

/**
 * Insert Description of class
 * @author Philippe
 */
public class VisualDistractorContentPane extends AbstractContentPane {
    
    private Timer m_displayTimer;
    
    private Timer m_downTimeTimer;
    
    private int m_numberOfDisplayedShapes;
    
    private AbstractShape [] shapesToDisplay;
    
    public VisualDistractorContentPane(SittingManager sittingManager, Dimension size){
        super(sittingManager);
        this.setLayout(new AbsoluteLayout());
        this.setSize(size);
        m_numberOfDisplayedShapes = 0;
        VisualDistractorParameter vdp = (VisualDistractorParameter)m_sittingManager.getTrueBlock(m_sittingManager.getCurrentBlock()).getParameters().get(VisualDistractorParameter.class);
        m_displayTimer = new Timer(vdp.getDisplayTime(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayTimerEnded();
            }
        });
        m_downTimeTimer = new Timer(vdp.getIntervalDuration(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downTimeTimerEnded();
            }
        });
       shapesToDisplay = (AbstractShape [])vdp.orderOfPlay(m_sittingManager.getCurrentSeed());
    }
    
    private void displayTimerEnded(){
        m_displayTimer.stop();
        this.remove(shapesToDisplay[m_numberOfDisplayedShapes]);
        this.revalidate();
        this.repaint();
        m_numberOfDisplayedShapes++;
        if(m_numberOfDisplayedShapes < shapesToDisplay.length){
            m_downTimeTimer.start();
        }
        else{
            m_sittingManager.VisualDistractionFinished();
        }
    }
    
    private void downTimeTimerEnded(){
        m_downTimeTimer.stop();
        displayNextShape();
    }
    
    private void displayNextShape(){
        shapesToDisplay[m_numberOfDisplayedShapes].setSize(this.getWidth()/4);
        Point shapePosition = new Point(this.getWidth()/2 - shapesToDisplay[m_numberOfDisplayedShapes].getHeight()/2, 
                this.getHeight()/2 - shapesToDisplay[m_numberOfDisplayedShapes].getHeight()/2);
        this.add(shapesToDisplay[m_numberOfDisplayedShapes],
                new AbsoluteConstraints(shapePosition, shapesToDisplay[m_numberOfDisplayedShapes].getSize()));
        this.revalidate();
        this.repaint();
        m_displayTimer.start();
    }
    
    @Override
    public void handleStateChange() {
        displayNextShape();
    }
}
