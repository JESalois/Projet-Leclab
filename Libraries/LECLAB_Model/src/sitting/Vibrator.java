/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sitting;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import parameters.TactileDistractorParameter;

/**
 * Insert Description of class
 * @author Philippe
 */
public class Vibrator {
    
    private String [] m_orderOfPlay;
    
    private SittingManager m_sittingManager;
    
    private StreamGobbler m_vibrator;
    
    private Timer m_vibrationTimer;
    
    private Timer m_downTimeTimer;
    
    private int m_numberOfVibrationsDone;

    public Vibrator(SittingManager sittingManager){
        m_sittingManager = sittingManager;
        TactileDistractorParameter tdp = (TactileDistractorParameter)m_sittingManager.getTrueBlock(m_sittingManager.getCurrentBlock()).getParameters().get(TactileDistractorParameter.class);
        m_orderOfPlay = (String []) tdp.orderOfPlay(m_sittingManager.getCurrentSeed());
        m_downTimeTimer = new Timer(tdp.getIntervalDuration(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downTimeTimerEnded();
            }
        });
        
        m_vibrationTimer = new Timer(tdp.getShortVibrationDuration(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vibrationTimerEnded();
            }
        });
        m_numberOfVibrationsDone = 0;
    }
    
    private void downTimeTimerEnded(){
        m_downTimeTimer.stop();
        vibrate();
    }
    
    private void vibrationTimerEnded(){
        m_vibrationTimer.stop();
        m_numberOfVibrationsDone++;
        if(m_numberOfVibrationsDone < m_orderOfPlay.length){
            System.out.println("SLEEP FOR: " + m_downTimeTimer.getDelay());
            m_downTimeTimer.start();
        }
        else{
            m_sittingManager.TactileDistractionFinished();
        }
    }
    
    public void vibrate(){
        setVibrationTimer();
        try {
            vibeForMS(m_vibrationTimer.getDelay());
        } catch (InterruptedException ex) {
            Logger.getLogger(Vibrator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Vibrator.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("VIBRATE FOR: " + m_vibrationTimer.getDelay());
        m_vibrationTimer.start();
    }
    
    private void setVibrationTimer(){
        TactileDistractorParameter tdp = (TactileDistractorParameter)m_sittingManager.getTrueBlock(m_sittingManager.getCurrentBlock()).getParameters().get(TactileDistractorParameter.class);
        if(m_orderOfPlay[m_numberOfVibrationsDone].equals("C")){
            m_vibrationTimer.setDelay(tdp.getShortVibrationDuration());
        }
        else{
            m_vibrationTimer.setDelay(tdp.getLongVibrationDuration());
        }
    }
    
    
        /**
	 * The name of the C program to run
	 */
	final String COMMAND_NAME = "boiteVibrationUSB";


	/**
	 * 
	 * @author Philippe de Sève
	 *
	 * The StreamGobbler
	 */
	private class StreamGobbler extends Thread
	{
	    InputStream is;
	    String type;
	    
	    StreamGobbler(InputStream is, String type)
	    {
	        this.is = is;
	        this.type = type;
	    }
	    
	    public void run()
	    {
	        try
	        {
	            InputStreamReader isr = new InputStreamReader(is);
	            BufferedReader br = new BufferedReader(isr);
	            String line=null;
	            while ( (line = br.readLine()) != null) {
	            	if (this.type == "ERROR" && line != "") {
            			throw new Error("erreur dans le programme " + COMMAND_NAME + ":" + line);
	            	}
	            	else {
	            		System.out.println(type + ">" + line);	            		
	            	}
	            }
            } catch (IOException ioe) {
                ioe.printStackTrace();  
            }
	    }
	}
	
	/**
	 * TOP is the max possible number of milliseconds the box is designed to run. It is much more than
	 * the typical use of 100ms
	 * 
	 * If TOP is not used in the function @see vibeForMS , the C program boiteVibrationUSB will exit with an error.
	 * If the boiteVibrationUSB did not check this, the program running on the robot would have an uint16_t overflow.
	 * leading to a pretty much random length for the vibration.
	 * 
	 */
	final int TOP = 65535;
	
	/**
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws throw new Error("erreur dans le programme " + COMMAND_NAME);
	 * @throws new Error When ms is greater than maximum value: 65535
	 * 
	 * There is a lot of Error thrown around. I do not know the desired behaviour for unexpected failure.
	 * An error in this module should not be quietly ignored, as this will directly lead error in experimental protocol
	 * 
	 * @param ms A value of 0 is useless, because the vibration will run for 0 ms, and the module already does that at rest.
	 *           A value of 100 ms is typical for past use cases.
	 *           The maximum value is 65535 ( just to avoid overflows)
	 *           
	 */
	public void vibeForMS( int ms) throws InterruptedException, IOException {

		//final unsigned int TOP = 65535;
		if (ms > TOP) {
			System.out.println("ms greater than " + String.valueOf(TOP) + ": " + String.valueOf(ms));
			throw new Error();
		}
            
        String osName = System.getProperty("os.name" );

        //initializing
       
        //cmd[2] = "";
        Runtime rt = Runtime.getRuntime();
        Process proc;
        
        if( osName.equals( "Windows NT" ) )
        {
        	String[] cmd = new String[4];
        	cmd[0] = "cmd.exe" ;
            cmd[1] = "/C" ;
            cmd[2] = COMMAND_NAME;
    		cmd[3] = String.valueOf(ms);
    		
    		//System.out.println("Execing " + cmd[0] + " " + cmd[1]+ " " + cmd[2] + " " + cmd[3]);
    		
            proc = rt.exec(cmd);
        }
        else if( osName.equals( "Windows 95" ) )
        {
        	String[] cmd = new String[4];
        	cmd[0] = "command.com" ;
            cmd[1] = "/C" ;
            cmd[2] = COMMAND_NAME;
    		cmd[3] = String.valueOf(ms);
    		
    		//System.out.println("Execing " + cmd[0] + " " + cmd[1]+ " " + cmd[2] + " " + cmd[3]);
    		
            proc = rt.exec(cmd);
        }
        else {
        	//System.out.println("system is not Windows NT nor Windows 95");
        	String[] cmd = new String[2];
            cmd[0] = COMMAND_NAME;
    		cmd[1] = String.valueOf(ms);
    		
    		//System.out.println("Execing " + cmd[0] + " " + cmd[1]);
    		
        	proc = rt.exec(cmd);
        }
            
        // any error message?
        StreamGobbler errorGobbler = new 
            StreamGobbler(proc.getErrorStream(), "ERROR");            
        
        // any output?
        StreamGobbler outputGobbler = new 
            StreamGobbler(proc.getInputStream(), "OUTPUT");
            
        // kick them off
        errorGobbler.start();
        outputGobbler.start();
                                
        // any error???
        int exitVal = proc.waitFor();
        if ( exitVal != 0) {
        	throw new Error("program " + COMMAND_NAME + " exited with error code: " + String.valueOf(exitVal));
        }
    }
}
