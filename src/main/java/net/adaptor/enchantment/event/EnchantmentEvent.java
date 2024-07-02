package net.adaptor.enchantment.event;

import net.adaptor.enchantment.EnchantmentX;
import net.adaptor.enchantment.api.EnchantmentXRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class EnchantmentEvent {
    public static void enchantmentWorldTickHandler(World world) {
        MinecraftServer server = world.getServer();
        if (server == null) return;
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            for (EnchantmentX x: EnchantmentXRegistry.getEnchantmentXs()) {
                x.tickHandler(server,player);
            }
        }
    }
    public static ActionResult attackEntityHandler(PlayerEntity player, World world, Hand hand, Entity entity, HitResult hitResult) {
        for (EnchantmentX x: EnchantmentXRegistry.getEnchantmentXs()) {
            x.entityAttackHandler(player,world,hand,entity,hitResult);
        }
        return ActionResult.PASS;
    }
    public static TypedActionResult<ItemStack> useItemHandler(PlayerEntity player, World world, Hand hand) {
        for (EnchantmentX x: EnchantmentXRegistry.getEnchantmentXs()) {
            x.itemUseHandler(player,world,hand);
        }
        return TypedActionResult.pass(player.getMainHandStack());
    }
}
