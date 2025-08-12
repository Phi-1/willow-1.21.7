package phi.willow.items;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import phi.willow.Willow;
import phi.willow.data.Profession;
import phi.willow.data.ProfessionLevel;
import phi.willow.registry.WillowEffects;
import phi.willow.registry.WillowItems;
import phi.willow.registry.WillowToolMaterials;
import phi.willow.util.ProfessionUtil;
import phi.willow.util.TickTimers;

import java.util.List;

public class TheHeraldItem extends AxeItem {

    public TheHeraldItem(Settings settings) {
        super(WillowToolMaterials.THE_HERALD, 7.0f, -3.0f, settings
                .fireproof()
                .rarity(Rarity.EPIC));
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            ItemStack stack = player.getMainHandStack();
            if (stack.isOf(WillowItems.THE_HERALD))
                this.afterBreakBlock(world, player, pos, state, blockEntity, stack);
        });
    }

    private void afterBreakBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack)
    {
        if (world.isClient)
            return;
        if (!state.isIn(BlockTags.LOGS))
            return;
        ProfessionLevel level = ProfessionUtil.getProfessionLevel(player, Profession.WOODCUTTING);
        if (ProfessionUtil.canUseToolAtLevel(level, stack))
            chopLogsAround(pos, world, stack, player);
    }

    private void chopLogsAround(BlockPos pos, World world, ItemStack axe, PlayerEntity player)
    {
        for (BlockPos checkPos : BlockPos.iterateOutwards(pos, 1, 1, 1))
        {
            BlockState state = world.getBlockState(checkPos);
            if (state.isIn(BlockTags.LOGS))
            {
                world.breakBlock(checkPos, true, player);
                axe.damage(1, player);
                PlayerBlockBreakEvents.AFTER.invoker().afterBlockBreak(world, player, checkPos, state, null);
            }
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
        final int effectRadius = 8;
        final int bonusBolts = 1;
        final int effectDelayTicks = 12;
        List<MobEntity> mobs = target.getWorld().getEntitiesByClass(MobEntity.class, Box.of(target.getEyePos(), effectRadius, effectRadius, effectRadius), mob -> true);
        for (int i = 0; i < mobs.size() + bonusBolts; i++)
        {
            TickTimers.schedule(() -> {
                player.addStatusEffect(new StatusEffectInstance(WillowEffects.LIGHTNING_RESISTANCE, effectDelayTicks, 1));
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
        final int cooldownTicks = 4 * 20;
        cooldownManager.set(stack, cooldownTicks);
    }


}
