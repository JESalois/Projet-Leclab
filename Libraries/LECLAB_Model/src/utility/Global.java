package utility;

/**
 * This class contains various path used in the application
 * @author Philippe
 */
public class Global {
    public static final String RESSOURCE_PATH = System.getProperty("user.dir").substring(0, System.getProperty("user.dir").lastIndexOf("\\")) + "\\Resources\\";
    public static final String VIDEO_FOLDER_PATH = Global.RESSOURCE_PATH + "Video\\Treated\\";
    public static final String NOISE_FOLDER_PATH = Global.RESSOURCE_PATH + "Bruit\\Treated\\";
    public static final String PARAMETERS_FOLDER_PATH = Global.RESSOURCE_PATH + "Parameters\\";
    public static final String VLC_APPLICATION_PATH_32 = Global.RESSOURCE_PATH + "vlc-2.0.5\\";
    public static final String VLC_APPLICATION_PATH_64 = Global.RESSOURCE_PATH + "vlc-2.0.5-64\\";
    public static final String [] SUPPORTED_VIDEO_FORMAT = {".avi", ".mkv", ".mp4"};
    public static final String [] SUPPORT_AUDIO_FORMAT = {".mp3", ".wav"};
}
