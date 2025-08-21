package phi.willow.statuseffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

public class RecallStatusEffect extends StatusEffect {
    public RecallStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0x6acce8);
    }

//    @Override
//    public void onApplied(LivingEntity entity, int amplifier) {
//        if (!(entity instanceof ServerPlayerEntity player))
//            return;
//        // TODO: im lost
//        ServerPlayerEntity.Respawn respawn = player.getRespawn();
//        if (respawn == null)
//            return;
//        ServerWorld respawnWorld = player.getServer().getWorld(respawn.dimension());
//        if (!player.getWo)
//        player.teleport(, )
//    }
}
