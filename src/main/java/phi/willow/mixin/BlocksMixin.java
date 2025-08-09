package phi.willow.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phi.willow.blocks.AppleBlossomLeavesBlock;

import java.util.Objects;
import java.util.function.Function;

@Mixin(Blocks.class)
public class BlocksMixin {

    @Shadow
    private static RegistryKey<Block> keyOf(String id) { return null; }

    @Inject(method = "register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/block/AbstractBlock$Settings;)Lnet/minecraft/block/Block;", at = @At("HEAD"), cancellable = true)
    private static void register(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings, CallbackInfoReturnable<Block> cir) {
        if (Objects.equals(id, "oak_leaves")) {
            Function<AbstractBlock.Settings, Block> replacement = AppleBlossomLeavesBlock::new;
            cir.setReturnValue(Blocks.register(keyOf(id), replacement, settings));
        }
    }
}
