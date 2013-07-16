package view.component.answergrid;

import contoller.TrialAnswerController;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import sitting.BlockResult;
import sitting.SittingManager;
import sitting.TrialResult;
import java.util.ArrayList;

/**
 * Insert Description of class
 * @author Philippe
 */
public abstract class AbstractAnswerGrid extends JPanel{
    
    protected JToggleButton[][] m_buttons; 
    
    protected int m_answerChoices;
    
    Font m_buttonFont;
    
    TrialAnswerController m_controller;
    
    SittingManager m_sittingManager;
    
    public AbstractAnswerGrid(TrialAnswerController controller, SittingManager sittingManager) {
        this.setLayout(new GridLayout());
        this.setFont(new Font("Tahoma", 0, 50));
        m_answerChoices = 7;
        m_buttonFont = new Font("Tahoma", Font.BOLD, 30);
        m_controller = controller;
        m_sittingManager = sittingManager;
    }
   
    protected abstract void paint();
    
    public abstract void submitAnswers();
    
    protected void createToggleButtons(JToggleButton [] rightAnswers, ArrayList<JToggleButton>[] wrongAnswers, int noLanes, int [] rightAnswerPos){
        m_buttons = new JToggleButton[noLanes][this.m_answerChoices];
        for (int i = 0; i < m_buttons.length; i++){
            for (int j = 0; j < m_buttons[i].length; j++){
                if (j == rightAnswerPos[i]){
                    m_buttons[i][j] = rightAnswers[i];
                }
                else{
                    m_buttons[i][j] =  wrongAnswers[i].remove(0);
                }
                m_buttons[i][j].setFont(m_buttonFont);
                m_buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        onToggleButtonPressed(e);
                    }
                });
            }
        }
    }
    
    protected void addToggleButtons(){
        ButtonGroup [] buttonGroups = new ButtonGroup[m_buttons.length];
        for (int i = 0; i < buttonGroups.length; i++)
        {
            buttonGroups[i] = new ButtonGroup(); 
        }
        for (int i = 0; i < m_buttons[0].length; i++){
            for (int j = 0; j < m_buttons.length; j++){
                this.add(m_buttons[j][i]);
                buttonGroups[j].add(m_buttons[j][i]);
            }
        }
    }
    
    protected void onToggleButtonPressed(ActionEvent e){
        for (int i = 0; i < m_buttons[0].length; i++){
            for (int j = 0; j < m_buttons.length; j++){
                if(m_buttons[j][i].equals((JToggleButton)e.getSource())){
                    setAnswerOfButton(j, i);
                }   
            }
        }
    }
    
    protected abstract ArrayList<JToggleButton>[] generatePossibleAnswers();
    
    protected abstract void setAnswerOfButton(int posX, int posY);
}
