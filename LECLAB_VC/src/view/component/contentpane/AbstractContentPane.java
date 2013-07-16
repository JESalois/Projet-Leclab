package view.component.contentpane;

import javax.swing.JPanel;
import sitting.SittingManager;

/**
 *
 * @author Philippe
 */
public abstract class AbstractContentPane extends JPanel {
    protected SittingManager m_sittingManager;
    
    public AbstractContentPane(SittingManager sittingManager){
        super();
        m_sittingManager = sittingManager;
    }
    
    public SittingManager getSittingManager(){
        return m_sittingManager;
    }
    
    public abstract void handleStateChange();
}
