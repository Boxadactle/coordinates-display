package me.boxadactle.coordinatesdisplay.util;

import com.mojang.blaze3d.vertex.PoseStack;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;

import javax.annotation.Nullable;
import java.text.DecimalFormat;

public class HudRenderer extends GuiComponent {


    int w = 0;
    int h = 0;

    public HudRenderer() {

    }

    public void render(PoseStack matrices, Vec3i pos, ChunkPos chunkPos, float cameraYaw, Holder<Biome> biome, int x, int y) {
        try {
            renderOverlay(matrices, pos, chunkPos, cameraYaw, biome, x, y);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void render(PoseStack matrices, Vec3i pos, ChunkPos chunkPos, float cameraYaw, Holder<Biome> biome, int x, int y, float scale) {
        try {
            matrices.pushPose();

            matrices.scale(scale, scale, scale);

            renderOverlay(matrices, pos, chunkPos, cameraYaw, biome, x, y);
            matrices.pushPose();
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

    public void renderOverlay(PoseStack matrices, Vec3i pos, ChunkPos chunkPos, float cameraYaw, @Nullable Holder<Biome> biome, int x, int y) throws NullPointerException {

        Minecraft client = Minecraft.getInstance();
        
        DecimalFormat decimalFormat = new DecimalFormat(CoordinatesDisplay.CONFIG.get().decimalRounding ? "0.00" : "0");

        Component xText = Component.translatable("hud.coordinatesdisplay.x",
                Component.literal(decimalFormat.format(pos.getX())).withStyle(style -> style.withColor(ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor))));
        Component yText = Component.translatable("hud.coordinatesdisplay.y",
                Component.literal(decimalFormat.format(pos.getY())).withStyle(style -> style.withColor(ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor))));
        Component zText = Component.translatable("hud.coordinatesdisplay.z",
                Component.literal(decimalFormat.format(pos.getZ())).withStyle(style -> style.withColor(ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor))));

        Component chunkX = Component.translatable("hud.coordinatesdisplay.chunk.x",
                Component.literal(Integer.toString(chunkPos.x)).withStyle(style -> style.withColor(ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor))));
        Component chunkZ = Component.translatable("hud.coordinatesdisplay.chunk.z",
                Component.literal(Integer.toString(chunkPos.z)).withStyle(style -> style.withColor(ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor))));

        float yaw = Mth.wrapDegrees(cameraYaw);
        Component directionText = CoordinatesDisplay.CONFIG.get().renderDirection ? Component.translatable("hud.coordinatesdisplay.direction",
                Component.translatable("hud.coordinatesdisplay.direction." + ModUtil.getDirectionFromYaw(yaw)).withStyle(style -> style.withColor(ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor)))) :
                Component.literal("");

        Component biomeText;
        if (biome != null && client.level != null) {
            biomeText = CoordinatesDisplay.CONFIG.get().renderBiome ? Component.translatable("hud.coordinatesdisplay.biome", ModUtil.colorize(Component.literal(ModUtil.parseIdentifier(ModUtil.printBiome(biome))), BiomeColors.getBiomeColor(ModUtil.parseIdentifier(ModUtil.printBiome(biome)), ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor)))) :
                    Component.literal("");
        }
        else
            biomeText = Component.translatable("hud.coordinatesdisplay.biome",  Component.literal("Plains").withStyle(style -> style.withColor(ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor))));

        int th = 10;
        int p = CoordinatesDisplay.CONFIG.get().padding;
        int tp = CoordinatesDisplay.CONFIG.get().textPadding;

        w = p + ModUtil.getLongestLength(xText, yText, zText) +
                (CoordinatesDisplay.CONFIG.get().renderChunkData ? tp + ModUtil.getLongestLength(chunkX, chunkZ) :
                        ModUtil.getLongestLength(xText, yText, zText, biomeText, directionText) - ModUtil.getLongestLength(xText, yText, zText)) + p;
        h = p + (th * 3) + (CoordinatesDisplay.CONFIG.get().renderDirection ? tp + th : 0) +
                (CoordinatesDisplay.CONFIG.get().renderBiome ? (CoordinatesDisplay.CONFIG.get().renderDirection ? 0 : tp) + th : 0) + p;

        if (CoordinatesDisplay.CONFIG.get().renderBackground) {
            if (client.font.width(biomeText.getString()) > w || client.font.width(directionText) > w) {
                fill(matrices, x, y, x + p + ModUtil.getLongestLength(biomeText, directionText), y + h, ModUtil.TRANSPARENT_GRAY);
            } else {
                fill(matrices, x, y, x + w, y + h, ModUtil.TRANSPARENT_GRAY);
            }
        }


        client.font.drawShadow(matrices, xText, x + p, y + p, ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
        client.font.drawShadow(matrices, yText, x + p, y + p + th, ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
        client.font.drawShadow(matrices, zText, x + p, y + p + (th * 2), ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));

        if (CoordinatesDisplay.CONFIG.get().renderChunkData) {
            client.font.drawShadow(matrices, chunkX, x + p + ModUtil.getLongestLength(xText, yText, zText) + tp, y + p, ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
            client.font.drawShadow(matrices, chunkZ, x + p + ModUtil.getLongestLength(xText, yText, zText) + tp, y + p + (th), ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
        }

        if (CoordinatesDisplay.CONFIG.get().renderDirection) {
            client.font.drawShadow(matrices, directionText, x + p, y + p + (th * 3) + tp, ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
        }

        if (CoordinatesDisplay.CONFIG.get().renderBiome) {
            client.font.drawShadow(matrices, biomeText, x + p, y + p + (th * 3) + tp + (CoordinatesDisplay.CONFIG.get().renderDirection ? th : 0), ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
        }
    }

}