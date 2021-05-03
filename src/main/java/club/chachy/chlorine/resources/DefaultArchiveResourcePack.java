package club.chachy.chlorine.resources;

import club.chachy.chlorine.resources.list.ArchiveEntryList;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.ResourcePackFileNotFoundException;
import org.apache.commons.compress.archivers.ArchiveEntry;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultArchiveResourcePack extends AbstractResourcePack {
    private final Splitter entryNameSplitter = FileResourcePack.entryNameSplitter;

    private final ArchiveEntryList entryList;

    public DefaultArchiveResourcePack(File file) {
        super(file);
        entryList = new ArchiveEntryList(file);
    }

    @Override
    protected InputStream getInputStreamByName(String name) throws IOException {
        byte[] bytes = entryList.getBytes(entryList.findArchiveByName(name));
        if (bytes == null) {
            throw new ResourcePackFileNotFoundException(resourcePackFile, name);
        }
        return new ByteArrayInputStream(bytes);
    }

    @Override
    protected boolean hasResourceName(String name) {
        return entryList.findArchiveByName(name) != null;
    }

    @Override
    public Set<String> getResourceDomains() {
        Set<String> domains = new HashSet<>();

        for (ArchiveEntry entry : entryList) {
            String s = entry.getName();
            if (s.startsWith("assets/")) {
                List<String> list = Lists.newArrayList(entryNameSplitter.split(s));

                if (list.size() > 1) {
                    String domain = list.get(1);

                    if (domain.equals(domain.toLowerCase())) {
                        domains.add(domain);
                    } else {
                        logNameNotLowercase(domain);
                    }
                }
            }
        }

        return domains;
    }

    @Override
    public String getPackName() {
        String name = resourcePackFile.getName();
        return name.substring(0, name.lastIndexOf('.'));
    }
}
