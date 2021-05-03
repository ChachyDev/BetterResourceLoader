package club.chachy.chlorine.resources.list;

import club.chachy.chlorine.utils.Utils;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArchiveEntryList extends ArrayList<ArchiveEntry> {
    private final Map<ArchiveEntry, byte[]> archiveEntryHashMap = new HashMap<>();

    public ArchiveEntryList(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {
                try (ArchiveInputStream archiveInputStream = Utils.INSTANCE.getArchiveStreamFactory().createArchiveInputStream(bufferedInputStream)) {
                    ArchiveEntry entry;
                    while ((entry = archiveInputStream.getNextEntry()) != null) {
                        add(entry);
                        byte[] buffer = new byte[(int) entry.getSize()];
                        int read = archiveInputStream.read(buffer, 0, buffer.length);
                        if (read == buffer.length) {
                            archiveEntryHashMap.put(entry, buffer);
                        }
                    }
                }
            }
        } catch (ArchiveException | IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBytes(ArchiveEntry entry) {
        if (entry == null) return null;
        return archiveEntryHashMap.get(entry);
    }

    public ArchiveEntry findArchiveByName(String name) {
        for (ArchiveEntry entry : this) {
            if (entry.getName().equals(name)) {
                return entry;
            }
        }
        return null;
    }
}
