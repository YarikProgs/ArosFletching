package net.aros.afletching.init;

import net.aros.afletching.screen.FletchingScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import static net.aros.afletching.ArosFletching.MOD_ID;

public class ModOtherThings {
    public static ExtendedScreenHandlerType<FletchingScreenHandler> FLETCHING_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(FletchingScreenHandler::new);
    public static Identifier INTERACT_WITH_FLETCHING_TABLE_STAT;

    public static void init() {
        FLETCHING_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, new Identifier(MOD_ID, "fletching"), FLETCHING_SCREEN_HANDLER);
        INTERACT_WITH_FLETCHING_TABLE_STAT = registerFletchingStat();
    }

    private static @NotNull Identifier registerFletchingStat() {
        Identifier identifier = new Identifier("interact_with_fletching_table");
        Registry.register(Registry.CUSTOM_STAT, "interact_with_fletching_table", identifier);
        Stats.CUSTOM.getOrCreateStat(identifier, StatFormatter.DEFAULT);
        return identifier;
    }
}
