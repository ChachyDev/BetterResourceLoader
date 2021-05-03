package club.chachy.chlorine.utils;

import org.apache.commons.compress.archivers.ArchiveStreamFactory;

public class Utils {
    public static Utils INSTANCE = new Utils();

    private final ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();

    public ArchiveStreamFactory getArchiveStreamFactory() {
        return archiveStreamFactory;
    }
}
