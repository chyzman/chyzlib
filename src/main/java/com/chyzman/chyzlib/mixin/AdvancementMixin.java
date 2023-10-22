package com.chyzman.chyzlib.mixin;

import com.chyzman.chyzlib.pond.AdvancementDuck;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Advancement.class)
public abstract class AdvancementMixin implements AdvancementDuck {
    @Unique
    private boolean recallable = true;

    @Override
    public void chyzlib$markUnRecallable() {
        recallable = false;
    }

    @Override
    public boolean chyzlib$isRecallable() {
        return recallable;
    }

    @Mixin(Advancement.Builder.class)
    static class AdvancementBuilderMixin implements AdvancementDuck.BuilderDuck {
        @Unique
        private boolean recallable = true;

        @Override
        public void chyzlib$markUnRecallable() {
            recallable = false;
        }

        @Inject(method = "build(Lnet/minecraft/util/Identifier;)Lnet/minecraft/advancement/Advancement;", at = @At("TAIL"))
        public void makeUnRecallable$AdvancementBuilder(Identifier id, CallbackInfoReturnable<Advancement> cir) {
            if (!recallable) {
                cir.getReturnValue().chyzlib$markUnRecallable();
            }
        }
    }
}