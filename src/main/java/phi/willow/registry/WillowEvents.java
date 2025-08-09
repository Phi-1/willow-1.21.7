package phi.willow.registry;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import phi.willow.Willow;
import phi.willow.data.PlayerProfessionState;
import phi.willow.data.Profession;
import phi.willow.data.ProfessionLevel;
import phi.willow.data.WillowPersistentState;
import phi.willow.util.ProfessionUtil;

import java.util.Optional;

public class WillowEvents {

    public static void register()
    {
        // TODO: xp stuff
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, blockState, blockEntity) -> {
            if (world.isClient)
                return;
            MinecraftServer server = world.getServer();
            if (server == null)
                return;
            WillowPersistentState state = WillowPersistentState.getServerState(server);
            state.getTestData().testNumber++;
            player.sendMessage(Text.literal(Integer.toString(state.getTestData().testNumber)), true);

            // NOTE: world#breakBlock does not trigger this event
//            world.breakBlock(pos.down(), true, player);
            Willow.LOGGER.info(WillowTags.Items.NOVICE_USABLE_TOOLS.toString());
        });

        // TODO: manuals
        LootTableEvents.MODIFY.register(((key, tableBuilder, source, registries) -> {
            Optional<RegistryKey<LootTable>> zombie = EntityType.ZOMBIE.getLootTableKey();
            if (zombie.isEmpty())
                return;
            if (key == zombie.get() || LootTables.VILLAGE_WEAPONSMITH_CHEST == key) {
                tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(Items.ANCIENT_DEBRIS)));
            }
        }));

        ServerPlayerEvents.JOIN.register((player) -> {
            PlayerProfessionState state = WillowPersistentState.getServerState(player.getServer()).getPlayerProfessionState().getPlayerState(player);
            // TODO: test with higher levels
            WillowNetworking.syncPlayerProfessionState(player, state);
        });

        ServerTickEvents.START_WORLD_TICK.register(WillowEvents::armorProficiencyTick);
    }

    public static final Identifier ARMOR_MOVEMENT_DEBUFF = Identifier.of(Willow.MOD_ID, "armor_movement_debuff");
    public static final double ARMOR_MOVEMENT_DEBUFF_AMOUNT = -0.3;

    private static void armorProficiencyTick(ServerWorld world)
    {
        if (world.getServer().getTicks() % 20 != 0)
            return;
        for (ServerPlayerEntity player : world.getPlayers())
        {
            int nDebuffs = 0;
            for (int i = 36; i <= 39; i++)
            {
                ItemStack armorPiece = player.getInventory().getStack(i);
                if (armorPiece.isEmpty() || !armorPiece.isIn(WillowTags.Items.MASTER_USABLE_TOOLS))
                    continue;
                ProfessionLevel level = ProfessionUtil.getProfessionLevel(player, Profession.FIGHTING);
                if (!ProfessionUtil.canUseToolAtLevel(level, armorPiece))
                    nDebuffs++;
            }
            EntityAttributeInstance ms = player.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED);
            if (ms == null)
                continue;
            if (nDebuffs > 0)
            {
                int msMult = nDebuffs > 2 ? 2 : 1;
                EntityAttributeModifier modifier = new EntityAttributeModifier(ARMOR_MOVEMENT_DEBUFF, ARMOR_MOVEMENT_DEBUFF_AMOUNT * msMult, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
                if (ms.hasModifier(ARMOR_MOVEMENT_DEBUFF))
                    ms.updateModifier(modifier);
                else
                    ms.addTemporaryModifier(modifier);
            }
            else
            {
                if (ms.hasModifier(ARMOR_MOVEMENT_DEBUFF))
                    ms.removeModifier(ARMOR_MOVEMENT_DEBUFF);
            }
        }
    }

}
