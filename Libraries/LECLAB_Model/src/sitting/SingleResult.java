package sitting;
import java.util.Random;

/**
 * This class represent a single result made by a participant. It contains the 
 * time it took to answer a single question , the answer he gave and the 
 * correct answer.
 * @author Philippe de Sève , Jean-Etienne Salois
 */
public class SingleResult {
    
    /**
     * Random number generator
     */
    protected Random m_random;
    
    /**
     * Time the participant took to answer
     */
    private double m_answerTime;
    
    /**
     * the index representing the right answer
     */
    protected int m_rightAnswer;
    
    /**
     * the index representing the user answer
     */
    private int m_userAnswer = -1;
    
    /**
     * default constructor.
     */
    public SingleResult(){
        m_random = new Random();
        m_rightAnswer = Math.abs(m_random.nextInt(7));
    }
    
    
    /**
     * Setter for m_answerTime
     * @param time 
     *          the time taken by the user to provide an answer
     */
    public void setAnswerTime(double time){
        m_answerTime = time;
    }    
    
    /** 
     * Getter for m_answerTime
     * @return the time taken by the user to provide an answer
     */
    public double getAnswerTime(){
         return m_answerTime;
    }
    
    /**
     * Tells if the answer the user gave was the right one
     * @return if the answer is correct
     */
    public boolean isRightAnswer(){
        return m_rightAnswer==m_userAnswer;
    }
    
    /**
     * Getter for m_rightAnswer
     * @return the right answer
     */
    public int getRightAnswer()
    {
        return m_rightAnswer;
    }
    
    /** 
     * Getter for m_userAnswer
     * @return the index of the user answer
     */
    public int getUserAnswer(){
        return m_userAnswer;
    }
    
    /**
     * Setter for userAnswer
     * @param answer 
     */
    public void setUserAnswer(int answer){
        m_userAnswer = answer;
    }
    
    /**
     * Setter for m_rightAnswer
     * @param answer
     *          index of the right answer
     */
    public void setRightAnswer(int answer)
    {
        m_rightAnswer = answer;
    }
}