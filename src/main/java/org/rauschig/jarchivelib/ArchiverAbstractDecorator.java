package org.rauschig.jarchivelib;

import org.rauschig.jarchivelib.ArchiveStream;
import org.rauschig.jarchivelib.Archiver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public abstract class ArchiverAbstractDecorator implements Archiver {

    protected CommonsArchiver archiver;

    @Override
    public File create(String archive, File destination, File source) throws IOException {
        return archiver.create(archive, destination, source);
    }

    @Override
    public File create(String archive, File destination, File... sources) throws IOException {
        return archiver.create(archive, destination, sources);
    }

    @Override
    public void extract(File archive, File destination) throws IOException {
        archiver.extract(archive, destination);
    }

    @Override
    public void extract(InputStream archive, File destination) throws IOException {
        archiver.extract(archive, destination);
    }

    @Override
    public ArchiveStream stream(File archive) throws IOException {
        return archiver.stream(archive);
    }

    @Override
    public String getFilenameExtension() {
        return archiver.getFilenameExtension();
    }
}
