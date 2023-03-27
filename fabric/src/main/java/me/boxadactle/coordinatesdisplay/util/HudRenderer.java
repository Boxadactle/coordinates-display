package me.boxadactle.coordinatesdisplay.util;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
import java.text.DecimalFormat;

public class HudRenderer extends DrawableHelper {

    private final MinecraftClient client = MinecraftClient.getInstance();

    ModConfig config;

    int w = 0;
    int h = 0;

    public HudRenderer(ModConfig config) {
        CoordinatesDisplay.CONFIG = config;
    }

    public void updateConfig(ModConfig config) {
        CoordinatesDisplay.CONFIG = config;
    }

    public void render(MatrixStack matrices, Vec3d pos, ChunkPos chunkPos, float cameraYaw, float cameraPitch, RegistryEntry<Biome> biome, int x, int y, boolean minMode) {
        try {
            if (!minMode) {
                renderOverlay(matrices, pos, chunkPos, cameraYaw, biome, x, y);
            } else {
                renderOverlayMin(matrices, pos, cameraYaw, cameraPitch, biome, x, y);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void render(MatrixStack matrices, Vec3d pos, ChunkPos chunkPos, float cameraYaw, float cameraPitch, RegistryEntry<Biome> biome, int x, int y, float scale, boolean minMode) {
        try {
            matrices.push();

            matrices.scale(scale, scale, scale);

            if (!minMode) {
                renderOverlay(matrices, pos, chunkPos, cameraYaw, biome, x, y);
            } else {
                renderOverlayMin(matrices, pos, cameraYaw, cameraPitch, biome, x, y);
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

    private void renderOverlay(MatrixStack matrices, Vec3d pos, ChunkPos chunkPos, float cameraYaw, @Nullable RegistryEntry<Biome> biome, int x, int y) throws NullPointerException {

        // variables

        DecimalFormat decimalFormat = new DecimalFormat(CoordinatesDisplay.CONFIG.roundPosToTwoDecimals ? "0.00" : "0");

        Text xpos = Text.literal(decimalFormat.format(pos.getX())).styled((style -> style.withColor(CoordinatesDisplay.CONFIG.dataColor)));
        Text ypos = Text.literal(decimalFormat.format(pos.getY())).styled((style -> style.withColor(CoordinatesDisplay.CONFIG.dataColor)));
        Text zpos = Text.literal(decimalFormat.format(pos.getZ())).styled((style -> style.withColor(CoordinatesDisplay.CONFIG.dataColor)));

        Text chunkxpos = Text.literal(Integer.toString(chunkPos.x)).styled((style -> style.withColor(CoordinatesDisplay.CONFIG.dataColor)));
        Text chunkzpos = Text.literal(Integer.toString(chunkPos.z)).styled((style -> style.withColor(CoordinatesDisplay.CONFIG.dataColor)));

        Text biometext;
        if (this.client.world != null) {
            String biomestring = biome != null ? ModUtil.parseIdentifier(ModUtil.getBiomeString(biome)) : "Plains";
            biometext = CoordinatesDisplay.CONFIG.renderBiome ? ModUtil.colorize(Text.literal(biomestring), BiomeColors.getBiomeColor(ModUtil.parseIdentifier(biomestring), CoordinatesDisplay.CONFIG.dataColor)) :
                    Text.literal("");
        } else
            biometext = Text.literal("Plains").styled((style -> style.withColor(CoordinatesDisplay.CONFIG.dataColor)));

        Text dtext = Text.translatable("hud.coordinatesdisplay." + ModUtil.getDirectionFromYaw(MathHelper.wrapDegrees(cameraYaw))).styled((style -> style.withColor(CoordinatesDisplay.CONFIG.dataColor)));

        Text xText = Text.translatable("hud.coordinatesdisplay.x", xpos);
        Text yText = Text.translatable("hud.coordinatesdisplay.y", ypos);
        Text zText = Text.translatable("hud.coordinatesdisplay.z", zpos);

        Text chunkX = Text.translatable("hud.coordinatesdisplay.chunk.x", chunkxpos);
        Text chunkZ = Text.translatable("hud.coordinatesdisplay.chunk.z", chunkzpos);

        float yaw = MathHelper.wrapDegrees(cameraYaw);
        Text directionText = CoordinatesDisplay.CONFIG.renderDirection ? Text.translatable("hud.coordinatesdisplay.direction", dtext, Text.literal(CoordinatesDisplay.CONFIG.renderDirectionInt ? " ("+decimalFormat.format(yaw)+")" : "")) : Text.literal("");

        Text biomeText = CoordinatesDisplay.CONFIG.renderBiome ? Text.translatable("hud.coordinatesdisplay.biome", biometext) : Text.literal("");

        Text minecraftVersion = Text.translatable("hud.coordinatesdisplay.version", ModVersion.getMCVersion());

        int th = 10;
        int p = CoordinatesDisplay.CONFIG.padding;
        int tp = CoordinatesDisplay.CONFIG.textPadding;

        w = ModUtil.calculateHudWidth(p, tp, xText, yText, zText, chunkX, chunkZ, directionText, biomeText, minecraftVersion);
        h = ModUtil.calculateHudHeight(th, p, tp, xText, yText, zText, chunkX, chunkZ, directionText, biomeText, minecraftVersion);

        // rendering
        if (CoordinatesDisplay.CONFIG.renderBackground) {
            fill(matrices, x, y, x + w, y + h, CoordinatesDisplay.CONFIG.backgroundColor);
        }


        this.getTextRenderer().drawWithShadow(matrices, xText, x + p, y + p, CoordinatesDisplay.CONFIG.definitionColor);
        this.getTextRenderer().drawWithShadow(matrices, yText, x + p, y + p + th, CoordinatesDisplay.CONFIG.definitionColor);
        this.getTextRenderer().drawWithShadow(matrices, zText, x + p, y + p + (th * 2), CoordinatesDisplay.CONFIG.definitionColor);

        if (CoordinatesDisplay.CONFIG.renderChunkData) {
            this.getTextRenderer().drawWithShadow(matrices, chunkX, x + p + ModUtil.getLongestTextLength(xText, yText, zText) + tp, y + p, CoordinatesDisplay.CONFIG.definitionColor);
            this.getTextRenderer().drawWithShadow(matrices, chunkZ, x + p + ModUtil.getLongestTextLength(xText, yText, zText) + tp, y + p + (th), CoordinatesDisplay.CONFIG.definitionColor);
        }

        if (CoordinatesDisplay.CONFIG.renderDirection) {
            this.getTextRenderer().drawWithShadow(matrices, directionText, x + p, y + p + (th * 3) + tp, CoordinatesDisplay.CONFIG.definitionColor);
        }

        if (CoordinatesDisplay.CONFIG.renderBiome) {
            this.getTextRenderer().drawWithShadow(matrices, biomeText, x + p, y + p + (th * 3) + tp + (CoordinatesDisplay.CONFIG.renderDirection ? th : 0), CoordinatesDisplay.CONFIG.definitionColor);
        }

        if (CoordinatesDisplay.CONFIG.renderMCVersion) {
            this.getTextRenderer().drawWithShadow(matrices, minecraftVersion, x + p, y + p + (th * 3) + tp + (CoordinatesDisplay.CONFIG.renderDirection ? th : 0) + (CoordinatesDisplay.CONFIG.renderBiome ? th : 0), CoordinatesDisplay.CONFIG.dataColor);
        }
    }

    private void renderOverlayMin(MatrixStack matrices, Vec3d pos, float cameraYaw, float cameraPitch, @Nullable RegistryEntry<Biome> biome, int x, int y) throws NullPointerException {
        Text xpos = Text.literal(Long.toString(Math.round(pos.getX()))).styled((style -> style.withColor(CoordinatesDisplay.CONFIG.dataColor)));
        Text ypos = Text.literal(Long.toString(Math.round(pos.getY()))).styled((style -> style.withColor(CoordinatesDisplay.CONFIG.dataColor)));
        Text zpos = Text.literal(Long.toString(Math.round(pos.getZ()))).styled((style -> style.withColor(CoordinatesDisplay.CONFIG.dataColor)));

        Text biometext;
        if (this.client.world != null) {
            String biomestring = biome != null ? ModUtil.parseIdentifier(ModUtil.getBiomeString(biome)) : "Plains";
            biometext = CoordinatesDisplay.CONFIG.renderBiome ? ModUtil.colorize(Text.literal(biomestring), BiomeColors.getBiomeColor(ModUtil.parseIdentifier(biomestring), CoordinatesDisplay.CONFIG.dataColor)) :
                    Text.literal("");
        } else
            biometext = Text.literal("Plains").styled((style -> style.withColor(CoordinatesDisplay.CONFIG.dataColor)));

        float yaw = MathHelper.wrapDegrees(cameraYaw);
        float pitch = MathHelper.wrapDegrees(cameraPitch);
        Text direction = Text.translatable("hud.coordinatesdisplay.min.direction" + ModUtil.getDirectionFromYaw(yaw));

        Text xText = Text.translatable("hud.coordinatesdisplay.min.x", xpos);
        Text yText = Text.translatable("hud.coordinatesdisplay.min.y", ypos);
        Text zText = Text.translatable("hud.coordinatesdisplay.min.z", zpos);

        Text pitchText = Text.literal(pitch > 0 ? "+" : "-");
        Text directionText = Text.translatable("hud.coordinatesdisplay.min." + ModUtil.getDirectionFromYaw(yaw), direction);
        Text yawText = Text.literal(yaw > 0 ? "+" : "-");

        Text biomeText = CoordinatesDisplay.CONFIG.renderBiome ? Text.translatable("hud.coordinatesdisplay.min.biome", biometext) : Text.literal("");

        int p = CoordinatesDisplay.CONFIG.padding;
        int th = 10;
        int dpadding = CoordinatesDisplay.CONFIG.textPadding;

        w = ModUtil.calculateHudWidthMin(p, th, dpadding, xText, yText, zText, yawText, pitchText, directionText, biomeText);
        h = ModUtil.calculateHudHeightMin(p, th);

        // rendering
        if (CoordinatesDisplay.CONFIG.renderBackground) {
            fill(matrices, x, y, x + w, y + h, CoordinatesDisplay.CONFIG.backgroundColor);
        }

        this.getTextRenderer().drawWithShadow(matrices, xText, x + p, y + p, CoordinatesDisplay.CONFIG.definitionColor);
        this.getTextRenderer().drawWithShadow(matrices, yText, x + p, y + p + th, CoordinatesDisplay.CONFIG.definitionColor);
        this.getTextRenderer().drawWithShadow(matrices, zText, x + p, y + p + (th * 2), CoordinatesDisplay.CONFIG.definitionColor);

        this.getTextRenderer().drawWithShadow(matrices, biomeText, x + p, y + p + (th * 3), CoordinatesDisplay.CONFIG.definitionColor);
        {
            int r = ModUtil.getLongestTextLength(xText, yText, zText, biomeText);
            int dstart = (x + w) - p - this.getTextRenderer().getWidth(directionText);
            int ypstart = (x + w) - p - this.getTextRenderer().getWidth(yawText);

            this.getTextRenderer().drawWithShadow(matrices, pitchText, ypstart, y + p, CoordinatesDisplay.CONFIG.definitionColor);
            this.getTextRenderer().drawWithShadow(matrices, directionText, dstart, y + p + th, CoordinatesDisplay.CONFIG.dataColor);
            this.getTextRenderer().drawWithShadow(matrices, yawText, ypstart, y + p + (th * 2), CoordinatesDisplay.CONFIG.definitionColor);
        }

    }

    public TextRenderer getTextRenderer() {
        return this.client.textRenderer;
    }
    
}
