package view.component.contentpane;

import contoller.TrialAnswerController;
import experiment.Block;
import experiment.Experimentation;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import parameters.TactileDistractorParameter;
import parameters.VisualDistractorParameter;
import parameters.geometry.AbstractShape;
import sitting.BlockResult;
import sitting.SittingManager;
import view.component.answergrid.AbstractAnswerGrid;
import view.component.answergrid.TactileAnswerGrid;
import view.component.answergrid.VisualAnswerGrid;
import view.component.answergrid.WordAnswerGrid;
/**
 *
 * @author Philippe
 */
public class TrialAnswersContentPane  extends AbstractContentPane implements Observer{
    JButton m_finishButton;
    
    JToggleButton m_applyButton;

    TrialAnswerController m_controller;
    
    BlockResult m_blockResult;
    
    private AbstractAnswerGrid m_answerGrid;
   
    /**
     * Creates new form TrialAnswersContentPane
     */
    public TrialAnswersContentPane(SittingManager sittingManager){
        super(sittingManager);        
        initComponents();
        this.setLayout(new GridBagLayout());
        

        m_blockResult = new BlockResult(sittingManager.getTrueBlock(sittingManager.getCurrentBlock()).getTrials().size(),Experimentation.getInstance().getWordsPerTrial());
        m_controller = new TrialAnswerController(sittingManager, m_blockResult);
        m_controller.addObserver(this);
        addSubmitPanel();
    }
    
    private void setAnswerGrid(AbstractAnswerGrid answerGrid){
        if(m_answerGrid != null){
            this.remove(m_answerGrid);
        }
        m_answerGrid = answerGrid;
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 0.80;
        c.gridx=0;
        c.gridy=0;
        this.add(m_answerGrid, c);
        this.revalidate();
        this.repaint();
    }
    
    public void setWordAnswerGrid(){
         setAnswerGrid(new WordAnswerGrid(m_sittingManager.getTrueTrial(m_sittingManager.getCurrentBlock(),
                 m_sittingManager.getCurrentTrial()).getWords().toArray(new String [0]), m_controller, m_sittingManager));
         m_controller.startTime();
    }
    
    public void setTactileAnswerGrid(){
        TactileDistractorParameter tdp = (TactileDistractorParameter)m_sittingManager.getTrueBlock(m_sittingManager.getCurrentBlock()).getParameters().get(TactileDistractorParameter.class);
        setAnswerGrid(new TactileAnswerGrid((String[])tdp.orderOfPlay(m_sittingManager.getCurrentSeed()), m_controller, m_sittingManager));
        m_controller.startTime();
    }
    
    public void setVisualAnswerGrid(){
        VisualDistractorParameter vdp = (VisualDistractorParameter)m_sittingManager.getTrueBlock(m_sittingManager.getCurrentBlock()).getParameters().get(VisualDistractorParameter.class);
        setAnswerGrid(new VisualAnswerGrid((AbstractShape[])vdp.orderOfPlay(m_sittingManager.getCurrentSeed()), m_controller, m_sittingManager));
        m_controller.startTime();
    }
   
    private void addSubmitPanel(){
        
        //On recréé le bouton soumettre
        m_applyButton = new JToggleButton("Soumettre");
        m_applyButton.setFont(new Font("Tahoma", 0, 36));
        m_applyButton.setEnabled(false);
        m_applyButton.addActionListener(new ActionListener(){
             @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_answerGrid.submitAnswers();
            }
        });
        
        m_finishButton = new JButton("Continuer");
        m_finishButton.setFont(new Font("Tahoma", 0, 36));
        m_finishButton.setEnabled(false);
        m_finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_finishButtonActionPerformed(e);
            }
        });
        
        JPanel submitPane = new JPanel(new GridLayout(1,2));

        //Add controls to set up horizontal and vertical gaps
        submitPane.add(m_applyButton);
        submitPane.add(m_finishButton);
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx= 0;
        c.gridy= 1;
        c.weighty = 0.20;
        this.add(submitPane, c);
    }

    @Override
    public void handleStateChange() {
        setWordAnswerGrid();
    }

    @Override
    public void update(Observable o, Object arg) {
        Block currentBlock = m_sittingManager.getTrueBlock(m_sittingManager.getCurrentBlock());
        switch ((String)arg) {
            case "WORD_DONE":
                if(currentBlock.hasTactileDistraction()){
                    this.setTactileAnswerGrid();
                    resetApplyButton();
                }
                else if(currentBlock.hasVisualDistraction()){
                    this.setVisualAnswerGrid();
                    resetApplyButton();
                }
                else{
                    enableFinishButton();
                }
                break;
            case "TACTILE_DONE":
                if(currentBlock.hasVisualDistraction()){
                    this.setVisualAnswerGrid();
                    resetApplyButton();
                }
                else{
                    enableFinishButton();
                }
                break;
            case "VISUAL_DONE":
                enableFinishButton();
                break;
            case "SET_ENABLE":
                m_applyButton.setEnabled(true);
                break;
        }
    }
    
    private void resetApplyButton(){
        m_applyButton.setSelected(false);
        m_applyButton.setEnabled(false);
    }
    
    private void enableFinishButton(){
        m_finishButton.setEnabled(true);
        m_applyButton.setEnabled(false);
    }
    
    private void m_finishButtonActionPerformed(ActionEvent e){
        m_controller.finishTrial();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
