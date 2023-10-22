package com.chyzman.chyzlib.mixin;

import com.chyzman.chyzlib.pond.EntitySelectorDuck;
import com.chyzman.chyzlib.pond.EntitySelectorReaderDuck;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.util.math.Box;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Function;

@Mixin(EntitySelectorReader.class)
public abstract class EntitySelectorReaderMixin implements EntitySelectorReaderDuck {

    @Shadow private int limit;
    @Shadow private boolean includesNonPlayers;
    @Shadow private boolean senderOnly;
    private char tempChar;
    private boolean isTarget = false;
    private double length = 1024;
    private double margin = 0;

    @Inject(method = "readAtVariable",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/command/EntitySelectorReader;predicate:Ljava/util/function/Predicate;",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    public void makeSelectorReaderDetectT(CallbackInfo ci, int i, char c) {
        if (c == 't') {
            limit = 1;
            includesNonPlayers = true;
            senderOnly = false;
            isTarget = true;
        }
    }

    @ModifyVariable(method = "readAtVariable",
            at = @At(
                    value = "JUMP",
                    opcode = Opcodes.IF_ICMPNE,
                    ordinal = 4,
                    shift = At.Shift.BY,
                    by = -2)
    )
    public char makeSelectorReaderDetectT$hideTheCharacter(char c) {
        tempChar = c;
        if (c == 't') {
            return 'e';
        }
        return c;
    }

    @ModifyVariable(method = "readAtVariable",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=2147483647",
                    ordinal = 1)
    )
    public char makeSelectorReaderDetectT$putRealCharacterBack(char bob) {
        //Bob is e
        return tempChar;
    }

    @Inject(method = "build",
            at = @At(
                    value = "TAIL"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    public void makeSelectorReaderDetectT$passTheT(CallbackInfoReturnable<EntitySelector> cir, Box box, Function function) {
        if (isTarget) {
            ((EntitySelectorDuck)cir.getReturnValue()).chyzlib$setTarget();
            ((EntitySelectorDuck)cir.getReturnValue()).chyzlib$setLength(length);
            ((EntitySelectorDuck)cir.getReturnValue()).chyzlib$setMargin(margin);
        }
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