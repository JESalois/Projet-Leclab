package experiment;
import event.LeclabEvent;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import parameters.Parameter;
import sitting.SittingManager;

/**
 * This class represent an experimentation.
 * This class is a singleton. 
 * Therefore the instance should be obtained used the getInstance static method.
 * It contains all the blocks to be presented to a subject.
 * It contains all the global parameters for the experiment.
 * The SittingManager class will interact with this class
 * in order to present and manipulate the information presented
 * to subject
 * 
 * @see Block
 * @see Parameter
 * @see SittingManager
 * 
 * @author Philippe de Sève, Jean-Etienne Salois
 */
public class Experimentation extends Observable implements Serializable{
   
    /**
     * The reference kept to the instance of the singleton
     */
    private static Experimentation m_instance = null;
    /**
     * Complete list of all the block the experiment will present.
     * @see Block
     */
    private LinkedList<Block> m_blocks;
    /**
     * Complete map of all the parameters chosen by the designer affecting
     * the experiment. These are presets only and the designer can decide to
     * turn on or off certain parameters for individual blocks.
     * This list contains only one instance of each parameters.
     * @see Parameter
     */
    private HashMap<Class, Parameter> m_presetParameters;
    
    /**
     * The number of words in each trials.
     * @see Trial
     */
    private int m_wordsPerTrial;
    
    /**
     * The number of trial to initially add at the creation of a block.
     */
    private int m_defaultNumberOfTrial;
    
    /**
     * Name of the experimentation, mostly used for saving and loading purpose.
     */    
    private String m_name;
    
    /**
     * This method returns the single instance of an experimentation in the 
     * whole execution of the program. This method is part of the singleton 
     * design pattern. It calls the private constructor of experimentation
     * @return m_instance
     *          the reference to the singleton.
     * @see Experimentation#m_instance
     * @see Experimentation#Experimentation()
     */
    public static Experimentation getInstance(){
        if (Experimentation.m_instance == null){
            Experimentation.m_instance = new Experimentation();
        }
        return Experimentation.m_instance;
    }
    
    /**
     * Return the list of preset parameters for the experiment.
     * @return m_presetParameters
     *              the list of preset parameters
     */
    public HashMap<Class, Parameter> getPresetParameters(){
        return m_presetParameters;
    }
    
    /**
     * Adds aParameter to the list of presets parameters
     * @param aParameter 
     *          the parameter to be added
     */
    public void addParameter(Parameter aParameter){
        m_presetParameters.put(aParameter.getClass(), aParameter);
        setChanged();
        notifyObservers(LeclabEvent.PARAMETER_ADDED);
    }
    
    /**
     * Removes the parameter with the given class from the preset map of parameter
     * @param aParameterClass 
     *          the class defining the parameter 
     */
    public void removeParameter(Class aParameterClass){
        m_presetParameters.remove(aParameterClass);
        setChanged();
        notifyObservers(LeclabEvent.PARAMETER_REMOVED);
    }
    
    /**
     * The constructor is private in order to implement the singleton pattern.
     * This constructor should never be called expect in the getInstance() 
     * method.
     */
    private Experimentation() {     
        m_presetParameters = new HashMap<Class, Parameter>();
        m_blocks = new LinkedList<Block>();
        m_wordsPerTrial = 0;
        m_defaultNumberOfTrial = 0;
        m_name = "Nouvelle experimentation";
    }
    
    /**
     * Getter for m_blocks.
     * @return the list of blocks.
     * @see #m_blocks
     */
    public LinkedList<Block> getBlocks(){
        return m_blocks;
    }
    
    /**
     * Setter for m_wordsPerTrial
     * @param wordsPerTrial
     *          number of words in each trials of the experimentation.
     * @see #m_wordsPerTrial
     */
    public void setWordsPerTrial(int wordsPerTrial){
        m_wordsPerTrial = wordsPerTrial;
    }
    
    /**
     * Getter for m_wordsPerTrial.
     * @return number of words in each trials of the experimentation.
     * @see #m_wordsPerTrial
     */
    public int getWordsPerTrial(){
        return m_wordsPerTrial;
    }
    
    /**
     * Setter for m_defaultNumberOfTrial
     * @param defaultNumberOfTrial
     *          the default number of trial at the creation of a new block
     * @see #m_defaultNumberOfTrial
     */
    public void setDefautNumberOfTrials(int defaultNumberOfTrial){
        m_defaultNumberOfTrial = defaultNumberOfTrial;
    }
    
    /**
     * Returns the default number of trial at the creation of a new block
     * @return the default number of trial
     */
    public int getDefaultNumberOfTrials(){
        return m_defaultNumberOfTrial;
    }
    
    /**
     * Add a block to the experimentation using the experimentation parameters
     * and the next default name is generated.
     */
    public void addBlock(){
        Block block = new Block(m_presetParameters);
        block.setName(getNextBlockName());
        m_blocks.add(block);
        setChanged();
        notifyObservers(LeclabEvent.BLOCK_ADDED);        
    }
    
    /**
     * Returns the block called by it's name
     * @param name
     *          name of the block wanted
     * @return the found block or null if not found
     */
    public Block getBlock(String name){
        //TODO use exception instead of returning null if the block isn't found
        for (Block block: m_blocks){
            if (block.getName() == name )
                return block;            
        }
        return null;
    }

    /**
     * Remove a block from the experimentation.
     * @param block
     *          the block to remove
     */
    public void removeBlock(Block block) {
        m_blocks.remove(block);
        setChanged();
        notifyObservers(LeclabEvent.BLOCK_REMOVED);
    }

    /**
     * Clear the experimentation by removing all blocks and parameters.
     */
    public void clear() {
         m_blocks.clear();
         m_presetParameters.clear();         
         setChanged();
         notifyObservers(LeclabEvent.EXPERIMENTATION_CLEARED);
    }

    /**
     * Add specific block to experimentation.
     * @param block 
     *          the block to add
     */
    public void addBlock(Block block) {
        m_blocks.add(block);
        setChanged();
        notifyObservers(LeclabEvent.BLOCK_ADDED);
    }

    /**
     * Generate a new name for a block.
     * @return the name generated.
     */
    private String getNextBlockName() {
        //Find "biggest name"
        String blockName = "";
        Boolean blockWithSameName = true;
        for(int i = 1;blockWithSameName;i++){
            blockWithSameName = false;
            blockName = "Bloc "+ (m_blocks.size()+i);
            for(Block block: m_blocks){
                if(blockName.equals(block.getName())){
                    blockWithSameName = true;
                    break;
                }
            }
        }
        return blockName; 
    }
    
    /**
     * Change the current experimentation to a loaded one.
     * @param loadedExperiementation
     *          the instance of experimentation to be loaded.
     */
    public void load(Experimentation loadedExperiementation) {
        m_blocks = loadedExperiementation.m_blocks;
        m_defaultNumberOfTrial = loadedExperiementation.m_defaultNumberOfTrial;
        m_presetParameters = loadedExperiementation.m_presetParameters;
        m_wordsPerTrial = loadedExperiementation.m_wordsPerTrial;
        setChanged();
        notifyObservers(LeclabEvent.EXPERIMENTATION_LOADED);
    }
    
    /**
     * Setter for m_name
     * @param name
     *          the experimentation name.
     */
    public void setName(String name){
        m_name = name;
    }
    
     /**
     * Getter for m_name.
     * @return he experimentation name
     */
    public String getName(){
        return m_name;
    }
    
    /**
     * Get all block that are not practice blocks.
     * @return real blocks, non-practice blocks.
     */
    public LinkedList<Block> getRealBlocks(){
       LinkedList<Block> realBlocks = new LinkedList<Block>();
       for(Block block: Experimentation.getInstance().getBlocks()){
           if(!block.isPractice()){
               realBlocks.add(block);
           }
       }
       return realBlocks;
    }
    
    /**
     * Get all blocks set as practice blocks.
     * @return practice blocks.
     */
    public LinkedList<Block> getPracticeBlocks(){
       LinkedList<Block> practiceBlocks = new LinkedList<Block>();
       for(Block block: Experimentation.getInstance().getBlocks()){
           if(block.isPractice())
               practiceBlocks.add(block);
       }
       return practiceBlocks;
    }    
}
