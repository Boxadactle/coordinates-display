package me.boxadactle.coordinatesdisplay.util;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
import java.text.DecimalFormat;

public class HudRenderer {

    private final MinecraftClient client = MinecraftClient.getInstance();

    int w = 0;
    int h = 0;

    int x = 0;
    int y = 0;

    int scaleSize;

    float scale = CoordinatesDisplay.CONFIG.get().hudScale;

    public HudRenderer() {}

    public boolean isHovered(int mouseX, int mouseY) {
        return ModUtil.isMouseHovering(Math.round(mouseX / scale), Math.round(mouseY / scale), x, y, w, h);
    }

    public boolean isScaleButtonHovered(int mouseX, int mouseY) {
        int scaleX = (x + w - scaleSize);
        int scaleY = (y + h - scaleSize);
        return ModUtil.isMouseHovering(Math.round(mouseX / scale), Math.round(mouseY / scale), scaleX, scaleY, scaleX + scaleSize, scaleY + scaleSize);
    }

    public void render(DrawContext drawContext, Vec3d pos, ChunkPos chunkPos, float cameraYaw, float cameraPitch, RegistryEntry<Biome> biome, int x, int y, boolean minMode, boolean moveOverlay) {
        try {
            if (!minMode) {
                renderOverlay(drawContext, pos, chunkPos, cameraYaw, biome, x, y);
            } else {
                renderOverlayMin(drawContext, pos, cameraYaw, cameraPitch, biome, x, y);
            }

            if (moveOverlay) {
                renderMoveOverlay(drawContext, x, y);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void render(DrawContext drawContext, Vec3d pos, ChunkPos chunkPos, float cameraYaw, float cameraPitch, RegistryEntry<Biome> biome, int x, int y, boolean minMode, boolean moveOverlay, float scale) {
        try {
            MatrixStack matrices = drawContext.getMatrices();

            matrices.push();

            matrices.scale(scale, scale, scale);

            this.scale = scale;

            if (!minMode) {
                renderOverlay(drawContext, pos, chunkPos, cameraYaw, biome, x, y);
            } else {
                renderOverlayMin(drawContext, pos, cameraYaw, cameraPitch, biome, x, y);
            }

            if (moveOverlay) {
                renderMoveOverlay(drawContext, x, y);
            }

            matrices.pop();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    private void renderOverlay(DrawContext drawContext, Vec3d pos, ChunkPos chunkPos, float cameraYaw, @Nullable RegistryEntry<Biome> biome, int x, int y) throws NullPointerException {

        this.x = x;
        this.y = y;

        // variables

        DecimalFormat decimalFormat = new DecimalFormat(CoordinatesDisplay.CONFIG.get().roundPosToTwoDecimals ? "0.00" : "0");

        Text xpos = Text.literal(decimalFormat.format(pos.getX())).styled((style -> style.withColor(CoordinatesDisplay.CONFIG.get().dataColor)));
        Text ypos = Text.literal(decimalFormat.format(pos.getY())).styled((style -> style.withColor(CoordinatesDisplay.CONFIG.get().dataColor)));
        Text zpos = Text.literal(decimalFormat.format(pos.getZ())).styled((style -> style.withColor(CoordinatesDisplay.CONFIG.get().dataColor)));

        Text chunkxpos = Text.literal(Integer.toString(chunkPos.x)).styled((style -> style.withColor(CoordinatesDisplay.CONFIG.get().dataColor)));
        Text chunkzpos = Text.literal(Integer.toString(chunkPos.z)).styled((style -> style.withColor(CoordinatesDisplay.CONFIG.get().dataColor)));

        Text biometext;
        if (this.client.world != null) {
            String biomestring = biome != null ? ModUtil.parseIdentifier(ModUtil.getBiomeString(biome)) : "Plains";
            biometext = CoordinatesDisplay.CONFIG.get().renderBiome ? ModUtil.colorize(Text.literal(biomestring), CoordinatesDisplay.BiomeColors.getBiomeColor(biomestring, CoordinatesDisplay.CONFIG.get().dataColor)) :
                    Text.literal("");
        } else
            biometext = Text.literal("Plains").styled((style -> style.withColor(CoordinatesDisplay.CONFIG.get().dataColor)));

        Text dtext = Text.translatable("hud.coordinatesdisplay." + ModUtil.getDirectionFromYaw(MathHelper.wrapDegrees(cameraYaw))).styled((style -> style.withColor(CoordinatesDisplay.CONFIG.get().dataColor)));

        Text xText = Text.translatable("hud.coordinatesdisplay.x", xpos);
        Text yText = Text.translatable("hud.coordinatesdisplay.y", ypos);
        Text zText = Text.translatable("hud.coordinatesdisplay.z", zpos);

        Text chunkX = Text.translatable("hud.coordinatesdisplay.chunk.x", chunkxpos);
        Text chunkZ = Text.translatable("hud.coordinatesdisplay.chunk.z", chunkzpos);

        float yaw = MathHelper.wrapDegrees(cameraYaw);
        Text directionText = CoordinatesDisplay.CONFIG.get().renderDirection ? Text.translatable("hud.coordinatesdisplay.direction", dtext, Text.literal(CoordinatesDisplay.CONFIG.get().renderDirectionInt ? " ("+decimalFormat.format(yaw)+")" : "")) : Text.literal("");

        Text biomeText = CoordinatesDisplay.CONFIG.get().renderBiome ? Text.translatable("hud.coordinatesdisplay.biome", biometext) : Text.literal("");

        Text minecraftVersion = Text.translatable("hud.coordinatesdisplay.version", ModVersion.getMCVersion());

        int th = 10;
        int p = CoordinatesDisplay.CONFIG.get().padding;
        int tp = CoordinatesDisplay.CONFIG.get().textPadding;

        w = ModUtil.calculateHudWidth(p, tp, xText, yText, zText, chunkX, chunkZ, directionText, biomeText, minecraftVersion);
        h = ModUtil.calculateHudHeight(th, p, tp, xText, yText, zText, chunkX, chunkZ, directionText, biomeText, minecraftVersion);

        // rendering
        if (CoordinatesDisplay.CONFIG.get().renderBackground) {
            drawContext.fill(x, y, x + w, y + h, CoordinatesDisplay.CONFIG.get().backgroundColor);
        }


        drawInfo(drawContext, xText, x + p, y + p, CoordinatesDisplay.CONFIG.get().definitionColor);
        drawInfo(drawContext, yText, x + p, y + p + th, CoordinatesDisplay.CONFIG.get().definitionColor);
        drawInfo(drawContext, zText, x + p, y + p + (th * 2), CoordinatesDisplay.CONFIG.get().definitionColor);

        if (CoordinatesDisplay.CONFIG.get().renderChunkData) {
            drawInfo(drawContext, chunkX, x + p + ModUtil.getLongestTextLength(xText, yText, zText) + tp, y + p, CoordinatesDisplay.CONFIG.get().definitionColor);
            drawInfo(drawContext, chunkZ, x + p + ModUtil.getLongestTextLength(xText, yText, zText) + tp, y + p + (th), CoordinatesDisplay.CONFIG.get().definitionColor);
        }

        if (CoordinatesDisplay.CONFIG.get().renderDirection) {
            drawInfo(drawContext, directionText, x + p, y + p + (th * 3) + tp, CoordinatesDisplay.CONFIG.get().definitionColor);
        }

        if (CoordinatesDisplay.CONFIG.get().renderBiome) {
            drawInfo(drawContext, biomeText, x + p, y + p + (th * 3) + tp + (CoordinatesDisplay.CONFIG.get().renderDirection ? th : 0), CoordinatesDisplay.CONFIG.get().definitionColor);
        }

        if (CoordinatesDisplay.CONFIG.get().renderMCVersion) {
            drawInfo(drawContext, minecraftVersion, x + p, y + p + (th * 3) + tp + (CoordinatesDisplay.CONFIG.get().renderDirection ? th : 0) + (CoordinatesDisplay.CONFIG.get().renderBiome ? th : 0), CoordinatesDisplay.CONFIG.get().dataColor);
        }
    }

    private void renderOverlayMin(DrawContext drawContext, Vec3d pos, float cameraYaw, float cameraPitch, @Nullable RegistryEntry<Biome> biome, int x, int y) throws NullPointerException {
        this.x = x;
        this.y = y;

        Text xpos = Text.literal(Long.toString(Math.round(pos.getX()))).styled((style -> style.withColor(CoordinatesDisplay.CONFIG.get().dataColor)));
        Text ypos = Text.literal(Long.toString(Math.round(pos.getY()))).styled((style -> style.withColor(CoordinatesDisplay.CONFIG.get().dataColor)));
        Text zpos = Text.literal(Long.toString(Math.round(pos.getZ()))).styled((style -> style.withColor(CoordinatesDisplay.CONFIG.get().dataColor)));

        Text biometext;
        if (this.client.world != null) {
            String biomestring = biome != null ? ModUtil.parseIdentifier(ModUtil.getBiomeString(biome)) : "Plains";
            biometext = CoordinatesDisplay.CONFIG.get().renderBiome ? ModUtil.colorize(Text.literal(biomestring), CoordinatesDisplay.BiomeColors.getBiomeColor(biomestring, CoordinatesDisplay.CONFIG.get().dataColor)) :
                    Text.literal("");
        } else
            biometext = Text.literal("Plains").styled((style -> style.withColor(CoordinatesDisplay.CONFIG.get().dataColor)));

        float yaw = MathHelper.wrapDegrees(cameraYaw);
        float pitch = MathHelper.wrapDegrees(cameraPitch);
        Text direction = Text.translatable("hud.coordinatesdisplay.min.direction" + ModUtil.getDirectionFromYaw(yaw));

        Text xText = Text.translatable("hud.coordinatesdisplay.min.x", xpos);
        Text yText = Text.translatable("hud.coordinatesdisplay.min.y", ypos);
        Text zText = Text.translatable("hud.coordinatesdisplay.min.z", zpos);

        Text pitchText = Text.literal(pitch > 0 ? "+" : "-");
        Text directionText = Text.translatable("hud.coordinatesdisplay.min." + ModUtil.getDirectionFromYaw(yaw), direction);
        Text yawText = Text.literal(yaw > 0 ? "+" : "-");

        Text biomeText = CoordinatesDisplay.CONFIG.get().renderBiome ? Text.translatable("hud.coordinatesdisplay.min.biome", biometext) : Text.literal("");

        int p = CoordinatesDisplay.CONFIG.get().padding;
        int th = 10;
        int dpadding = CoordinatesDisplay.CONFIG.get().textPadding;

        w = ModUtil.calculateHudWidthMin(p, th, dpadding, xText, yText, zText, yawText, pitchText, directionText, biomeText);
        h = ModUtil.calculateHudHeightMin(p, th);

        // rendering
        if (CoordinatesDisplay.CONFIG.get().renderBackground) {
            drawContext.fill(x, y, x + w, y + h, CoordinatesDisplay.CONFIG.get().backgroundColor);
        }

        drawInfo(drawContext, xText, x + p, y + p, CoordinatesDisplay.CONFIG.get().definitionColor);
        drawInfo(drawContext, yText, x + p, y + p + th, CoordinatesDisplay.CONFIG.get().definitionColor);
        drawInfo(drawContext, zText, x + p, y + p + (th * 2), CoordinatesDisplay.CONFIG.get().definitionColor);

        drawInfo(drawContext, biomeText, x + p, y + p + (th * 3), CoordinatesDisplay.CONFIG.get().definitionColor);
        {
            int dstart = (x + w) - p - this.getTextRenderer().getWidth(directionText);
            int ypstart = (x + w) - p - this.getTextRenderer().getWidth(yawText);

            drawInfo(drawContext, pitchText, ypstart, y + p, CoordinatesDisplay.CONFIG.get().definitionColor);
            drawInfo(drawContext, directionText, dstart, y + p + th, CoordinatesDisplay.CONFIG.get().dataColor);
            drawInfo(drawContext, yawText, ypstart, y + p + (th * 2), CoordinatesDisplay.CONFIG.get().definitionColor);
        }

    }

    private void renderMoveOverlay(DrawContext drawContext, int x, int y) {
        int color = 0x50c7c7c7;
        scaleSize = 5;
        int scaleColor = 0x99d9fffa;

        // overlay color
        drawContext.fill(x, y, x + w, y + h, color);

        // scale square
        int scaleX = x + w - scaleSize;
        int scaleY = y + h - scaleSize;
        drawContext.fill(scaleX, scaleY, scaleX + scaleSize, scaleY + scaleSize, scaleColor);
    }

    public TextRenderer getTextRenderer() {
        return this.client.textRenderer;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private void drawInfo(DrawContext drawContext, Text text, int x, int y, int color) {
            drawContext.drawText(this.getTextRenderer(), text, x, y, color, CoordinatesDisplay.CONFIG.get().hudTextShadow);
    }
}
