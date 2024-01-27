package net.aros.afletching.util;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

public interface IEntityDataSaver {
    NbtCompound aengine_getPersistentData();

    static IEntityDataSaver of(Entity entity) {
        return (IEntityDataSaver) entity;
    }
}
