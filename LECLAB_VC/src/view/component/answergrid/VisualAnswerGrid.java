package view.component.answergrid;

import contoller.TrialAnswerController;
import experiment.Experimentation;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import parameters.VisualDistractorParameter;
import parameters.geometry.AbstractShape;
import parameters.geometry.Circle;
import parameters.geometry.Diamond;
import parameters.geometry.Hexagon;
import parameters.geometry.Octogon;
import parameters.geometry.Pentagon;
import parameters.geometry.Pentagram;
import parameters.geometry.Rectangle;
import parameters.geometry.Trapeze;
import parameters.geometry.Triangle;
import sitting.SittingManager;
import sitting.TrialResult;
import view.component.button.VisualAnswerButton;

/**
 * Insert Description of class
 * @author Philippe
 */
public class VisualAnswerGrid extends AbstractAnswerGrid{
    
    private static final AbstractShape [] possibleShapes = {new Circle(),
        new Rectangle(), 
        new Diamond(), 
        new Hexagon(), 
        new Octogon(), 
        new Pentagon(), 
        new Pentagram(), 
        new Trapeze(), 
        new Triangle()
    };
    
    private static final Color [] possibleColors = {Color.BLUE,
        Color.CYAN, 
        Color.RED, 
        Color.YELLOW,
        Color.MAGENTA, 
        Color.GREEN};
    
    private AbstractShape [] m_answer;
    
    public VisualAnswerGrid(AbstractShape [] rightAnswers, TrialAnswerController controller, SittingManager sittingManager){
        super(controller, sittingManager);
        for(AbstractShape shape : rightAnswers){
            shape.setSize(50);
        }
        m_answer = rightAnswers;
        VisualAnswerButton[] answersButton = new VisualAnswerButton[1];
        answersButton[0] = new VisualAnswerButton(rightAnswers);
        TrialResult tr = m_sittingManager.getBlockResult(m_sittingManager.getCurrentBlock()).getTrialResult(m_sittingManager.getCurrentTrial());
        int [] rightAnswer = {tr.getVisualResult().getRightAnswer()};
        createToggleButtons(answersButton, generatePossibleAnswers(),1, rightAnswer);
        paint();
    }

    @Override
    protected void paint() {
         //reset la vue 
        this.removeAll();
        this.revalidate();
        
        ((GridLayout)this.getLayout()).setRows(m_answerChoices + 1);
        ((GridLayout)this.getLayout()).setColumns(1);
        
        //En-tête des colonnes
        this.add(new Label("Formes", JLabel.CENTER));
        
        //Création dynamique de tous les boutons
        addToggleButtons();   
    }

    @Override
    public void submitAnswers() {
        m_controller.submitVisual();
    }

    @Override
    protected void setAnswerOfButton(int posX, int posY) {
        m_controller.setVisualAnswer(posX, posY, ((VisualAnswerButton)this.m_buttons[posX][posY]).getDisplayedShape());
    }

    @Override
    protected ArrayList<JToggleButton>[] generatePossibleAnswers() {
        ArrayList<JToggleButton>[] possibleAnswers = new ArrayList[1];
        for(int i = 0; i < possibleAnswers.length; i++){
            possibleAnswers[i] = new ArrayList<>(m_answerChoices - 1);
        }
        
        for(int i = 0; i < possibleAnswers.length; i++){
            ArrayList<AbstractShape []> listOfShapes = createRandomShapeSequences(m_sittingManager.getCurrentSeed(),  m_answerChoices - 1);
            for(int j = 0; j < m_answerChoices - 1; j++){
                 possibleAnswers[i].add(new VisualAnswerButton(listOfShapes.get(j)));
            }
        }
        return possibleAnswers;
    }
    
    private ArrayList<AbstractShape[]> createRandomShapeSequences(int seed, int numberOfSequences){
        ArrayList<AbstractShape []> sequences = new ArrayList<>(numberOfSequences);
        Random rand = new Random(m_sittingManager.getCurrentSeed());
        while(sequences.size() != numberOfSequences){
            AbstractShape [] shapes = new AbstractShape[m_answer.length];
            for(int i = 0; i < m_answer.length; i++){
                shapes[i] = VisualAnswerGrid.possibleShapes[rand.nextInt(VisualAnswerGrid.possibleShapes.length)].clone();
                shapes[i].setSize(50, 50);
                shapes[i].setShapeColor(VisualAnswerGrid.possibleColors[rand.nextInt(VisualAnswerGrid.possibleColors.length)]);
            }
            if(!shapeListContains(sequences, shapes) && !Arrays.equals(shapes, m_answer)){
                sequences.add(shapes);
            }
        }
        return sequences;    
    }
    
    private boolean shapeListContains( ArrayList<AbstractShape []> shapeListList, AbstractShape [] shapeList){
        for(int i = 0; i < shapeListList.size(); i++){
            if(Arrays.equals(shapeListList.get(i), shapeList)){
                return true;
            }
        }
        return false;
    }
}
