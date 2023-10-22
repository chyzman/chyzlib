package com.chyzman.chyzlib.util;

import com.chyzman.chyzlib.util.MixinHooks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;

import java.util.function.Predicate;

public class InteractionUtil {

    public static EntityHitResult raycastEntities(Entity entity, double reach, double margin, Predicate<Entity> predicate) {
        var maxReach = entity.getRotationVec(0).multiply(reach);

        MixinHooks.EXTRA_TARGETING_MARGIN = margin;
        var entityTarget = ProjectileUtil.raycast(
                entity,
                entity.getEyePos(),
                entity.getEyePos().add(maxReach),
                entity.getBoundingBox().stretch(maxReach),
                candidate -> {
                    if (candidate.isSpectator()) return false;
                    if (entity.getVehicle() != null && entity.getVehicle().equals(candidate)) return false;
                    return predicate.test(candidate);
                },
                reach * reach
        );
        MixinHooks.EXTRA_TARGETING_MARGIN = 0;

        return entityTarget;
    }
}