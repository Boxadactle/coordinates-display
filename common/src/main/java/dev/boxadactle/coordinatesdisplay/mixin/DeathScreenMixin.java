package dev.boxadactle.coordinatesdisplay.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.DecimalFormat;

@Mixin(DeathScreen.class)
public class DeathScreenMixin extends Screen {
    protected DeathScreenMixin(Component title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void init(CallbackInfo ci) {
        if (CoordinatesDisplay.CONFIG.get().displayPosOnDeathScreen) {
            addRenderableWidget(new Button.Builder(Component.translatable("button.coordinatesdisplay.copy"), (button) -> {
                button.setMessage(Component.translatable("button.coordinatesdisplay.copied"));
                button.active = false;

                int x = (int) Math.round(ClientUtils.getClient().player.getX());
                int y = (int) Math.round(ClientUtils.getClient().player.getY());
                int z = (int) Math.round(ClientUtils.getClient().player.getZ());

                ClientUtils.getClient().keyboardHandler.setClipboard(x + " " + y + " " + z);
                CoordinatesDisplay.LOGGER.info("Copied death position to clipboard");
            }).bounds(this.width / 2 - 100, this.height / 4 + 120, 200, 20).build());
        }
    }

    @Inject(at = @At("RETURN"), method = "render")
    private void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (CoordinatesDisplay.CONFIG.get().displayPosOnDeathScreen) {
            DecimalFormat d = new DecimalFormat("0.00");

            String x = d.format(ClientUtils.getClient().player.getX());
            String y = d.format(ClientUtils.getClient().player.getY());
            String z = d.format(ClientUtils.getClient().player.getZ());
            Component pos = GuiUtils.colorize(Component.translatable("message.coordinatesdisplay.location", x, y, z), CoordinatesDisplay.CONFIG.get().deathPosColor);
            RenderUtils.drawTextCentered(guiGraphics, Component.translatable("message.coordinatesdisplay.deathpos", pos), this.width / 2, 115, GuiUtils.WHITE);
        }
    }
}
