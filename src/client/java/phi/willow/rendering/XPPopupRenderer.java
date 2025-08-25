package phi.willow.rendering;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import phi.willow.Willow;
import phi.willow.data.Profession;
import phi.willow.data.ProfessionLevel;

import java.util.ArrayList;
import java.util.List;

public class XPPopupRenderer {

    private static final int POPUP_LIFETIME = 60;
    private static final int OVERLAY_LIFETIME = 240;

    private static final List<XPPopup> activePopups = new ArrayList<>();
    // Only render the first one, remove when faded out, then render the next
    private static final List<LevelupOverlay> activeLevelupOverlays = new ArrayList<>();

    private static final Identifier PLUS = Identifier.of(Willow.MOD_ID, "textures/gui/plus.png");
    private static final ImmutableMap<ProfessionLevel, Identifier> MINING_ICONS = new ImmutableMap.Builder<ProfessionLevel, Identifier>()
            .put(ProfessionLevel.NOVICE, Identifier.ofVanilla("textures/item/wooden_pickaxe.png"))
            .put(ProfessionLevel.APPRENTICE, Identifier.ofVanilla("textures/item/stone_pickaxe.png"))
            .put(ProfessionLevel.EXPERT, Identifier.ofVanilla("textures/item/iron_pickaxe.png"))
            .put(ProfessionLevel.MASTER, Identifier.ofVanilla("textures/item/diamond_pickaxe.png"))
            .build();
    private static final ImmutableMap<ProfessionLevel, Identifier> WOODCUTTING_ICONS = new ImmutableMap.Builder<ProfessionLevel, Identifier>()
            .put(ProfessionLevel.NOVICE, Identifier.ofVanilla("textures/item/wooden_axe.png"))
            .put(ProfessionLevel.APPRENTICE, Identifier.ofVanilla("textures/item/stone_axe.png"))
            .put(ProfessionLevel.EXPERT, Identifier.ofVanilla("textures/item/iron_axe.png"))
            .put(ProfessionLevel.MASTER, Identifier.ofVanilla("textures/item/diamond_axe.png"))
            .build();
    private static final ImmutableMap<ProfessionLevel, Identifier> FIGHTING_ICONS = new ImmutableMap.Builder<ProfessionLevel, Identifier>()
            .put(ProfessionLevel.NOVICE, Identifier.ofVanilla("textures/item/wooden_sword.png"))
            .put(ProfessionLevel.APPRENTICE, Identifier.ofVanilla("textures/item/stone_sword.png"))
            .put(ProfessionLevel.EXPERT, Identifier.ofVanilla("textures/item/iron_sword.png"))
            .put(ProfessionLevel.MASTER, Identifier.ofVanilla("textures/item/diamond_sword.png"))
            .build();
    private static final ImmutableMap<ProfessionLevel, Identifier> FARMING_ICONS = new ImmutableMap.Builder<ProfessionLevel, Identifier>()
            .put(ProfessionLevel.NOVICE, Identifier.ofVanilla("textures/item/wooden_hoe.png"))
            .put(ProfessionLevel.APPRENTICE, Identifier.ofVanilla("textures/item/stone_hoe.png"))
            .put(ProfessionLevel.EXPERT, Identifier.ofVanilla("textures/item/iron_hoe.png"))
            .put(ProfessionLevel.MASTER, Identifier.ofVanilla("textures/item/diamond_hoe.png"))
            .build();

    public static void createPopup(Profession profession, ProfessionLevel level)
    {
        activePopups.add(new XPPopup(profession, level, POPUP_LIFETIME, 0.0f, 0.0f, (float) (Math.random() * 2 - 1) * 0.8f, (float) (Math.random() * 2 - 1) * 0.8f));
    }

    public static void createLevelupNotification(Profession profession, ProfessionLevel level)
    {
        activeLevelupOverlays.add(new LevelupOverlay(profession, level, OVERLAY_LIFETIME));
    }

    public static void render(DrawContext context, RenderTickCounter tickCounter)
    {
        renderPopups(context, tickCounter);
        renderOverlay(context, tickCounter);
    }

    private static void renderOverlay(DrawContext context, RenderTickCounter tickCounter)
    {
        if (activeLevelupOverlays.isEmpty())
            return;
        LevelupOverlay overlay = activeLevelupOverlays.getFirst();
        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int x = width / 2 - textRenderer.getWidth(overlay.text) / 2;
        int y = height / 2 - textRenderer.fontHeight * 2;
        float opacity = overlay.lifeTime > OVERLAY_LIFETIME / 2.0f ? 1.0f : overlay.lifeTime / (OVERLAY_LIFETIME / 2.0f);
        context.drawText(textRenderer, overlay.text, x, y, ColorHelper.fromFloats(opacity, 1.0f, 1.0f, 1.0f), true);
        overlay.lifeTime -= tickCounter.getTickProgress(false);
        if (overlay.lifeTime <= 0)
            activeLevelupOverlays.removeFirst();
    }

    private static void renderPopups(DrawContext context, RenderTickCounter tickCounter)
    {
        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();
        List<XPPopup> toRemove = new ArrayList<>();
        for (XPPopup popup : activePopups)
        {
            Identifier icon = switch(popup.profession)
            {
                case MINING -> MINING_ICONS.get(popup.level);
                case WOODCUTTING -> WOODCUTTING_ICONS.get(popup.level);
                case FARMING -> FARMING_ICONS.get(popup.level);
                case FIGHTING -> FIGHTING_ICONS.get(popup.level);
            };
            float opacity = Math.max(0, popup.lifeTime / POPUP_LIFETIME);
            int plusOffset = 4;
            int size = 16;
            context.drawTexture(RenderPipelines.GUI_TEXTURED, icon, (int) (width / 2.0f + popup.x - size / 2.0f), (int) (height / 2.0f + popup.y - size / 2.0f), 0, 0, size, size, size, size, ColorHelper.fromFloats(opacity, 1.0f, 1.0f, 1.0f));
            context.drawTexture(RenderPipelines.GUI_TEXTURED, PLUS, (int) (width / 2.0f + popup.x - size / 2.0f) + plusOffset, (int) (height / 2.0f + popup.y - size / 2.0f) + plusOffset, 0, 0, size, size, size, size, ColorHelper.fromFloats(opacity, 1.0f, 1.0f, 1.0f));

            popup.lifeTime -= tickCounter.getTickProgress(false);
            if (popup.lifeTime <= 0)
            {
                toRemove.add(popup);
                continue;
            }
            popup.x += popup.xSpeed * tickCounter.getTickProgress(false);
            popup.y += popup.ySpeed * tickCounter.getTickProgress(false);
        }
        activePopups.removeAll(toRemove);
    }

    // TODO: golden popup
    private static class XPPopup
    {
        public Profession profession;
        public ProfessionLevel level;
        public float lifeTime;
        public float x, y;
        public float xSpeed, ySpeed;

        public XPPopup(Profession profession, ProfessionLevel level, float lifeTime, float x, float y, float xSpeed, float ySpeed) {
            this.profession = profession;
            this.level = level;
            this.lifeTime = lifeTime;
            this.x = x;
            this.y = y;
            this.xSpeed = xSpeed;
            this.ySpeed = ySpeed;
        }
    }

    // TODO: translatable text
    private static class LevelupOverlay
    {
        public Profession profession;
        public ProfessionLevel level;
        public float lifeTime;
        public Text text;

        public LevelupOverlay(Profession profession, ProfessionLevel level, float lifeTime)
        {
            this.profession = profession;
            this.level = level;
            this.lifeTime = lifeTime;
            this.text = Text.literal("You have reached " + this.level.label + " in " + this.profession.label);
        }
    }
}
