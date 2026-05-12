package aua.core;

import aua.utils.FileUtil;

import java.io.FileNotFoundException;
import java.io.IOException;

public class StorageManager {
    static final String defaultPath = "data.txt";

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

