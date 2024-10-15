package io.bluestaggo.divergeprog.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Unique
    private static boolean isBroken(ItemStack equipment) {
        return equipment.isDamageable() && equipment.getDamage() >= equipment.getMaxDamage();
    }

    @Inject(
        method = "forEachEnchantment(Lnet/minecraft/enchantment/EnchantmentHelper$Consumer;Lnet/minecraft/item/ItemStack;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void forEachEnchantmentButUnbroken(
        EnchantmentHelper.Consumer consumer, ItemStack stack, CallbackInfo ci
    ) {
        if (isBroken(stack)) {
            ci.cancel();
        }
    }

    @Inject(
        method = "getLevel",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void getLevelButUnbroken(
        Enchantment enchantment,
        ItemStack stack,
        CallbackInfoReturnable<Integer> cir
    ) {
        if (isBroken(stack)) {
            cir.setReturnValue(0);
        }
    }
}
