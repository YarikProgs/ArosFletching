package net.aros.afletching;

import net.aros.afletching.init.*;
import net.aros.afletching.network.DoWhistleC2SPacket;
import net.aros.afletching.util.WhistleHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArosFletching implements ModInitializer {
	public static final String MOD_ID = "afletching";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Aros's fletching is initializing!");
		ModRecipes.init();
		ModOtherThings.init();
		ModEntityTypes.init();
		ModItems.init();
		ModSounds.init();
		ModEffects.init();

		ServerPlayNetworking.registerGlobalReceiver(DoWhistleC2SPacket.ID, DoWhistleC2SPacket::receive);
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> WhistleHelper.copy(newPlayer, WhistleHelper.getWhistleData(oldPlayer)));
	}
}
