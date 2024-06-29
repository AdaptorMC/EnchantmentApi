package net.adaptor.enchantment_api;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.adaptor.enchantment_api.enchantment.EnchantAttributeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
	public static final String MOD_ID = "adaptor";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Identifier id(String name) {
		return Identifier.of(Main.MOD_ID, name);
	}

	@Override
	public void onInitialize() {
		EnchantAttributeHandler.registerEnchantmentAttributes();
	}
}