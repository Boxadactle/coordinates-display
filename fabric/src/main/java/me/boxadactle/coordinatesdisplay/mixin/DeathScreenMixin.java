package me.boxadactle.coordinatesdisplay.mixin;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import me.boxadactle.coordinatesdisplay.util.ModUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.DecimalFormat;

@Mixin(DeathScreen.class)
public class DeathScreenMixin extends Screen {
    protected DeathScreenMixin(Text title) {
        super(title);
    }

    MinecraftClient client = MinecraftClient.getInstance();

    @Inject(at = @At("RETURN"), method = "init")
    private void init(CallbackInfo ci) {
        if (CoordinatesDisplay.CONFIG.displayPosOnDeathScreen) {
            this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 120, 200, 20, new TranslatableText("button.coordinatesdisplay.copy"), (button) -> {
                button.setMessage(new TranslatableText("button.coordinatesdisplay.copied"));
                button.active = false;

                int x = (int) Math.round(client.player.getX());
                int y = (int) Math.round(client.player.getY());
                int z = (int) Math.round(client.player.getZ());

                client.keyboard.setClipboard(x + " " + y + " " + z);
                CoordinatesDisplay.LOGGER.info("Copied death position to clipboard");
            }));
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

            Text pos = new TranslatableText("message.coordinatesdisplay.location", x, y, z).styled(style -> style.withColor(CoordinatesDisplay.CONFIG.deathPosColor));
            Text deathPos = new TranslatableText("message.coordinatesdisplay.deathpos", pos).styled(style -> style.withColor(CoordinatesDisplay.CONFIG.definitionColor));
            drawCenteredText(matrices, this.textRenderer, deathPos, this.width / 2, 115, ModUtils.WHITE);
        }
    }
}
