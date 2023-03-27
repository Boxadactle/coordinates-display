package me.boxadactle.coordinatesdisplay.util;

import com.mojang.blaze3d.vertex.PoseStack;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.text.DecimalFormat;

public class HudRenderer extends GuiComponent {


    int w = 0;
    int h = 0;

    public HudRenderer() {

    }

    public void render(PoseStack matrices, Vec3 pos, ChunkPos chunkPos, float cameraYaw, float cameraPitch, Holder<Biome> biome, boolean minMode, int x, int y) {
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

    public void render(PoseStack matrices, Vec3 pos, ChunkPos chunkPos, float cameraYaw, float cameraPitch, Holder<Biome> biome, boolean minMode, int x, int y, float scale) {
        try {
            matrices.pushPose();

            matrices.scale(scale, scale, scale);

            if (!minMode) {
                renderOverlay(matrices, pos, chunkPos, cameraYaw, biome, x, y);
            } else {
                renderOverlayMin(matrices, pos, cameraYaw, cameraPitch, biome, x, y);
            }

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

    private void renderOverlay(PoseStack matrices, Vec3 pos, ChunkPos chunkPos, float cameraYaw, @Nullable Holder<Biome> biome, int x, int y) throws NullPointerException {

        Minecraft client = Minecraft.getInstance();
        
        DecimalFormat decimalFormat = new DecimalFormat(CoordinatesDisplay.CONFIG.get().decimalRounding ? "0.00" : "0");

        Component xComponent = Component.translatable("hud.coordinatesdisplay.x",
                Component.literal(decimalFormat.format(pos.x)).withStyle(style -> style.withColor(ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor))));
        Component yComponent = Component.translatable("hud.coordinatesdisplay.y",
                Component.literal(decimalFormat.format(pos.y)).withStyle(style -> style.withColor(ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor))));
        Component zComponent = Component.translatable("hud.coordinatesdisplay.z",
                Component.literal(decimalFormat.format(pos.z)).withStyle(style -> style.withColor(ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor))));

        Component chunkX = Component.translatable("hud.coordinatesdisplay.chunk.x",
                Component.literal(Integer.toString(chunkPos.x)).withStyle(style -> style.withColor(ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor))));
        Component chunkZ = Component.translatable("hud.coordinatesdisplay.chunk.z",
                Component.literal(Integer.toString(chunkPos.z)).withStyle(style -> style.withColor(ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor))));

        float yaw = Mth.wrapDegrees(cameraYaw);
        Component directionComponent = Component.translatable("hud.coordinatesdisplay.direction",
                Component.translatable("hud.coordinatesdisplay." + ModUtil.getDirectionFromYaw(yaw)).withStyle(style -> style.withColor(ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor))),
                CoordinatesDisplay.CONFIG.get().renderDirectionInt ? Component.literal("(").append(Component.literal(decimalFormat.format(yaw))).append(Component.literal(")")) : Component.literal(""));

        Component biomeComponent;
        if (biome != null && client.level != null) {
            biomeComponent = CoordinatesDisplay.CONFIG.get().renderBiome ? Component.translatable("hud.coordinatesdisplay.biome", ModUtil.colorize(Component.literal(ModUtil.parseIdentifier(ModUtil.printBiome(biome))), BiomeColors.getBiomeColor(ModUtil.parseIdentifier(ModUtil.printBiome(biome)), ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor)))) :
                    Component.literal("");
        }
        else
            biomeComponent = Component.translatable("hud.coordinatesdisplay.biome",  Component.literal("Plains").withStyle(style -> style.withColor(ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor))));

        Component minecraftVersion = Component.translatable("hud.coordinatesdisplay.mcversion", CoordinatesDisplay.MOD_VERSION.getMCVersion());

        int th = 10;
        int p = CoordinatesDisplay.CONFIG.get().padding;
        int tp = CoordinatesDisplay.CONFIG.get().textPadding;

        w = ModUtil.calculateHudWidth(p, tp, xComponent, yComponent, zComponent, chunkX, chunkZ, directionComponent, biomeComponent, minecraftVersion);
        h = ModUtil.calculateHudHeight(th, p, tp, xComponent, yComponent, zComponent, chunkX, chunkZ, directionComponent, biomeComponent, minecraftVersion);

        if (CoordinatesDisplay.CONFIG.get().renderBackground) {
            if (client.font.width(biomeComponent.getString()) > w || client.font.width(directionComponent) > w) {
                fill(matrices, x, y, x + p + ModUtil.getLongestTextLength(biomeComponent, directionComponent), y + h, ModUtil.TRANSPARENT_GRAY);
            } else {
                fill(matrices, x, y, x + w, y + h, ModUtil.TRANSPARENT_GRAY);
            }
        }


        client.font.drawShadow(matrices, xComponent, x + p, y + p, ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
        client.font.drawShadow(matrices, yComponent, x + p, y + p + th, ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
        client.font.drawShadow(matrices, zComponent, x + p, y + p + (th * 2), ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));

        if (CoordinatesDisplay.CONFIG.get().renderChunkData) {
            client.font.drawShadow(matrices, chunkX, x + p + ModUtil.getLongestTextLength(xComponent, yComponent, zComponent) + tp, y + p, ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
            client.font.drawShadow(matrices, chunkZ, x + p + ModUtil.getLongestTextLength(xComponent, yComponent, zComponent) + tp, y + p + (th), ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
        }

        if (CoordinatesDisplay.CONFIG.get().renderDirection) {
            client.font.drawShadow(matrices, directionComponent, x + p, y + p + (th * 3) + tp, ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
        }

        if (CoordinatesDisplay.CONFIG.get().renderBiome) {
            client.font.drawShadow(matrices, biomeComponent, x + p, y + p + (th * 3) + tp + (CoordinatesDisplay.CONFIG.get().renderDirection ? th : 0), ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
        }

        if (CoordinatesDisplay.CONFIG.get().renderMCVersion) {
            client.font.drawShadow(matrices, minecraftVersion, x + p, y + p + (th * 3) + tp + (CoordinatesDisplay.CONFIG.get().renderDirection ? th : 0) + (CoordinatesDisplay.CONFIG.get().renderBiome ? th : 0), ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor));
        }
    }

    private void renderOverlayMin(PoseStack matrices, Vec3 pos, float cameraYaw, float cameraPitch, @Nullable Holder<Biome> biome, int x, int y) throws NullPointerException {
        Minecraft client = Minecraft.getInstance();

        Component xpos = ModUtil.colorize(Component.literal(Long.toString(Math.round(pos.x))), ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor));
        Component ypos = ModUtil.colorize(Component.literal(Long.toString(Math.round(pos.y))), ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor));
        Component zpos = ModUtil.colorize(Component.literal(Long.toString(Math.round(pos.z))), ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor));

        Component biomeC = Component.literal("Plains");
        if (client.level != null) {
            String biomestring = biome != null ? ModUtil.parseIdentifier(ModUtil.printBiome(biome)) : "Plains";
            biomeC = CoordinatesDisplay.CONFIG.get().renderBiome ? ModUtil.colorize(Component.literal(biomestring), BiomeColors.getBiomeColor(ModUtil.parseIdentifier(biomestring), ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor))) :
                    Component.literal("");
        }

        float yaw = Mth.wrapDegrees(cameraYaw);
        float pitch = Mth.wrapDegrees(cameraPitch);
        Component direction = Component.translatable("hud.coordinatesdisplay.min.direction" + ModUtil.getDirectionFromYaw(yaw));

        Component xComponent = Component.translatable("hud.coordinatesdisplay.min.x", xpos);
        Component yComponent = Component.translatable("hud.coordinatesdisplay.min.y", ypos);
        Component zComponent = Component.translatable("hud.coordinatesdisplay.min.z", zpos);

        Component pitchComponent = Component.literal(pitch > 0 ? "+" : "-");
        Component directionComponent = Component.translatable("hud.coordinatesdisplay.min." + ModUtil.getDirectionFromYaw(yaw), direction);
        Component yawComponent = Component.literal(yaw > 0 ? "+" : "-");

        Component biomeComponent = CoordinatesDisplay.CONFIG.get().renderBiome ? Component.translatable("hud.coordinatesdisplay.min.biome", biomeC) : Component.literal("");

        int p = CoordinatesDisplay.CONFIG.get().padding;
        int th = 10;
        int dpadding = CoordinatesDisplay.CONFIG.get().textPadding;

        w = ModUtil.calculateHudWidthMin(p, th, dpadding, xComponent, yComponent, zComponent, yawComponent, pitchComponent, directionComponent, biomeComponent);
        h = ModUtil.calculateHudHeightMin(p, th);

        // rendering
        if (CoordinatesDisplay.CONFIG.get().renderBackground) {
            fill(matrices, x, y, x + w, y + h, ModUtil.TRANSPARENT_GRAY);
        }

        this.getComponentRenderer().drawShadow(matrices, xComponent, x + p, y + p, ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
        this.getComponentRenderer().drawShadow(matrices, yComponent, x + p, y + p + th, ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
        this.getComponentRenderer().drawShadow(matrices, zComponent, x + p, y + p + (th * 2), ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));

        this.getComponentRenderer().drawShadow(matrices, biomeComponent, x + p, y + p + (th * 3), ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
        {
            int r = ModUtil.getLongestTextLength(xComponent, yComponent, zComponent, biomeComponent);
            int dstart = (x + w) - p - this.getComponentRenderer().width(directionComponent);
            int ypstart = (x + w) - p - this.getComponentRenderer().width(yawComponent);

            this.getComponentRenderer().drawShadow(matrices, pitchComponent, ypstart, y + p, ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
            this.getComponentRenderer().drawShadow(matrices, directionComponent, dstart, y + p + th, ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().dataColor));
            this.getComponentRenderer().drawShadow(matrices, yawComponent, ypstart, y + p + (th * 2), ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().definitionColor));
        }

    }

    public Font getComponentRenderer() {
        return Minecraft.getInstance().font;
    }


}