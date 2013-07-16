package contoller;
import experiment.Block;
import experiment.Experimentation;
import experiment.Trial;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import parameters.NoiseParameter;
import parameters.Parameter;
import parameters.TactileDistractorParameter;
import parameters.VisualDistractorParameter;
import utility.Global;
import view.ParameterView;

/**
 * Controller of the DesignerView. Used mainly to control the experiment package.
 * @author Jean-Étienne
 * @see view.DesignerView
 * @see experiment.Experimentation
 * @see experiment.Block
 * @see experiment.Trial
 */
public class DesignerViewController{

    /**
    * Initialize the controller
    */
    public DesignerViewController(){}
    
    /**
     * Add or remove tactile distractor from currently selected block
     * @param distactorState
     *          whether or not there will be a tactile distractor in this block
     * @param block 
     *          the block to be affected
     */
    public void setTactileDistractor(Boolean distractorState, Block block){
        //The distractor needs to be in the block
        if(distractorState)
        {   //If the distractor is not already in bloc
            if(block.getParameters().get(TactileDistractorParameter.class) == null){
                //add the distractor to the block
                block.addParameter(Experimentation.getInstance().getPresetParameters().get(TactileDistractorParameter.class));
            }
        }
        else{//The distractor needs to be absent from the block
            //If the distrctor is prently in the block
            if(block.getParameters().get(TactileDistractorParameter.class) != null){
                //Remove it from the block
                block.removeParameter(TactileDistractorParameter.class);
            }
        }        
    }
    
    /**
     * Add or remove visual distractor from currently selected block
     * @param distactorState
     *          whether or not there will be a visual distractor in this block
     * @param block 
     *          the block to be affected
     */         
    public void setVisualDistractor(Boolean distactorState, Block block){
        //The distractor needs to be in the block
        if(distactorState)
        {   //If the distractor is not already in bloc
            if(block.getParameters().get(VisualDistractorParameter.class) == null){
                //add the distractor to the block
                block.addParameter(Experimentation.getInstance().getPresetParameters().get(VisualDistractorParameter.class));
            }
        }
        else{//The distractor needs to be absent from the block
            //If the distractor is presently in the block
            if(block.getParameters().get(VisualDistractorParameter.class) != null){
                //Remove it from the block
                block.removeParameter(VisualDistractorParameter.class);
            }
        }        
    }
    
    /**
    * Add or remove noise distractor from currently selected block
     * @param distactorState
     *          whether or not there will be a noise in this block
     * @param block 
     *          the block to be affected
    */
    public void setNoise(Boolean distactorState, Block block){
        //The distractor needs to be in the block
        if(distactorState)
        {   //If the distractor is not already in bloc
            if(block.getParameters().get(NoiseParameter.class) == null){
                //add the distractor to the block
                block.addParameter(Experimentation.getInstance().getPresetParameters().get(NoiseParameter.class));
            }
        }
        else{//The distractor needs to be absent from the block
            //If the distrctor is prently in the block
            if(block.getParameters().get(NoiseParameter.class) != null){
                //Remove it from the block
                block.removeParameter(NoiseParameter.class);
            }
        }        
    }
    
    /**
     * Save the specified block in a file
     * @param block 
     *          the block to save
     * @param file
     *          the file used to save to block
     */
    public void saveBlock(Block block, File file) throws FileNotFoundException, IOException{
        // Write to disk with FileOutputStream
        FileOutputStream f_out = new FileOutputStream(file.getAbsolutePath());

        // Write object with ObjectOutputStream
        ObjectOutputStream obj_out = new ObjectOutputStream (f_out);

        // Write object out to disk
        obj_out.writeObject ( block );
    }
    
    /**
     * Add new block to experiment. he block is automatically generated
     * with the experimentation settings.
     */
    public void addBlock(){
        Experimentation.getInstance().addBlock();
    }
    
    /**
     * Remove a specific block from the experiment.
     * @param blockToDelete
     *          block to remove from the experiment.
     */
    public void deleteBlock(Block blockToDelete) {
        Experimentation.getInstance().removeBlock(blockToDelete);
    }
    
    /**
     * Save the experimentation in a file
     * @param file
     *          the file used to save the experimentation
     */
    public void saveExperimentation(File file) throws FileNotFoundException, IOException{
        // Write to disk with FileOutputStream
        FileOutputStream f_out = new FileOutputStream(file.getAbsolutePath());

        // Write object with ObjectOutputStream
        ObjectOutputStream obj_out = new ObjectOutputStream (f_out);

        // Write object out to disk
        obj_out.writeObject ( Experimentation.getInstance() );

        //Change experiementation name to reflect
        Experimentation.getInstance().setName(file.getName().replace(".expe", ""));
    }
    
    /**
     * Load an experimentation from a file
     * @param fileName 
     */
    public void loadExperimentation(File file) throws FileNotFoundException, IOException, ClassNotFoundException{
        // Read from disk using FileInputStream
        FileInputStream f_in = new FileInputStream(file.getAbsolutePath());

        // Read object using ObjectInputStream
        ObjectInputStream obj_in = new ObjectInputStream (f_in);

        // Read an object
        Object obj = obj_in.readObject();

        if (obj instanceof Experimentation)
        {
                // Cast object to Block
                Experimentation loadedExperiementation = (Experimentation) obj;                
                Experimentation.getInstance().load(loadedExperiementation);
        }        
    }
    
    /**
     * Clear the current experimentation
     */
    public void newExperimentation(){
        Experimentation.getInstance().clear();
        //Reload the default parameters of the experiment
        loadDefaultParameters();
    }
    
    /**
     * Start new ParemetersView to modify experimentation parameters
     */
    public void modifyExperimentationParameters(){
        new ParameterView().setVisible(true);
    }
    
    /**
     * Load new block and add it to the experimentation
     * @param fileName
     *          the file used to load the block
     */
    public void loadBlock(File file) throws FileNotFoundException, IOException, ClassNotFoundException{
        // Read from disk using FileInputStream
        FileInputStream f_in = new FileInputStream(file.getAbsolutePath());

        // Read object using ObjectInputStream
        ObjectInputStream obj_in = new ObjectInputStream (f_in);

        // Read an object
        Object obj = obj_in.readObject();

        if (obj instanceof Block)
        {
                // Cast object to Block
                Block loadedBlock = (Block) obj;

                Experimentation.getInstance().addBlock(loadedBlock);
        }
    }
    
    /**
     * Add or delete the number of trials needed to match numberOfTrials. This 
     * function is threaded because it could result in waiting time on slow
     * computers (mainly because of the way the things are rendered to the screen).
     * @param numberOfTrials
     *          number of trials wanted in the block
     * @param block
     *          block to modify
     */
    public void setBlockTrialsNumber(final int numberOfTrials, final Block block) {        
        SwingWorker swingWorker = new SwingWorker()
        {
            @Override
            protected Object doInBackground() throws FileNotFoundException {
               int currentNumberOfTrials =  block.getTrials().size();
                while(numberOfTrials != currentNumberOfTrials){
                    if (numberOfTrials > currentNumberOfTrials){
                        block.createRandomTrial();
                        currentNumberOfTrials++;
                    }
                    if (numberOfTrials < currentNumberOfTrials){
                        block.removeLastTrial();
                        currentNumberOfTrials--;
                    }
                }                  
                return null;
            }            
        };        
        swingWorker.execute();
    }

    /**
     * Copy the block passed and add it to the experimentation
     * @param block
     *          the block to copy
     */
    public void duplicateBlock(Block block) {
        Block duplicatedBloc = new Block(block);            
        Experimentation.getInstance().addBlock(duplicatedBloc);
    }

    /**
     * Set the practice state of the presently selected block
     * @param state
     *          new practice state
     * @param block
     *          block to modify
     */
    public void setPractice(boolean state, Block block) {
        block.setIsPractice(state);
    }
    
    /**
     * Set the hasPractices tate of the presently selected block
     * @param state
     * @param block 
     */
    public void setRetroaction(boolean state, Block block) {
        block.setHasRetroaction(state);
    }

    /**
     * Set the currently selected block name
     * @param block
     *          block to modify
     * @param name 
     *          name to affect to the block
     */
    public void setBlockName(Block block, String name) {
        block.setName(name);
    }
    
    /**
     * Set the currently selected trial videoFile
     * @param trial
     *          trial to modify
     * @param fileName 
     *          name of the file to affect to the trial
     */
    public void setTrialFile(Trial trial, String fileName){
        trial.setVideoFile(new File(fileName));
    }

    /**
     * Load default parameters from the path in Global.PARAMETERS_FOLDER_PATH
     */
    public void loadDefaultParameters(){ 
        try{
            // Read from disk using FileInputStream
            FileInputStream f_in = new FileInputStream(Global.PARAMETERS_FOLDER_PATH+"//default_parameters");

            // Read object using ObjectInputStream
            ObjectInputStream obj_in = new ObjectInputStream (f_in);

            // Read an object
            Object obj = obj_in.readObject();

            Experimentation loadedExperiementation = null;

            if (obj instanceof Experimentation)
            {
                    // Cast object to Block
                    loadedExperiementation = (Experimentation) obj;
                    Experimentation.getInstance().setDefautNumberOfTrials(loadedExperiementation.getDefaultNumberOfTrials());
                    Experimentation.getInstance().setWordsPerTrial(loadedExperiementation.getWordsPerTrial());

                    for(Parameter param: loadedExperiementation.getPresetParameters().values()){
                        Experimentation.getInstance().addParameter(param);
                    }
            } 
        }
        catch (FileNotFoundException ex) {
            //There are no saved param
        } catch (IOException ex) {
            //The loaded param file was corrupted, deleting it
            File corruptedFile = new File(Global.PARAMETERS_FOLDER_PATH+"//default_parameters");
            corruptedFile.delete();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Ceci n'est pas normal, faudrait en parler à J-E ou Phil:\n  "
                    +ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
