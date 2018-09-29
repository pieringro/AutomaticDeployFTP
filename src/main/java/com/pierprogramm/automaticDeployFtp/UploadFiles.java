package com.pierprogramm.automaticDeployFtp;

import com.pierprogramm.automaticDeployFtp.config.Configurator;
import com.pierprogramm.automaticDeployFtp.config.ConfiguratorKeys;
import com.pierprogramm.automaticDeployFtp.util.FTPUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.util.Arrays;
import java.util.List;


public class UploadFiles extends FilesHandler {

    public UploadFiles(List<String> namesFilesToIgnore, List<String> namesDirsToIgnore) {
        this.init();
        this.namesFilesToIgnore = namesFilesToIgnore;
        this.namesDirsToIgnore = namesDirsToIgnore;
    }

    private List<String> namesFilesToIgnore;
    private List<String> namesDirsToIgnore;

    /**
     * @param pathDirOnServer path directory di partenza parent che deve esistere sul server
     * @param pathDirToUpload path directory locale da caricare
     */
    public boolean uploadDirectory(String pathDirToUpload, String pathDirOnServer) {
        boolean result = true;

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            boolean loginResult = ftpClient.login(user, pass);
            if (loginResult) {
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
                FTPUtil.uploadDirectory(ftpClient, pathDirOnServer, pathDirToUpload, "", namesFilesToIgnore, namesDirsToIgnore);
                ftpClient.logout();
                ftpClient.disconnect();
                result = true;
            } else {
                result = false;
                System.err.println("Login failed.");
            }
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }

    public boolean uploadSingleFile(String pathFileToUpload, String pathRemoteFileOnServer) {
        boolean result;

        boolean loginResult = connectAndLogin();
        if (loginResult) {
            System.out.println("Uploading " + pathFileToUpload + ", into " + pathRemoteFileOnServer);
            result = connectionClient.uploadSingleFile(pathFileToUpload, pathRemoteFileOnServer);

            if (result) {
                System.out.println("UPLOADED a file to: "
                        + pathRemoteFileOnServer);
            } else {
                System.out.println("COULD NOT upload the file: "
                        + pathFileToUpload);
            }
            result = connectionClient.disconnect();
            if (!result) {
                System.out.println("Error during disconnect.");
            }
        } else {
            result = false;
            System.err.println("Login failed.");
        }

        return result;
    }

    private boolean connectAndLogin() {
        System.out.println("Connecting to " + server + "...");
        boolean connectResult = connectionClient.connect(server, port);
        if (connectResult) {
            System.out.println("Connected");
            System.out.println("Logging in ...");
            boolean loginResult = connectionClient.login(user, pass);
            if(loginResult){
                System.out.println("Logged.");
            }
            return loginResult;
        }

        return false;
    }

}
