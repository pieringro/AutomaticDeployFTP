package com.pierprogramm.automaticDeployFtp.config;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.security.krb5.Config;

import static org.junit.Assert.*;

public class ConfiguratorTest {

    private Configurator configurator;

    @Before
    public void setUp() throws Exception {
        configurator = Configurator.getInstance();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSetProperty() {
        configurator.setProperty("password", "value");
    }
}