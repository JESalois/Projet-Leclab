package contoller;
import experiment.Experimentation;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import parameters.*;
import parameters.geometry.AbstractShape;
import utility.Global;


/**
 * This class interact with the model when notified by the ParameterView.
 * It maintains a cache during its existence to keep track of the changes
 * to push to the model when the user presses the "ok" button in the view.
 * It does not directly update the model in order to revert in the cases where 
 * the user presses the "cancel" button.
 * 
 * @author Jean-Etienne Salois
 */
public class ParameterViewController {
    
    /**
     * A cache the controller updates for the tactile distractor. 
     * It will be used to update the model when the window closes.
     */
    private TactileDistractorParameter m_tactileDistractorParameterCache;
    
    /**
     * A cache the controller updates for the visual distractor. 
     * It will be used to update the model when the window closes.
     */
    private VisualDistractorParameter m_visualDistractorParameterCache;
    
    /**
     * A cache the controller updates for the background noise. 
     * It will be used to update the model when the window closes.
     */
    private NoiseParameter m_noiseParameterCache;
    
    /**
     * Initialize the controller
     */
    public ParameterViewController(){
        m_tactileDistractorParameterCache = new TactileDistractorParameter();
        m_visualDistractorParameterCache = new VisualDistractorParameter();
        m_noiseParameterCache = new NoiseParameter();
    }
    
    /**
     * Sets the tactile distractor cache with an existing tactile distractor.
     * @param tactileDist 
     *          tactile distractor
     */
    public void setTactileDistractorCache(TactileDistractorParameter tactileDist){
        m_tactileDistractorParameterCache = new TactileDistractorParameter(tactileDist);
    }
    
    /**
     * Sets the visual distractor cache with an existing visual distractor
     * @param visualDist 
     *          visual distractor
     */
    public void setVisualDistractorCache(VisualDistractorParameter visualDist){
        m_visualDistractorParameterCache = new VisualDistractorParameter(visualDist);
    }
    
    /**
     * Sets the noise parameter cache with an existing noise parameter
     * @param noise 
     *      noise parameter
     */
    public void setNoiseParameterCache(NoiseParameter noise){
        m_noiseParameterCache = new NoiseParameter(noise);
    }
    
    /**
     * Sets the short vibration duration in the cached tactile parameter
     * @param userInput 
     *          the short vibration duration
     * @return if the value as successfully been assigned
     */
    public boolean setShortVibDuration(String userInput){
        return m_tactileDistractorParameterCache.setShortVibrationDuration(Integer.parseInt(userInput));
    }
    
    /**
     * Sets the long vibration duration in the cached tactile parameter
     * @param userInput
     *          the long vibration duration
     * @return if the value as successfully been assigned
     */
    public boolean setLongVibDuration(String userInput){
        return m_tactileDistractorParameterCache.setLongVibrationDuration(Integer.parseInt(userInput));
    }
    
    /**
     * Sets the interval duration in the cached tactile parameter
     * @param userInput
     *          the interval duration
     * @return if the value as successfully been assigned
     */
    public boolean setTactileDistractorIntervalDuration(String userInput){
        return m_tactileDistractorParameterCache.setIntervalDuration(Integer.parseInt(userInput));
    }
    
    /**
     * Sets the number of vibrations in the cached tactile parameter
     * @param userInput
     *          the number of vibrations
     * @return if the value as successfully been assigned
     */
    public boolean setNumberOfVibrations(String userInput){
       return m_tactileDistractorParameterCache.setNumberOfVibrations(Integer.parseInt(userInput));
    }
    
    /**
     * Sets the number of appearance of any shape in the cached visual distractor parameter
     * @param userInput
     *          the number of appearance
     * @return if the value as successfully been assigned
     */
    public boolean setNumberOfAppearance(String userInput){
        return m_visualDistractorParameterCache.setNumberOfAppearance(Integer.parseInt(userInput));
    }
    
    /**
     * Sets the interval duration in the cached visual distractor parameter
     * @param userInput
     *          the interval duration
     * @return if the value as successfully been assigned
     */
    public boolean setVisualDistractorIntervalDuration(String userInput){
        return m_visualDistractorParameterCache.setIntervalDuration(Integer.parseInt(userInput));
    }
    
    /**
     * Sets the display time in the cached visual distractor parameter
     * @param userInput
     * @return if the value as successfully been assigned
     */
    public boolean setDisplayTime(String userInput){
        return m_visualDistractorParameterCache.setDisplayTime(Integer.parseInt(userInput));
    }
    
    /**
     * Takes the tactile distractor parameter designed in this view and pushed it in the
     * list of preset parameters of the Experimentation. 
     * @see Experimentation
     */
    public void pushTactileDistractorParameter(){
        Experimentation.getInstance().addParameter(m_tactileDistractorParameterCache);
    }
    
    /**
     * Takes the visual distractor parameter designed in this view and pushed it in the
     * list of preset parameters of the Experimentation. 
     * @see Experimentation
     */
    public void pushVisualDistractorParameter(){
        Experimentation.getInstance().addParameter(m_visualDistractorParameterCache);
    }
    
    /**
     * Takes the noise parameter designed in this view and pushes it in the list
     * of preset parameters of the Experimentation. 
     * @see Experimentation
     */
    public void pushNoiseParameter(){
        Experimentation.getInstance().addParameter(m_noiseParameterCache);
    }
    
    /**
     * Remove the parameter of the given class from the list of preset of
     * the Experimentation. 
     * @see Experimentation
     */
    public void removeParameterFromExperiment(Class parameterClass){
        Experimentation.getInstance().removeParameter(parameterClass);
    }
    
    /**
     * Sets the tactile parameter position in the trial
     * @param position 
     *          the position in the trial in relation to the sentence
     */
    public void setTactileParameterPositionInTrial(EParameterPosition position){
        m_tactileDistractorParameterCache.setParameterPositionInTrial(position);
    }
    
    /**
     * Sets the visual parameter position in the trial
     * @param position 
     *          the position in the trial in relation to the sentence
     */
    public void setVisualParameterPositionInTrial(EParameterPosition position){
        m_visualDistractorParameterCache.setParameterPositionInTrial(position);
    }
    
    /**
     * adds a color to the cached visual parameter
     * @param aColor 
     *          the color to add
     */
    public void addColorToVisualDitractor(Color aColor){
        m_visualDistractorParameterCache.addColor(aColor);
    }
    
    /**
     * Removes a color from the cached visual parameter
     * @param aColor 
     *          the color to remove
     */
    public void removeColorFromVisualDitractor(Color aColor){
        m_visualDistractorParameterCache.removeColor(aColor);
    }
    
    /**
     * adds a shape to the cached visual parameter
     * @param aShape 
     *          the shape to add
     */
    public void addShape(AbstractShape aShape){
        m_visualDistractorParameterCache.addShape(aShape);
    }
    
    /**
     * Removes a shape from the cached visual parameter
     * @param aShape 
     *          the shape to remove
     */
    public void removeShapeFromVisualDistractor(Class aShape){
        m_visualDistractorParameterCache.removeShape(aShape);
    }
    
    /**
     * Set the filename of the background noise
     * @param noiseFileName 
     *          the filename
     */
    public void setBackgroundNoise(String noiseFileName){
        m_noiseParameterCache.setNewFile(Global.NOISE_FOLDER_PATH+noiseFileName);
    }
    
    /**
     * Set the words per trial for the experimentation
     * @param wordsPerTrial
     *          number of word by trial
     */
    public void setWordsPerTrial(int wordsPerTrial){
        Experimentation.getInstance().setWordsPerTrial(wordsPerTrial);
    }

    /**
     * Set the default number of trials per block for the experimentation
     * @param trialsPerBlock
     *          number of trials by block
     */
    public void setTrialsPerBlock(int trialsPerBlock) {
        Experimentation.getInstance().setDefautNumberOfTrials(trialsPerBlock);
    }
    
    /**
     * Save current parameters to the drive as he default parameters to be
     * loaded in future.
     * The file saved represents the experimentation which contains all contains
     * all necessary parameters.
     */
    public void saveDefault() throws FileNotFoundException, IOException {
        // Write to disk with FileOutputStream
        FileOutputStream f_out = new FileOutputStream(Global.PARAMETERS_FOLDER_PATH+"//default_parameters");

        // Write object with ObjectOutputStream
        ObjectOutputStream obj_out = new ObjectOutputStream (f_out);

        // Write object out to disk
        obj_out.writeObject ( Experimentation.getInstance() );
    }
}
