package io.bluestaggo.divergeprog.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.bluestaggo.divergeprog.DivergentProgression;
import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements FabricItemStack {
    @Unique private static final Text BROKEN_TEXT = Text.translatable(Util.createTranslationKey(
            "item", DivergentProgression.id("tooltip.broken"))).formatted(Formatting.RED);

    @Shadow public abstract boolean isDamageable();
    @Shadow public abstract int getDamage();
    @Shadow public abstract int getMaxDamage();
    @Shadow public abstract Item getItem();
    @Shadow public abstract void decrement(int amount);
    @Shadow public abstract NbtCompound getNbt();

    @Unique
    private boolean isBroken() {
        return isDamageable() && getDamage() >= getMaxDamage();
    }

    @Inject(
            method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;decrement(I)V",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    private void damageRestrictDecrement(
        int amount,
        LivingEntity entity,
        Consumer<Item> breakCallback,
        CallbackInfo ci
    ) {
        Item item = getItem();
        boolean noDestroy = item instanceof ToolItem || item instanceof ArmorItem || item instanceof ShieldItem;
        if (!noDestroy) {
            decrement(amount);
        }

        ci.cancel();
    }

    @Inject(
            method = "getTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/Item;appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/client/item/TooltipContext;)V",
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            )
    )
    private void getBrokenTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        if (isBroken()) {
            list.add(BROKEN_TEXT);
        }
    }

    @Inject(
            method = "useOnBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    private void useOnBlockIfNotBroken(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (isBroken()) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }

    @Inject(
            method = "getMiningSpeedMultiplier",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getMiningSpeedMultiplierIfNotBroken(BlockState state, CallbackInfoReturnable<Float> cir) {
        if (isBroken()) {
            cir.setReturnValue(1.0f);
        }
    }

    @Inject(
            method = "use",
            at = @At("HEAD"),
            cancellable = true
    )
    private void useIfNotBroken(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (isBroken()) {
            cir.setReturnValue(TypedActionResult.fail((ItemStack) (Object) this));
        }
    }

    @Inject(
            method = "finishUsing",
            at = @At("HEAD"),
            cancellable = true
    )
    private void finishUsingIfNotBroken(World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (isBroken()) {
            cir.setReturnValue((ItemStack) (Object) this);
        }
    }


    @Inject(
            method = "postHit",
            at = @At("HEAD"),
            cancellable = true
    )
    private void postDamageEntityIfNotBroken(LivingEntity target, PlayerEntity player, CallbackInfo ci) {
        if (isBroken()) {
            ci.cancel();
        }
    }

    @Inject(
            method = "postMine",
            at = @At("HEAD"),
            cancellable = true
    )
    private void postMineIfNotBroken(World world, BlockState state, BlockPos pos, PlayerEntity miner, CallbackInfo ci) {
        if (isBroken()) {
            ci.cancel();
        }
    }

    @Inject(
            method = "isSuitableFor",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isSuitableForIfNotBroken(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (isBroken()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "getUseAction",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getUseActionIfNotBroken(CallbackInfoReturnable<UseAction> cir) {
        if (isBroken()) {
            cir.setReturnValue(UseAction.NONE);
        }
    }

    @Inject(
            method = "onStoppedUsing",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onStoppedUsingIfNotBroken(World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (isBroken()) {
            ci.cancel();
        }
    }

/*
    @Inject(
            method = "attri",
            at = @At("HEAD"),
            cancellable = true
    )
    private void applyAttributeModifiersIfNotBroken(EquipmentSlot slot, BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> attributeModifierConsumer, CallbackInfo ci) {
        if (isBroken()) {
            ci.cancel();
        }
    }
*/
    @Inject(
            method = "isEnchantable",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isEnchantableIfNotBroken(CallbackInfoReturnable<Boolean> cir) {
        if (isBroken()) {
            cir.setReturnValue(false);
        }
    }
}
