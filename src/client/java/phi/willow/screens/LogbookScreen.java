package phi.willow.screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import phi.willow.data.Profession;
import phi.willow.data.client.SyncedProfessionState;

public class LogbookScreen extends Screen {
    public LogbookScreen() {
        super(Text.literal("Journeyman's Logbook"));
    }

    @Override
    protected void init() {
        ButtonWidget button = ButtonWidget.builder(Text.literal("hey"), (btn -> {

        })).dimensions(120, 40, 40, this.textRenderer.fontHeight).build();
        this.addDrawableChild(button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        final int marginLeft = 40;
        final int paddingText = 10;
        // Minecraft doesn't have a "label" widget, so we'll have to draw our own text.
        // We'll subtract the font height from the Y position to make the text appear above the button.
        // Subtracting an extra 10 pixels will give the text some padding.
        // textRenderer, text, x, y, color, hasShadow
        for (int line = 0; line < Profession.values().length; line++)
        {
            context.drawText(this.textRenderer, Text.literal(Profession.values()[line].label), marginLeft, (marginLeft + marginLeft * line) - this.textRenderer.fontHeight - paddingText, 0xFFFFFFFF, true);
        }
    }
}
