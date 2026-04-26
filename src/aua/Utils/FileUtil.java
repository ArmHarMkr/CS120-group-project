package aua.Utils;
import java.io.*;

public class FileUtil {
    public static void saveStringToFile(String[] content, String path) throws FileNotFoundException, IOException {
        PrintWriter outputStream = null;

        outputStream = new PrintWriter(new FileOutputStream(path));

        for(int i = 0; i<content.length; i++){
            outputStream.println(content[i]);
        }

        outputStream.close();


        System.out.println("All contents were written to the file.");
    }

    public static String[] loadStringsFromFile(String path) throws FileNotFoundException,IOException {
        // Partially filled array
        String[] loadedData = new String[Short.MAX_VALUE];
        int lineIndex = 0;

        BufferedReader inputStream = new BufferedReader(new FileReader(path));

        String line = inputStream.readLine();
        while(line != null){
            loadedData[lineIndex++] = line;
            line = inputStream.readLine();
        }

        inputStream.close();

        // replacing partially filled array with an array of an appropriate size
        String[] dataWithAppropriateArrayLength = new String[lineIndex];
        for(int i = 0; i<dataWithAppropriateArrayLength.length; i++){
            dataWithAppropriateArrayLength[i] = loadedData[i];
        }

        return dataWithAppropriateArrayLength;
    }
}
