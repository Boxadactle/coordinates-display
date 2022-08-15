package me.boxadactle.coordinatesdisplay.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3d;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;

import javax.annotation.Nullable;
import java.text.DecimalFormat;

public class HudOverlay extends GuiComponent {


    int w = 0;
    int h = 0;

    public HudOverlay() {

    }

    public void render(PoseStack matrices, Vector3d pos, ChunkPos chunkPos, float cameraYaw, Holder<Biome> biome, int x, int y) {
        try {
            renderOverlay(matrices, pos, chunkPos, cameraYaw, biome, x, y);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void render(PoseStack matrices, Vector3d pos, ChunkPos chunkPos, float cameraYaw, Holder<Biome> biome, int x, int y, float scale) {
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

    public void renderOverlay(PoseStack matrices, Vector3d pos, ChunkPos chunkPos, float cameraYaw, @Nullable Holder<Biome> biome, int x, int y) throws NullPointerException {

        Minecraft client = Minecraft.getInstance();
        
        DecimalFormat decimalFormat = new DecimalFormat(CoordinatesDisplay.CONFIG.get().decimalRounding ? "0.00" : "0");

        Component xText = new TranslatableComponent("hud.coordinatesdisplay.x",
                new TextComponent(decimalFormat.format(pos.x)).withStyle(style -> style.withColor(ModUtils.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor))));
        Component yText = new TranslatableComponent("hud.coordinatesdisplay.y",
                new TextComponent(decimalFormat.format(pos.y)).withStyle(style -> style.withColor(ModUtils.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor))));
        Component zText = new TranslatableComponent("hud.coordinatesdisplay.z",
                new TextComponent(decimalFormat.format(pos.z)).withStyle(style -> style.withColor(ModUtils.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor))));

        Component chunkX = new TranslatableComponent("hud.coordinatesdisplay.chunk.x",
                new TextComponent(Integer.toString(chunkPos.x)).withStyle(style -> style.withColor(ModUtils.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor))));
        Component chunkZ = new TranslatableComponent("hud.coordinatesdisplay.chunk.z",
                new TextComponent(Integer.toString(chunkPos.z)).withStyle(style -> style.withColor(ModUtils.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor))));

        float yaw = Mth.wrapDegrees(cameraYaw);
        Component directionText = CoordinatesDisplay.CONFIG.get().renderDirection ? new TranslatableComponent("hud.coordinatesdisplay.direction",
                new TranslatableComponent("hud.coordinatesdisplay.direction." + ModUtils.getDirectionFromYaw(yaw)).withStyle(style -> style.withColor(ModUtils.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor)))) :
                new TextComponent("");

        Component biomeText;
        if (biome != null && client.level != null) {
            biomeText = CoordinatesDisplay.CONFIG.get().renderBiome ? new TranslatableComponent("hud.coordinatesdisplay.biome",
                    new TextComponent(ModUtils.parseIdentifier(ModUtils.printBiome(biome))).withStyle(style -> style.withColor(ModUtils.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor)))) :
                    new TextComponent("");
        }
        else
            biomeText = new TranslatableComponent("hud.coordinatesdisplay.biome",  new TextComponent("Plains").withStyle(style -> style.withColor(ModUtils.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor))));

        int th = 10;
        int p = CoordinatesDisplay.CONFIG.get().padding;
        int tp = CoordinatesDisplay.CONFIG.get().textPadding;

        w = p + ModUtils.getLongestLength(xText, yText, zText) +
                (CoordinatesDisplay.CONFIG.get().renderChunkData ? tp + ModUtils.getLongestLength(chunkX, chunkZ) :
                        ModUtils.getLongestLength(xText, yText, zText, biomeText, directionText) - ModUtils.getLongestLength(xText, yText, zText)) + p;
        h = p + (th * 3) + (CoordinatesDisplay.CONFIG.get().renderDirection ? tp + th : 0) +
                (CoordinatesDisplay.CONFIG.get().renderBiome ? (CoordinatesDisplay.CONFIG.get().renderDirection ? 0 : tp) + th : 0) + p;

        if (CoordinatesDisplay.CONFIG.get().renderBackground) {
            if (client.font.width(biomeText.getString()) > w || client.font.width(directionText) > w) {
                fill(matrices, x, y, x + p + ModUtils.getLongestLength(biomeText, directionText), y + h, ModUtils.TRANSPARENT_GRAY);
            } else {
                fill(matrices, x, y, x + w, y + h, ModUtils.TRANSPARENT_GRAY);
            }
        }


        client.font.drawShadow(matrices, xText, x + p, y + p, ModUtils.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
        client.font.drawShadow(matrices, yText, x + p, y + p + th, ModUtils.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
        client.font.drawShadow(matrices, zText, x + p, y + p + (th * 2), ModUtils.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));

        if (CoordinatesDisplay.CONFIG.get().renderChunkData) {
            client.font.drawShadow(matrices, chunkX, x + p + ModUtils.getLongestLength(xText, yText, zText) + tp, y + p, ModUtils.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
            client.font.drawShadow(matrices, chunkZ, x + p + ModUtils.getLongestLength(xText, yText, zText) + tp, y + p + (th), ModUtils.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
        }

        if (CoordinatesDisplay.CONFIG.get().renderDirection) {
            client.font.drawShadow(matrices, directionText, x + p, y + p + (th * 3) + tp, ModUtils.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
        }

        if (CoordinatesDisplay.CONFIG.get().renderBiome) {
            client.font.drawShadow(matrices, biomeText, x + p, y + p + (th * 3) + tp + (CoordinatesDisplay.CONFIG.get().renderDirection ? th : 0), ModUtils.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
        }
    }

}