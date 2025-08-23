package phi.willow.screens;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import phi.willow.Willow;
import phi.willow.clienthooks.ScreenOpeningHooks;
import phi.willow.data.Profession;
import phi.willow.data.ProfessionLevel;
import phi.willow.registry.WillowNetworking;
import phi.willow.util.ProfessionUtil;

import java.util.ArrayList;
import java.util.List;

public class LogbookScreen extends Screen {

    private int marginLeft = 0;
    private int marginTop = 0;
    private int elementMargin = 0;
    private int rowContentVerticalOffset = 0;
    private final int buttonPadding = 8;
    private final List<Integer> rowStarts = new ArrayList<>();
    private final List<ButtonWidget> levelButtons = new ArrayList<>();

    public LogbookScreen() {
        super(Text.literal("Journeyman's Logbook"));
    }

    // TODO: translatable text on everything
    @Override
    protected void init() {
        marginLeft = (int) (this.width * 0.05f);
        marginTop = (int) (this.height * 0.05f);
        elementMargin = (int) (this.width * 0.025f);
        rowContentVerticalOffset = this.textRenderer.fontHeight + 10;
        final int rows = Profession.values().length;
        final ClientPlayerEntity player = MinecraftClient.getInstance().player;

        int c = (this.height - marginTop) / rows;
        for (int i = 0; i < rows; i++)
        {
            this.rowStarts.add(i * c);
            Profession profession = Profession.values()[i];
            ProfessionLevel level = ProfessionUtil.getProfessionLevel(player, profession);
            Text buttonText = getLevelButtonText(level);
            ButtonWidget button = ButtonWidget.builder(buttonText, (btn -> {
                ClientPlayNetworking.send(new WillowNetworking.TryLevelProfessionC2SPacket(profession));
                this.close();
            })).dimensions(marginLeft, rowStarts.get(i) + rowContentVerticalOffset + marginTop, this.textRenderer.getWidth(buttonText) + buttonPadding, this.textRenderer.fontHeight * 2).build();
            this.addDrawableChild(button);
            this.levelButtons.add(button);
        }
    }

    private Text getLevelButtonText(ProfessionLevel level)
    {
        String raw = "  " + level.playerLevelsToNext;
        return Text.literal(raw).withColor(0xFFAAFF66);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        // TODO: chosen expertise if master
        // TODO: dont show xp out of xp if required is 0
        for (int line = 0; line < rowStarts.size(); line++)
        {
            PlayerEntity player = MinecraftClient.getInstance().player;
            Profession profession = Profession.values()[line];
            ProfessionLevel level = ProfessionUtil.getProfessionLevel(player, profession);
            ButtonWidget button = this.levelButtons.get(line);
            Text buttonText = getLevelButtonText(level);
            button.setMessage(buttonText);
            button.setWidth(this.textRenderer.getWidth(buttonText) + buttonPadding);
            if (level.playerLevelsToNext == 0)
                button.active = false;
            int xp = ProfessionUtil.getXPTowardsNextLevel(player, profession);
            String progressText = xp + " / " + (level.xpToNext == 0 ? "--" : level.xpToNext);
            int rowStart = rowStarts.get(line);
            context.drawText(this.textRenderer, Text.literal(profession.label + " - " + level.label), marginLeft, rowStart + marginTop, 0xFFFFFFFF, true);
            context.drawText(this.textRenderer, Text.literal(progressText), marginLeft + button.getWidth() + elementMargin, rowStart + marginTop + rowContentVerticalOffset + textRenderer.fontHeight / 2 + 1, 0xFFFFFFFF, true);
            context.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.ofVanilla("textures/entity/experience_orb.png"), button.getX() + 2, button.getY() + button.getHeight() / 4, 48, 48, textRenderer.fontHeight, textRenderer.fontHeight, 16, 16, 64, 64, 0xFFAAFF66);
        }
    }
}
