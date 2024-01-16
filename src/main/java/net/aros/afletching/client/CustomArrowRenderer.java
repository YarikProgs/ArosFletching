package net.aros.afletching.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;

public class CustomArrowRenderer extends ProjectileEntityRenderer<PersistentProjectileEntity> {
    private final Identifier texture;

    public CustomArrowRenderer(EntityRendererFactory.Context context, Identifier texture) {
        super(context);
        this.texture = texture;
    }

    @Override
    public Identifier getTexture(PersistentProjectileEntity arrowEntity) {
        return texture;
    }
}
