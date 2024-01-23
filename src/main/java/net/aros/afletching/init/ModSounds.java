package net.aros.afletching.init;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.aros.afletching.ArosFletching.MOD_ID;

public class ModSounds {
    public static final SoundEvent TERRACOTTA_ARROW_BLOCK_HIT = register(new Identifier(MOD_ID, "terracotta_arrow.block_hit"));
    public static final SoundEvent TERRACOTTA_ARROW_ENTITY_HIT = register(new Identifier(MOD_ID, "terracotta_arrow.entity_hit"));

    static SoundEvent register(Identifier sound) {
        return Registry.register(Registry.SOUND_EVENT, sound, new SoundEvent(sound));
    }

    public static void init() {}
}
