package com.pierprogramm.automaticDeployFtp.compress;

import org.rauschig.jarchivelib.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Compressor {

    private static final String DEFAULT_ARCHIVE_TYPE = "tar";

    public Compressor(String fileArchive, String folderDestination, String archiveType) {
        init(fileArchive, folderDestination, archiveType);
    }

    public Compressor(String folderPathOrigin, String folderPathDestination) {
        init(folderPathOrigin, folderPathDestination, DEFAULT_ARCHIVE_TYPE);
    }

    public Compressor(CompressorConfig config) {
        init(config);
    }

    public Compressor() {

    }

    private void init(String folderPathOrigin, String folderPathDestination, String archiveType) {
        archive = new File(folderPathOrigin);
        destination = new File(folderPathDestination);
        this.archiveType = archiveType;
    }

    private void init(CompressorConfig config) {
        this.config = config;
        this.setDefaultConfiguration(config);
        archive = new File(config.folderPathOrigin + "/" + config.archiveFileName);
        destination = new File(config.folderPathDestination + "/" + config.archiveFileName);
        compressDestinationFolder = new File(config.folderPathDestination);
        this.archiveType = config.archiveType;
        this.archiveFileName = config.archiveFileName;
        for (String fileOrFolderPath : config.fileAndFolderPaths) {
            this.fileAndFolderPaths.add(new File(fileOrFolderPath));
        }
    }

    private void setDefaultConfiguration(CompressorConfig config) {
        if (config.archiveType == null || config.archiveType.isEmpty()) {
            config.archiveType = DEFAULT_ARCHIVE_TYPE;
        }
        if (config.fileAndFolderPaths == null) {
            config.fileAndFolderPaths = new ArrayList<String>();
        }
    }

    private File archive;
    private File destination;
    private File compressDestinationFolder;
    private String archiveFileName;
    private String archiveType;
    private List<File> fileAndFolderPaths = new ArrayList<File>();
    private CompressorConfig config;


    public void extract() {
        try {
            Archiver archiver = ArchiverFactory.createArchiver(archiveType);
            archiver.extract(archive, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void compress() {
        //try {
//            Archiver archiver = ArchiverFactory.createArchiver(archiveType);
//            archiver.create(archiveFileName, compressDestinationFolder,
//                    fileAndFolderPaths.toArray(new File[fileAndFolderPaths.size()]));
        CommonsArchiver archiver = (CommonsArchiver) ArchiverFactory.createArchiver(archiveType);
        ArchiverCustomPathDecorator decorator = new ArchiverCustomPathDecorator(archiver);
//        archiver.createWithCustomPaths(archiveFileName, compressDestinationFolder,
//                fileAndFolderPaths.toArray(new File[fileAndFolderPaths.size()]));
        decorator.create(archiveFileName, compressDestinationFolder,
                fileAndFolderPaths.toArray(new File[fileAndFolderPaths.size()]));


//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public CompressorConfig getConfig() {
        CompressorConfig config = null;
        try {
            config = (CompressorConfig) this.config.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return config;
    }

}
