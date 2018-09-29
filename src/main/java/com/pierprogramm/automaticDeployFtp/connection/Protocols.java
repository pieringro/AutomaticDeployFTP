package com.pierprogramm.automaticDeployFtp.connection;

public enum Protocols {

    FTP("FTP"),
    FTPS("FTPS"),
    SFTP("SFTP");

    private String text;

    Protocols(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
