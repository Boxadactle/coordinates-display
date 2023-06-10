package me.boxadactle.coordinatesdisplay.util;

import com.mojang.blaze3d.vertex.PoseStack;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.text.DecimalFormat;

public class HudRenderer {


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

    public void render(GuiGraphics guiGraphics, Vec3 pos, ChunkPos chunkPos, float cameraYaw, float cameraPitch, Holder<Biome> biome, int x, int y, boolean minMode, boolean moveOverlay) {
        try {
            if (!minMode) {
                renderOverlay(guiGraphics, pos, chunkPos, cameraYaw, biome, x, y);
            } else {
                renderOverlayMin(guiGraphics, pos, cameraYaw, cameraPitch, biome, x, y);
            }

            if (moveOverlay) {
                renderMoveOverlay(guiGraphics, x, y);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void render(GuiGraphics guiGraphics, Vec3 pos, ChunkPos chunkPos, float cameraYaw, float cameraPitch, Holder<Biome> biome, int x, int y, boolean minMode, boolean moveOverlay, float scale) {
        try {
            PoseStack matrices = guiGraphics.pose();

            matrices.pushPose();

            matrices.scale(scale, scale, scale);

            if (!minMode) {
                renderOverlay(guiGraphics, pos, chunkPos, cameraYaw, biome, x, y);
            } else {
                renderOverlayMin(guiGraphics, pos, cameraYaw, cameraPitch, biome, x, y);
            }

            if (moveOverlay) {
                renderMoveOverlay(guiGraphics, x, y);
            }

            matrices.popPose();

            this.scale = scale;
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

    private void renderOverlay(GuiGraphics guiGraphics, Vec3 pos, ChunkPos chunkPos, float cameraYaw, @Nullable Holder<Biome> biome, int x, int y) throws NullPointerException {
        this.x = x;
        this.y = y;

        Minecraft client = Minecraft.getInstance();
        
        DecimalFormat decimalFormat = new DecimalFormat(CoordinatesDisplay.CONFIG.get().decimalRounding ? "0.00" : "0");

        Component xComponent = Component.translatable("hud.coordinatesdisplay.x",
                Component.literal(decimalFormat.format(pos.x)).withStyle(style -> style.withColor(CoordinatesDisplay.CONFIG.get().dataColor)));
        Component yComponent = Component.translatable("hud.coordinatesdisplay.y",
                Component.literal(decimalFormat.format(pos.y)).withStyle(style -> style.withColor(CoordinatesDisplay.CONFIG.get().dataColor)));
        Component zComponent = Component.translatable("hud.coordinatesdisplay.z",
                Component.literal(decimalFormat.format(pos.z)).withStyle(style -> style.withColor(CoordinatesDisplay.CONFIG.get().dataColor)));

        Component chunkX = Component.translatable("hud.coordinatesdisplay.chunk.x",
                Component.literal(Integer.toString(chunkPos.x)).withStyle(style -> style.withColor(CoordinatesDisplay.CONFIG.get().dataColor)));
        Component chunkZ = Component.translatable("hud.coordinatesdisplay.chunk.z",
                Component.literal(Integer.toString(chunkPos.z)).withStyle(style -> style.withColor(CoordinatesDisplay.CONFIG.get().dataColor)));

        float yaw = Mth.wrapDegrees(cameraYaw);
        Component directionComponent = Component.translatable("hud.coordinatesdisplay.direction",
                Component.translatable("hud.coordinatesdisplay." + ModUtil.getDirectionFromYaw(yaw)).withStyle(style -> style.withColor(CoordinatesDisplay.CONFIG.get().dataColor)),
                CoordinatesDisplay.CONFIG.get().renderDirectionInt ? Component.literal("(").append(Component.literal(decimalFormat.format(yaw))).append(Component.literal(")")) : Component.literal(""));

        Component biomeComponent;
        if (biome != null && client.level != null) {
            biomeComponent = CoordinatesDisplay.CONFIG.get().renderBiome ? Component.translatable("hud.coordinatesdisplay.biome", ModUtil.colorize(Component.literal(ModUtil.parseIdentifier(ModUtil.printBiome(biome))), CoordinatesDisplay.BiomeColors.getBiomeColor(ModUtil.parseIdentifier(ModUtil.printBiome(biome)), CoordinatesDisplay.CONFIG.get().dataColor))) :
                    Component.literal("");
        }
        else
            biomeComponent = Component.translatable("hud.coordinatesdisplay.biome",  Component.literal("Plains").withStyle(style -> style.withColor(CoordinatesDisplay.CONFIG.get().dataColor)));

        Component minecraftVersion = Component.translatable("hud.coordinatesdisplay.version", ModVersion.getMCVersion());

        int th = 10;
        int p = CoordinatesDisplay.CONFIG.get().padding;
        int tp = CoordinatesDisplay.CONFIG.get().textPadding;

        w = ModUtil.calculateHudWidth(p, tp, xComponent, yComponent, zComponent, chunkX, chunkZ, directionComponent, biomeComponent, minecraftVersion);
        h = ModUtil.calculateHudHeight(th, p, tp, xComponent, yComponent, zComponent, chunkX, chunkZ, directionComponent, biomeComponent, minecraftVersion);

        if (CoordinatesDisplay.CONFIG.get().renderBackground) {
            if (client.font.width(biomeComponent.getString()) > w || client.font.width(directionComponent) > w) {
                guiGraphics.fill(x, y, x + p + ModUtil.getLongestTextLength(biomeComponent, directionComponent), y + h, CoordinatesDisplay.CONFIG.get().backgroundColor);
            } else {
                guiGraphics.fill(x, y, x + w, y + h, CoordinatesDisplay.CONFIG.get().backgroundColor);
            }
        }


        drawInfo(guiGraphics, xComponent, x + p, y + p, CoordinatesDisplay.CONFIG.get().definitionColor);
        drawInfo(guiGraphics, yComponent, x + p, y + p + th, CoordinatesDisplay.CONFIG.get().definitionColor);
        drawInfo(guiGraphics, zComponent, x + p, y + p + (th * 2), (CoordinatesDisplay.CONFIG.get().definitionColor));

        if (CoordinatesDisplay.CONFIG.get().renderChunkData) {
            drawInfo(guiGraphics, chunkX, x + p + ModUtil.getLongestTextLength(xComponent, yComponent, zComponent) + tp, y + p, CoordinatesDisplay.CONFIG.get().definitionColor);
            drawInfo(guiGraphics, chunkZ, x + p + ModUtil.getLongestTextLength(xComponent, yComponent, zComponent) + tp, y + p + (th), CoordinatesDisplay.CONFIG.get().definitionColor);
        }

        if (CoordinatesDisplay.CONFIG.get().renderDirection) {
            drawInfo(guiGraphics, directionComponent, x + p, y + p + (th * 3) + tp, CoordinatesDisplay.CONFIG.get().definitionColor);
        }

        if (CoordinatesDisplay.CONFIG.get().renderBiome) {
            drawInfo(guiGraphics, biomeComponent, x + p, y + p + (th * 3) + tp + (CoordinatesDisplay.CONFIG.get().renderDirection ? th : 0), CoordinatesDisplay.CONFIG.get().definitionColor);
        }

        if (CoordinatesDisplay.CONFIG.get().renderMCVersion) {
            drawInfo(guiGraphics, minecraftVersion, x + p, y + p + (th * 3) + tp + (CoordinatesDisplay.CONFIG.get().renderDirection ? th : 0) + (CoordinatesDisplay.CONFIG.get().renderBiome ? th : 0), CoordinatesDisplay.CONFIG.get().dataColor);
        }
    }

    private void renderOverlayMin(GuiGraphics guiGraphics, Vec3 pos, float cameraYaw, float cameraPitch, @Nullable Holder<Biome> biome, int x, int y) throws NullPointerException {
        this.x = x;
        this.y = y;

        Minecraft client = Minecraft.getInstance();

        Component xpos = ModUtil.colorize(Component.literal(Long.toString(Math.round(pos.x))), CoordinatesDisplay.CONFIG.get().dataColor);
        Component ypos = ModUtil.colorize(Component.literal(Long.toString(Math.round(pos.y))), CoordinatesDisplay.CONFIG.get().dataColor);
        Component zpos = ModUtil.colorize(Component.literal(Long.toString(Math.round(pos.z))), CoordinatesDisplay.CONFIG.get().dataColor);

        Component biomeC = Component.literal("Plains");
        if (client.level != null) {
            String biomestring = biome != null ? ModUtil.parseIdentifier(ModUtil.printBiome(biome)) : "Plains";
            biomeC = CoordinatesDisplay.CONFIG.get().renderBiome ? ModUtil.colorize(Component.literal(biomestring), CoordinatesDisplay.BiomeColors.getBiomeColor(biomestring, CoordinatesDisplay.CONFIG.get().dataColor)) :
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
            guiGraphics.fill(x, y, x + w, y + h, ModUtil.TRANSPARENT_GRAY);
        }

        drawInfo(guiGraphics, xComponent, x + p, y + p, CoordinatesDisplay.CONFIG.get().definitionColor);
        drawInfo(guiGraphics, yComponent, x + p, y + p + th, CoordinatesDisplay.CONFIG.get().definitionColor);
        drawInfo(guiGraphics, zComponent, x + p, y + p + (th * 2), CoordinatesDisplay.CONFIG.get().definitionColor);

        drawInfo(guiGraphics, biomeComponent, x + p, y + p + (th * 3), CoordinatesDisplay.CONFIG.get().definitionColor);
        {
            int r = ModUtil.getLongestTextLength(xComponent, yComponent, zComponent, biomeComponent);
            int dstart = (x + w) - p - this.getComponentRenderer().width(directionComponent);
            int ypstart = (x + w) - p - this.getComponentRenderer().width(yawComponent);

            drawInfo(guiGraphics, pitchComponent, ypstart, y + p, CoordinatesDisplay.CONFIG.get().definitionColor);
            drawInfo(guiGraphics, directionComponent, dstart, y + p + th, CoordinatesDisplay.CONFIG.get().dataColor);
            drawInfo(guiGraphics, yawComponent, ypstart, y + p + (th * 2), CoordinatesDisplay.CONFIG.get().definitionColor);
        }

    }

    private void renderMoveOverlay(GuiGraphics guiGraphics, int x, int y) {
        int color = 0x50c7c7c7;
        scaleSize = 5;
        int scaleColor = 0x99d9fffa;

        // overlay color
        guiGraphics.fill(x, y, x + w, y + h, color);

        // scale square
        int scaleX = x + w - scaleSize;
        int scaleY = y + h - scaleSize;
        guiGraphics.fill(scaleX, scaleY, scaleX + scaleSize, scaleY + scaleSize, scaleColor);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Font getComponentRenderer() {
        return Minecraft.getInstance().font;
    }

    private void drawInfo(GuiGraphics guiGraphics, Component component, int x, int y, int color) {
        guiGraphics.drawString(this.getComponentRenderer(), component, x, y, color, CoordinatesDisplay.CONFIG.get().hudTextShadow);
    }

}