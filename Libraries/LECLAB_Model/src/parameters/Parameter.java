package parameters;

import experiment.Block;
import experiment.Experimentation;
import java.io.Serializable;
import java.util.Objects;

/**
 * Abstract class implementing the core functionalities  of all the parameters
 * affecting the experiment.
 * A parameter will alway be enable by default and therefore be played.
 * However the designer can decide to disable a parameter for a particular
 * block.
 * 
 * @see Experimentation
 * @see Block
 * 
 * @author Jean-Etienne Salois, Philippe de Sève
 */
abstract public class Parameter implements Serializable{
    /**
     * Position in relation to the sentence of the trial.
     */
    private EParameterPosition m_positionInTrial;
    
    /**
     * Default constructor. Creates a parameter.
     */
    public Parameter(){
        m_positionInTrial = EParameterPosition.NONE;
    }
    
    /**
     * Initialize a parameter with a predefined position in the trial
     * @param positionInTrial 
     *          the position in relation to the sentence in the trial
     */
    public Parameter(EParameterPosition positionInTrial){
        m_positionInTrial = positionInTrial;
    }
    
    /**
     * Getter for m_positionInTrial
     * @return parameter position in the trial
     * @see #m_positionInTrial
     */
    public EParameterPosition getParameterPositionInTrial(){
        return m_positionInTrial;
    }
    
    /**
     * Setter for m_positionInTrial
     * @param position the new parameter position
     * @see #m_positionInTrial
     */
    public void setParameterPositionInTrial(EParameterPosition position){
        m_positionInTrial = position;
    }
    
    /**
     * Implements the behavior of the parameter when it is to be played
     * during a block.
     * The function uses a seed to generate a random sequence of stimulus to
     * be played.
     * 
     * @param seed
     *          the seed used to generate the stimulus sequence
     * @see Block
     */
    abstract public Object [] orderOfPlay(int seed);
    
    /**
     * The clone function must be implemented for all subclasses
     * @return 
     */
    @Override
    abstract public Parameter clone();
    
     /**
     * HashCode redefinition
     * @return hashCode
     */
    @Override
    public int hashCode(){
        return Objects.hash(m_positionInTrial);
    }

    /**
     * Tells if the object passed in parameters is equal to this one
     * @param obj
     *          the object to copy
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
        
        final Parameter parameter = (Parameter)obj;
        
        return Objects.equals(m_positionInTrial, parameter.m_positionInTrial);
    }    
}
