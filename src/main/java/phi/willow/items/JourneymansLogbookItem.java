package phi.willow.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import phi.willow.clienthooks.ScreenOpeningHooks;

public class JourneymansLogbookItem extends Item {
    public JourneymansLogbookItem(Settings settings) {
        super(settings
                .maxCount(1));
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient)
            if (ScreenOpeningHooks.openLogbookScreen != null)
                ScreenOpeningHooks.openLogbookScreen.run();
        return ActionResult.SUCCESS;
    }
}
