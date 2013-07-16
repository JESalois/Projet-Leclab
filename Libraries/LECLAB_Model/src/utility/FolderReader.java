package utility;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class encapsulates the methods to interacts with some folders
 * 
 * @author Jean-Etienne Salois
 */
public class FolderReader {
    /**
     * Return the list of filename thats are of supported formats and 
     * have the correct amount of words.
     * @param folder 
     *          the folder to check
     * @param wordcount 
     *          the number of word the file must have
     * @param supportedFormats 
     *          an array containing the format that will be checked
     * @return the list of filename matching the wordcount and the supported formats
     */
    public static ArrayList<String> getFilenamesWithWordCount(String folderPath, String [] supportedFormats, int wordcount){
         ArrayList<String> results = FolderReader.getFilenamesOfSupportedFormats(folderPath, supportedFormats);
         ArrayList<String> resultsToRemove = new ArrayList<String>();
         for(String result:results){
             if(FolderReader.countOccurencesOf(result, "_") != wordcount - 1){
                resultsToRemove.add(result);
             }
         }
         results.removeAll(resultsToRemove);
         return results;
    }
    
    /**
     * Returns a list of specified format within a folder
     * @param folderPath 
     *          the path to the folder containing the files
     * @param supportedFormats 
     *          An array containing all the supported format
     * @return 
     */
    public static ArrayList<String> getFilenamesOfSupportedFormats(String folderPath, String [] supportedFormats){
        String [] filenames = new File(folderPath).list();
        ArrayList<String> result = new ArrayList<String>();
        if(filenames!=null){
            for(String filename: filenames){
                int index = filename.lastIndexOf(".");
                if(index != -1){
                    String extension = filename.substring(index);            
                    if(Arrays.asList(supportedFormats).contains(extension)){
                        result.add(filename);
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * Return the number of occurrence of a substring in a string
     * @param string 
     *          the string in which the search in done
     * @param substring 
     *          the substring to count the number of occurrence
     * @return the number of occurrence
     */
    private static int countOccurencesOf(String string, String substring){
        int lastIndex = 0;
        int count =0;
        while(lastIndex != -1){
            lastIndex = string.indexOf(substring,lastIndex);
            if(lastIndex != -1){
                count ++;
                lastIndex += substring.length();
            }
        }
        return count;
    }
}
