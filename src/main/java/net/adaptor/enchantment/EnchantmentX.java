package net.adaptor.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.List;

public class EnchantmentX {

    public static int lastTick = 0;

    private ItemStack itemStack;
    private final RegistryKey<Enchantment> enchantment;
    public EquipmentSlot slot;
    private TickHandler tickHandler;
    private AttackEntityHandler attackEntityHandler;
    private ItemUseHandler itemUseHandler;

    public EnchantmentX(RegistryKey<Enchantment> enchantment) {
        this.itemStack = ItemStack.EMPTY;
        this.enchantment = enchantment;
        this.slot = EquipmentSlot.BODY;
        this.tickHandler = null;
        this.attackEntityHandler = null;
        this.itemUseHandler = null;
    }
    public EnchantmentX setSlot(EquipmentSlot slot) {
        this.slot = slot;
        return this;
    }

    public EnchantmentX setTickHandler(TickHandler executor) {
        this.tickHandler = executor;
        return this;
    }

    public EnchantmentX setAttackEntityHandler(AttackEntityHandler executor) {
        this.attackEntityHandler = executor;
        return this;
    }

    public EnchantmentX setItemUseExecutor(ItemUseHandler executor) {
        this.itemUseHandler = executor;
        return this;
    }

    /*
    * Handlers that handler from different events
    * every enchantment can do different purpose in different situation
    * */
    /*
    * Tick handler that process every tick
    * avoid use this is recommend
    * */
    @FunctionalInterface
    public interface TickHandler {
        void run(MinecraftServer server, ServerPlayerEntity player, int level);
    }
    public void tickHandler(MinecraftServer server, ServerPlayerEntity player) {
        if (tickHandler ==null) return;
        if (lastTick==server.getTicks()) return;
        lastTick = server.getTicks();
        if (player.getEquippedStack(slot).isEmpty()) return;
        itemStack = player.getEquippedStack(slot);
        if (noEnchantment(server)) return;
        tickHandler.run(server,player, getLevel(server));
    }
    /*
     * Attack handler that process when hit an entity
     * */
    @FunctionalInterface
    public interface AttackEntityHandler {
        void run(PlayerEntity player, World world, Hand hand, Entity entity, HitResult hitResult, int level);
    }
    public void entityAttackHandler(PlayerEntity player, World world, Hand hand, Entity entity, HitResult hitResult) {
        if (attackEntityHandler == null) return;
        if (world.getServer()==null) return;
        if (noEnchantment(world.getServer())) return;
        attackEntityHandler.run(player,world,hand,entity,hitResult,getLevel(world.getServer()));
    }
    /*
     * Handler that process when use item (Right Click)
     * */
    @FunctionalInterface
    public interface ItemUseHandler {
        void run(PlayerEntity player, World world, Hand hand, int level);
    }
    public void itemUseHandler(PlayerEntity player,World world, Hand hand) {
        if (itemUseHandler == null) return;
        if (world.getServer()==null) return;
        if (noEnchantment(world.getServer())) return;
        itemUseHandler.run(player,world,hand,getLevel(world.getServer()));
    }
    /*
    * common functions
    * */
    public boolean noEnchantment(MinecraftServer server) {
        if (!itemStack.hasEnchantments()) return true;
        return !itemStack.getEnchantments().getEnchantments().contains(getEntry(server, enchantment.getValue()));
    }
    public int getLevel(MinecraftServer server) {
        return EnchantmentHelper.getLevel(getEntry(server,enchantment.getValue()),itemStack);
    }

    private static RegistryEntry<Enchantment> getEntry(MinecraftServer server, Identifier id) {
        return server.getRegistryManager().get(RegistryKeys.ENCHANTMENT)
                .getEntry(id)
                .orElseThrow(() -> new IllegalArgumentException("Enchantment with id " + id + " not found"));
    }
}
