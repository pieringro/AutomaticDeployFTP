package com.pierprogramm.automaticDeployFtp;

import com.pierprogramm.automaticDeployFtp.config.Configurator;
import com.pierprogramm.automaticDeployFtp.config.ConfiguratorKeys;

import java.util.Arrays;

public abstract class FilesHandler {


    public void init(){
        server = Configurator.getInstance().getProperty(ConfiguratorKeys.HOST);
        port = Integer.parseInt(Configurator.getInstance().getProperty(ConfiguratorKeys.PORT));
        user = Configurator.getInstance().getProperty(ConfiguratorKeys.USER);
        pass = Configurator.getInstance().getProperty(ConfiguratorKeys.PASSWORD);
    }

    String server;
    String user;
    String pass;
    int port;


}
