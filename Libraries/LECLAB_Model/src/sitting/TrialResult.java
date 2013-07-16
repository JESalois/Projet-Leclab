package sitting;
import experiment.Experimentation;
import parameters.TactileDistractorParameter;
/**
 * Results of a participant for a trial of the experimentation.
 * @author Jean-Etienne Salois
 */
public class TrialResult {
    
    /**
     * tactile distractor answer from participant
     */
    private DistractorResult m_tactileAnswer;
    /**
     * visual distractor answer from participant
     */
    private DistractorResult m_visualAnswer;
    /**
     * number of words in the trial
     */
    private int m_numOfWords;
    /**
     * trials id number
     */
    private int m_trialNumber;

    /**
     * words answered by participant
     */
    private WordResult[] m_wordAnswers;
     /**
     * Constructor
     * @param numOfWords
     *          number of words in the trial
     */
    public TrialResult(int numOfWords) {
        m_numOfWords = numOfWords;
        m_wordAnswers = new WordResult[numOfWords];   
        m_visualAnswer = new DistractorResult(7);
        for (int i = 0; i < numOfWords; i++){
            m_wordAnswers[i] = new WordResult();
        }        
        
        TactileDistractorParameter buzz = 
                (TactileDistractorParameter)Experimentation.getInstance().
                getPresetParameters().get(TactileDistractorParameter.class);
        if (buzz != null){   
            int num = (int)Math.pow(buzz.getNumberOfVibration(), 2) < 7 ? 
                    (int)Math.pow(buzz.getNumberOfVibration(), 2): 7;
            m_tactileAnswer = new DistractorResult(num);
        }        
    }
    
    /**
     * Setter for m_trialNumber
     * @param number
     *          id number of the trial
     * @see #m_trialNumber
     */
    public void setTrialNumber(int number)
    {
        m_trialNumber = number;
    }
    
    /**
     * Getter for m_trialNumber
     * @return id number of the trial
     * @see #m_trialNumber
     */
    public int getTrialNumber(){
        return m_trialNumber;
    }
    
    /**
     * Getter for m_numOfWords
     * @return the number of word in the trial
     * @see #m_numOfWords
     */
    public int getNumOfWords(){
        return m_numOfWords;
    }
    
    /**
     * Getter for m_tactileAnswer
     * @return the tactile distractor answer
     * @see #m_tactileAnswer
     */
    public DistractorResult getTactileResult(){
        return m_tactileAnswer;
    }
    
     /**
     * Getter for m_visualAnswer
     * @return the visual distractor answer
     * @see #m_visualAnswer
     */
    public DistractorResult getVisualResult(){
        return m_visualAnswer;
    }
    
    /**
    * Get the word answer at the specified index
    * @param num
    *           the index of the word
    * @return the word answer
    */
    public WordResult getWordAnswer(int num){
        
        if (num < m_numOfWords && num >= 0)
            return m_wordAnswers[num];
        
        return null;        
    }
}
