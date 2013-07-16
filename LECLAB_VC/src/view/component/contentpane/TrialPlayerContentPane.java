package view.component.contentpane;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import contoller.TrialPlayerController;
import java.awt.BorderLayout;
import sitting.SittingManager;
import sitting.SittingState;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import utility.Global;

/**
 *
 * @author Philippe
 */
public class TrialPlayerContentPane extends AbstractContentPane{
    
    private EmbeddedMediaPlayerComponent m_EmbeddedMediaPlayer;
    
    private TrialPlayerController m_controller;
    
    /**
     * Creates new form TrialPlayerContentPane
     */
    public TrialPlayerContentPane(SittingManager sittingManager) {
        super(sittingManager);
        initComponents();
        
        m_controller = new TrialPlayerController(sittingManager);
        
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), Global.VLC_APPLICATION_PATH_64);
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
        
        this.setLayout(new BorderLayout(100, 100));
        
        m_EmbeddedMediaPlayer = new EmbeddedMediaPlayerComponent();
        m_EmbeddedMediaPlayer.getMediaPlayer().addMediaPlayerEventListener(m_controller);
        this.add(m_EmbeddedMediaPlayer, BorderLayout.CENTER);   
    }
    
    public void play(){
        int currentBlock = this.getSittingManager().getCurrentBlock();
        int currentTrial = this.getSittingManager().getCurrentTrial();
        String mediaFileName = this.getSittingManager().getTrueTrial(currentBlock, currentTrial).getVideoFile().toString();
        m_EmbeddedMediaPlayer.getMediaPlayer().playMedia(mediaFileName);
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

    @Override
    public void handleStateChange() {
        this.play();
    }
   }