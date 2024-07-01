package net.adaptor.enchantment.api;

import net.adaptor.enchantment.EnchantmentX;
import net.adaptor.enchantment.Main;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentXRegistry {
    private static final List<EnchantmentX> enchantmentXs = new ArrayList<>();
    private static final List<EnchantmentXProvider> providers = new ArrayList<>();

    public static void registerX(EnchantmentX x) {
        enchantmentXs.add(x);
    }

    public static void registerXProvider(EnchantmentXProvider provider) {
        providers.add(provider);
        provider.addEnchantmentXs();
    }

    public static List<EnchantmentX> getEnchantmentXs() {
        for (EnchantmentXProvider provider : providers) {
            provider.addEnchantmentXs();
        }
        return enchantmentXs;
    }

    public static void initializeEnchantmentXs() {
        Main.LOGGER.info("Loaded {} recipes", enchantmentXs.size());
    }
}
