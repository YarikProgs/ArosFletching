package net.aros.afletching.network;

import net.aros.afletching.util.WhistleHelper;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.UUID;

import static net.aros.afletching.ArosFletching.MOD_ID;

public class DoWhistleC2SPacket {
    public static final Identifier ID = new Identifier(MOD_ID, "do_whistle");

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        UUID uuid = buf.readUuid();
        int stack = buf.readInt();

        LivingEntity entity = null;
        for (ServerWorld world : server.getWorlds()) if ((entity = (LivingEntity) world.getEntity(uuid)) != null) break;
        if (entity == null) {
            WhistleHelper.clearWhistle(player);
            return;
        }

        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20 * 20, 0, false, false));
        if (stack > 0) entity.damage(DamageSource.player(player).setUsesMagic().setBypassesProtection().setBypassesArmor(), 4 * stack);
        WhistleHelper.clearWhistle(player);
    }
}
