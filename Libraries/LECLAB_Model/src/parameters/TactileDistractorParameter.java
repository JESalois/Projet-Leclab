package parameters;
import java.util.Objects;
import java.util.Random;

/**
 * This is a children class of Parameter.
 * It encapsulate the behavior of the tactile distractor during the experiment
 * such as the the duration time of a vibration.
 * The distractor implements a long and a short vibrations.
 * 
 * @see Parameter
 * 
 * @author Philippe de Sève, Jean-Etienne Salois
 */
public class TactileDistractorParameter extends Parameter{
    /**
     * Duration in milliseconds of the long vibration.
     */
    private int m_longVibrationDuration;
    
    /** 
     * Duration in milliseconds of the short vibration.
     */
    private int m_shortVibrationDuration;
    
    /** 
     * Duration in milliseconds of the down time between 2 vibrations.
     */
    private int m_intervalDuration;
    
    /** 
     * Number of vibrations to happen during a trial.
     */
    private int m_numberOfVibrations;
    
    /**
     * Default constructor. Number of vibration and vibration lengths are set
     * to 0.
     */
    public TactileDistractorParameter(){
        super(EParameterPosition.BEFORE);
        m_longVibrationDuration = 0;
        m_shortVibrationDuration = 0;
        m_intervalDuration = 0;
        m_numberOfVibrations = 0;
    }
    
    /**
     * Creates a tactile distractor parameters by providing each attributes a value.
     * @param longVibrationDuration
     *          Duration in milliseconds of the long vibration.
     * @param shortVibrationDuration
     *          Duration in milliseconds of the short vibration.
     * @param intervalDuration
     *          Duration in milliseconds of the down time between 2 vibrations.
     * @param numberOfVibration
     *          Number of vibrations to happen during a trial.
     * @param positionInTrial
     *          List of all the parameters affecting the experiment.
     * @see #m_longVibrationDuration
     * @see #m_shortVibrationDuration
     * @see #m_intervalDuration
     * @see #m_numberOfVibrations
     * @see #m_positionInTrial
     */
    public TactileDistractorParameter(int longVibrationDuration, int shortVibrationDuration,
            int intervalDuration, int numberOfVibration, EParameterPosition positionInTrial){
        super(positionInTrial);
        m_longVibrationDuration = longVibrationDuration;
        m_shortVibrationDuration = shortVibrationDuration;
        m_numberOfVibrations = numberOfVibration;
        m_intervalDuration = intervalDuration;
    }
    
    /**
     * Copy constructor.
     * @param distractor 
     *          the tactile distractor parameter to be copied
     */
    public TactileDistractorParameter(TactileDistractorParameter distractor){
        super(distractor.getParameterPositionInTrial());
        m_longVibrationDuration = distractor.m_longVibrationDuration;
        m_shortVibrationDuration = distractor.m_shortVibrationDuration;
        m_numberOfVibrations = distractor.m_numberOfVibrations;
        m_intervalDuration = distractor.m_intervalDuration;
    }
    
    /**
     * Redefinition of clone function.
     * @return the cloned TactileDistractorParameter object
     */
    @Override
    public TactileDistractorParameter clone(){
        return new TactileDistractorParameter(this);
    }
    
    /**
     * Setter for m_shortVibrationDuration. It must be greater than 0 to be assigned.
     * @param shortVibrationDuration 
     *          the new short vibration duration
     * @return if the assignation was a success
     * @see #m_shortVibrationDuration
     */
    public boolean setShortVibrationDuration(int shortVibrationDuration){
        //TODO use exception instead of boolean return
        if(shortVibrationDuration >= 0){
            m_shortVibrationDuration = shortVibrationDuration;
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * Setter for m_longVibrationDuration. It must be greater than 0 to be assigned.
     * @param longVibrationDuration 
     *          the new long vibration duration 
     * @return if the assignation was a success
     * @see #m_shortVibrationDuration
     */
    public boolean setLongVibrationDuration(int longVibrationDuration){
        //TODO use exception instead of boolean return
        if(longVibrationDuration >= 0){
            m_longVibrationDuration = longVibrationDuration;
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * Setter for m_intervalDuration. It must be greater than 0 to be assigned.
     * @param intervalDuration 
     *          the new interval duration 
     * @return if the assignation was a success
     * @see #m_intervalDuration
     */
    public boolean setIntervalDuration(int intervalDuration){
        //TODO use exception instead of boolean return
        if(intervalDuration >= 0){
            m_intervalDuration = intervalDuration;
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * Setter of m_numberOfVibrations. It must be greater than 0 to be assigned.
     * @param numberOfDuration 
     *          the new number of vibration
     * @return if the assignation was a success
     * @see #m_numberOfVibrations
     */
    public boolean setNumberOfVibrations(int numberOfVibrations){
        //TODO use exception instead of boolean return
        if(numberOfVibrations >= 0){
            m_numberOfVibrations = numberOfVibrations;
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * Getter for m_shortVibrationDuration.
     * @return short vibration duration
     * @see #m_shortVibrationDuration
     */
    public int getShortVibrationDuration(){
        return m_shortVibrationDuration;
    }
    
    /**
     * Getter for m_longVibrationDuration.
     * @return long vibration duration
     * @see #m_longVibrationDuration
     */
    public int getLongVibrationDuration(){
        return m_longVibrationDuration;
    }
    
    /**
     * Getter form_numberOfVibrations
     * @return number of vibrations
     * @see #m_numberOfVibrations
     */
    public int getNumberOfVibration(){
        return m_numberOfVibrations;
    }
    
    /**
     * Getter for m_intervalDuration
     * @return interval duration
     * @see #m_intervalDuration
     */
    public int getIntervalDuration(){
        return m_intervalDuration;
    }
    
     /**
     * orderOfPlay implementation. Implements the behavior of the parameter 
     * when it is to be played during a block. The function uses a seed to 
     * generate a random sequence of stimulus to be played.
     * @param seed
     *          seed used to generate stimulus sequence 
     * @return the order, a string table composed of "L" for long vibrations and
     * "C" for short ones.
     */
    @Override
    public Object[] orderOfPlay(int seed) {
        Random rand = new Random(seed);
        String[] order = new String[m_numberOfVibrations];
        for(int i = 0; i < order.length; i++){
            if(rand.nextInt(2) > 0){
                order[i] = "L";
            }
            else{
                order[i] = "C";
            }
        }
        return order;
    }
    
     /**
     * HashCode redefinition
     * @return hashCode
     */
    @Override
    public int hashCode(){
        return Objects.hash(super.hashCode(), m_intervalDuration, 
                m_longVibrationDuration, m_numberOfVibrations, m_shortVibrationDuration);
    }

    /**
     * Tells if the object passed in parameters is equal to this one
     * @param obj
     *  the object to copy
     * @Return if the object is equals 
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
        
        final TactileDistractorParameter tactileParam = (TactileDistractorParameter)obj;
        
        return super.equals(tactileParam) 
                && Objects.equals(m_intervalDuration, tactileParam.m_intervalDuration)
                && Objects.equals(m_longVibrationDuration, tactileParam.m_longVibrationDuration)
                && Objects.equals(m_numberOfVibrations, tactileParam.m_numberOfVibrations)
                && Objects.equals(m_shortVibrationDuration, tactileParam.m_shortVibrationDuration);        
    } 
}
