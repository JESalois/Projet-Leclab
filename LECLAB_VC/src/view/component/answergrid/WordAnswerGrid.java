package view.component.answergrid;

import contoller.TrialAnswerController;
import experiment.Experimentation;
import java.awt.GridLayout;
import java.awt.Label;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import sitting.SittingManager;
import sitting.TrialResult;
import utility.FolderReader;
import utility.Global;

/**
 * Insert Description of class
 * @author Philippe
 */
public class WordAnswerGrid extends AbstractAnswerGrid {
    
    private List<String> m_answers;
    
    public WordAnswerGrid(String [] words, TrialAnswerController controller, SittingManager sittingManager){
        super(controller, sittingManager);
        JToggleButton[] answersButton = new JToggleButton[words.length];
        for(int i = 0; i < words.length; i++){
            answersButton[i] = new JToggleButton(words[i]);
        }
        m_answers = Arrays.asList(words);
        TrialResult tr = m_sittingManager.getBlockResult(m_sittingManager.getCurrentBlock()).getTrialResult(m_sittingManager.getCurrentTrial());
        int [] rightanswers = new int[Experimentation.getInstance().getWordsPerTrial()];
        for(int i = 0; i < rightanswers.length; i++){
            rightanswers[i] = tr.getWordAnswer(i).getRightAnswer();
        }
        createToggleButtons(answersButton, generatePossibleAnswers(),Experimentation.getInstance().getWordsPerTrial(), rightanswers);
        paint();
    }

    @Override
    public void paint() {
        //reset la vue 
        this.removeAll();
        this.revalidate();
        
        ((GridLayout)this.getLayout()).setRows(m_answerChoices + 1);
        ((GridLayout)this.getLayout()).setColumns(Experimentation.getInstance().getWordsPerTrial());
        
        //En-tête des colonnes
        for (int i = 1; i <= Experimentation.getInstance().getWordsPerTrial(); i++){       
            this.add(new Label("Mot "+i, JLabel.CENTER));            
        }
        
        //Création dynamique de tous les boutons
        addToggleButtons();
    }

    @Override
    public void submitAnswers() {
        m_controller.submitWord();
    }

    @Override
    protected void setAnswerOfButton(int posX, int posY) {
       m_controller.setWordAnswer(posX, posY, this.m_buttons[posX][posY].getText());
    }

    @Override
    protected ArrayList<JToggleButton>[] generatePossibleAnswers() {
        ArrayList<String> filenames = FolderReader.getFilenamesWithWordCount(Global.VIDEO_FOLDER_PATH, Global.SUPPORTED_VIDEO_FORMAT, Experimentation.getInstance().getWordsPerTrial());
        ArrayList<String> [] words = new ArrayList[Experimentation.getInstance().getWordsPerTrial()];
        for(int i = 0; i < words.length; i++){
            words[i] = new ArrayList<String>();
        }

        for(String filename: filenames){
            String [] wordsTmp = filename.split("_");
            //Remove to extention of the file in the last word
            String lastWord = wordsTmp[wordsTmp.length - 1];
            lastWord = lastWord.substring(0, lastWord.lastIndexOf("."));
            wordsTmp[wordsTmp.length - 1] = lastWord;
            for(int i = 0; i < wordsTmp.length; i++){
                if(!words[i].contains(wordsTmp[i]) && !m_answers.contains(wordsTmp[i])){
                    words[i].add(wordsTmp[i]);
                }
            }
        }
        Random rand = new Random(0);
        ArrayList<JToggleButton>[] possibleAnswers = new ArrayList[Experimentation.getInstance().getWordsPerTrial()];
        for(int i = 0; i < possibleAnswers.length; i++){
            possibleAnswers[i] = new ArrayList<>(m_answerChoices - 1);
        }
        
        for(int i = 0; i < possibleAnswers.length; i++){
            for(int j = 0; j < m_answerChoices - 1; j++){
                int randomIndex = rand.nextInt(words[i].size());
                 String answer = words[i].get(randomIndex);
                 words[i].remove(randomIndex);
                 possibleAnswers[i].add(new JToggleButton(answer));
            }
        }
        return possibleAnswers;
    }
}
