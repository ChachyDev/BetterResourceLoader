package club.chachy.chlorine.mixin.client.resources;

import club.chachy.chlorine.Chlorine;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Mixin(ResourcePackRepository.Entry.class)
public class MixinEntry {
    private final Map<String, Constructor<? extends IResourcePack>> entryResourcePackCache = new HashMap<>();

    @Shadow
    @Final
    private File resourcePackFile;

    @Shadow
    private IResourcePack reResourcePack;

    /**
     * @author ChachyDev
     * @reason Return a more correct string!
     */
    @Overwrite
    public String toString() {
        return String.format("%s:%s:%d", resourcePackFile.getName(), resourcePackFile.isDirectory() ? "Folder" : "Archive", resourcePackFile.lastModified());
    }

    @Inject(method = "updateResourcePack", at = @At(value = "FIELD", target = "Lnet/minecraft/client/resources/ResourcePackRepository$Entry;reResourcePack:Lnet/minecraft/client/resources/IResourcePack;", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER), cancellable = true)
    private void updateResourcePack(CallbackInfo ci) {
        for (Map.Entry<String, Class<? extends IResourcePack>> entry : Chlorine.getResourcePackHashMap().entrySet()) {
            if (resourcePackFile.getName().endsWith("." + entry.getKey())) {
                try {
                    Constructor<? extends IResourcePack> resourcePack = entryResourcePackCache.get(entry.getKey());
                    // Cache constructor
                    if (resourcePack == null) {
                        resourcePack = entry.getValue().getConstructor(File.class);
                        entryResourcePackCache.put(entry.getKey(), resourcePack);
                    }
                    reResourcePack = resourcePack.newInstance(resourcePackFile);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
