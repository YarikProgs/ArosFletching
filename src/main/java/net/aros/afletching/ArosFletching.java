package net.aros.afletching;

import net.aros.afletching.init.ModEntityTypes;
import net.aros.afletching.init.ModItems;
import net.aros.afletching.init.ModOtherThings;
import net.aros.afletching.init.ModRecipes;
import net.fabricmc.api.ModInitializer;
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
		ModItems.init();
		ModEntityTypes.init();
	}
}
