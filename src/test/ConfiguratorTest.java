import com.pierprogramm.automaticDeployFtp.config.Configurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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