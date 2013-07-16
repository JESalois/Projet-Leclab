package view.component.button;

import java.awt.Dimension;
import javax.swing.JToggleButton;
import parameters.geometry.AbstractShape;
import view.icons.ShapeListIcon;

/**
 * Insert Description of class
 * @author Philippe
 */
public class VisualAnswerButton extends JToggleButton{
    
    private AbstractShape[] m_shapesToDisplay;
    
    public VisualAnswerButton(AbstractShape[] shapesToDisplay){
        super();
        m_shapesToDisplay = shapesToDisplay;
        this.setIcon(new ShapeListIcon(m_shapesToDisplay, 3));
    }
    
    public AbstractShape[] getDisplayedShape(){
        return m_shapesToDisplay;
    }

}
