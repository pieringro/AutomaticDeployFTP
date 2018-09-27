package com.pierprogramm.automaticDeployFtp.util;

import javax.xml.bind.DatatypeConverter;

public class Encrypter {

    public static String encrypt(String data){
        return DatatypeConverter.printBase64Binary(data.getBytes());
    }

    public static String decrypt(String dataEncrypted){
        if(dataEncrypted == null || dataEncrypted.isEmpty()){
            return "";
        }
        return new String(DatatypeConverter.parseBase64Binary(dataEncrypted));
    }
}
