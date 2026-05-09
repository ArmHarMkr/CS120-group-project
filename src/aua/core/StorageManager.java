package aua.Core;

import aua.Utils.FileUtil;

import java.io.FileNotFoundException;
import java.io.IOException;

public class StorageManager {
    private static final String defaultPath = "/Users/surenpoghosyan/CS120-group-project/src/data.txt";
    private final String path;

    public StorageManager(){
        this.path = defaultPath;
    }

    public StorageManager(String path){
        this.path = path;
    }

    public String getPath(){
        return this.path;
    }

    public String[] load() throws IOException {
        return FileUtil.loadStringsFromFile(this.path);
    }

    public void save(String[] content) throws FileNotFoundException, IOException {
        FileUtil.saveStringsToFile(content, this.path);
    }
}
