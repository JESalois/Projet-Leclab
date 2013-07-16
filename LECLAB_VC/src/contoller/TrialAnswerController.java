package contoller;
import experiment.Experimentation;
import java.util.Observable;
import parameters.geometry.AbstractShape;
import sitting.BlockResult;
import sitting.DistractorResult;
import sitting.SittingManager;
import sitting.TrialResult;
import sitting.WordResult;
import statistics.AStat;
/**
 * @author Philippe
 */
public class TrialAnswerController extends Observable {
    
    SittingManager m_sittingManager;
    double m_answerSheetTime;

    
    public  TrialAnswerController (SittingManager sittingManager, BlockResult blockResult){
        m_sittingManager = sittingManager;
    }
    
    public void submitWord(){
        m_sittingManager.getBlockResult(m_sittingManager.getCurrentBlock()).getTrialResult(m_sittingManager.getCurrentTrial()).setTrialNumber(m_sittingManager.getCurrentTrial());
        this.setChanged();
        notifyObservers("WORD_DONE");
    }
    
    public void submitTactile(){
        setChanged();
        notifyObservers("TACTILE_DONE");
    }
    
    public void submitVisual(){
        setChanged();
        notifyObservers("VISUAL_DONE");
    }

    public void startTime(){
        m_answerSheetTime = System.currentTimeMillis();
    }
    
    public void setWordAnswer(int posX,int posY, String word){
        TrialResult tr = m_sittingManager.getBlockResult(m_sittingManager.getCurrentBlock()).getTrialResult(m_sittingManager.getCurrentTrial());
        WordResult wordResult = tr.getWordAnswer(posX);
        wordResult.setAnswerTime((double)(System.currentTimeMillis()- m_answerSheetTime)/1000.0);
        wordResult.setUserAnswer(posY,word);
        
        boolean enableSubmit = true;
        for (int i =0; i < Experimentation.getInstance().getWordsPerTrial(); i++){
                if (tr.getWordAnswer(i).getUserAnswer()==-1){
                    enableSubmit = false;
                }
        }
        if (enableSubmit){
            setChanged();
            notifyObservers("SET_ENABLE");
        }
    }
    
    public void setTactileAnswer(int posX,int posY, AbstractShape[] shapes){
        TrialResult tr = m_sittingManager.getBlockResult(m_sittingManager.getCurrentBlock()).getTrialResult(m_sittingManager.getCurrentTrial());
        DistractorResult tactileResult = tr.getTactileResult();
        tactileResult.setAnswerTime((double)(System.currentTimeMillis()- m_answerSheetTime)/1000.0);
        tactileResult.setUserAnswer(posY, shapes);
        setChanged();
        notifyObservers("SET_ENABLE");
    }
    
    public void setVisualAnswer(int posX,int posY, AbstractShape[] shapes){
        TrialResult tr = m_sittingManager.getBlockResult(m_sittingManager.getCurrentBlock()).getTrialResult(m_sittingManager.getCurrentTrial());
        DistractorResult visualResult = tr.getVisualResult();
        visualResult.setAnswerTime((double)(System.currentTimeMillis()- m_answerSheetTime)/1000.0);
        visualResult.setUserAnswer(posY, shapes);
        setChanged();
        notifyObservers("SET_ENABLE");
    }
    
    public void finishTrial(){
        AStat.getInstance().loadBlockResults(m_sittingManager.getBlockResult(m_sittingManager.getCurrentBlock()),m_sittingManager);
        AStat.getInstance().calculate();
        m_sittingManager.trialFinished();
    }
}
