package net.aros.afletching;

import net.aros.afletching.init.ModOtherThings;
import net.aros.afletching.screen.FletchingScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ArosFletchingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModOtherThings.FLETCHING_SCREEN_HANDLER, FletchingScreen::new);
    }
}
