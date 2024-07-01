package net.adaptor.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModTags {
    public static class Enchantments {
        public static final TagKey<Enchantment> WISDOM = createTag("wisdom");
        public static final TagKey<Enchantment> LEACH = createTag("leach");
        public static final TagKey<Enchantment> MANIC = createTag("manic");

        private static TagKey<Enchantment> createTag(String name) {
            return TagKey.of(RegistryKeys.ENCHANTMENT, Main.id(name));
        }
    }
}
