package net.aros.afletching.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.aros.afletching.init.ModEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.aros.afletching.ArosFletching.MOD_ID;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;

    @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableBlend()V"))
    private void renderHotbarInject(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
        if (client != null && client.player != null && client.player.hasStatusEffect(ModEffects.CONFUSION)) {
            RenderSystem.setShaderTexture(0, new Identifier(MOD_ID, "textures/gui/captured_hotbar_overlay.png"));

            int width = 185, height = 26;
            int offset = -1;
            DrawableHelper.drawTexture(matrices, scaledWidth / 2 - 91+offset, scaledHeight - 22+offset, 0, 0, width, height, width, height);
        }
    }
}
