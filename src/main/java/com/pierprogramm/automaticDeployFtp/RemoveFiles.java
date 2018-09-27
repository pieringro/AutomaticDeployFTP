package com.pierprogramm.automaticDeployFtp;

import com.pierprogramm.automaticDeployFtp.config.Configurator;
import com.pierprogramm.automaticDeployFtp.config.ConfiguratorKeys;
import com.pierprogramm.automaticDeployFtp.util.FTPUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RemoveFiles extends FilesHandler {

    public RemoveFiles(List<String> namesFilesToPreserve, List<String> namesDirsToPreserve){
        this.init();
        this.namesFilesToPreserve = namesFilesToPreserve;
        this.namesDirsToPreserve = namesDirsToPreserve;
    }

    private String rootDirToPreserve;
    private List<String> namesFilesToPreserve;
    private List<String> namesDirsToPreserve;


    public boolean clearDirectory(String pathDirectoryToRemove){
        boolean result = true;
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            boolean loginResult = ftpClient.login(user, pass);
            if(loginResult){

                rootDirToPreserve = pathDirectoryToRemove;
                this.deleteDirectory(pathDirectoryToRemove, ftpClient);

                ftpClient.logout();
                ftpClient.disconnect();
                result = true;
            }
            else{
                result = false;
                System.err.println("Login failed.");
            }
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }


    private void deleteDirectory(String path, FTPClient ftpClient) throws IOException{
        FTPFile[] files=ftpClient.listFiles(path);
        if(files != null && files.length > 0) {
            for (FTPFile ftpFile : files) {
                if(ftpFile.isDirectory()){
                    String completePathName = path + "/" + ftpFile.getName();
                    if(!this.namesDirsToPreserve.contains(completePathName)){
                        System.out.println("Trying to remove directory " + completePathName);
                        deleteDirectory(completePathName, ftpClient);
                    }
                    else{
                        System.out.println("PRESERVED directory " + completePathName + ".");
                    }
                }
                else {
                    String deleteFilePath = path + "/" + ftpFile.getName();

                    if(!this.namesFilesToPreserve.contains(deleteFilePath)) {
                        System.out.println("Deleting file " + deleteFilePath);
                        boolean deleteFileResult = ftpClient.deleteFile(deleteFilePath);
                        if(!deleteFileResult){
                            System.out.println("Cannot delete file "+deleteFilePath);
                        }
                    }
                    else{
                        System.out.println("PRESERVED file "+deleteFilePath+".");
                    }
                }
            }
        }

        if(!path.equals(rootDirToPreserve)) {
            System.out.println("Removing directory " + path);
            boolean removeDirResult = ftpClient.removeDirectory(path);
            if(!removeDirResult){
                System.out.println("Cannot remove directory "+path);
            }
        }
    }

}
