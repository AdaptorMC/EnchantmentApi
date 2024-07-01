package net.adaptor.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class EnchantmentX {

    private ItemStack itemStack;
    private final RegistryKey<Enchantment> enchantment;
    public EquipmentSlot slot;
    private EnchantmentExecutor tickExecutor;
    private AttackEntityHandler entityAttackExecutor;
    private ItemUseHandler itemUseHandler;
    private int gap;

    public EnchantmentX(RegistryKey<Enchantment> enchantment) {
        this.itemStack = ItemStack.EMPTY;
        this.enchantment = enchantment;
        this.slot = EquipmentSlot.BODY;
        this.gap = 20;
        this.tickExecutor = null;
        this.entityAttackExecutor = null;
        this.itemUseHandler = null;
    }

    public EnchantmentX setGap(int gap) {
        this.gap = gap;
        return this;
    }

    public EnchantmentX setSlot(EquipmentSlot slot) {
        this.slot = slot;
        return this;
    }

    public EnchantmentX setTickExecutor(EnchantmentExecutor executor) {
        this.tickExecutor = executor;
        return this;
    }

    public EnchantmentX setEntityAttackExecutor(AttackEntityHandler executor) {
        this.entityAttackExecutor = executor;
        return this;
    }

    public EnchantmentX setItemUseExecutor(ItemUseHandler executor) {
        this.itemUseHandler = executor;
        return this;
    }

    @FunctionalInterface
    public interface EnchantmentExecutor {
        void run(MinecraftServer server, ServerPlayerEntity player, int level);
    }

    @FunctionalInterface
    public interface AttackEntityHandler {
        void run(PlayerEntity player, World world, Hand hand, Entity entity, HitResult hitResult, int level);
    }

    @FunctionalInterface
    public interface ItemUseHandler {
        void run(PlayerEntity player, World world, Hand hand, int level);
    }

    public void tickHandler(MinecraftServer server, ServerPlayerEntity player) {
        if (player.getEquippedStack(slot).isEmpty()) return;
        itemStack = player.getEquippedStack(slot);
        if (!itemStack.hasEnchantments()) return;
        if (!itemStack.getEnchantments().getEnchantments().contains(of(server,enchantment.getValue()))) return;
        if (tickExecutor ==null) return;
        String current = "enchantment_api."+enchantment.getValue().toTranslationKey()+"#"+player.age;
        if (player.getCommandTags().isEmpty()) {
            tickExecutor.run(server,player, EnchantmentHelper.getLevel(of(server,enchantment.getValue()),itemStack));
            player.addCommandTag(current);
        }
        else {
            for (String commandTag:player.getCommandTags()) {
                List<String> part = List.of(commandTag.split("#"));
                if (part.size()!=2) return;
                if (part.getFirst().equals("enchantment_api."+enchantment.getValue().toTranslationKey())) {
                    int lastTime = Integer.parseInt(part.getLast());
                    if (Math.abs(player.age-lastTime)>gap) {
                        tickExecutor.run(server,player, EnchantmentHelper.getLevel(of(server,enchantment.getValue()),itemStack));
                        player.removeCommandTag(commandTag);
                        player.addCommandTag(current);
                    }
                }
                else {
                    tickExecutor.run(server,player, EnchantmentHelper.getLevel(of(server,enchantment.getValue()),itemStack));
                    player.addCommandTag(current);
                }
            }
        }
    }

    public void entityAttackHandler(PlayerEntity player, World world, Hand hand, Entity entity, HitResult hitResult) {
        if (entityAttackExecutor == null) return;
        if (world.getServer()==null) return;
        entityAttackExecutor.run(player,world,hand,entity,hitResult,EnchantmentHelper.getLevel(of(world.getServer(),enchantment.getValue()),itemStack));
    }

    public void itemUseHandler(PlayerEntity player,World world, Hand hand) {
        if (world.getServer()==null) return;
        itemUseHandler.run(player,world,hand,EnchantmentHelper.getLevel(of(world.getServer(),enchantment.getValue()),itemStack));
    }

    public static void leachHandler(MinecraftServer server, ServerPlayerEntity player) {
//        ItemStack item = player.getEquippedStack(EquipmentSlot.MAINHAND);
//        if (hasEnchantment(item, ModTags.Enchantments.LEACH)) {
//            int gap = player.age - player.getLastAttackTime();
//
//            if (gap < 20 && gap % 5 == 0) {
//                float value = (float) (player.getAttributes().getValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.02 * level(server, item, Main.id("leach")));
//                player.heal(value);
//                player.sendMessage(Text.of("+❤" + 4 * value), true);
//            }
//        }
    }

    public static void manicHandler(MinecraftServer server, ServerPlayerEntity player) {
//        ItemStack item = player.getEquippedStack(EquipmentSlot.MAINHAND);
//        if (hasEnchantment(item, ModTags.Enchantments.MANIC)) {
//            int level = level(server, item, Main.id("manic"));
//
//            int gap = player.age - player.getLastAttackTime();
//
//            if (gap < 20 && gap % 5 == 0) {
//                float value = (float) (player.getAttributes().getValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.02 * level);
//                LivingEntity target = getNearestEntity(player.getWorld(), player, level);
//                if (target != null) target.damage(player.getDamageSources().magic(), value * 2);
//                player.damage(player.getDamageSources().magic(), value);
//            }
//        }
    }

    public static boolean hasEnchantment(ItemStack itemStack, TagKey<Enchantment> enchantment) {
        return EnchantmentHelper.hasAnyEnchantmentsIn(itemStack, enchantment);
    }

    public boolean checkEnchantment (ServerPlayerEntity player) {
        if (player.getMainHandStack().getItem().equals(this.itemStack.getItem())) return false;
        return false;
    }

    private static RegistryEntry<Enchantment> of(MinecraftServer server, Identifier id) {
        return server.getRegistryManager().get(RegistryKeys.ENCHANTMENT)
                .getEntry(id)
                .orElseThrow(() -> new IllegalArgumentException("Enchantment with id " + id + " not found"));
    }

    public static LivingEntity getNearestEntity(World world, Entity sourceEntity, double maxDistance) {
        Vec3d sourcePos = sourceEntity.getPos();
        Box searchBox = new Box(
                sourcePos.x - maxDistance, sourcePos.y - maxDistance, sourcePos.z - maxDistance,
                sourcePos.x + maxDistance, sourcePos.y + maxDistance, sourcePos.z + maxDistance
        );

        LivingEntity nearestEntity = null;
        double nearestDistance = Double.MAX_VALUE;

        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, searchBox, e -> e != sourceEntity)) {
            double distance = entity.squaredDistanceTo(sourcePos);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestEntity = entity;
            }
        }

        return nearestEntity;
    }
}
