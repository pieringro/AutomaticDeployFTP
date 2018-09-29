package com.pierprogramm.automaticDeployFtp.connection;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.userauth.UserAuthException;
import net.schmizz.sshj.xfer.FileSystemFile;

import java.io.IOException;

public class SFTPConnectionClient implements ConnectionClient {

    SSHClient sshClient;
    SFTPClient sftpClient;

    public boolean connect(String server, int port) {
        boolean result = false;
        try {
            sshClient = new SSHClient();
            sshClient.loadKnownHosts();
            sshClient.connect(server, port);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean login(String username, String password) {
        boolean result = false;

        try {
            sshClient.authPassword(username, password);
            sftpClient = sshClient.newSFTPClient();
            result = (sftpClient != null);
        } catch (UserAuthException e) {
            e.printStackTrace();
        } catch (TransportException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean uploadSingleFile(String pathFileToUpload, String pathRemoteFileOnServer) {
        boolean result = false;
        try {
            sftpClient.put(new FileSystemFile(pathFileToUpload), pathRemoteFileOnServer);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean disconnect() {
        boolean result = false;
        try {
            sftpClient.close();
            sshClient.disconnect();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
