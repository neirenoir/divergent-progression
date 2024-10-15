package io.bluestaggo.divergeprog.mixin;

import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    private boolean isBroken(ItemStack itemStack) {
        return itemStack.isDamageable() && itemStack.getDamage() >= itemStack.getMaxDamage();
    }

    @ModifyVariable(
        method = "attack",
        at = @At("STORE"),
        ordinal = 0
    )
    private float resetAttackDamageIfBroken(float f) {
        var itemStack = getMainHandStack();
        double acc = 0;
        if (isBroken(itemStack) && !itemStack.isEmpty()) {
            Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers =
                itemStack.getAttributeModifiers(EquipmentSlot.MAINHAND);
            if (attributeModifiers.containsKey(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
                // Get the attack damage modifier
                Collection<EntityAttributeModifier> modifiers =
                    attributeModifiers.get(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                for (EntityAttributeModifier modifier : modifiers) {
                    // Return the attack damage value (typically only one modifier for weapons)
                    acc += modifier.getValue();
                }
            }
            // 2 is the default attack damage done by punching
            return Math.max(f - (float) acc, 2f);
        } else {
            return f;
        }
    }
}
