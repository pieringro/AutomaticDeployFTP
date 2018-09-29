import static org.junit.Assert.*;

import com.pierprogramm.automaticDeployFtp.UploadFiles;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.xfer.FileSystemFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class UploadFilesTest {

    @Test
    public void testUploadSingleFile(){
        UploadFiles test = new UploadFiles(null, null);
        test.uploadSingleFile("pathFileToUpload", "pathRemoteFileOnServer");
    }


}