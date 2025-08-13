package phi.willow.mixin;

import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractBlock.AbstractBlockState.class)
public interface AbstractBlockStateAccessor {

    @Mutable
    @Accessor("toolRequired")
    public void setToolRequired(boolean required);

}
