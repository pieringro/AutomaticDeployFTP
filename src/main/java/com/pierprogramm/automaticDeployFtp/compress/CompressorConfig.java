package com.pierprogramm.automaticDeployFtp.compress;

import java.util.List;

public class CompressorConfig implements Cloneable{
    public CompressorConfig(){

    }

    public String archiveFileName;
    public String folderPathOrigin;
    public String folderPathDestination;
    public String archiveType;
    public List<String> fileAndFolderPaths;

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
