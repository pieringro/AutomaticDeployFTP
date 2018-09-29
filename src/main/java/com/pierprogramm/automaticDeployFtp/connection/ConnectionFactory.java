package com.pierprogramm.automaticDeployFtp.connection;

import java.util.HashMap;
import java.util.Map;

public class ConnectionFactory {

    private static Map<Protocols, ConnectionClient> matching = new HashMap<Protocols, ConnectionClient>();
    static {
        matching.put(Protocols.FTP, new FTPConnectionClient());
        matching.put(Protocols.SFTP, new SFTPConnectionClient());
    }


    public static ConnectionClient createConnection(Protocols protocol){
        return matching.get(protocol);
    }

}
