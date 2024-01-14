package net.aros.afletching;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArosFletching implements ModInitializer {
	public static final String MOD_ID = "afletching";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Aros's fletching is initializing!");
	}
}
