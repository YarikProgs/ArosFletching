package net.aros.afletching;

import net.aros.afletching.client.CeramicShardRenderer;
import net.aros.afletching.client.CustomArrowRenderer;
import net.aros.afletching.init.ModEntityTypes;
import net.aros.afletching.init.ModOtherThings;
import net.aros.afletching.screen.FletchingScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.util.Identifier;

import static net.aros.afletching.ArosFletching.MOD_ID;

public class ArosFletchingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModOtherThings.FLETCHING_SCREEN_HANDLER, FletchingScreen::new);

        EntityRendererRegistry.register(ModEntityTypes.TNT_ARROW, ctx -> new CustomArrowRenderer(ctx, new Identifier(MOD_ID, "textures/entity/projectiles/tnt_arrow.png")));
        EntityRendererRegistry.register(ModEntityTypes.GLOWING_ARROW, ctx -> new CustomArrowRenderer(ctx, new Identifier(MOD_ID, "textures/entity/projectiles/glowing_arrow.png")));
        EntityRendererRegistry.register(ModEntityTypes.PRISMARINE_ARROW, ctx -> new CustomArrowRenderer(ctx, new Identifier(MOD_ID, "textures/entity/projectiles/prismarine_arrow.png")));
        EntityRendererRegistry.register(ModEntityTypes.TERRACOTTA_ARROW, ctx -> new CustomArrowRenderer(ctx, new Identifier(MOD_ID, "textures/entity/projectiles/terracotta_arrow.png")));
        EntityRendererRegistry.register(ModEntityTypes.BEGINNER_ARROW, ctx -> new CustomArrowRenderer(ctx, new Identifier(MOD_ID, "textures/entity/projectiles/beginner_arrow.png")));
        EntityRendererRegistry.register(ModEntityTypes.CERAMIC_SHARD, CeramicShardRenderer::new);
    }
}
