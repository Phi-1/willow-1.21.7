package phi.willow.items;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import phi.willow.Willow;
import phi.willow.data.Profession;
import phi.willow.data.ProfessionLevel;
import phi.willow.registry.WillowEffectsAndPotions;
import phi.willow.registry.WillowItems;
import phi.willow.registry.WillowToolMaterials;
import phi.willow.util.ProfessionUtil;
import phi.willow.util.TickTimers;

import java.util.List;

public class TheHeraldItem extends AxeItem {

    public TheHeraldItem(Settings settings) {
        super(WillowToolMaterials.THE_HERALD, 9.0f, -3.0f, settings
                .fireproof()
                .rarity(Rarity.EPIC));
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(TheHeraldItem::resistLightningDamageWhileHolding);
    }

    private static boolean resistLightningDamageWhileHolding(LivingEntity entity, DamageSource damageSource, float damage)
    {
        if (damageSource.isOf(DamageTypes.LIGHTNING_BOLT) && entity instanceof PlayerEntity player && player.getMainHandStack().isOf(WillowItems.THE_HERALD))
            return false;
        return true;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!state.isIn(BlockTags.LOGS) || !(miner instanceof ServerPlayerEntity player))
            return super.postMine(stack, world, state, pos, miner);
        ProfessionLevel level = ProfessionUtil.getProfessionLevel(player, Profession.WOODCUTTING);
        if (ProfessionUtil.canUseToolAtLevel(level, stack))
            chopLogsAround(pos, world, player);
        return super.postMine(stack, world, state, pos, miner);
    }

    private void chopLogsAround(BlockPos pos, World world, ServerPlayerEntity player)
    {
        int i = 0;
        for (BlockPos checkPos : BlockPos.iterateOutwards(pos, 1, 1, 1))
        {
            BlockState state = world.getBlockState(checkPos);
            BlockPos copy = new BlockPos(checkPos.getX(), checkPos.getY(), checkPos.getZ());
            if (state.isIn(BlockTags.LOGS))
            {
                TickTimers.schedule(() -> {
                    player.interactionManager.tryBreakBlock(copy);
                }, 1 + i);
            }
            i++;
        }
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.postDamageEntity(stack, target, attacker);
        if (!(attacker instanceof PlayerEntity player) || !(player.getWorld() instanceof ServerWorld world))
            return;
        ItemCooldownManager cooldownManager = player.getItemCooldownManager();
        if (cooldownManager.isCoolingDown(stack))
            return;
        final int effectRadius = 12;
        final int bonusBolts = 2;
        // NOTE: keep above 10 ticks so iframes settle
        final int effectDelayTicks = 18;
        List<MobEntity> mobs = target.getWorld().getEntitiesByClass(MobEntity.class, Box.of(target.getEyePos(), effectRadius, effectRadius, effectRadius), mob -> true);
        for (int i = 0; i < mobs.size() + bonusBolts; i++)
        {
            TickTimers.schedule(() -> {
//                player.addStatusEffect(new StatusEffectInstance(WillowEffectsAndPotions.LIGHTNING_RESISTANCE, effectDelayTicks, 1));
                int maxSearches = mobs.size();
                int searches = 0;
                while (true)
                {
                    // TODO: this is probably not how I want it to work, look at it with clear mind at some point
                    MobEntity mob = mobs.get(world.random.nextInt(mobs.size()));
                    if (mob.isAlive())
                    {
                        EntityType.LIGHTNING_BOLT.spawn(world, mob.getBlockPos(), SpawnReason.TRIGGERED);
                        break;
                    }
                    searches++;
                    if (searches >= maxSearches)
                        break;
                }
            }, effectDelayTicks + effectDelayTicks * i);
        }
        final int cooldownTicks = 8 * 20;
        cooldownManager.set(stack, cooldownTicks);
    }


}
