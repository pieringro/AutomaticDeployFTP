package com.pierprogramm.automaticDeployFtp;

import com.pierprogramm.automaticDeployFtp.config.Configurator;
import com.pierprogramm.automaticDeployFtp.config.ConfiguratorKeys;
import com.pierprogramm.automaticDeployFtp.connection.ConnectionClient;
import com.pierprogramm.automaticDeployFtp.connection.ConnectionFactory;
import com.pierprogramm.automaticDeployFtp.connection.Protocols;

import java.util.Arrays;

public abstract class FilesHandler {


    public void init() {
        Configurator config = Configurator.getInstance();
        server = config.getProperty(ConfiguratorKeys.HOST);
        port = Integer.parseInt(config.getProperty(ConfiguratorKeys.PORT));
        user = config.getProperty(ConfiguratorKeys.USER);
        pass = config.getProperty(ConfiguratorKeys.PASSWORD);
        connectionClient = ConnectionFactory.createConnection(
                Protocols.valueOf(
                        config.getProperty(ConfiguratorKeys.PROTOCOL_CONNECTION)
                )
        );
    }

    String server;
    String user;
    String pass;
    int port;
    ConnectionClient connectionClient;

}
