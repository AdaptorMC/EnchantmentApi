package net.adaptor.enchantment.client.tooltip;

import net.adaptor.enchantment.Main;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;

import java.util.List;

public class EnchantmentDescTooltip implements ItemTooltipCallback {
    @Override
    public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> lines) {
        EnchantmentHelper.getEnchantments(stack).getEnchantments().forEach(enchantment -> {
            RegistryKey<Enchantment> registryKey= enchantment.getKey().get(); // mojang change a thing need to get by this :(
            String translateKey = registryKey.getValue().toTranslationKey("enchantment"); // mojang change a thing need to get by this :( | this one is prefix like <prefix>.<id>.desc
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).getContent() instanceof TranslatableTextContent text && text.getKey().equals(translateKey)) {
                    Main.LOGGER.info(translateKey);
                    lines.add(i + 1, Text.literal(" - ").formatted(Formatting.GRAY).append(Text.translatable(translateKey + ".desc").formatted(Formatting.DARK_GRAY)));
                    break;
                }
            }
        });
    }
}
