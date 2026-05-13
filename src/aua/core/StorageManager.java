package aua.core;

import aua.utils.FileUtil;

import java.io.FileNotFoundException;
import java.io.IOException;

public class StorageManager {
//    https://stackoverflow.com/a/4362858
    static final String defaultPath = System.getProperty("user.dir")+"/src/aua/data.txt";

    /**
     *
     * @return
     */
    public static String getDefaultPath(){
        return defaultPath;
    }

    /**
     *
     * @return
     * @throws IOException
     */
    public String[] load() throws IOException {
        return FileUtil.loadStringsFromFile(defaultPath);
    }

    /**
     *
     * @param content
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void save(String[] content) throws FileNotFoundException, IOException {
        FileUtil.saveStringsToFile(content, defaultPath);
    }
}

