package sitting;
import event.LeclabEvent;
import experiment.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Random;
import parameters.*;
import utility.Pair;

/**
 * In charge of the experimentation turn of events.
 * @author Philippe de Sève, Jean-Etienne Salois
 */

public class SittingManager extends Observable implements Serializable{
    
    /**
     * Block representing an error, used for checkup
     */
    private static final Pair<Integer, Integer[]> ERROR_BLOCK = new Pair(-1, -1);
    
    /**
     * Seed used when generating random numbers
     */
    public static final int RANDOM_SEED = generateExperimentSeed();
    
    /**
     * Name of the participant
     */
    private String m_subjectName;
    
    /**
     * Name used for saving and loading purpose
     */
    private String m_name;

    /**
     * the order in which the the blocks will be played. And, for each blocks,
     * the order in which the trials will be played
     * This is a Mx1xNi array where the first dimension is the number of 
     * Block. The second dimensions is the index of the blocks in the 
     * m_blocks attribute of the Experimentation class. The third dimensions
     * is the order of the trials for the block. In contains indexes referring
     * to the m_trials attribute of the Block class
     * @see Block
     * @see Experimentation
     */
    private ArrayList<Pair<Integer, Integer[]>> m_orderOfExperimentation;
    
    /**
     * The block which is currently being played by the sitting manager.
     * Referring the index of in the m_orderOfExperimentation attribute.
     */
    private int m_currentBlock;
    
    /**
     * the trial currently being played by the sitting manager.
     * Referring the index of in the m_orderOfExperimentation attribute.
     */
    private int m_currentTrial;
    
    /**
     * tells if the video has been played
     */
    Boolean m_trialVideoPlayed;
    
    /**
     * The current state of the sitting
     */
    private SittingState m_state;
        
    /**
     * Names of the block results
     */
    private LinkedList<String> m_blockResultsFileNames;
    
    /**
     * Block results
     */
    private BlockResult [] m_experimentationResult;
    
    /**
     * Random number generator
     */
    private Random m_randomNumberGenerator;
    
    /**
     * Practice block the subject is currently in
     */
    private Block m_currentPracticeBlock;
    
    /**
     * Generate a new sitting manager
     */
    public SittingManager() {
        
        m_blockResultsFileNames = new LinkedList<String>();
        m_orderOfExperimentation = new ArrayList<Pair<Integer, Integer[]>>(Experimentation.getInstance().getRealBlocks().size());
        m_currentBlock = 0;
        m_currentTrial = 0;
        m_currentPracticeBlock = new Block();
        m_subjectName = "";
        m_name = "";
        m_trialVideoPlayed = false;
        m_state = SittingState.WAITING_ON_SUPERVISOR;
        m_experimentationResult = new BlockResult[Experimentation.getInstance().getRealBlocks().size()];
        m_randomNumberGenerator = new Random(RANDOM_SEED);
    }
    
    /**
     * Returns the order  of the experimentation
     * @return the order of the experimentation
     */
    public ArrayList<Pair<Integer, Integer[]>> getExperimentOrder(){
        return m_orderOfExperimentation;
    }
    
    /**
     * Getter for m_currentBlock 
     * @return the block currently being played
     */
    public int getCurrentBlock(){
        return m_currentBlock;
    }
    
    /**
     * Getter for m_currentTrial
     * @return the trial currently being played
     */
    public int getCurrentTrial(){
        return m_currentTrial;
    }
    
    /**
     * Returns the id associated to a block at a given index in the 
     * m_orderOfExperimentation attribute.
     * @param index index in the m_orderOfExperimentation array
     * @return the block id
     */
    public int getBlockId(int index){
        return m_orderOfExperimentation.get(index).x;
    }
    
    /**
     * Getter for m_state
     * @return current state
     */
    public SittingState getState(){
        return m_state;
    }
   
    /**
     * Returns the block associated to the index in the m_orderOfExperimentation
     * @param index 
     *          the index in the m_orderOfExperimentation array
     * @return the Block associated to the index
     */
    public Block getTrueBlock(int index){
        if (index<Experimentation.getInstance().getRealBlocks().size()){
            return Experimentation.getInstance().getRealBlocks().get(this.getBlockId(index));
        }
        else {return null;}
    }    
 
    /**
     * Returns the trial identified by its ID at the given position in the
     * m_orderOfExperimentation array
     * @param blockIndex block index in the m_orderOfExperimentation
     * @param trialIndex trial index in the m_orderOfExperimentation
     * @return the Trial at the given position
     */
    public Trial getTrueTrial(int blockIndex, int trialIndex){
        if(trialIndex>=this.getBlockById((this.getBlockId(blockIndex))).y.length || trialIndex<0){
            return null;
        }
        int trialId = this.getBlockById((this.getBlockId(blockIndex))).y[trialIndex];
        Block block = this.getTrueBlock(blockIndex);
        return block.getTrials().get(trialId);
    }
    
    /**
     * Return a specific block result
     * @param blockIdx
     *          index of the block for which the results are needed
     * @return 
     */
    public BlockResult getBlockResult(int blockIdx){
        return m_experimentationResult[blockIdx];
    }
    
    /**
     * Setter for m_currentTrial
     * @param trialIndex 
     *          the index in the y parameter of the pair in the 
     * m_orderOfExperiementation attribute
     */
    public void setCurrentTrial(int trialIndex){
        m_currentTrial = trialIndex;
    }
    
    /**
     * Sets the number of the block to be played
     * @param blockIndex 
     *          the index in the m_orderOfExperimentation attribute
     */
    public void setCurrenBlock(int blockIndex){
      m_currentBlock = blockIndex;
    }
    
    /**
     * Sets the current state of the experimentation
     * @param state 
     *      the state in which the experimentation is in
     */
    private void setCurrentState(SittingState state){
        if(!m_state.equals(state)){
            m_state = state;
            this.setChanged();
            this.notifyObservers(LeclabEvent.SITTING_STATE_CHANGED);
        }
    }
    
    /**
     * Initiate the block by changing it's current state
     */
    public void initiateBlock(){
        this.setCurrentState(SittingState.START_OF_BLOCK);
    }
    
    /**
     * Initiate a practice block by changing it's current state
     * @param practiceBlock
     *          the practice block to initiate
     */
    public void initiatePracticeBlock(Block practiceBlock){
        m_currentPracticeBlock = practiceBlock;
        this.setCurrentState(SittingState.START_OF_PRACTICE);
    }
    
    /**
     * Initiate the next trial by changing it's state to playing distractor or video
     */
    public void initiateTrial(){
        this.setCurrentState(SittingState.START_OF_TRIAL);
        m_trialVideoPlayed = false;
        Block block = this.getTrueBlock(m_currentBlock);
        if(block.hasVisualDistraction() && block.getParameters().get(VisualDistractorParameter.class).getParameterPositionInTrial().equals(EParameterPosition.BEFORE)){
            this.setCurrentState(SittingState.PLAYING_VISUAL_DISTRACTOR);
        }
        else if(block.hasTactileDistraction()&& block.getParameters().get(TactileDistractorParameter.class).getParameterPositionInTrial().equals(EParameterPosition.BEFORE)){
            this.setCurrentState(SittingState.PLAYING_TACTILE_DISTRACTOR);
        }
        else{
            this.setCurrentState(SittingState.PLAYING_TRIAL_VIDEO);
        }
    }
    
    /**
     * Change de states if the sitting to indicate that the trial video just finished
     */
    public void trialVideoFinished(){
        m_trialVideoPlayed = true;
        Block block = this.getTrueBlock(m_currentBlock);
        if(block.hasVisualDistraction() && block.getParameters().get(VisualDistractorParameter.class).getParameterPositionInTrial().equals(EParameterPosition.AFTER)){
            this.setCurrentState(SittingState.PLAYING_VISUAL_DISTRACTOR);
        }
        else if(block.hasTactileDistraction()&& block.getParameters().get(TactileDistractorParameter.class).getParameterPositionInTrial().equals(EParameterPosition.AFTER)){
            this.setCurrentState(SittingState.PLAYING_TACTILE_DISTRACTOR);
        }
        else{
            this.setCurrentState(SittingState.WAITING_USER_INPUT);
        }
    }
    
    /**
     * Change the state accordingly after a visual distractor finished
     */
    public void VisualDistractionFinished(){
        Block block = this.getTrueBlock(m_currentBlock);
        Parameter vdp = block.getParameters().get(VisualDistractorParameter.class);
        if(block.hasTactileDistraction() && block.getParameters().get(TactileDistractorParameter.class).getParameterPositionInTrial().equals(vdp.getParameterPositionInTrial())){
            this.setCurrentState(SittingState.PLAYING_TACTILE_DISTRACTOR);
        }
        else if(m_trialVideoPlayed){
            this.setCurrentState(SittingState.WAITING_USER_INPUT);
        }
        else{
            this.setCurrentState(SittingState.PLAYING_TRIAL_VIDEO);
        }
    }
    
     /**
     * Change the state accordingly after a visual distractor finished
     */
    public void TactileDistractionFinished(){
        if(m_trialVideoPlayed){
            this.setCurrentState(SittingState.WAITING_USER_INPUT);
        }
        else{
            this.setCurrentState(SittingState.PLAYING_TRIAL_VIDEO);
        }
    }
    
    /**
    * Change the state accordingly after a trial finished
    */
    public void trialFinished(){
        m_currentTrial++;
        if(m_currentTrial == m_orderOfExperimentation.get(m_currentBlock).y.length){
            m_currentTrial = 0;
            m_currentBlock++;
            if(m_currentBlock == m_orderOfExperimentation.size()){
                this.setCurrentState(SittingState.END_OF_SITTING);
            }
            else{
                this.setCurrentState(SittingState.WAITING_ON_SUPERVISOR);
            }
        }else{
            initiateTrial();
        }
    }
    
    /**
     * Start vibrations
     */
    public void startVibrations(){
        new Vibrator(this).vibrate();
    }
    
    /**
     * Return true if the m_orderOfExperimentation contains the block 
     * identified by id
     * @param id 
     *          the block identifier
     * @return if it contains the block
     */
    private boolean containsBlock(int id){
        for(Pair<Integer, Integer[]> block: m_orderOfExperimentation){
            if(block.x.equals(id)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Return true if the m_orderOfExperimentation contains the trial 
     * identified by its block and its trial id
     * @param blockId 
     *          Identifier of the block containing the trial
     * @param trialId 
     *          trial identifier
     * @return if it contains the trial
     */
    private boolean containsTrial(int blockId, int trialId){
        for(Integer trial: this.getBlockById(blockId).y){
            if(trial != null && trial.equals(trialId)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Return the block and its trials identified by the block id
     * @param id 
     *          block identifier
     * @return a pair containing the block and the list of its trial id or
     *  the ERROR_BLOCK constant if it has not found the block
     */
    private Pair<Integer, Integer[]> getBlockById(int id){
        for(Pair<Integer, Integer[]> block: m_orderOfExperimentation){
            if(block.x.equals(id)){
                return block;
            }
        }
        Pair<Integer, Integer[]> notFound = new Pair(-1, -1); 
        return SittingManager.ERROR_BLOCK;
    }
    
    /**
     * Returns a new block ID
     * @param maxValue 
     *          the maximum value the block id can have
     * @return a block ID
     */
    private int createBlockId(int maxValue){
        int blockId = m_randomNumberGenerator.nextInt(maxValue);
        while(this.containsBlock(blockId)){
               blockId = m_randomNumberGenerator.nextInt(maxValue);
        }
        return blockId;
    }
    
    /**
     * Assigns a list of trials ID to a block
     * @param blockId 
     *          Identifier to the block to which we must assigns the trials 
     * @param maxValue 
     *          the maximum value the trial IDs can have.
     */
    private void assignTrialsId(int blockId, int maxValue){
        Integer[] trialsId = new Integer[maxValue];
        for(int i = 0; i < trialsId.length; i++){
            int trialId = m_randomNumberGenerator.nextInt(maxValue);
            while(this.containsTrial(blockId, trialId)){
                trialId = m_randomNumberGenerator.nextInt(maxValue);
             }
            this.getBlockById(blockId).y[i] = trialId;
        }
    }
    
    /**
     * Create a new order for the experimentation by assigning a new ID
     * to each block and trials for the experimentation.
     * Notify observers.
     */
    public final void randomizeOrder(){
       for(int i = 0; i < Experimentation.getInstance().getRealBlocks().size(); i++){
           int blockID = this.createBlockId(Experimentation.getInstance().getRealBlocks().size());
           int numberOfTrials = Experimentation.getInstance().getRealBlocks().get(blockID).getTrials().size();
           m_orderOfExperimentation.add(new Pair<Integer, Integer[]>(blockID, new Integer[numberOfTrials]));
           this.assignTrialsId(blockID, numberOfTrials);
       }
       this.setChanged();
       this.notifyObservers(LeclabEvent.NEW_EXPERIMENTATION_ORDER);
    }

    /**
     * Change the instance of the sitting manager to reflect the one passed in
     * parameters
     * @param loadedSittingManager
     *          the sitting manager to be loaded
     */
    public void load(SittingManager loadedSittingManager) {
        m_blockResultsFileNames = loadedSittingManager.m_blockResultsFileNames;
        m_currentBlock = loadedSittingManager.m_currentBlock;
        m_currentTrial = loadedSittingManager.m_currentTrial;
        m_name = loadedSittingManager.m_name;
        m_orderOfExperimentation = loadedSittingManager.m_orderOfExperimentation;
        m_subjectName = loadedSittingManager.m_subjectName;
        setChanged();
        notifyObservers(this);
    }
    
     /**
     * Setter for m_name
     * @param name
     *          subject name
     * @see #m_name
     */
    public void setSubjectName(String name) {
        m_subjectName = name;
        if(m_name.equals("")){
            m_name = name;
        }
    }
    
    /**
     * Getter for m_subjectName
     * @return subject name
     * @see #m_name
     */
    public String getSubjectName(){
        return m_subjectName;
    }
    
     /**
     * Setter for m_name
     * @param name
     *          sitting name
     * @see #m_name
     */
    public void setName(String name) {
        m_name = name;
    }
    
    /**
     * Getter for m_name
     * @return sitting name
     * @see #m_name
     */
    public String getName(){
        return m_name;
    }
   
    /**
     * Initialize block result
     */
    public final void initializeBlockResult(){
        for(int i = 0; i < m_experimentationResult.length; i++){
            m_experimentationResult[i] = new BlockResult(m_orderOfExperimentation.get(i).y.length, Experimentation.getInstance().getWordsPerTrial());
            m_experimentationResult[i].setName(this.getTrueBlock(m_orderOfExperimentation.get(i).x).getName());
        }
    }
    
    /**
     * Generate a seed for the experiment based on the words the trials in it 
     * contain. The seed will always be the same for the same experiment, and the
     * random generation from it will stay the same.
     * @return 
     */
    private static final int generateExperimentSeed(){
        String allTheWords = "";
        for(Block block : Experimentation.getInstance().getBlocks()){
            for(Trial trial : block.getTrials()){
                for(String word : trial.getWords()){
                    allTheWords += word;
                }
            }
        }
        return Math.abs(allTheWords.hashCode());
        //TODO add a unique ID to the parameter and use it to generateExperimentSeed too
    }
    
    /**
     * Return the current seed.
     * @return 
     */
    public int getCurrentSeed(){
        int counter = 0;
        for(int i = 0; i < m_currentBlock; i++){
            counter += m_orderOfExperimentation.get(i).y.length;
        }
        counter += m_currentTrial;
        return SittingManager.RANDOM_SEED + counter;
    }
}

