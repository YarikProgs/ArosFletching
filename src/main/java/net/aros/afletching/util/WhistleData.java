package net.aros.afletching.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public record WhistleData(@NotNull PlayerEntity whistler, @Nullable UUID target, int stack) {
    public boolean isEmpty() {
        return target == null;
    }

    public boolean equals(UUID uuid) {
        return Objects.equals(target, uuid);
    }

    public boolean equals(LivingEntity target) {
        return target != null && equals(target.getUuid());
    }
}
