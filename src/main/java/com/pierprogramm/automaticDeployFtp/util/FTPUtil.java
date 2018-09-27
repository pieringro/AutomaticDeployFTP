package com.pierprogramm.automaticDeployFtp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 * This utility class implements method for uploading a whole directory from
 * local computer to a remote FTP server, based on Apache Commons Net library.
 *
 * @author www.codejava.net
 */
public class FTPUtil {

    public static final String REMOTE_FILE_SEPARATOR = "/";

    /**
     * Upload a whole directory (including its nested sub directories and files)
     * to a FTP server.
     *
     * @param ftpClient       an instance of org.apache.commons.net.ftp.FTPClient class.
     * @param remoteDirPath   Path of the destination directory on the server.
     * @param localParentDir  Path of the local directory being uploaded.
     * @param remoteParentDir Path of the parent directory of the current directory on the
     *                        server (used by recursive calls).
     * @throws IOException if any network or IO error occurred.
     */
    public static void uploadDirectory(FTPClient ftpClient,
                                       String remoteDirPath, String localParentDir, String remoteParentDir, List<String> namesFilesToIgnore,
                                       List<String> namesDirsToIgnore)
            throws IOException {

        System.out.println("LISTING directory: " + localParentDir);

        File localDir = new File(localParentDir);
        File[] subFiles = localDir.listFiles();
        if (subFiles != null && subFiles.length > 0) {
            for (File item : subFiles) {
                String remoteFilePath = remoteDirPath + "/" + remoteParentDir
                        + "/" + item.getName();
                if (remoteParentDir.equals("")) {
                    remoteFilePath = remoteDirPath + "/" + item.getName();
                }

                if (item.isFile()) {
                    // upload the file
                    String localFilePath = item.getAbsolutePath();
                    System.out.println("About to upload the file: " + localFilePath);

                    if (namesFilesToIgnore.contains(item.getName())) {
                        System.out.println("File ignored " + localFilePath);
                    } else {
                        boolean uploaded = uploadSingleFile(ftpClient,
                                localFilePath, remoteFilePath);
                        if (uploaded) {
                            System.out.println("UPLOADED a file to: "
                                    + remoteFilePath);
                        } else {
                            System.out.println("COULD NOT upload the file: "
                                    + localFilePath);
                        }
                    }

                } else {

                    if (namesDirsToIgnore.contains(item.getName())) {
                        System.out.println("Directory ignored " + item.getName());
                    } else {
                        // create directory on the server
                        boolean created = ftpClient.makeDirectory(remoteFilePath);
                        if (created) {
                            System.out.println("CREATED the directory: "
                                    + remoteFilePath);
                        } else {
                            System.out.println("COULD NOT create the directory: "
                                    + remoteFilePath);
                        }

                        // upload the sub directory
                        String parent = remoteParentDir + "/" + item.getName();
                        if (remoteParentDir.equals("")) {
                            parent = item.getName();
                        }

                        localParentDir = item.getAbsolutePath();
                        uploadDirectory(ftpClient, remoteDirPath, localParentDir,
                                parent, namesFilesToIgnore, namesDirsToIgnore);
                    }
                }
            }
        }
    }


    public static boolean uploadSingleFile(FTPClient ftpClient,
                                           String localFilePath, String remoteFilePath) throws IOException {
        int indexOfLastFileSeparator = remoteFilePath.lastIndexOf(REMOTE_FILE_SEPARATOR);
        String remoteFileName = remoteFilePath.substring(indexOfLastFileSeparator+1);
        remoteFilePath = remoteFilePath.substring(0, indexOfLastFileSeparator);
        return uploadSingleFile(ftpClient, localFilePath, remoteFilePath, remoteFileName);
    }

    /**
     * Upload a single file to the FTP server.
     *
     * @param ftpClient      an instance of org.apache.commons.net.ftp.FTPClient class.
     * @param localFilePath  Path of the file on local computer
     * @param remoteFilePath Path of the file on remote the server
     * @return true if the file was uploaded successfully, false otherwise
     * @throws IOException if any network or IO error occurred.
     */
    public static boolean uploadSingleFile(FTPClient ftpClient,
                                           String localFilePath, String remoteFilePath, String remoteFileName) throws IOException {
        File localFile = new File(localFilePath);

        InputStream inputStream = new FileInputStream(localFile);
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            return ftpClient.storeFile(remoteFilePath+REMOTE_FILE_SEPARATOR+remoteFileName, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            inputStream.close();
        }
    }
}