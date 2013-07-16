package contoller;

import java.util.Observable;
import sitting.SittingManager;

/**
 * @author Philippe
 */
public class SubjectViewController extends Observable{
    
    SittingManager m_sittingManager;    
    
    
    public SubjectViewController(SittingManager manager){
        m_sittingManager = manager;
    }

}
