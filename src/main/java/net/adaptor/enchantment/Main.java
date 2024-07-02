package net.adaptor.enchantment;

import net.adaptor.enchantment.api.EnchantmentXRegistry;
import net.adaptor.enchantment.event.EnchantmentEvent;
import net.adaptor.enchantment.init.ModEnchantmentXInit;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.impl.event.interaction.InteractionEventsRouter;
import net.minecraft.entity.EntityInteraction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
	public static final String MOD_ID = "enchantment_api";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Identifier id(String name) {
		return Identifier.of(Main.MOD_ID, name);
	}

	@Override
	public void onInitialize() {
		EnchantmentXRegistry.registerXProvider(new ModEnchantmentXInit());
		ServerTickEvents.END_WORLD_TICK.register(EnchantmentEvent::enchantmentWorldTickHandler);
		AttackEntityCallback.EVENT.register(EnchantmentEvent::attackEntityHandler);
		UseItemCallback.EVENT.register(EnchantmentEvent::useItemHandler);
		EnchantmentXRegistry.initializeEnchantmentXs();
	}
}