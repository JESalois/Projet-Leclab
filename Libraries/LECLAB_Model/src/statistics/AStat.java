package statistics;
import sitting.BlockResult;
import sitting.TrialResult;
import java.io.*;
import parameters.TactileDistractorParameter;
import parameters.VisualDistractorParameter;
import sitting.SittingManager;
import sitting.WordResult;
import sitting.DistractorResult;


/**
 *
 * @author Philippe de Sève
 */
public class AStat {
    
        /**
     * The reference kept to the instance of the singleton
     */
    private static AStat m_instance = null;
    private static double m_tempsDeReponse[];
    private static BlockResult m_block;
    private int m_nbrTrials;
    private int m_nbrMots;

    private float m_moyenneTempsDeReponse;
    private float m_ecartTypeTempsDeReponse;
    private int[] m_bonnesReponses;
    private double[] m_moyennes;
    private SittingManager m_sittingManager;
    
    public static AStat getInstance()
    {
        if (AStat.m_instance == null){
            AStat.m_instance = new AStat();
        }
        return AStat.m_instance;
    }
        
    private void calculTempsDeReponse(){
        
       
        int nbrMots = m_block.getWordsPerTrial();
        
        double rep;
        double time;
        
        //temps de réponse chaque essai
        for (int i = 0; i<m_nbrTrials; i++){
            
            rep = 0;
            
            for (int j = 0; j<nbrMots; j++){
                WordResult result = m_block.getTrialResult(i).getWordAnswer(j);
                time = m_block.getTrialResult(i).getWordAnswer(j).getAnswerTime();
                if (rep < time){
                    rep = time;
                } 
            }
            
            m_tempsDeReponse[i] = rep;
        }        
        
        //moyenne des temps de réponses
        m_moyenneTempsDeReponse=0;
        for (int i = 0; i<m_nbrTrials; i++){
            m_moyenneTempsDeReponse+=m_tempsDeReponse[i];     
        }
        m_moyenneTempsDeReponse=m_moyenneTempsDeReponse/(m_nbrTrials+1);
        
        //écart type
        m_ecartTypeTempsDeReponse=0;
        for (int i = 0; i<m_nbrTrials; i++){
            m_ecartTypeTempsDeReponse+=Math.abs(m_tempsDeReponse[i]-m_moyenneTempsDeReponse);     
        }
        m_ecartTypeTempsDeReponse=m_ecartTypeTempsDeReponse/(m_nbrTrials+1);
        
        
    }
    
    private void CalculMoyenne(){
        m_bonnesReponses = new int[m_nbrMots];
        m_moyennes = new double[m_nbrMots];
        
        for (int j=0; j< m_nbrMots; j++){
            m_bonnesReponses[j] = 0;
        }
        for (int i = 0; i < m_nbrTrials;i++){
            for (int j=0; j< m_nbrMots; j++){
                if (m_block.getTrialResult(i).getWordAnswer(j).isRightAnswer())
                    m_bonnesReponses[j]++;
            }
        }
        
        for (int j=0; j< m_nbrMots; j++){
            m_moyennes[j] = (double)m_bonnesReponses[j]/(double)m_nbrTrials*100;
        }
    }
    
    public double getCurrentWordAverage(BlockResult block, int num ){
        
        
        
        double moyenne;
        int bonnesReponses = 0;

        for (int i = 0; i < num;i++){
            for (int j=0; j< block.getWordsPerTrial(); j++){
                if (block.getTrialResult(i).getWordAnswer(j).isRightAnswer())
                    bonnesReponses++;
            }
        }
        
        
        moyenne = (double)bonnesReponses/((double)num*(double)block.getWordsPerTrial())*100;
        return moyenne;
        
    }
    
    public double getCurrentTactileAverage(BlockResult block, int num){
        
        
        double moyenne;
        int bonnesReponses = 0;

        for (int i = 0; i < num;i++){
            
                if (block.getTrialResult(i).getTactileResult().isRightAnswer())
                    bonnesReponses++;
            
        }
        
        
        moyenne = (double)bonnesReponses/(double)num*100;
        return moyenne;
        
    }
    
    public double getCurrentVisualAverage(BlockResult block, int num){
        
        double moyenne;
        int bonnesReponses = 0;

        for (int i = 0; i < num;i++){
            
            if (block.getTrialResult(i).getVisualResult().isRightAnswer())
                bonnesReponses++;
            //if (block.getTrialResult(i).getVisualResult(1).getIsRightAnswer())
                    //bonnesReponses++;
        }
        
        
        moyenne = (double)bonnesReponses/((double)num)*100;
        return moyenne;

    }
    public double getCurrentWordAverageTime(BlockResult block, int num ){
        
        
        double moyenne;
        double tempsTotal = 0;

        for (int i = 0; i < num;i++){
            for (int j=0; j< block.getWordsPerTrial(); j++){
                tempsTotal+=block.getTrialResult(i).getWordAnswer(j).getAnswerTime();
            }
        }
        
        
        moyenne = tempsTotal/((double)num*(double)block.getWordsPerTrial());
        return moyenne;
        
    }
     
    public double getCurrentTactileAverageTime(BlockResult block, int num){
        
        
        double moyenne;
        double tempsTotal = 0;

        for (int i = 0; i < num;i++){
            
                tempsTotal+=block.getTrialResult(i).getTactileResult().getAnswerTime();
            
        }
        
        
        moyenne = (double)tempsTotal/(double)num;
        return moyenne;
        
    }
    
    public double getCurrentVisualAverageTime(BlockResult block, int num){
        
        
        double moyenne;
        double tempsTotal = 0;

        for (int i = 0; i < num;i++){
            tempsTotal+=block.getTrialResult(i).getVisualResult().getAnswerTime();
            //tempsTotal+=block.getTrialResult(i).getVisualResult(1).getAnswerTime();
        }
        
        
        moyenne = (double)tempsTotal/((double)num*2);
        return moyenne;

    }
    
    private void saveStats(){
        try{
            FileWriter fstream = new FileWriter(m_block.getName()+".csv");
            BufferedWriter out = new BufferedWriter(fstream);
           
            //En tête
            out.write("Compteur,ID,,");
            for (int i = 1; i<= m_nbrMots; i++){
                out.write("Mot "+i+",");
            }
            out.write(",Temps de reponse,,");
            for (int i = 1; i<= m_nbrMots; i++){
                out.write("VRep-Mot"+i+",VTest-Mot"+i+",VTime-Mot"+i+",");
            }
            if (m_sittingManager.getTrueBlock(m_sittingManager.getCurrentBlock()).getParameters().get(TactileDistractorParameter.class)!= null){
                out.write("DStimuli,DTest,DLongeur(s),DTimeRep,");
            }
            if (m_sittingManager.getTrueBlock(m_sittingManager.getCurrentBlock()).getParameters().get(VisualDistractorParameter.class)!= null){
                out.write("DForme,DFormeTest,DCouleur,DCouleurTest,DLongeur(s),DTimeRep,");
            }
            out.write("TempsTotal,");
            out.newLine();
            
            //Données
            TrialResult currentTrial;
            double distTime;
            for (int i =1; i<=m_nbrTrials;i++){
                currentTrial = m_block.getTrialResult(i-1);
                distTime = 0;
                out.write(i+","+currentTrial.getTrialNumber()+",,");
                
                for (int j = 0; j< m_nbrMots; j++){
                    if (currentTrial.getWordAnswer(j).isRightAnswer() == true)
                        out.write("ok,");
                    else
                        out.write("FAUX,");
                }    
                out.write(","+m_tempsDeReponse[i-1]+",,");
                for (int j = 0; j< m_nbrMots; j++){
                    out.write(currentTrial.getWordAnswer(j).getRightAnswer()+",");
                    out.write(currentTrial.getWordAnswer(j).getUserAnswer()+",");
                    out.write(currentTrial.getWordAnswer(j).getAnswerTime()+",");
                }
                
                if (m_sittingManager.getTrueBlock(m_sittingManager.getCurrentBlock()).getParameters().get(TactileDistractorParameter.class)!= null){
                out.write(currentTrial.getTactileResult().getRightAnswer()+",");
                out.write(currentTrial.getTactileResult().getUserAnswer()+",");
                //delai de distracteur
                out.write(currentTrial.getTactileResult().getAnswerTime()+",");
                }
                if (m_sittingManager.getTrueBlock(m_sittingManager.getCurrentBlock()).getParameters().get(VisualDistractorParameter.class)!= null){

                    out.write(currentTrial.getVisualResult().getRightAnswer()+",");
                    out.write(currentTrial.getVisualResult().getUserAnswer()+",");
                    distTime = currentTrial.getVisualResult().getAnswerTime();
                    out.write(distTime+",");
                }
                double totaltime = distTime+m_tempsDeReponse[i-1];
                out.write(totaltime+"");
                out.newLine();
            }
            out.write(",,,,,Moyenne,,"+m_moyenneTempsDeReponse);
            out.newLine();
            out.write(",,,,,Ecart-Type,,"+m_ecartTypeTempsDeReponse);
            out.newLine();out.newLine();out.newLine();
            out.write(",RESULTATS-Mots");
            out.newLine();
            int bonnesReponses = 0;
            int reponsesTotal = m_nbrTrials*m_nbrMots;
            
            for (int i = 1; i <= m_nbrMots; i++){
                out.write(",Nombre de bonnes reponses pour le mot"+i+":,"+m_bonnesReponses[i-1]+"/"+m_nbrTrials+","+m_moyennes[i-1]+"%");
                out.newLine();
                bonnesReponses +=m_bonnesReponses[i-1];
            }
            double moyenneTotal = (double)bonnesReponses/(double)reponsesTotal;
            out.write(",Nombre total de bonnes reponses:," + bonnesReponses +"/"+reponsesTotal+","+moyenneTotal+"%,");
            
            out.close();
            fstream.close();
        }
        catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
        
    }
    
    public void loadBlockResults(BlockResult block,SittingManager sittingManager){
        m_block = block;

        m_sittingManager = sittingManager;
    }
     
    
    
    
    public void calculate(){
        
        m_nbrTrials = m_block.getNumberOfTrials();
        m_nbrMots = m_block.getWordsPerTrial();
        m_tempsDeReponse = new double[ m_nbrTrials];

        
        calculTempsDeReponse();
        CalculMoyenne();
        
        saveStats();
        
    }
    
    /**
     * Default constructor private.
     * Must use getInstance() to create an instance.
     */
    private AStat(){}
}
