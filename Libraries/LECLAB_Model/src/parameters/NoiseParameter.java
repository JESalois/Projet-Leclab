package parameters;
import java.io.File;
import java.util.Objects;

/**
 * This is a children class of Parameter.
 * It encapsulate the behavior of the background noise during playing
 * during the experiment to disturb the subject.
 * 
 * @see Parameter
 * 
 * @author Jean-Etienne Salois, Philippe de Sève 
 */
public class NoiseParameter extends Parameter{
    /**
     * the media file thats contains to sound to play during the experiment.
     */
    private File m_noiseFile;
    
    /**
     * Default constructor.
     */
    public NoiseParameter(){
        //TODO initialize with a default noiseFile: noiseFile = new File("C:/DFAULTBABBLE.MP3")
        super(EParameterPosition.CONSTANT);
    }
    
    /**
     * Copy constructor.
     * @param distractor
     *          the noise parameter distractor to copy
     */
    public NoiseParameter(NoiseParameter distractor){
        super(distractor.getParameterPositionInTrial());
        m_noiseFile = distractor.getFile();
    }
    
    /**
     * Initialize the file associated to the path provided.
     * @param path 
     *          path to the media file
     */
    public NoiseParameter(String path){
        super(EParameterPosition.CONSTANT);
        m_noiseFile = new File(path);
    }
    
    /**
     * clone redefinition
     * @return
     *      the NoiseParameter object cloned
     */
    @Override
    public NoiseParameter clone(){
        return new NoiseParameter(this);
    }
    
    /**
     * Change the media file associated to the noise with the one specified by
     * the path
     * 
     * @param path 
     *          path to the new media file
     */
    public void setNewFile(String path){
        m_noiseFile = new File(path);
    }
    
    /**
     * Get the name of the noise file 
     * @return the filename of the noise file
     */
    public String getNoiseFilename(){
        return m_noiseFile.getName();
    }
    
    /**
     * Getter for m_noiseFile
     * @return the noise file     
     */
    public File getFile(){
        return m_noiseFile;
    }

    /**
     * orderOfPlay implementation. Implements the behavior of the parameter 
     * when it is to be played during a block. The function uses a seed to 
     * generate a random sequence of stimulus to be played.
     * @param seed
     *          seed used to generate stimulus sequence
     * @return the order, a string table, composed with the name of the noise file
     */
    @Override
    public Object[] orderOfPlay(int seed) {
        String [] order =  new String[1];
        order[1] = m_noiseFile.getName();
        return order;
    }
    
    /**
     * HashCode redefinition
     * @return hashCode
     */
    @Override
    public int hashCode(){
        return Objects.hash(super.hashCode(),m_noiseFile);
    }

    /**
     * Equals redefinition. Tells if the object passed in parameters is equal to this one.
     * @param obj
     *          the object to copy
     * @Return boolean 
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
        
        final NoiseParameter noiseParam = (NoiseParameter)obj;
        
        return super.equals(noiseParam) && Objects.equals(m_noiseFile, noiseParam.m_noiseFile);        
    }    
}
