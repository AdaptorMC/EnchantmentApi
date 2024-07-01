package net.adaptor.enchantment.client;

import net.adaptor.enchantment.client.tooltip.EnchantmentDescTooltip;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class EnchantmentApiClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register(new EnchantmentDescTooltip());
    }
}
