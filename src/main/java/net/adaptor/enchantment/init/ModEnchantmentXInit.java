package net.adaptor.enchantment.init;

import net.adaptor.enchantment.EnchantmentX;
import net.adaptor.enchantment.Main;
import net.adaptor.enchantment.api.EnchantmentXProvider;
import net.adaptor.enchantment.api.EnchantmentXRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

import java.util.Random;

public class ModEnchantmentXInit implements EnchantmentXProvider {

    public static final RegistryKey<Enchantment> WISDOM = of(Main.id("wisdom"));
    public static final RegistryKey<Enchantment> LEACH = of(Main.id("leach"));
    public static final RegistryKey<Enchantment> MANIC = of(Main.id("manic"));

    private static RegistryKey<Enchantment> of(Identifier id) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, id);
    }

    public static EnchantmentX efficiency = new EnchantmentX(Enchantments.EFFICIENCY)
            .setSlot(EquipmentSlot.MAINHAND)
            .setItemUseExecutor(((player, world, hand, level) -> player.sendMessage(Text.of("test message efficiency"),true)));
    @Override
    public void addEnchantmentXs() {

        EnchantmentXRegistry.registerX(efficiency);
        EnchantmentX wisdom = new EnchantmentX(WISDOM)
                .setSlot(EquipmentSlot.HEAD)
                .setTickHandler((server, player, level) -> {
                    if (player.experienceLevel >= level * 10) return;
                    double chance = new Random().nextDouble();
                    if (chance > 0.01 * level) return;
                    ExperienceOrbEntity orb = EntityType.EXPERIENCE_ORB.spawn(player.getServerWorld(),player.getBlockPos().add(0,2,0), SpawnReason.EVENT);
                    if (orb!=null) orb.setId(new Random().nextInt(25565));
                });
        EnchantmentXRegistry.registerX(wisdom);
        EnchantmentX manic = new EnchantmentX(MANIC)
                .setSlot(EquipmentSlot.MAINHAND)
                .setTickHandler((server, player, level) -> {
                    int gap = player.age - player.getLastAttackTime();
                    if (gap < 20 && gap % 5 == 0) {
                        float value = (float) (player.getAttributes().getValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.02 * level);
                        LivingEntity target = player.getWorld().getClosestEntity(
                                LivingEntity.class,
                                TargetPredicate.createNonAttackable().setBaseMaxDistance(6.0*level),
                                player,
                                player.getX(),
                                player.getY(),
                                player.getZ(),
                                player.getBoundingBox().expand(2*level,2,2*level)
                        );
                        if (target != null) target.damage(player.getDamageSources().magic(), value * 2);
                        player.damage(player.getDamageSources().magic(), value);
                    }
                });
        EnchantmentXRegistry.registerX(manic);

        EnchantmentX leach = new EnchantmentX(LEACH)
                .setSlot(EquipmentSlot.MAINHAND)
                .setAttackEntityHandler(((player, world, hand, entity, hitResult, level) -> {
                    int gap = player.age - player.getLastAttackTime();
                    if (gap < 20 && gap % 5 == 0) {
                        float value = (float) (player.getAttributes().getValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.02 * level);
                        player.heal(value);
                        player.sendMessage(Text.of("+❤" + 4 * value), true);
                    }
                }));
        EnchantmentXRegistry.registerX(leach);
    }
}
