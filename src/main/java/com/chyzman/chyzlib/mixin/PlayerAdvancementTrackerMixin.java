package com.chyzman.chyzlib.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(PlayerAdvancementTracker.class)
public abstract class PlayerAdvancementTrackerMixin{
    @Shadow @Final private Map<Advancement, AdvancementProgress> progress;

    @Shadow protected abstract void beginTracking(Advancement advancement);

    @Inject(method = "endTrackingCompleted", at = @At("TAIL"))
    public void stopRecallingUnRecallable(Advancement advancement, CallbackInfo ci) {
        if (!advancement.chyzlib$isRecallable()) {
            progress.remove(advancement);
            beginTracking(advancement);
        }
    }
}