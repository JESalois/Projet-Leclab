package experiment;
import event.LeclabEvent;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Observable;
import java.util.Random;
import parameters.*;
import sitting.SittingManager;
import utility.FolderReader;
import utility.Global;
/**
 * This class represent a single Block from an experimentation.
 * It contains all the trials to be presented to a subject.
 * It is a single child to the Experiment Class.
 * While there are some preset for the parameters of the experimentation,
 * the designer can modify these presets for individual blocks.
 * The SittingManager will interact with this class to assign
 * the order in which to play the blocks. You can alter some of
 * the value of some parameters giving by the Experiment class
 * 
 * @see Experimentation
 * @see Trial
 * @see Parameter
 * @see SittingManager
 * 
 * @author Jean-Etienne Salois, Philippe De Seve
 */
public class Block extends Observable implements Serializable {
      
    /**
     * Complete list of all the parameters affecting the block.
     * This list should be inherited from the presets of the experimentation.
     * The designer can then decide whether or not for a particular block 
     * to enable the parameters.
     * 
     * @see Experimentation
     * @see Parameter
     */
    private HashMap<Class, Parameter> m_blockParameters;
    /**
     * Complete list of all the trials the experiment will present.
     * @see Trial
     */
    private LinkedList<Trial> m_trials;
    
    /**
     * Name representing the block for the user.
     * It is automatically assigned and can be changed by the user while designing
     * the experimentation. 
     */
    private String m_name = "";
    
    /**
     * Specifies if the block is a practice block.
     */
    private Boolean m_isPractice = false;
    
    /**
     * Specifies if the block has retroaction.
     */
    private Boolean m_hasRetroaction = false;
    
    /**
     * Default constructor. 
     * The default block has no trials, has no presets, is not a practice block, 
     * has no retroaction, and it is named "Block par default".
     * It should be avoided.
     */
    public Block() {
            m_trials = new LinkedList<Trial>();
            m_blockParameters = new HashMap<Class, Parameter>();
    }
    
    /**
     * Copy constructor, generates an identical block with ("copie") added
     * to its name. The composing elements of the block are cloned, they are not
     * the same instances.
     * 
     * @param blockToCopy 
     *              The block to be used to instantiate the new one.
     */
    public Block(Block blockToCopy) {
            m_trials = (LinkedList<Trial>) blockToCopy.m_trials.clone();
            HashMap<Class, Parameter> params = new HashMap<Class, Parameter>();
            for(Parameter param: blockToCopy.m_blockParameters.values()){
                params.put(param.getClass(), param.clone());
            }
            m_blockParameters = params;
            m_isPractice = blockToCopy.m_isPractice;
            m_hasRetroaction = blockToCopy.m_hasRetroaction;
            m_name = blockToCopy.getName()+" (copie)";
    }
    
    /**
     * This constructor takes the presets parameters from the experimentation 
     * and instantiate its own list of parameters to affect the block.
     * @param presets
     *          the presets parameters from the experimentation
     */
    public Block(HashMap<Class, Parameter> presets){
        m_blockParameters = new HashMap<Class, Parameter>();        
        m_trials = new LinkedList<Trial>();
        for(int i=0; i<Experimentation.getInstance().getDefaultNumberOfTrials(); i++){
            this.createRandomTrial();
        }
        m_isPractice = false;
        m_hasRetroaction = false;
    }
    
    /**
     * Setter for m_name
     * @param name 
     *          the block's new name
     * @see #m_name
     */
   public void setName(String name){
        m_name = name;
        setChanged();
        notifyObservers(LeclabEvent.BLOCK_NAME_CHANGED);
    }
    
    /**
     * Getter for m_name
     * @return the block's name
     * @see #m_name
     */
    public String getName(){
        return m_name;
    }
    
    /**
     * Getter for m_trials
     * @return the trials composing the block.
     * @see #m_trials
     */
    public LinkedList<Trial> getTrials()    {
        return m_trials;
    }
    
     /**
     * Setter for m_isPractice
     * @param state 
     *          if the block is a practice block.
     * @see #m_isPractice
     */
    public void setIsPractice(Boolean state){
        m_isPractice = state;
        if(m_hasRetroaction && !m_isPractice){
            setHasRetroaction(false);
        }
        setChanged();
        notifyObservers(LeclabEvent.BLOCK_PRACTICE_STATE_CHANGED);
    }
    
    /**
     * getter for m_isPractice
     * @return if the block is a practice block.
     * @see #m_isPractice
     */
    public boolean isPractice() {
        return m_isPractice;
    }
    
     /**
     * Setter for m_hasRetroaction. The retroaction will only be set to true
     * if the block is a practice block (having its field m_isPractice being true).
     * @param state
     *          if the block has retroaction.
     * @see #m_hasRetroaction
     */
    public void setHasRetroaction(Boolean state){
        if(state && m_isPractice){           
            m_hasRetroaction = true;
        }
        else{
            m_hasRetroaction = false;
        }
        setChanged();
        notifyObservers(LeclabEvent.BLOCK_RETROACTION_STATE_CHANGED);            
    }
    
    /**
     * getter for m_hasRetroaction
     * @return if the block has retroaction.
     * @see #m_hasRetroaction
     */
    public boolean hasRetroaction() {
        return m_hasRetroaction;
    }
    
    /**
     * getter for m_blockParameters.
     * @return the parameters of the block
     */
    public HashMap<Class, Parameter> getParameters(){
        return m_blockParameters;
    }
    
    /**
     * Create a new trial by randomly choosing a video file from the ones
     * available in the resource folder and adds it to the list of trial
     * @return if the creation and the insertions was successfully done
     */
    public boolean createRandomTrial(){
        //TODO use exception instead of boolean return
        ArrayList<String> possibleFilenames = FolderReader.getFilenamesWithWordCount(Global.VIDEO_FOLDER_PATH,  
                Global.SUPPORTED_VIDEO_FORMAT,
                Experimentation.getInstance().getWordsPerTrial());
        Random rand = new Random();
        while(possibleFilenames.size() > 0){
            int idx = rand.nextInt(possibleFilenames.size());
            String aPossibleFilename = possibleFilenames.get(idx);
            if(!this.containsTrial(aPossibleFilename)){
                 Trial randomTrial = new Trial(new File(Global.VIDEO_FOLDER_PATH+ aPossibleFilename));
                 addTrial(randomTrial);
                 return true;
            }
            else{
                possibleFilenames.remove(idx);
            }
        }
        return false;
    }    

    /**
     * Removes the last trials in the trial list of the block.
     */
    public void removeLastTrial() {
        m_trials.removeLast();
        setChanged();
        notifyObservers(LeclabEvent.TRIAL_REMOVED);
    }
    
    /**
     * Checks whether or not the block contains the trials identified by its
     * video filename.
     * @param videoFileName 
     *          the filename identifying the video of the trial.
     * @return true if it contains false otherwise.
     */
    public boolean containsTrial(String videoFileName){
        for(Trial trial:m_trials){
            if(trial.getVideoFile().getName().equals(videoFileName)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Add a parameter to the parameter list of the block.
     * @param param 
     *          the parameter to add.
     */
    public void addParameter(Parameter param){
        m_blockParameters.put(param.getClass(), param);
    }
    
     /**
     * Remove a parameter to the parameter list of the block.
     * @param paramClass
     *          the class of the parameter to be removed.
     */
    public void removeParameter(Class paramClass){
        m_blockParameters.remove(paramClass);
    }
    
    /**
     * Add a trial to the block<s list of trials.
     * @param trial
     *          the trial to add.
     */
    private void addTrial(Trial trial){
        m_trials.add(trial);
        setChanged();
        notifyObservers(LeclabEvent.TRIAL_ADDED);
    }
    
    /**
     * Tells if the block has a tactile distractor.
     * @return if there is a tactile distractor.
     */
    public boolean hasTactileDistraction(){
        for(Parameter parameter: m_blockParameters.values()){
            if(parameter instanceof TactileDistractorParameter){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Tells if the block has a visual distractor.
     * @return if there is a visual distractor
     */
    public boolean hasVisualDistraction(){
        for(Parameter parameter: m_blockParameters.values()){
            if(parameter instanceof VisualDistractorParameter){
                return true;
            }
        }
        return false;
    }
    
    /**
     * HashCode redefinition
     * @return hashCode
     */
    @Override
    public int hashCode(){
        return Objects.hash(m_blockParameters, m_hasRetroaction, m_isPractice, m_name, m_trials);
    }

    /**
     * Equals redefinition. Tells if the object passed in parameters is equal to this one
     * @param obj
     *      the object to be compared to.
     * @Return if the object is equal 
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final Block block = (Block)obj;
        
        return Objects.equals(m_blockParameters, block.m_blockParameters ) 
                && Objects.equals(m_hasRetroaction, block.m_hasRetroaction)
                && Objects.equals(m_isPractice, block.m_isPractice)
                && Objects.equals(m_name, block.m_name)
                && Objects.equals(m_trials, block.m_trials);        
    }
}
