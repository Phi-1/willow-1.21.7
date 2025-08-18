package phi.willow.mixin;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import phi.willow.registry.WillowTags;
import phi.willow.util.TickTimers;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void addWoodenToolRequirements(Block block, Reference2ObjectArrayMap<Property<?>, Comparable<?>> propertyMap, MapCodec<BlockState> codec, CallbackInfo ci)
    {
        TickTimers.schedule(() -> {
            AbstractBlock.AbstractBlockState self = self();
            // Also add tool requirements to logs by so they respect the needs_tool tags
            if (self.isIn(WillowTags.Blocks.NEEDS_WOODEN_TOOL) || self.isIn(BlockTags.LOGS))
                ((AbstractBlockStateAccessor) self).setToolRequired(true);
        }, 1);
    }

    @Unique
    private AbstractBlock.AbstractBlockState self()
    {
        return (AbstractBlock.AbstractBlockState) (Object) this;
    }

}
