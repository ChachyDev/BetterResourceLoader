package club.chachy.chlorine;

import club.chachy.chlorine.resources.DefaultArchiveResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod(modid = Chlorine.MODID, version = Chlorine.VERSION, name = Chlorine.NAME)
public class Chlorine {
    public static final String MODID = "chlorine";
    public static final String VERSION = "0.0.2";
    public static final String NAME = "Chlorine";

    private static final Map<String, Class<? extends IResourcePack>> resourcePackHashMap = new HashMap<>();

    private static void registerFormat(String format, Class<? extends IResourcePack> resourcePack) {
        resourcePackHashMap.put(format, resourcePack);
    }

    public static Map<String, Class<? extends IResourcePack>> getResourcePackHashMap() {
        return resourcePackHashMap;
    }

    static {
        Class<? extends IResourcePack> defaultArchiveResourcePack = DefaultArchiveResourcePack.class;
        registerFormat("tar", defaultArchiveResourcePack);
    }
}
