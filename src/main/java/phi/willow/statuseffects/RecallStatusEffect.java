package phi.willow.statuseffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

public class RecallStatusEffect extends StatusEffect {
    public RecallStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 8976373);
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        if (!(entity instanceof ServerPlayerEntity player))
            return true;
        // TODO: sound or effect?
        // TODO: find empty spawn position, this just plunks you on your bed
        ServerPlayerEntity.Respawn respawn = player.getRespawn();
        BlockPos respawnPos;
        ServerWorld respawnWorld;
        if (respawn == null)
        {
            respawnWorld = world.getServer().getWorld(World.OVERWORLD);
            if (respawnWorld == null)
                return true;
            respawnPos = respawnWorld.getSpawnPos();
        }
        else
        {
            respawnPos = respawn.pos();
            respawnWorld = player.getServer().getWorld(respawn.dimension());
            if (respawnWorld == null)
                return true;
        }
        player.getInventory().dropAll();
        player.teleportTo(new TeleportTarget(respawnWorld, respawnPos.toCenterPos(), new Vec3d(0,0,0), player.headYaw, player.getPitch(), TeleportTarget.NO_OP));
        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
