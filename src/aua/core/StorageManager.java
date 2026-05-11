package aua.core;

import aua.utils.FileUtil;

import java.io.FileNotFoundException;
import java.io.IOException;

public class StorageManager {
    static final String defaultPath = "/Users/surenpoghosyan/CS120-group-project/src/data.txt";

    public static String getDefaultPath(){
        return defaultPath;
    }

    public String[] load() throws IOException {
        return FileUtil.loadStringsFromFile(defaultPath);
    }

    public void save(String[] content) throws FileNotFoundException, IOException {
        FileUtil.saveStringsToFile(content, defaultPath);
    }
}

