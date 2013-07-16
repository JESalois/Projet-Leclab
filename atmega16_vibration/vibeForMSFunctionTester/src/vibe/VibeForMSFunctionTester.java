package vibe;

import java.io.*;

/**
 * 
 * @author tb
 * This class checks the only argument, the lenght in MS to run the vibration.
 * and calls the C program accessible by command-line
 * 
 * The only important function here is vibeForMS(), but the inner class StreamGobbler is crucial to 
 * the vibeForMS endeavor
 * 
 * This code is an adaption for the project use of this tutorial
 * http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html?page=1
 *
 */
public class VibeForMSFunctionTester
{
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	//http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html?page=1
	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		VibeForMSFunctionTester vibeForMSFunctionTester = new VibeForMSFunctionTester();
		vibeForMSFunctionTester.vibeForMS(5000);
	}
	
	/**
	 * The name of the C program to run
	 */
	final String COMMAND_NAME = "boiteVibrationUSB";
	
	/**
	 * 
	 * @author tb
	 *
	 * The StreamGobbler
	 */
	class StreamGobbler extends Thread
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
	 * This is the constructor for the tester of the class
	 * It will become useless when integrated with the main project
	 */
	public VibeForMSFunctionTester() {
		
	}
	
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
//        else if ( osName.equals("Windows 7") || osName.equals("Windows XP")) {
//        	String[] cmd = new String[2];
//            cmd[0] = COMMAND_NAME+".exe";
//    		cmd[1] = String.valueOf(ms);
//    		
//    		//System.out.println("Execing " + cmd[0] + " " + cmd[1]);
//    		
//        	proc = rt.exec(cmd);
//        	
//        }
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
