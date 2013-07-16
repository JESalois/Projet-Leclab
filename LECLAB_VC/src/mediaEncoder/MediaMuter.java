package mediaEncoder;
import com.xuggle.mediatool.*;
import com.xuggle.mediatool.event.*;
import com.xuggle.xuggler.*;
import java.io.File;

/**
 * Class used to encode video and audio muting one of their stereo channel. Does
 * not modify the video stream itself, but mute an audio channel of the 
 * audio stream. Will force the audio to be stereo and mute one of the channels.
 * 
 * Certain problems are known when the output file is a AVI container. You can
 * use MP4 container for output file to ensure good results.
 * @author Jean-Étienne Salois
 */
public class MediaMuter  extends MediaToolAdapter implements Runnable {
    
    /**
     * Media writer in charge of encoding creating the output video file.
     */
    private IMediaWriter m_writer;
    
    /**
     * Media reader in charge of decoding input video file.
     */
    private IMediaReader m_reader;
    
    /**
     * Output video file
     */
    private File m_outputFile;
    
    /**
     * Audio resampler use to force the audio to be stereo (2 audio channels)
     */
    private IAudioResampler m_audioResampler = null;
    
    /**
     * Channel to mute. Left if 0, right is 1
     */
    private int m_channelToMute;
    
    /**
     * Constructor
     * @param inputFile
     *          media file to treat
     * @param outputFile 
     *          treated media file
     */
    public MediaMuter (File inputFile, File outputFile, StereoChannel channelToMute) {
        m_outputFile = outputFile;
        //Determining wich channel to mute
        if(channelToMute == StereoChannel.LEFT_CHANNEL){
            m_channelToMute = 0;
        }
        else{
            m_channelToMute = 1;
        }            
        m_reader = ToolFactory.makeReader(inputFile.getAbsolutePath());
        //Insert the encoder in the pipeline
        m_reader.addListener(this);
    }
    
    /**
     * Function called when streams are added, force added audio stream to be
     * stereo but leaves video unchanged.
     * @param event 
     */
    @Override
    public void onAddStream(IAddStreamEvent event) {
        //Index of the added stream
        int streamIndex = event.getStreamIndex();
        //Stream code for that stream, represent the decoding and encoding way
        //to process the stream
        IStreamCoder streamCoder = event.getSource().getContainer().getStream(streamIndex).getStreamCoder();
        //Force a stereo encoding for audio streams
        if (streamCoder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
            m_writer.addAudioStream(streamIndex, streamIndex, 2, 44100);
        }
        //Keep video streams setting
        else if (streamCoder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
            streamCoder.setWidth(streamCoder.getWidth());
            streamCoder.setHeight(streamCoder.getHeight());
            m_writer.addVideoStream(streamIndex, streamIndex, streamCoder.getWidth(), streamCoder.getHeight());
        }
        //Dispatch the event
        super.onAddStream(event);
    }
    
    /**
     * Function called when audio samples are read.
     * @param event 
     */
    @Override
    public void onAudioSamples(IAudioSamplesEvent event) {
        //The samples
        IAudioSamples samples = event.getAudioSamples();
        //Set the resampler if it is not already existing, for stereo sampling
        if (m_audioResampler == null) {
            m_audioResampler = IAudioResampler.make(2, samples.getChannels(), 44100, samples.getSampleRate());
        }
        //If there are samples to process
        if (event.getAudioSamples().getNumSamples() > 0) {
            //Resample the samples to make then stereo
            IAudioSamples out = IAudioSamples.make(samples.getNumSamples(), samples.getChannels());
            m_audioResampler.resample(out, samples, samples.getNumSamples());

            //Mute left channel of the stereo samples
            for(int i=0; i<out.getNumSamples(); i++){               
                out.setSample(i, m_channelToMute, out.getFormat(), 0);
            }
            
            //Dispatch the event
            AudioSamplesEvent asc = new AudioSamplesEvent(event.getSource(), out, event.getStreamIndex());
            super.onAudioSamples(asc);
            out.delete();
        }
    }
    
    /**
     * Decode and re-encode the input media file 
     */
    @Override
    public void run() {
        //Media writer encoding the output file
        m_writer = ToolFactory.makeWriter(m_outputFile.getAbsolutePath(), m_reader);
        //Insert the writer as next step in the pipeline
        this.addListener(m_writer);
        
        //Process media and ignore exeption
        Boolean keepOn=true;
        while(keepOn){				
            try{
                if(m_reader.readPacket()!=null){
                    keepOn=false;
                }
            }
            catch(Exception e){
                System.out.printf(e.getMessage());
            }
        }
    }
}