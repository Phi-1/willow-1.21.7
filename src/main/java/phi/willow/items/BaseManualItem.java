package phi.willow.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import phi.willow.data.Profession;
import phi.willow.data.ProfessionLevel;
import phi.willow.util.ProfessionUtil;

public class BaseManualItem extends Item {

    public final Profession profession;
    public final ProfessionLevel level;
    public final int flatXPReward;

    public BaseManualItem(Settings settings, Profession profession, ProfessionLevel level) {
        super(settings);
        this.profession = profession;
        this.level = level;
        this.flatXPReward = (int) (level.xpToNext * 0.1f);
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        if (!(player instanceof ServerPlayerEntity serverPlayer))
            return ActionResult.SUCCESS;
        ItemStack stack = player.getStackInHand(hand);
        ProfessionLevel playerLevel = ProfessionUtil.getProfessionLevel(player, this.profession);
        int levelDifference = this.level.ordinal() - playerLevel.ordinal();
        if (levelDifference <= 0)
        {
            // Give some preset amount of xp
            ProfessionUtil.increaseXP(serverPlayer, this.profession, this.flatXPReward);
            stack.decrementUnlessCreative(1, player);
        }
        else
        {
            // Else, level profession to this manual's level
            ProfessionUtil.levelTo(this.level, this.profession, serverPlayer);
            stack.decrementUnlessCreative(1, player);
        }
        world.playSound(player, player.getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
        return ActionResult.SUCCESS;
    }
}
