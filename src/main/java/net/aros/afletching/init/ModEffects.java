package net.aros.afletching.init;

import net.aros.afletching.effects.AllSeeingStatusEffect;
import net.aros.afletching.effects.ConfusionStatusEffect;
import net.aros.afletching.effects.RageStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.aros.afletching.ArosFletching.MOD_ID;

public class ModEffects {
    public static final RageStatusEffect RAGE = register("rage", new RageStatusEffect());
    public static final ConfusionStatusEffect CONFUSION = register("confusion", new ConfusionStatusEffect());
    public static final AllSeeingStatusEffect ALL_SEEING = register("all_seeing", new AllSeeingStatusEffect());

    static <T extends StatusEffect> T register(String name, T effect) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(MOD_ID, name), effect);
    }

    public static void init() {}
}
