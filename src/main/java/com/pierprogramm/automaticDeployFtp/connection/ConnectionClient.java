package com.pierprogramm.automaticDeployFtp.connection;

public interface ConnectionClient {

    boolean connect(String server, int port);

    boolean login(String username, String password);

    boolean uploadSingleFile(String pathFileToUpload, String pathRemoteFileOnServer);

    boolean disconnect();
}
