package sitting;

import java.util.ArrayList;

/**
 * Results of a participant for a block of the experimentation. Composed of
 * multiples trial results.
 * @author Jean-Etienne Salois
 */
public class BlockResult{
    
    /*
     * Name of the participant.
     */
    private String m_name;
    
    /**
     * List of trial results
     */
    private ArrayList<TrialResult> m_trialResults;
    
    /** 
     * number of words by trial.
     */
    private int m_wordsPerTrial;

    /**
     * Construct a BlockResult based on number of trials and the number of words by trials.
     * Also creates the trial results needed.
     * @param numberOfTrials 
     *          number of trials in the block
     * @param wordsPerTrial
     *          number of words per trial
     */
    public BlockResult(int numberOfTrials, int wordsPerTrial) {
        m_wordsPerTrial = wordsPerTrial;

        m_trialResults = new  ArrayList<TrialResult>(numberOfTrials);
        
        for (int i = 0; i < numberOfTrials; i++)
        {
            m_trialResults.add(new TrialResult(wordsPerTrial));
        }        
    }
    
    /**
     * Returns the number or trials in the block result.
     * @return the number of trial
     */
    public int getNumberOfTrials(){
        return m_trialResults.size();
    }
    
    /**
     * Getter for m_wordsPerTrial
     * @return the number of words per trial
     * @see #m_wordsPerTrial
     */
    public int getWordsPerTrial(){
        return m_wordsPerTrial;
    }

    /**
     * Getter for m_trialResults
     * @return all trialResults of the block
     */
    public ArrayList<TrialResult> getTrialResults(){
        return m_trialResults;
    }
    
    /**
     * Get a specific trial result by index
     * @param index
     *          index of the trial to get
     */
    public TrialResult getTrialResult(int index){
        //TODO use exceptions instead of sending null if out of index
        if(index < m_trialResults.size()){
            return m_trialResults.get(index);
        }
        else {return null;}       
    }
    
    /**
     * Setter for m_name
     * @param name
     *          name of the participant
     * @see #m_name
     */
    public void setName(String name){
        m_name = name;
    }
    /**
     * Getter for m_name 
     * @return the name of the participant
     * @see #m_name
     */
    public String getName(){
        return m_name;
    }
}
