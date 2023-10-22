package com.chyzman.chyzlib.mixin;

import com.chyzman.chyzlib.util.MixinHooks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.function.Predicate;

@Mixin(ProjectileUtil.class)
public abstract class ProjectileUtilMixin {
    @ModifyArgs(
            method = "raycast",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;"
            )
    )
    private static void stopAttackingYourPassengers(Args args) {
        if (args.get(0) instanceof PlayerEntity player) {
            args.set(2, ((Predicate<Entity>) args.get(2)).and((Entity entity) -> !entity.isSpectator() && entity.canHit() && entity.getVehicle() != player));
        }
    }

    @ModifyArg(method = "raycast", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(D)Lnet/minecraft/util/math/Box;"))
    private static double increaseMargin(double value) {
        return value + MixinHooks.EXTRA_TARGETING_MARGIN;
    }
}