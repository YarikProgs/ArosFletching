package net.aros.afletching.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class WhistleHelper {
    private WhistleHelper() {}

    public static void whistleFor(@NotNull PlayerEntity whistler, LivingEntity target, boolean hurtTarget) {
        NbtCompound data = new NbtCompound();
        WhistleData whistleData = getWhistleData(whistler);
        data.putString("Target", target == null ? "" : target.getUuid().toString());
        data.putInt("Stack", (whistleData.equals(target) || whistleData.isEmpty()) && hurtTarget ? whistleData.stack() + 1 : whistleData.stack());
        IEntityDataSaver.of(whistler).aengine_getPersistentData().put("WhistleData", data);
    }

    public static void copy(PlayerEntity whistler, @NotNull WhistleData whistleData) {
        NbtCompound data = new NbtCompound();
        data.putString("Target", whistleData.target() == null ? "" : whistleData.target().toString());
        data.putInt("Stack", whistleData.stack());
        IEntityDataSaver.of(whistler).aengine_getPersistentData().put("WhistleData", data);
    }

    public static @NotNull WhistleData getWhistleData(@NotNull PlayerEntity player) {
        NbtCompound data = IEntityDataSaver.of(player).aengine_getPersistentData().getCompound("WhistleData");
        return new WhistleData(
                player,
                data.getString("Target").isEmpty() ? null : UUID.fromString(data.getString("Target")),
                data.getInt("Stack")
        );
    }

    public static void clearWhistle(@NotNull PlayerEntity whistler) {
        IEntityDataSaver.of(whistler).aengine_getPersistentData().put("WhistleData", new NbtCompound());
    }
}
