package view.component.answergrid;

import contoller.TrialAnswerController;
import experiment.Experimentation;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javax.swing.JToggleButton;
import parameters.TactileDistractorParameter;
import parameters.geometry.AbstractShape;
import parameters.geometry.Circle;
import parameters.geometry.Rectangle;
import parameters.geometry.Square;
import sitting.SittingManager;
import sitting.TrialResult;
import view.component.button.VisualAnswerButton;

/**
 * Insert Description of class
 * @author Philippe
 */
public class TactileAnswerGrid extends AbstractAnswerGrid{
    
    private String [] m_answer;
    
    public TactileAnswerGrid(String [] signal, TrialAnswerController controller, SittingManager sittingManager){
        super(controller, sittingManager);
        TactileDistractorParameter tdp = (TactileDistractorParameter)m_sittingManager.getTrueBlock(m_sittingManager.getCurrentBlock()).getParameters().get(TactileDistractorParameter.class);
        m_answerChoices = Math.pow(tdp.getNumberOfVibration(), 2) < m_answerChoices ? (int)Math.pow(tdp.getNumberOfVibration(), 2) : m_answerChoices;
        m_answer = signal;
        VisualAnswerButton[] answersButton = new VisualAnswerButton[1];
         answersButton[0] = new VisualAnswerButton(convertToShape(signal));
         TrialResult tr = m_sittingManager.getBlockResult(m_sittingManager.getCurrentBlock()).getTrialResult(m_sittingManager.getCurrentTrial());
         int [] rightAnswer = {tr.getTactileResult().getRightAnswer()};
        createToggleButtons(answersButton, generatePossibleAnswers(),1, rightAnswer);
        paint();
    }
    @Override
    protected void paint() {
         //reset la vue 
        this.removeAll();
        this.revalidate();
              
        ((GridLayout)this.getLayout()).setRows(m_answerChoices + 1);
        ((GridLayout)this.getLayout()).setColumns(Experimentation.getInstance().getWordsPerTrial());
        
        //En-tête des colonnes   
        this.add(new Label("Signal"));            
        
        //Création dynamique de tous les boutons
        addToggleButtons();
        
        this.revalidate();
        this.repaint();
    }

    @Override
    public void submitAnswers() {
        m_controller.submitTactile();
    }

    @Override
    protected void setAnswerOfButton(int posX, int posY) {
       m_controller.setTactileAnswer(posX, posY, ((VisualAnswerButton)this.m_buttons[posX][posY]).getDisplayedShape());
    }

    @Override
    protected ArrayList<JToggleButton>[] generatePossibleAnswers() {
        ArrayList<JToggleButton>[] possibleAnswers = new ArrayList[1];
        for(int i = 0; i < possibleAnswers.length; i++){
            possibleAnswers[i] = new ArrayList<>(m_answerChoices - 1);
        }
        
        for(int i = 0; i < possibleAnswers.length; i++){
            ArrayList<String []> sequences = createRandomVibrationSequences(m_sittingManager.getCurrentSeed(), m_answerChoices - 1);     
            for(int j = 0; j < m_answerChoices - 1; j++){
                 possibleAnswers[i].add(new VisualAnswerButton(convertToShape(sequences.get(j))));
            }
        }
        return possibleAnswers;
    }
    
    private ArrayList<String[]> createRandomVibrationSequences(int seed, int numberOfSequence){
        ArrayList<String []> sequences = new ArrayList<>(numberOfSequence);
        Random rand = new Random(seed);
        while(sequences.size() != numberOfSequence){
            String [] sequence = new String[m_answer.length];
            for(int i = 0; i < m_answer.length; i++){
                if(rand.nextInt(2) == 0){
                    sequence[i] = "C";
                }
                else
                {
                    sequence[i] = "L";
                }
            }
            if(!shapeListContains(sequences, sequence) && !Arrays.equals(sequence, m_answer)){
                sequences.add(sequence);
            }
        }
        return sequences;
    }
    
    private AbstractShape [] convertToShape(String [] signal){
        AbstractShape [] shapeArr = new AbstractShape[signal.length];
        for(int i = 0; i < signal.length; i++){
            if(signal[i].equals("C")){
                shapeArr[i] = new Circle();
                shapeArr[i].setSize(50);
            }
            else{
                shapeArr[i] = new Rectangle();
                shapeArr[i].setSize(100, 50);
            }
        }
        return shapeArr;
    }
    
    private boolean shapeListContains( ArrayList<String []> shapeListList, String [] shapeList){
        for(int i = 0; i < shapeListList.size(); i++){
            if(Arrays.equals(shapeListList.get(i), shapeList)){
                return true;
            }
        }
        return false;
    }
}
