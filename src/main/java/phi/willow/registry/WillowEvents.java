package phi.willow.registry;

import com.mojang.datafixers.kinds.Const;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.World;
import phi.willow.Willow;
import phi.willow.data.PlayerProfessionState;
import phi.willow.data.Profession;
import phi.willow.data.ProfessionLevel;
import phi.willow.data.WillowPersistentState;
import phi.willow.items.SledgeHammerItem;
import phi.willow.util.ProfessionUtil;
import phi.willow.util.TickTimers;

import java.util.List;
import java.util.Objects;

public class WillowEvents {

    public static void register()
    {
        ServerTickEvents.END_WORLD_TICK.register(TickTimers::onServerTick);
        LootTableEvents.MODIFY.register(WillowEvents::addStickDropToLeaves);
        LootTableEvents.MODIFY.register(WillowEvents::addHeraldDrop);
        LootTableEvents.MODIFY.register(WillowEvents::addManualsToLootTables);
        LootTableEvents.MODIFY.register(WillowEvents::addSmithingTemplatesToLootTables);
        if (FabricLoader.getInstance().isModLoaded("terralith"))
            LootTableEvents.MODIFY.register(WillowEvents::modifyTerralithLootTables);
        FuelRegistryEvents.BUILD.register(WillowEvents::registerFuelItems);
        TradeOfferHelper.registerWanderingTraderOffers(WillowEvents::addWanderingTraderTrades);
        PlayerBlockBreakEvents.AFTER.register(WillowEvents::gainBlockBreakXP);
        ServerLivingEntityEvents.AFTER_DAMAGE.register(WillowEvents::gainHurtOrBlockFightingXP);
        ServerLivingEntityEvents.AFTER_DEATH.register(WillowEvents::gainKillFightingXP);
        // TODO: reenable once fixed
//        ServerLivingEntityEvents.AFTER_DAMAGE.register(WillowEvents::doSledgehammerKnockback);

        ServerPlayerEvents.JOIN.register((player) -> {
            PlayerProfessionState state = WillowPersistentState.getServerState(player.getServer()).getPlayerProfessionState().getPlayerState(player);
            // FIXME: this plays the levelup sound if player is higher than novice in anything -> would be fixed by adding a separate levelup packet sent from server, instead of client calculating it
            WillowNetworking.syncPlayerProfessionState(player, state);
        });


        // TODO: add legendary sword to weaponsmith trades

        ServerTickEvents.START_WORLD_TICK.register(WillowEvents::armorProficiencyTick);
    }

    // TODO: move to sledgehammer class, into postDamageEntity
    private static void doSledgehammerKnockback(LivingEntity living, DamageSource source, float baseDamage, float damage, boolean blocked) {
        // TODO: prevent if not fully charged, and if not high enough level
        if (!(source.getAttacker() instanceof PlayerEntity player))
            return;
        ItemStack stack = source.getWeaponStack();
        if (stack == null || !(stack.getItem() instanceof SledgeHammerItem sledgeHammerItem))
            return;
        final int range = sledgeHammerItem.type == SledgeHammerItem.Type.HAMMER_OF_THE_DEEP ? 8 : 6;
        final float strength = sledgeHammerItem.type == SledgeHammerItem.Type.HAMMER_OF_THE_DEEP ? 1.2f : 0.8f;
        List<HostileEntity> mobs = living.getWorld().getEntitiesByClass(HostileEntity.class, Box.of(living.getPos(), range, range, range), LivingEntity::isAlive);
        for (HostileEntity mob : mobs)
        {
            mob.takeKnockback(strength, player.getX() - mob.getX(), player.getZ() - mob.getZ());
        }
        player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.PLAYERS, 1.0f, 1.0f);
    }

    private static void addSmithingTemplatesToLootTables(RegistryKey<LootTable> key, LootTable.Builder builder, LootTableSource source, RegistryWrapper.WrapperLookup registries)
    {
        LootPool.Builder pool = LootPool.builder();
        if (key == LootTables.ANCIENT_CITY_CHEST)
        {
            pool.with(ItemEntry.builder(WillowItems.ECHOIC_UPGRADE_SMITHING_TEMPLATE).conditionally(RandomChanceLootCondition.builder(0.2f)));
        }
    }

    private static void addManualsToLootTables(RegistryKey<LootTable> key, LootTable.Builder builder, LootTableSource source, RegistryWrapper.WrapperLookup registries)
    {
        LootPool.Builder pool = LootPool.builder();
        if (key == LootTables.HERO_OF_THE_VILLAGE_LIBRARIAN_GIFT_GAMEPLAY)
        {
            addAllManualsOfLevel(pool, ProfessionLevel.APPRENTICE, 0.3f);
            addAllManualsOfLevel(pool, ProfessionLevel.EXPERT, 0.05f);
            addAllManualsOfLevel(pool, ProfessionLevel.MASTER, 0.01f);
        }
        else if (key == LootTables.HERO_OF_THE_VILLAGE_FARMER_GIFT_GAMEPLAY)
        {
            addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.3f, Profession.FARMING);
            addManualsOfProfessions(pool, ProfessionLevel.EXPERT, 0.1f, Profession.FARMING);
            addManualsOfProfessions(pool, ProfessionLevel.MASTER, 0.05f, Profession.FARMING);
        }
        else if (key == LootTables.FISHING_TREASURE_GAMEPLAY)
        {
            builder.modifyPools(existingPool -> addAllManualsOfLevel(existingPool, ProfessionLevel.APPRENTICE, 1.0f));
        }
        else if (key == LootTables.ABANDONED_MINESHAFT_CHEST)
        {
            addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.1f, Profession.MINING);
            addManualsOfProfessions(pool, ProfessionLevel.EXPERT, 0.04f, Profession.MINING);
            pool.rolls(ConstantLootNumberProvider.create(2.0f));
        }
        else if (key == LootTables.ANCIENT_CITY_CHEST)
        {
            addAllManualsOfLevel(pool, ProfessionLevel.APPRENTICE, 0.3f);
            addAllManualsOfLevel(pool, ProfessionLevel.EXPERT, 0.2f);
            addAllManualsOfLevel(pool, ProfessionLevel.MASTER, 0.04f);
            pool.rolls(ConstantLootNumberProvider.create(3.0f));
        }
        else if (key == LootTables.BASTION_BRIDGE_CHEST)
        {
            addAllManualsOfLevel(pool, ProfessionLevel.APPRENTICE, 0.3f);
            addAllManualsOfLevel(pool, ProfessionLevel.EXPERT, 0.2f);
            pool.rolls(ConstantLootNumberProvider.create(2.0f));
        }
        else if (key == LootTables.BASTION_HOGLIN_STABLE_CHEST)
        {
            addAllManualsOfLevel(pool, ProfessionLevel.APPRENTICE, 0.3f);
            addAllManualsOfLevel(pool, ProfessionLevel.EXPERT, 0.2f);
            pool.rolls(ConstantLootNumberProvider.create(2.0f));
        }
        else if (key == LootTables.BASTION_OTHER_CHEST)
        {
            addAllManualsOfLevel(pool, ProfessionLevel.APPRENTICE, 0.3f);
            addAllManualsOfLevel(pool, ProfessionLevel.EXPERT, 0.2f);
            pool.rolls(ConstantLootNumberProvider.create(2.0f));
        }
        else if (key == LootTables.BASTION_TREASURE_CHEST)
        {
            addAllManualsOfLevel(pool, ProfessionLevel.APPRENTICE, 0.2f);
            addAllManualsOfLevel(pool, ProfessionLevel.EXPERT, 0.15f);
            addAllManualsOfLevel(pool, ProfessionLevel.MASTER, 0.02f);
            pool.rolls(ConstantLootNumberProvider.create(3.0f));
        }
        else if (key == LootTables.BURIED_TREASURE_CHEST)
        {
            addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.3f, Profession.FIGHTING);
            addManualsOfProfessions(pool, ProfessionLevel.EXPERT, 0.05f, Profession.FIGHTING);
        }
        else if (key == LootTables.DESERT_PYRAMID_CHEST)
        {
            addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.05f, Profession.FARMING, Profession.FIGHTING);
            addManualsOfProfessions(pool, ProfessionLevel.EXPERT, 0.01f, Profession.FARMING, Profession.FIGHTING);
            pool.rolls(ConstantLootNumberProvider.create(2.0f));
        }
        else if (key == LootTables.IGLOO_CHEST_CHEST)
        {
            addAllManualsOfLevel(pool, ProfessionLevel.APPRENTICE, 0.15f);
            pool.rolls(ConstantLootNumberProvider.create(2.0f));
        }
        else if (key == LootTables.JUNGLE_TEMPLE_CHEST)
        {
            addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.2f, Profession.WOODCUTTING, Profession.FARMING);
            addManualsOfProfessions(pool, ProfessionLevel.EXPERT, 0.02f, Profession.WOODCUTTING, Profession.FARMING);
            pool.rolls(ConstantLootNumberProvider.create(2.0f));
        }
        else if (key == LootTables.NETHER_BRIDGE_CHEST)
        {
            addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.2f, Profession.FIGHTING, Profession.FARMING, Profession.MINING);
            addManualsOfProfessions(pool, ProfessionLevel.EXPERT, 0.1f, Profession.FIGHTING, Profession.FARMING, Profession.MINING);
            pool.rolls(ConstantLootNumberProvider.create(2.0f));
        }
        else if (key == LootTables.PILLAGER_OUTPOST_CHEST)
        {
            addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.2f, Profession.WOODCUTTING, Profession.FIGHTING);
            addManualsOfProfessions(pool, ProfessionLevel.EXPERT, 0.05f, Profession.WOODCUTTING, Profession.FIGHTING);
            pool.rolls(ConstantLootNumberProvider.create(2.0f));
        }
        else if (key == LootTables.SHIPWRECK_MAP_CHEST)
        {
            addAllManualsOfLevel(pool, ProfessionLevel.APPRENTICE, 0.1f);
            pool.rolls(ConstantLootNumberProvider.create(3.0f));
        }
        else if (key == LootTables.SHIPWRECK_SUPPLY_CHEST)
        {
            addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.3f, Profession.FARMING);
        }
        else if (key == LootTables.SIMPLE_DUNGEON_CHEST)
        {
            addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.3f, Profession.FIGHTING, Profession.MINING);
            addManualsOfProfessions(pool, ProfessionLevel.EXPERT, 0.04f, Profession.FIGHTING, Profession.MINING);
        }
        else if (key == LootTables.SPAWN_BONUS_CHEST)
        {
            addAllManualsOfLevel(pool, ProfessionLevel.APPRENTICE, 0.5f);
            pool.rolls(ConstantLootNumberProvider.create(4.0f));
        }
        else if (key == LootTables.STRONGHOLD_CORRIDOR_CHEST)
        {
            addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.3f, Profession.FIGHTING, Profession.MINING);
            addManualsOfProfessions(pool, ProfessionLevel.EXPERT, 0.1f, Profession.FIGHTING, Profession.MINING);
            addManualsOfProfessions(pool, ProfessionLevel.MASTER, 0.01f, Profession.FIGHTING, Profession.MINING);
        }
        else if (key == LootTables.STRONGHOLD_CROSSING_CHEST)
        {
            addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.3f, Profession.FIGHTING, Profession.MINING);
            addManualsOfProfessions(pool, ProfessionLevel.EXPERT, 0.1f, Profession.FIGHTING, Profession.MINING);
        }
        else if (key == LootTables.STRONGHOLD_LIBRARY_CHEST)
        {
            addAllManualsOfLevel(pool, ProfessionLevel.APPRENTICE, 0.3f);
            addAllManualsOfLevel(pool, ProfessionLevel.EXPERT, 0.1f);
            addAllManualsOfLevel(pool, ProfessionLevel.MASTER, 0.01f);
            pool.rolls(ConstantLootNumberProvider.create(3.0f));
        }
        else if (key == LootTables.UNDERWATER_RUIN_SMALL_CHEST)
        {
            addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.3f, Profession.FARMING);
        }
        else if (key == LootTables.UNDERWATER_RUIN_BIG_CHEST)
        {
            addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.3f, Profession.FARMING, Profession.FIGHTING);
        }
        else if (key == LootTables.WOODLAND_MANSION_CHEST)
        {
            addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.2f, Profession.FARMING, Profession.FIGHTING, Profession.WOODCUTTING);
            addManualsOfProfessions(pool, ProfessionLevel.EXPERT, 0.1f, Profession.FARMING, Profession.FIGHTING, Profession.WOODCUTTING);
            addManualsOfProfessions(pool, ProfessionLevel.MASTER, 0.02f, Profession.FARMING, Profession.FIGHTING, Profession.WOODCUTTING);
            pool.rolls(ConstantLootNumberProvider.create(3.0f));
        }
        else if (key == LootTables.VILLAGE_PLAINS_CHEST || key == LootTables.VILLAGE_SAVANNA_HOUSE_CHEST)
        {
            addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.1f);
        }
        else if (key == LootTables.VILLAGE_SNOWY_HOUSE_CHEST || key == LootTables.VILLAGE_TAIGA_HOUSE_CHEST)
        {
            addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.1f, Profession.WOODCUTTING);
        }
        else if (key == LootTables.VILLAGE_DESERT_HOUSE_CHEST)
        {
            addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.05f, Profession.FARMING);
            addManualsOfProfessions(pool, ProfessionLevel.EXPERT, 0.02f, Profession.FARMING);
            pool.rolls(ConstantLootNumberProvider.create(2.0f));
        }
        else if (key == LootTables.VILLAGE_WEAPONSMITH_CHEST || key == LootTables.VILLAGE_ARMORER_CHEST)
        {
            addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.3f, Profession.FIGHTING);
        }
        else
            return;
        builder.pool(pool);
    }

    private static void addAllManualsOfLevel(LootPool.Builder pool, ProfessionLevel level, float chance)
    {
        addManualsOfProfessions(pool, level, chance, Profession.values());
    }

    private static void addManualsOfProfessions(LootPool.Builder pool, ProfessionLevel level, float chance, Profession ...professions)
    {
        for (Profession profession : professions)
        {
            pool.with(ItemEntry.builder(getManualFor(profession, level)).conditionally(RandomChanceLootCondition.builder(chance)));
        }
    }

    private static Item getManualFor(Profession profession, ProfessionLevel level)
    {
        return switch (profession)
        {
            case MINING -> switch (level)
            {
                case NOVICE -> Items.AIR;
                case APPRENTICE -> WillowItems.APPRENTICE_MINING_MANUAL;
                case EXPERT -> WillowItems.EXPERT_MINING_MANUAL;
                case MASTER -> WillowItems.MASTER_MINING_MANUAL;
            };
            case WOODCUTTING -> switch (level)
            {
                case NOVICE -> Items.AIR;
                case APPRENTICE -> WillowItems.APPRENTICE_WOODCUTTING_MANUAL;
                case EXPERT -> WillowItems.EXPERT_WOODCUTTING_MANUAL;
                case MASTER -> WillowItems.MASTER_WOODCUTTING_MANUAL;
            };
            case FARMING -> switch(level)
            {
                case NOVICE -> Items.AIR;
                case APPRENTICE -> WillowItems.APPRENTICE_FARMING_MANUAL;
                case EXPERT -> WillowItems.EXPERT_FARMING_MANUAL;
                case MASTER -> WillowItems.MASTER_FARMING_MANUAL;
            };
            case FIGHTING -> switch(level)
            {
                case NOVICE -> Items.AIR;
                case APPRENTICE -> WillowItems.APPRENTICE_FIGHTING_MANUAL;
                case EXPERT -> WillowItems.EXPERT_FIGHTING_MANUAL;
                case MASTER -> WillowItems.MASTER_FIGHTING_MANUAL;
            };
        };
    }

    private static void gainHurtOrBlockFightingXP(LivingEntity living, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked)
    {
        // Increase xp on block
        if (living instanceof ServerPlayerEntity player && blocked)
            ProfessionUtil.gainBaseXP(Profession.FIGHTING, player, 1, false);
        // And on damaging another entity
        else if (source.getAttacker() instanceof ServerPlayerEntity player)
        {
            // Gain 1 flat xp on small hits
            float modifier = baseDamageTaken <= 1 ? 1.0f / (float) Profession.FIGHTING.instanceXP : 1.0f;
            ProfessionUtil.gainBaseXP(Profession.FIGHTING, player, modifier, false);
        }
    }

    private static void gainKillFightingXP(LivingEntity entity, DamageSource source)
    {
        if (source.getAttacker() instanceof ServerPlayerEntity player && entity instanceof HostileEntity)
            ProfessionUtil.gainBaseXP(Profession.FIGHTING, player, 2, false);
    }

    private static void gainBlockBreakXP(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity)
    {
        if (!(player instanceof ServerPlayerEntity serverPlayer))
            return;
        ItemStack stack = player.getMainHandStack();
        if (!stack.getItem().isCorrectForDrops(stack, state))
            return;
        if (stack.isIn(ItemTags.PICKAXES))
        {
            if (!ProfessionUtil.canUseToolAtLevel(ProfessionUtil.getProfessionLevel(player, Profession.MINING), stack))
                return;
            float mult = 1;
            if (state.isIn(BlockTags.NEEDS_DIAMOND_TOOL))
                mult = 3;
            else if (state.isIn(BlockTags.NEEDS_IRON_TOOL))
                mult = 2;
            else if (state.isIn(BlockTags.NEEDS_STONE_TOOL))
                mult = 1.5f;
            if (state.isIn(WillowTags.Blocks.VANILLA_ORES))
                mult += 3.0f;
            ProfessionUtil.gainBaseXP(Profession.MINING, serverPlayer, mult, false);
        }
        else if (stack.isIn(ItemTags.SHOVELS))
        {
            if (!ProfessionUtil.canUseToolAtLevel(ProfessionUtil.getProfessionLevel(player, Profession.MINING), stack))
                return;
            if (world.random.nextInt(4) == 0)
                ProfessionUtil.gainBaseXP(Profession.MINING, serverPlayer, 1,  false);
        }
        else if (stack.isIn(ItemTags.AXES) && state.isIn(BlockTags.LOGS))
        {
            if (!ProfessionUtil.canUseToolAtLevel(ProfessionUtil.getProfessionLevel(player, Profession.WOODCUTTING), stack))
                return;
            float mult = 1;
            if (state.isIn(BlockTags.NEEDS_DIAMOND_TOOL))
                mult = 3;
            else if (state.isIn(BlockTags.NEEDS_IRON_TOOL))
                mult = 2;
            else if (state.isIn(BlockTags.NEEDS_STONE_TOOL))
                mult = 1.5f;
            ProfessionUtil.gainBaseXP(Profession.WOODCUTTING, serverPlayer, mult, false);
        }
    }

    private static void addWanderingTraderTrades(TradeOfferHelper.WanderingTraderOffersBuilder builder)
    {
        // NOTE: you can add a custom pool as well for custom traders with the pool.pool function
        // The special pool is 7 items big by default, and every trader picks 2 items from it at random. So the excavator has a 2 in 8 chance to show up currently
        builder.addOffersToPool(TradeOfferHelper.WanderingTraderOffersBuilder.SELL_SPECIAL_ITEMS_POOL, new TradeOffers.SellItemFactory(WillowItems.EXCAVATOR, 12, 1, 80));
    }

    private static void registerFuelItems(FuelRegistry.Builder builder, FuelRegistryEvents.Context context)
    {
        final int itemSmeltTime = 200;
        builder.add(WillowItems.KINDLING, itemSmeltTime * 4);
    }

    private static void modifyTerralithLootTables(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries)
    {
        // TODO: prolly just write a script for all the keys
        if (!Objects.equals(key.getValue().getNamespace(), "terralith"))
            return;
        LootPool.Builder pool = LootPool.builder();
        switch (key.getValue().getPath()) {
            case "village/fortified/smith/novice",
                 "village/desert/smith/novice" ->
            {
                addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.3f, Profession.MINING);
            }
            case "village/fortified/smith/expert",
                 "village/desert/smith/expert" ->
            {
                addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.2f, Profession.MINING);
                addManualsOfProfessions(pool, ProfessionLevel.EXPERT, 0.5f, Profession.MINING);
                pool.rolls(ConstantLootNumberProvider.create(2.0f));
            }
            case "village/fortified/archer",
                 "village/desert/archer" ->
            {
                addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.4f, Profession.FIGHTING);
            }
            // TODO: butcher cooking?
            case "village/fortified/cartographer",
                 "village/desert/cartographer" ->
            {
                addAllManualsOfLevel(pool, ProfessionLevel.APPRENTICE, 0.1f);
                pool.rolls(ConstantLootNumberProvider.create(2.0f));
            }
            case "village/fortified/generic",
                 "village/fortified/generic_low",
                 "village/desert/generic",
                 "village/desert/generic_low" ->
            {
                addAllManualsOfLevel(pool, ProfessionLevel.APPRENTICE, 0.05f);
            }
            case "village/fortified/library",
                 "village/desert/library" ->
            {
                addAllManualsOfLevel(pool, ProfessionLevel.APPRENTICE, 0.1f);
                addAllManualsOfLevel(pool, ProfessionLevel.EXPERT, 0.05f);
                pool.rolls(ConstantLootNumberProvider.create(3.0f));
            }
            case "village/fortified/mason",
                 "village/desert/mason" ->
            {
                // TODO: building manuals?
                // TODO: excavating instead of mining?
                addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.2f, Profession.MINING);
            }
            case "village/fortified/treasure",
                 "village/desert/treasure" ->
            {
                addAllManualsOfLevel(pool, ProfessionLevel.EXPERT, 0.1f);
            }
            case "village/fortified/tavern_downstairs",
                 "village/fortified/tavern_upstairs" ->
            {
                // TODO: willow foods
            }
            case "desert_outpost" ->
            {
                addManualsOfProfessions(pool, ProfessionLevel.APPRENTICE, 0.2f, Profession.FIGHTING);
                addManualsOfProfessions(pool, ProfessionLevel.EXPERT, 0.05f, Profession.FIGHTING);
                pool.rolls(ConstantLootNumberProvider.create(2.0f));
            }
//            case "village/fortified/fisherman":
//            case "village/desert/fisherman":
//                // TODO: fishing manuals?
//                break;
//            case "mage/barracks",
//                 "mage/extras",
//                 "mage/treasure" ->
//            case "witch_hut":
//            case "":
//                // TODO: alchemy manuals
//                break;
        };
        tableBuilder.pool(pool);
    }

    private static void addStickDropToLeaves(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries)
    {
        final List<RegistryKey<LootTable>> leaves = List.of(
                Blocks.OAK_LEAVES.getLootTableKey().orElseThrow(),
                Blocks.BIRCH_LEAVES.getLootTableKey().orElseThrow(),
                Blocks.SPRUCE_LEAVES.getLootTableKey().orElseThrow(),
                Blocks.JUNGLE_LEAVES.getLootTableKey().orElseThrow(),
                Blocks.ACACIA_LEAVES.getLootTableKey().orElseThrow(),
                Blocks.DARK_OAK_LEAVES.getLootTableKey().orElseThrow(),
                Blocks.MANGROVE_LEAVES.getLootTableKey().orElseThrow(),
                Blocks.CHERRY_LEAVES.getLootTableKey().orElseThrow(),
                Blocks.PALE_OAK_LEAVES.getLootTableKey().orElseThrow(),
                Blocks.AZALEA_LEAVES.getLootTableKey().orElseThrow(),
                Blocks.FLOWERING_AZALEA_LEAVES.getLootTableKey().orElseThrow()
        );
        if (leaves.contains(key))
        {
            tableBuilder.pool(LootPool.builder()
                    .with(ItemEntry.builder(Items.STICK))
                    .conditionally(RandomChanceLootCondition.builder(0.1f)));
        }
    }

    private static void addHeraldDrop(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries)
    {
        if (key == LootTables.HERO_OF_THE_VILLAGE_WEAPONSMITH_GIFT_GAMEPLAY)
        {
            tableBuilder.pool(LootPool.builder().conditionally(RandomChanceLootCondition.builder(0.2f)).with(ItemEntry.builder(WillowItems.THE_HERALD)));
        }
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
                if (armorPiece.isEmpty() || !armorPiece.isIn(WillowTags.Items.MASTER_USABLE_EQUIPMENT))
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
