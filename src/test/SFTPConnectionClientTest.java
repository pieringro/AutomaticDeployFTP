import com.pierprogramm.automaticDeployFtp.connection.SFTPConnectionClient;
import org.junit.Test;

import static org.junit.Assert.*;

public class SFTPConnectionClientTest {

    @Test
    public void testUploadSingleFile(){
        boolean result;
        SFTPConnectionClient sftp = new SFTPConnectionClient();
        result = sftp.connect("host", 22);
        assertTrue(result);

        result = sftp.login("pierprogramm", "password");
        assertTrue(result);

        result = sftp.uploadSingleFile("pathFileToUpload", "pathRemoteFileOnServer");
        assertTrue(result);

        result = sftp.disconnect();
        assertTrue(result);
    }


}