package com.pierprogramm.automaticDeployFtp;

import com.pierprogramm.automaticDeployFtp.compress.Compressor;
import com.pierprogramm.automaticDeployFtp.compress.CompressorConfig;
import com.pierprogramm.automaticDeployFtp.config.Configurator;
import com.pierprogramm.automaticDeployFtp.config.ConfiguratorKeys;
import com.pierprogramm.automaticDeployFtp.util.FTPUtil;

import java.io.Console;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static String remoteExistingRootDir;
    private static String localRootDirToUpload;
    private static String localSingleFileNameToUpload;
    private static String localSingleFilePathFolderToUpload;

    private static String archiveLocalFilePathOrigin;
    private static String archiveLocalFolderPathDestination;
    private static String archiveFileName;
    private static String archiveType;

    private static RemoveFiles removeFiles;
    private static UploadFiles uploadFiles;
    private static Compressor compressor;

    private static Configurator configurator;

    public static void main(String[] args) {
        init();

        switch (args.length) {
            case 0:
                handleZeroArgument();
                break;
            case 1:
                handleOneArgument(args[0]);
                break;
            case 2:
                handleTwoArgument(args[0], args[1]);
                break;
            default:
                printHelp();
        }
    }

    private static void printHelp() {
        System.out.println(
                "Usage\n" +
                        " no params : delete and then upload directory configured" +
                        " -d | -D | -delete | -r | -R | -remove : to only delete all directory configured \n" +
                        " -u | -U | -upload | -deploy : to only upload directory configured \n" +
                        " -c | -compress : compress files configured\n" +
                        " (-c | -compress) (-u | -U | -upload | -deploy) : compress files configured and upload generated archive \n" +
                        " (-u | -U | -upload | -deploy) -s : to only upload single file configured \n" +
                        " -set (password | pass) : set password and save it encrypted in config file"
        );
    }

    private static void init() {
        configurator = Configurator.getInstance();
        remoteExistingRootDir = configurator.getProperty(ConfiguratorKeys.REMOTE_ROOT_DIR_TO_UPLOAD);
        localRootDirToUpload = configurator.getProperty(ConfiguratorKeys.LOCAL_ROOT_DIR_TO_UPLOAD);
        localSingleFilePathFolderToUpload = configurator.getProperty(ConfiguratorKeys.LOCAL_SINGLE_FILE_PATH_FOLDER_TO_UPLOAD);
        localSingleFileNameToUpload = configurator.getProperty(ConfiguratorKeys.LOCAL_SINGLE_FILE_NAME_TO_UPLOAD);

        List<String> namesFilesToPreserve = Arrays.asList(
                configurator.getProperty(ConfiguratorKeys.FILES_TO_PRESERVE).split(",")
        );
        List<String> namesDirsToPreserve = Arrays.asList(
                configurator.getProperty(ConfiguratorKeys.DIRS_TO_PRESERVE).split(",")
        );

        List<String> namesFilesToIgnore = Arrays.asList(
                configurator.getProperty(ConfiguratorKeys.FILES_TO_IGNORE).split(",")
        );
        List<String> namesDirsToIgnore = Arrays.asList(
                configurator.getProperty(ConfiguratorKeys.DIRS_TO_IGNORE).split(",")
        );

        removeFiles = new RemoveFiles(namesFilesToPreserve, namesDirsToPreserve);
        uploadFiles = new UploadFiles(namesFilesToIgnore, namesDirsToIgnore);

        archiveLocalFilePathOrigin = configurator.getProperty(ConfiguratorKeys.ARCHIVE_LOCAL_FOLDER_PATH_ORIGIN);
        archiveLocalFolderPathDestination = configurator.getProperty(ConfiguratorKeys.ARCHIVE_LOCAL_FOLDER_PATH_DESTINATION);
        archiveType = configurator.getProperty(ConfiguratorKeys.ARCHIVE_TYPE);
        archiveFileName = configurator.getProperty(ConfiguratorKeys.ARCHIVE_FILE_NAME);

        CompressorConfig compressorConfig = new CompressorConfig();
        compressorConfig.folderPathOrigin = archiveLocalFilePathOrigin;
        compressorConfig.archiveFileName = archiveFileName;
        compressorConfig.folderPathDestination = archiveLocalFolderPathDestination;
        compressorConfig.archiveType = archiveType;
        compressorConfig.fileAndFolderPaths = Arrays.asList(
                configurator.getProperty(ConfiguratorKeys.ARCHIVE_LOCAL_FILES_FOLDERS_PATH_TO_COMPRESS).split(",")
        );
        compressor = new Compressor(compressorConfig);
    }

    private static void handleZeroArgument() {
        System.out.println(String.format("======= Removing files from '%s' ...", remoteExistingRootDir));
        boolean clearResult = removeFiles.clearDirectory(remoteExistingRootDir);
        if (clearResult) {
            System.out.println("======= Files removed.");
        } else {
            System.err.println("======= Error during files removing.");
        }

        System.out.println();

        System.out.println(String.format("======= Uploading files from '%s' to '%s' ...", localRootDirToUpload, remoteExistingRootDir));
        boolean uploadResult = uploadFiles.uploadDirectory(localRootDirToUpload, remoteExistingRootDir);
        if (uploadResult) {
            System.out.println("======= Files uploaded.");
        } else {
            System.err.println("======= Error during files uploading.");
        }
    }

    private static void handleOneArgument(String arg) {
        if (recognizeSingleArgumentDelete(arg)) {
            System.out.println("======= Removing files...");
            removeFiles.clearDirectory(remoteExistingRootDir);
            System.out.println("======= Files removed.");
        } else if (recognizeSingleArgumentUpload(arg)) {
            System.out.println("======= Uploading files...");
            uploadFiles.uploadDirectory(localRootDirToUpload, remoteExistingRootDir);
            System.out.println("======= Files uploaded.");
        } else if (recognizeSingleArgumentCompress(arg)) {
            System.out.println("======= Compressing files...");
            compressor.compress();
            System.out.println("======= Files compressed.");
        } else {
            System.out.println(String.format("Unknown param %s.", arg));
            printHelp();
        }
    }

    private static boolean recognizeSingleArgumentDelete(String arg) {
        return arg.equalsIgnoreCase("-d") || arg.equalsIgnoreCase("-delete") ||
                arg.equalsIgnoreCase("-r") || arg.equalsIgnoreCase("-remove");
    }

    private static boolean recognizeSingleArgumentUpload(String arg) {
        return arg.equalsIgnoreCase("-u") || arg.equalsIgnoreCase("-upload");
    }

    private static boolean recognizeSingleArgumentCompress(String arg) {
        return arg.equalsIgnoreCase("-c") || arg.equalsIgnoreCase("-compress");
    }

    private static void handleTwoArgument(String arg0, String arg1) {
        if (recognizeTwoArgumentSetPassword(arg0, arg1)) {
            Console console = System.console();
            if (console == null) {
                throw new RuntimeException("Unable to get System.console");
            }
            char[] pwd = console.readPassword("Insert password: ");
            configurator.setProperty("password", new String(pwd));
            System.out.println("Configuration done.");

        } else if (recognizeTwoArgumentUploadSingleFile(arg0, arg1)) {
            System.out.println("======= Uploading single file...");
            uploadFiles.uploadSingleFile(localSingleFilePathFolderToUpload + File.separator + localSingleFileNameToUpload,
                    remoteExistingRootDir + FTPUtil.REMOTE_FILE_SEPARATOR + localSingleFileNameToUpload);
            System.out.println("======= Files uploaded.");

        } else if (recognizeTwoArgumentCompressAndUpload(arg0, arg1)) {
            System.out.println("======= Compressing files...");
            compressor.compress();
            System.out.println("======= Files compressed.");

            System.out.println();

            System.out.println("======= Uploading single file...");
            uploadFiles.uploadSingleFile(localSingleFilePathFolderToUpload + File.separator + localSingleFileNameToUpload,
                    remoteExistingRootDir + FTPUtil.REMOTE_FILE_SEPARATOR + localSingleFileNameToUpload);
            System.out.println("======= Files uploaded.");
        } else {
            System.out.println(String.format("Unknown params %s %s.", arg0, arg1));
            printHelp();
        }
    }

    private static boolean recognizeTwoArgumentSetPassword(String arg0, String arg1) {
        return arg0.equalsIgnoreCase("-set") && (arg1.equalsIgnoreCase("pass") || arg1.equalsIgnoreCase("password"));
    }

    private static boolean recognizeTwoArgumentUploadSingleFile(String arg0, String arg1) {
        return recognizeSingleArgumentUpload(arg0) && arg1.equalsIgnoreCase("-s");
    }

    private static boolean recognizeTwoArgumentCompressAndUpload(String arg0, String arg1) {
        return recognizeSingleArgumentCompress(arg0) && recognizeSingleArgumentUpload(arg1);
    }

}
