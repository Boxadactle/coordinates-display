package me.boxadactle.coordinatesdisplay.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
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

    Minecraft client = Minecraft.getInstance();

    @Inject(at = @At("RETURN"), method = "init")
    private void init(CallbackInfo ci) {
        if (CoordinatesDisplay.CONFIG.get().displayPosOnDeathScreen) {
            this.addRenderableWidget(new PlainTextButton(this.width / 2 - 100, this.height / 4 + 120, 200, 20, Component.translatable("button.coordinatesdisplay.copy"), (button) -> {
                button.setMessage(Component.literal("button.coordinatesdisplay.copied"));
                button.active = false;

                int x = (int) Math.round(client.player.getX());
                int y = (int) Math.round(client.player.getY());
                int z = (int) Math.round(client.player.getZ());

                client.keyboardHandler.setClipboard(x + " " + y + " " + z);
                CoordinatesDisplay.LOGGER.info("Copied death position to clipboard");
            }, Minecraft.getInstance().font));
        }
    }

    @Inject(at = @At("RETURN"), method = "render")
    private void render(PoseStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (CoordinatesDisplay.CONFIG.get().displayPosOnDeathScreen) {
            DecimalFormat d = new DecimalFormat("0.00");

            String x = d.format(client.player.getX());
            String y = d.format(client.player.getY());
            String z = d.format(client.player.getZ());

            Component pos = Component.translatable("message.coordinatesdisplay.location", x, y, z).withStyle(style -> style.withColor(ModUtil.getColorDecimal(CoordinatesDisplay.CONFIG.get().deathPosColor)));
            drawCenteredString(matrices, this.font, Component.translatable("message.coordinatesdisplay.deathpos", pos), this.width / 2, 115, ModUtil.WHITE);
        }
    }
}