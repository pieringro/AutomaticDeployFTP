package com.pierprogramm.automaticDeployFtp.connection;

import com.pierprogramm.automaticDeployFtp.util.FTPUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

public class FTPConnectionClient implements ConnectionClient {

    FTPClient ftpClient;

    public boolean connect(String server, int port) {
        boolean result;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(server, port);
            result = true;
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    public boolean login(String username, String password) {
        boolean result;
        try {
            result = ftpClient.login(username, password);
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean uploadSingleFile(String pathFileToUpload, String pathRemoteFileOnServer) {
        boolean result;
        try {
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
            System.out.println("Uploading " + pathFileToUpload + ", into " + pathRemoteFileOnServer);
            result = FTPUtil.uploadSingleFile(ftpClient, pathFileToUpload, pathRemoteFileOnServer);
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    public boolean disconnect() {
        boolean result = true;
        try {
            result = ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }
}
