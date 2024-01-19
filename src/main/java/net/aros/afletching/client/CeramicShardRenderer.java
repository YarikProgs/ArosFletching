package net.aros.afletching.client;

import net.aros.afletching.projectiles.CeramicShardEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

import static net.aros.afletching.ArosFletching.MOD_ID;

public class CeramicShardRenderer extends ProjectileEntityRenderer<CeramicShardEntity> {
    public CeramicShardRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(CeramicShardEntity entity) {
        return new Identifier(MOD_ID, "textures/entity/projectiles/ceramic_shard.png");
    }
}
