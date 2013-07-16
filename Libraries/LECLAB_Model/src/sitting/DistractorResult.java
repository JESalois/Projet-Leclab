package sitting;

import parameters.geometry.AbstractShape;

/**
 * Specific single result for distractor. The distractor visual answer is also
 * saved.
 * @author Philippe de Sève, Jean-Étienne Salois
 */
public class DistractorResult extends SingleResult{
    /**
     * Visual answer chosen by the participant
     */
    private AbstractShape[] m_shapes;
    
    /**
     * Constructor
     * @param numAnswers
     *          index of the right answer
     */
    public DistractorResult(int numAnswers){
        super();
        m_rightAnswer = Math.abs(m_random.nextInt(numAnswers));        
    }    
    
    /**
     * Sets use answer by providing the index of the answer and the shapes
     * @param answer
     *          index of the answer selected
     * @param shapesAnswer
     *          shapes in the drawing selected
     */
    public void setUserAnswer(int answer, AbstractShape[] shapesAnswer){
        m_shapes = shapesAnswer;
        super.setUserAnswer(answer);        
    }
    
    /**
     * Getter for m_shapes
     * @return shapes selected by the user
     */
    public AbstractShape[] getShapes(){
        return m_shapes;
    }
}
