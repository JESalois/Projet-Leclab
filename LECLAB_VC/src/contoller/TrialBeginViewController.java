/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package contoller;

import sitting.SittingManager;
import sitting.SittingState;

/**
 * Insert Description of class
 * @author Philippe
 */
public class TrialBeginViewController {
    
    private SittingManager m_sittingManager;
    
    public TrialBeginViewController(SittingManager sittingManager){
        m_sittingManager = sittingManager;
    }
    
    public void startFirstTrial(){
        m_sittingManager.initiateTrial();
    }
}
