package sitting;

/**
 * Specific single word result.
 * saved.
 * @author Jean-Étienne
 */
public class WordResult extends SingleResult{
    /**
     * The word answered
     */
    private String m_wordAnswer;     
    
    /**
     * Default constructor
     */
    public WordResult(){super();}
    
     /**
     * Set the word answer
     * @param answer
     *          index of the answer
     * @param wordAnwser
     *          word answered
     */
    public void setUserAnswer(int answer, String wordAnwser){
        m_wordAnswer = wordAnwser;
        super.setUserAnswer(answer);        
    }
    
    /**
     * Getter for m_wordAnswer
     * @return the word answer
     * @see #m_wordAnswer
     */
    public String getWord(){
        return m_wordAnswer;
    }
}
