package aua.Utils;

public class StringUtil {
    public static final String defaultDelimiter = "%%";
    public static final String separator = "-";

    public static String[] parseDelimitedString(String value){
        return parseDelimitedString(value, defaultDelimiter);
    }

    public static String[] parseDelimitedString(String value, String delimiter){
        String[] reconstructedData = new String[Short.MAX_VALUE];
        int reconstructedDataIndex = 0;

        int prevIndex = 0;
        int nextIndex = value.indexOf(delimiter);

        while(nextIndex!=-1){
            reconstructedData[reconstructedDataIndex++] = value.substring(prevIndex, nextIndex);
            value = value.substring(nextIndex+delimiter.length());
            nextIndex = value.indexOf(delimiter);

            if(nextIndex == -1){
                reconstructedData[reconstructedDataIndex++] = value;
            }
        }

        String[] distilledReconstructedDataArray = new String[reconstructedDataIndex];
        for(int i = 0; i<distilledReconstructedDataArray.length; i++){
            distilledReconstructedDataArray[i] = reconstructedData[i];
        }


        return distilledReconstructedDataArray;
    }
}
