package net.aros.afletching.network;

import net.aros.afletching.util.WhistleHelper;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

import static net.aros.afletching.ArosFletching.MOD_ID;

public class WhistleS2CPacket {
    public static final Identifier ID = new Identifier(MOD_ID, "whistle");

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        if (client != null && client.player != null && client.world != null) {
            WhistleHelper.whistleFor(client.player, findEntity(client.world, buf.readUuid()), buf.readBoolean());
            client.player.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1f, 1.2f * 3f / (client.player.getRandom().nextFloat() * 0.2f + 0.9f));
        }
    }

    private static @Nullable LivingEntity findEntity(@NotNull ClientWorld world, UUID uuid) {
        for (Entity e : world.getEntities()) if (Objects.equals(e.getUuid(), uuid) && e instanceof LivingEntity livingEntity) return livingEntity;
        return null;
    }
}
