package mediaEncoder;


import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * This class contains static function use to treat media files
 * @author Jean-Étienne
 */
public class TreatMediaFiles{

    public TreatMediaFiles() {}   
    
    /**
     * Checks if there a new video or audio to treat. If so, video are transcoded
     * to mp4 containers and the left channel is muted. Audio file have their right 
     * channel muted.
     */
    static public void treatFiles() {
        
        ArrayList<File> videoFilesToEncode = new ArrayList<>();
        ArrayList<File> audioFilesToEncode = new ArrayList<>();
        
        //Search folders for video files not already treated and encode them
        if(((new File("../Resources/Video")).listFiles())==null){
                JOptionPane.showMessageDialog(null, "Erreur lors du traitment des fichiers multimedia, y-a-t'il des fichiers videos dans le dossier Ressources/Video/ ?\n"
                , "Error", JOptionPane.ERROR_MESSAGE);
        }
        else{
            for(File videoFile: (new File("../Resources/Video")).listFiles()){
                if(!videoFile.isDirectory())
                {
                    String fileNameWithoutExtension = videoFile.getName().substring(0,videoFile.getName().lastIndexOf('.'));
                    Boolean alreadyTreated = false;
                    if((new File("../Resources/Video/Treated").listFiles())!=null){
                        for(File treatedVideoFile: (new File("../Resources/Video/Treated").listFiles())){
                            if((fileNameWithoutExtension+".mp4").equals(treatedVideoFile.getName())){
                                alreadyTreated = true;
                                break;
                            }
                        }
                    }
                    if(!alreadyTreated){
                         videoFilesToEncode.add(videoFile);
                    }                    
                }
            }
        }
        
        
        //Search folers for audio files not already treated and encode them
        if(((new File("../Resources/Bruit")).listFiles())==null){
                JOptionPane.showMessageDialog(null, "Erreur lors du traitment des fichiers multimedia, y-a-t'il des fichiers videos dans le dossier Ressources/Videos/ ?\n"
                , "Error", JOptionPane.ERROR_MESSAGE);
        }
        else{
            for(File audioFile: (new File("../Resources/Bruit")).listFiles()){
                if(!audioFile.isDirectory())
                {
                    Boolean alreadyTreated = false;
                    if((new File("../Resources/Bruit/Treated").listFiles())!=null){
                        for(File treatedAudioFile: (new File("../Resources/Bruit/Treated").listFiles())){
                            if((audioFile.getName()).equals(treatedAudioFile.getName())){
                                alreadyTreated = true;
                                break;
                            }
                        }
                    }
                    if(!alreadyTreated){
                         audioFilesToEncode.add(audioFile);
                    }
                }
            }
        }
        //If there are files to encode
        if(!(videoFilesToEncode.isEmpty()&&audioFilesToEncode.isEmpty())){
            
            /*JFrame frame = new JFrame("Traitement de"+(videoFilesToEncode.size()+audioFilesToEncode.size())+"fichier(s) multimedia");
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.getContentPane().add(new JLabel("Traitement du fichier:"), BorderLayout.CENTER);
            JLabel fileTreated = new JLabel();
            frame.getContentPane().add(fileTreated);
            frame.pack();
            frame.setVisible(true);*/
            
            for(File videoFile: videoFilesToEncode){
                //fileTreated.setText(videoFile.getName());
                encodeVideo(videoFile);
            }
            for(File audioFile: audioFilesToEncode){
                //fileTreated.setText(audioFile.getName());
                encodeAudio(audioFile);
            }
        }
        
    }
    
    /**
     * Encode the passed video file
     * @param inputVideoFile 
     *          video file to treat
     */
    static private void encodeVideo(File inputVideoFile){
        String fileNameWithoutExtension = inputVideoFile.getName().substring(0,inputVideoFile.getName().lastIndexOf('.'));
        String extension = inputVideoFile.getName().substring(inputVideoFile.getName().lastIndexOf('.'),inputVideoFile.getName().length());
        if(extension.equals(".avi") || extension.equals(".mp4") ){
            //Force the output video file to be in a MP4 file because there are known issues with other container type
            File outputFile =  new File("../Resources/Video/Treated/"+fileNameWithoutExtension+".mp4");
            MediaMuter encoder = new MediaMuter(inputVideoFile, outputFile, StereoChannel.LEFT_CHANNEL);
            encoder.run();
        }
    }
    
    /**
     * Encode the passed audio file
     * @param inputAudioFile
     *          audio file to treat
     */
    static private void encodeAudio(File inputAudioFile){
        
        File outputFile = new File("../Resources/Bruit/Treated/"+inputAudioFile.getName());
        MediaMuter encoder = new MediaMuter(inputAudioFile, outputFile, StereoChannel.RIGHT_CHANNEL);
        encoder.run();
    }    
}
