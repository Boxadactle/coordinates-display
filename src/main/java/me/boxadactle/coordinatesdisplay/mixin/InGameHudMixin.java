package me.boxadactle.coordinatesdisplay.mixin;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.init.KeybindsInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.DecimalFormat;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
    @Shadow @Final MinecraftClient client;

    @Shadow public abstract TextRenderer getTextRenderer();

    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    // need to render the overlay in the render method
    @Inject(at = @At("RETURN"), method = "render")
    private void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (!this.client.options.hudHidden && CoordinatesDisplay.CONFIG.visible && !this.client.options.debugEnabled) {
            try {
                Entity camera = this.client.getCameraEntity();
                renderCoordinatesOverlay(matrices, camera.getPos(), camera.getChunkPos(), camera.getHorizontalFacing(), this.client.world.getBiome(camera.getBlockPos()), camera.getYaw());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        KeybindsInit.checkBindings();
    }

    // method to turn an angle into a direction string
    private String getDirection(float degrees) {
        String direction;
        String directions[] = new String[]{"North", "Northeast", "East", "Southeast", "South", "Southwest", "West", "Northwest", "North"};
        if (degrees > 0)
            direction = directions[Math.round(degrees / 45)];
        else {
            int index = Math.round(degrees / 45) * -1;
            direction = directions[8 - index];
        }
        return direction;
    }

    public void renderCoordinatesOverlay(MatrixStack matrices, Vec3d pos, ChunkPos chunkPos, Direction direction, Biome biome, float cameraYaw) {
        Text x = Text.of(CoordinatesDisplay.DefinitionColorPrefix + "X: " + CoordinatesDisplay.DataColorPrefix + (CoordinatesDisplay.CONFIG.roundPosToTwoDecimals ? decimalFormat.format(pos.getX()) : Math.round(pos.getX())));
        Text y = Text.of(CoordinatesDisplay.DefinitionColorPrefix + "Y: " + CoordinatesDisplay.DataColorPrefix + (CoordinatesDisplay.CONFIG.roundPosToTwoDecimals ? decimalFormat.format(pos.getY()) : Math.round(pos.getY())));
        Text z = Text.of(CoordinatesDisplay.DefinitionColorPrefix + "Z: " + CoordinatesDisplay.DataColorPrefix + (CoordinatesDisplay.CONFIG.roundPosToTwoDecimals ? decimalFormat.format(pos.getZ()) : Math.round(pos.getZ())));

        Text chunkX = CoordinatesDisplay.CONFIG.renderChunkData ? Text.of(CoordinatesDisplay.DefinitionColorPrefix + "Chunk X: " + CoordinatesDisplay.DataColorPrefix + chunkPos.x) : Text.of("");
        Text chunkZ = CoordinatesDisplay.CONFIG.renderChunkData ? Text.of(CoordinatesDisplay.DefinitionColorPrefix + "Chunk Z: " + CoordinatesDisplay.DataColorPrefix + chunkPos.z) : Text.of("");

        float yaw = MathHelper.wrapDegrees(cameraYaw);
        Text directionText = CoordinatesDisplay.CONFIG.renderDirection ? Text.of(CoordinatesDisplay.DefinitionColorPrefix + getDirection(yaw) + CoordinatesDisplay.DataColorPrefix  + " (" + decimalFormat.format(yaw) + ")") : Text.of("");

        Registry registry = this.client.world.getRegistryManager().get(Registry.BIOME_KEY);
        Text biomeText = CoordinatesDisplay.CONFIG.renderBiome ? Text.of(CoordinatesDisplay.DataColorPrefix + parseBiomeId(registry.getId(biome).toString())) : Text.of("");

        int th = 10;
        int p = CoordinatesDisplay.CONFIG.padding;
        int tp = CoordinatesDisplay.CONFIG.textPadding;

        if (CoordinatesDisplay.CONFIG.renderBackground) {
            int w = p + (CoordinatesDisplay.CONFIG.renderBiome && !CoordinatesDisplay.CONFIG.renderChunkData ? getLengthOfLongestTextObject(x, y, z, biomeText) : getLengthOfLongestTextObject(x, y, z)) +
                    (CoordinatesDisplay.CONFIG.renderChunkData ? tp + getLengthOfLongestTextObject(chunkX, chunkZ) + p : p);
            int h = p + (th * 3) + (CoordinatesDisplay.CONFIG.renderDirection ? tp + th : 0) + (CoordinatesDisplay.CONFIG.renderBiome ? (CoordinatesDisplay.CONFIG.renderDirection ? 0 : tp) + th : 0) + p;

            // this number came from the DebugHud.class as well
            int color = -1873784752;

            if (this.getTextRenderer().getWidth(biomeText) > w || this.getTextRenderer().getWidth(directionText) > w) {
                fill(matrices, 0, 0, p + getLengthOfLongestTextObject(biomeText, directionText) + p, h, color);
            } else {
                fill(matrices, 0, 0, w, h, color);
            }
        }


        this.getTextRenderer().drawWithShadow(matrices, x, p, p, 16777215);
        this.getTextRenderer().drawWithShadow(matrices, y, p, p + th, 16777215);
        this.getTextRenderer().drawWithShadow(matrices, z, p, p + (th * 2), 16777215);

        if (CoordinatesDisplay.CONFIG.renderChunkData) {
            this.getTextRenderer().drawWithShadow(matrices, chunkX, p + getLengthOfLongestTextObject(x, y, z) + tp, p, 16777215);
            this.getTextRenderer().drawWithShadow(matrices, chunkZ, p + getLengthOfLongestTextObject(x, y, z) + tp, p + (th), 16777215);
        }

        if (CoordinatesDisplay.CONFIG.renderDirection) {
            this.getTextRenderer().drawWithShadow(matrices, directionText, p, p + (th * 3) + p, 16777215);
        }

        if (CoordinatesDisplay.CONFIG.renderBiome) {
            this.getTextRenderer().drawWithShadow(matrices, biomeText, p, p + (th * 3) + p + (CoordinatesDisplay.CONFIG.renderDirection ? th : 0), 16777215);
        }
    }

    private int getLengthOfLongestTextObject(Text ...text) {
        int largest = 0;
        for (int i = 0; i < text.length; i++) {
            int t = this.getTextRenderer().getWidth(text[i]);
            if (t > largest) largest = t;
        }
        return largest;
    }

    private static String parseBiomeId(String id) {
        String name = "";

        String withoutNamespace = id.split(":")[1];
        String spaces = withoutNamespace.replaceAll("_", " ");
        for (String word : spaces.split("\\s")) {
            String firstletter = word.substring(0, 1);
            String therest = word.substring(1);
            name += firstletter.toUpperCase() + therest + " ";
        }

        return name.trim();
    }
}
