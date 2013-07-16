package experiment;
import event.LeclabEvent;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Observable;
import parameters.Parameter;
import sitting.SittingManager;

/**
 * This class represent a single Trial from a Block.
 * It contains the sentence the subject will have to ear and its parameter
 * inherited from its Block.
 * It is a single child to the Block Class.
 * The SittingManager will interact with this class to assign
 * the order in which to play the trial. 
 * 
 * @see Experimentation
 * @see Block
 * @see Parameter
 * @see SittingManager
 * 
 * @author Philippe de Sève, Jean-Etienne Salois
 */
public class Trial extends Observable implements Serializable{
    /**
    * The words contained in the trial. These word represent the spoken words
    * in the video file linked with this trial.
    * @see #m_videoFile;
    */
    private ArrayList<String> m_words;
    
    /*
     * The video file assiocated to the trial. The video file has the words represented
     * by the m_words attribute.
     * @see #m_words
     */
    private File m_videoFile;
    
    /**
     * Default constructor should be avoided. The trial should be instantiated
     * with a video file.     * 
     * @see #Trial(java.io.File)  
     */
    public Trial(){
        m_words = new ArrayList<String>();
        m_videoFile = new File("");
    }
    
    /**
     * Initialize the Trial with a video file.
     * The words of the trial are extracted from the file
     * using the following method:
     * "word1_word2_word3" => {"word1", "word2", "word3"}
     * @param videoFile
     *          video file for this trial.
     */
   public Trial(File videoFile){
       m_videoFile = videoFile;
       String [] words = videoFile.getName().split("_");
       //Remove to extention of the file in the last word
       String lastWord = words[words.length - 1];
       lastWord = lastWord.substring(0, lastWord.lastIndexOf("."));
       words[words.length - 1] = lastWord;
       
       m_words = new ArrayList<String>(Arrays.asList(words));
   }
    
    /**
     * Getter used to access words
     */
    public ArrayList<String> getWords(){
        return m_words;
    }
    
    /**
     * Return the video file
     * @return the video file
     */
    public File getVideoFile(){
        return m_videoFile;
    }
    
    /**
     * Setter for m_words. Word should only be set when the video file changed.
     * @param words
     *          words in the video files linked to the trial.
     */
    private void setWords(ArrayList<String> words){
       m_words = words;
       setChanged();
       notifyObservers(LeclabEvent.TRIAL_WORDS_CHANGED);
    }
    
    /**
     * Setter of m_videoFile
     * @param videoFile
     *          The video file associated to the trial.
     */
    public void setVideoFile(File videoFile){
       m_videoFile = videoFile;
       String [] words = videoFile.getName().split("_");
       //Remove to extention of the file in the last word
       String lastWord = words[words.length - 1];
       lastWord = lastWord.substring(0, lastWord.lastIndexOf("."));
       words[words.length - 1] = lastWord;
       setWords(new ArrayList<String>(Arrays.asList(words)));
    }
    
    /**
     * HashCode redefinition
     * @return hashCode
     */
    @Override
    public int hashCode(){
        return Objects.hash(m_videoFile, m_words);
    }

    /**
     * Equals redefinition. Tells if the object passed in parameters is equal to this one.
     * @param obj
     *          the object to compare
     * @Return if the trial is equals. 
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
        
        final Trial trial = (Trial)obj;
        
        return Objects.equals(m_videoFile, trial.m_videoFile) && Objects.equals(m_words, trial.m_words);        
    }
}
