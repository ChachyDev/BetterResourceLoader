package club.chachy.chlorine.mixin.client.resources;

import club.chachy.chlorine.Chlorine;
import net.minecraft.client.resources.IResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.Map;

@Mixin(targets = "net.minecraft.client.resources.ResourcePackRepository$1")
public class MixinResourcePackRepositoryFileFilter {

    @Inject(method = "accept(Ljava/io/File;)Z", at = @At("HEAD"), cancellable = true)
    private void accept(File file, CallbackInfoReturnable<Boolean> cir) {
        for (Map.Entry<String, Class<? extends IResourcePack>> entry : Chlorine.getResourcePackHashMap().entrySet()) {
            if (file.getName().endsWith("." + entry.getKey())) {
                cir.setReturnValue(true);
            }
        }
    }
}
