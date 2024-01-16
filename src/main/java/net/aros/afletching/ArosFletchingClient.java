package net.aros.afletching;

import net.aros.afletching.client.CustomArrowRenderer;
import net.aros.afletching.init.ModEntityTypes;
import net.aros.afletching.init.ModOtherThings;
import net.aros.afletching.screen.FletchingScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

import static net.aros.afletching.ArosFletching.MOD_ID;

public class ArosFletchingClient implements ClientModInitializer {
    public static final Identifier ARROW_PARTICLES = new Identifier(MOD_ID, "arrow_particles_s2c");

    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModOtherThings.FLETCHING_SCREEN_HANDLER, FletchingScreen::new);

        EntityRendererRegistry.register(ModEntityTypes.TNT_ARROW, ctx -> new CustomArrowRenderer(ctx, new Identifier(MOD_ID, "textures/entity/projectiles/tnt_arrow.png")));
        EntityRendererRegistry.register(ModEntityTypes.GLOWING_ARROW, ctx -> new CustomArrowRenderer(ctx, new Identifier(MOD_ID, "textures/entity/projectiles/glowing_arrow.png")));

        ClientPlayNetworking.registerGlobalReceiver(ARROW_PARTICLES, (client, handler, buf, rs) -> {
            Random r = new Random();
            BlockPos pos = buf.readBlockPos();
            if (client.world == null) return;
            for (int i = 0; i < 30; i++)
                client.particleManager.addParticle(ParticleTypes.GLOW, pos.getX(), pos.getY(), pos.getZ(), r.nextDouble() - r.nextDouble(), r.nextDouble() - r.nextDouble(), r.nextDouble() - r.nextDouble());
        });
    }
}
