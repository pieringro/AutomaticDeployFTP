package com.pierprogramm.automaticDeployFtp.config;

import com.pierprogramm.automaticDeployFtp.util.Encrypter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class Configurator {

    private static final String LINK_USER_CONFIG_FILE = "./config/config.properties";
    private static final String LINK_CONFIG_FILE = "./config/system.properties";

    public static Configurator instance;


    public static Configurator getInstance(){
        if(instance == null){
            File directory = new File("./");
            System.out.println(directory.getAbsolutePath());
            // System.out.println(Configurator.class.getResource("config.properties"));

            instance = new Configurator();
            boolean resultInit = instance.initConfigurator();
            if(!resultInit){
                throw new RuntimeException("Error config initialization. Read console.");
            }
        }
        return instance;
    }
    private Configurator(){}


    public boolean initialized = false;

    private Properties props;

    private List<String> keyEncoded = new ArrayList<String>(){{
        add(ConfiguratorKeys.PASSWORD);
    }};

    private boolean initConfigurator(){
        boolean result = true;

        File configFile = new File(LINK_USER_CONFIG_FILE);
        File autoConfigFile = new File(LINK_CONFIG_FILE);

        try {
            FileReader reader;
            if(autoConfigFile.exists()){
                reader = new FileReader(autoConfigFile);
            }
            else{
                reader = new FileReader(configFile);
            }
            //InputStream reader = this.getClass().getClassLoader().getResourceAsStream(LINK_USER_CONFIG_FILE);
            props = new Properties();
            props.load(reader);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            result = false;
        } catch (IOException ex) {
            ex.printStackTrace();
            result = false;
        }

        return result;
    }


    public String getProperty(String key){
        if(keyEncoded.contains(key)){
            return Encrypter.decrypt(props.getProperty(key));
        }
        return props.getProperty(key);
    }

    public void setProperty(String key, String value){
        if(keyEncoded.contains(key)){
            value = Encrypter.encrypt(value);
        }
        props.setProperty(key, value);
        try {
            props.store(new FileOutputStream(LINK_CONFIG_FILE), "auto generate");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

