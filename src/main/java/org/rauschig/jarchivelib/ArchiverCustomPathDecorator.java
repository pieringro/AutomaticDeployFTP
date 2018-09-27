package org.rauschig.jarchivelib;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.rauschig.jarchivelib.util.ArchiveUtility;

import java.io.File;
import java.io.IOException;

public class ArchiverCustomPathDecorator extends ArchiverAbstractDecorator {


    public ArchiverCustomPathDecorator(CommonsArchiver archiver) {
        this.archiver = archiver;
    }

    @Override
    public File create(String archiveName, File destinationFolder, File[] sources){

        File archiveFile = null;
        ArchiveOutputStream archiveOutStream = null;
        try {
            IOUtils.requireDirectory(destinationFolder);
            archiveFile = ArchiveUtility.createNewArchiveFile(archiveName, getFilenameExtension(), destinationFolder);
            archiveOutStream = ArchiveUtility.createArchiveOutputStream(archiver, archiveFile);
            ArchiveUtility.writeToArchive(destinationFolder, sources, archiveOutStream);
            archiveOutStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            IOUtils.closeQuietly(archiveOutStream);
        }

        return archiveFile;
    }

}
