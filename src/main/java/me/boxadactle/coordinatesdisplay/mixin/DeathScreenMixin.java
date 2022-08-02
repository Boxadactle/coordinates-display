package me.boxadactle.coordinatesdisplay.mixin;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.DecimalFormat;
import java.util.List;

@Mixin(DeathScreen.class)
public class DeathScreenMixin extends Screen {
    protected DeathScreenMixin(Text title) {
        super(title);
    }

    @Shadow @Final private List<ButtonWidget> buttons;

    @Inject(at = @At("RETURN"), method = "init")
    private void init(CallbackInfo ci) {
        if (CoordinatesDisplay.CONFIG.displayPosOnDeathScreen) {
            MinecraftClient c = MinecraftClient.getInstance();
            this.buttons.add(this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 120, 200, 20, Text.translatable("button.coordinatesdisplay.copy"), (button) -> {
                button.setMessage(Text.translatable("button.coordinatesdisplay.copied"));
                button.active = false;

                int x = (int) Math.round(c.player.getX());
                int y = (int) Math.round(c.player.getY());
                int z = (int) Math.round(c.player.getZ());

                c.keyboard.setClipboard(x + " " + y + " " + z);
                CoordinatesDisplay.LOGGER.info("Copied death position to clipboard");
            })));
        }
    }

    @Inject(at = @At("RETURN"), method = "render")
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (CoordinatesDisplay.CONFIG.displayPosOnDeathScreen) {
            MinecraftClient c = MinecraftClient.getInstance();

            DecimalFormat d = new DecimalFormat("0.00");

            String x = d.format(c.player.getX());
            String y = d.format(c.player.getY());
            String z = d.format(c.player.getZ());

            String pos = CoordinatesDisplay.DeathposColorPrefix + Text.translatable("message.coordinatesdisplay.location", x, y, z).getString();
            Text deathPos = Text.translatable("message.coordinatesdisplay.deathpos", pos);
            drawCenteredText(matrices, this.textRenderer, deathPos, this.width / 2, 115, 16777215);
        }
    }
}