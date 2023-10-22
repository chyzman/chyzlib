package com.chyzman.chyzlib.mixin;

import com.chyzman.chyzlib.pond.EntitySelectorDuck;
import com.chyzman.chyzlib.util.InteractionUtil;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.Vec3d;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.function.Predicate;

@Mixin(EntitySelector.class)
public abstract class EntitySelectorMixin implements EntitySelectorDuck {

    private boolean isTarget = false;
    private double length = 1024;
    private double margin = 0;

    @Inject(method = "getUnfilteredEntities",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/command/EntitySelector;senderOnly:Z",
                    opcode = Opcodes.GETFIELD),
            locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void makeSelectorDetectT(ServerCommandSource source, CallbackInfoReturnable<List<? extends Entity>> cir, Vec3d vec3d, Predicate predicate) {
        if (isTarget) {
            var raycast = InteractionUtil.raycastEntities(source.getEntity(), length, margin, predicate);
            if (raycast != null) {
                cir.setReturnValue(List.of(raycast.getEntity()));
            } else {
                cir.setReturnValue(List.of());
            }
        }
    }

    @Override
    public void chyzlib$setTarget() {
        isTarget = true;
    }

    @Override
    public void chyzlib$setLength(double length) {
        this.length = length;
    }

    @Override
    public void chyzlib$setMargin(double margin) {
        this.margin = margin;
    }
}