package org.rauschig.jarchivelib.util;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.CommonsArchiver;
import org.rauschig.jarchivelib.CommonsStreamFactory;
import org.rauschig.jarchivelib.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ArchiveUtility {



    /**
     * Returns a new ArchiveOutputStream for creating archives. Subclasses can override this to return their own custom
     * implementation.
     *
     * @param archiveFile the archive file to stream to
     * @return a new ArchiveOutputStream for the given archive file.
     * @throws IOException propagated IO exceptions
     */
    public static ArchiveOutputStream createArchiveOutputStream(CommonsArchiver archiver, File archiveFile) throws IOException {
        try {
            return CommonsStreamFactory.createArchiveOutputStream(archiver, archiveFile);
        } catch (ArchiveException e) {
            throw new IOException(e);
        }
    }



    /**
     * Creates a new File in the given destination. The resulting name will always be "archive"."fileExtension". If the
     * archive name parameter already ends with the given file name extension, it is not additionally appended.
     *
     * @param archive the name of the archive
     * @param extension the file extension (e.g. ".tar")
     * @param destination the parent path
     * @return the newly created file
     * @throws IOException if an I/O error occurred while creating the file
     */
    public static File createNewArchiveFile(String archive, String extension, File destination) throws IOException {
        if (!archive.endsWith(extension)) {
            archive += extension;
        }

        File file = new File(destination, archive);
        file.createNewFile();

        return file;
    }

    /**
     * Recursion entry point for {@link #writeToArchive(File, File[], ArchiveOutputStream)}.
     * <br>
     * Recursively writes all given source {@link File}s into the given {@link ArchiveOutputStream}.
     *
     * @param sources the files to write in to the archive
     * @param archive the archive to write into
     * @throws IOException when an I/O error occurs
     */
    public static void writeToArchive(File[] sources, ArchiveOutputStream archive) throws IOException {
        for (File source : sources) {
            if (!source.exists()) {
                throw new FileNotFoundException(source.getPath());
            } else if (!source.canRead()) {
                throw new FileNotFoundException(source.getPath() + " (Permission denied)");
            }

            writeToArchive(source.getParentFile(), new File[]{ source }, archive);
        }
    }


    /**
     * Recursively writes all given source {@link File}s into the given {@link ArchiveOutputStream}. The paths of the
     * sources in the archive will be relative to the given parent {@code File}.
     *
     * @param parent the parent file node for computing a relative path (see {@link IOUtils#relativePath(File, File)})
     * @param sources the files to write in to the archive
     * @param archive the archive to write into
     * @throws IOException  when an I/O error occurs
     */
    public static void writeToArchive(File parent, File[] sources, ArchiveOutputStream archive) throws IOException {
        for (File source : sources) {
            String relativePath = IOUtils.relativePath(parent, source);

            createArchiveEntry(source, relativePath, archive);

            if (source.isDirectory()) {
                writeToArchive(parent, source.listFiles(), archive);
            }
        }
    }

    /**
     * Creates a new {@link ArchiveEntry} in the given {@link ArchiveOutputStream}, and copies the given {@link File}
     * into the new entry.
     *
     * @param file the file to add to the archive
     * @param entryName the name of the archive entry
     * @param archive the archive to write to
     * @throws IOException when an I/O error occurs during FileInputStream creation or during copying
     */
    public static void createArchiveEntry(File file, String entryName, ArchiveOutputStream archive) throws IOException {
        ArchiveEntry entry = archive.createArchiveEntry(file, entryName);
        // TODO #23: read permission from file, write it to the ArchiveEntry
        archive.putArchiveEntry(entry);

        if (!entry.isDirectory()) {
            FileInputStream input = null;
            try {
                input = new FileInputStream(file);
                IOUtils.copy(input, archive);
            } finally {
                IOUtils.closeQuietly(input);
            }
        }

        archive.closeArchiveEntry();
    }

}
