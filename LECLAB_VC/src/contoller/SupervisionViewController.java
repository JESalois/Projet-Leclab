package contoller;

import experiment.Block;
import experiment.Experimentation;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;
import sitting.SittingManager;
import utility.Pair;

/**
 * Insert Description of class
 * @author Philippe
 */
public class SupervisionViewController extends Observable{
    private SittingManager m_sittingManager;
    
    public SupervisionViewController(Observer experimentObserver){
        m_sittingManager = new SittingManager();
        m_sittingManager.addObserver(experimentObserver);        
    }
    
    public void initializeExperiment(){
        m_sittingManager.randomizeOrder();
        m_sittingManager.initializeBlockResult();
        m_sittingManager.setCurrenBlock(0);
        m_sittingManager.setCurrentTrial(0);
    }
    
    public void startBlock(){
        m_sittingManager.initiateBlock();
    }
    
    public SittingManager getSittingManager(){
        return m_sittingManager;
    }

    public void setSittingSubjectName(String name) {
        m_sittingManager.setSubjectName(name);
    }

    /**
     * Save a session composed of the experimentation and the sitting manager
     * @param file
     *          file to save the session in
     */
    public void saveSession(File file) throws FileNotFoundException, IOException {
        // Write to disk with FileOutputStream
        FileOutputStream f_out = new FileOutputStream(file.getAbsolutePath());

        // Write object with ObjectOutputStream
        ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
        
        // Write object out to disk
        obj_out.writeObject ( new Pair(Experimentation.getInstance(), m_sittingManager) );

        //Change experiementation name to reflect
        m_sittingManager.setName(file.getName().replace(".expe", ""));
    }
    
    /**
    * Load a session. The session is composed of an experimentation and a sitting manager
    * @param fileName
    *           name of the file to load
    */
    public void loadExperimentation(File file) throws FileNotFoundException, IOException, ClassNotFoundException{
        // Read from disk using FileInputStream
        FileInputStream f_in = new FileInputStream(file.getAbsolutePath());

        // Read object using ObjectInputStream
        ObjectInputStream obj_in = new ObjectInputStream (f_in);

        // Read an object
        Object obj = obj_in.readObject();

        if (obj instanceof Pair )
        {
                // Cast object to a pair representing a session
                Pair<Experimentation, SittingManager> loadedSession = (Pair<Experimentation, SittingManager>) obj;
                
                //affect the loaded parameters
                Experimentation.getInstance().load(loadedSession.x);
                m_sittingManager.load(loadedSession.y);
        }
        
    }

    public void startPractice(Block practiceBlock) {
        m_sittingManager.initiatePracticeBlock(practiceBlock);
    }

}
