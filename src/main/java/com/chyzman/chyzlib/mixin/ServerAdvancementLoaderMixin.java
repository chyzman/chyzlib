package com.chyzman.chyzlib.mixin;

import com.chyzman.chyzlib.pond.AdvancementDuck;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(ServerAdvancementLoader.class)
public abstract class ServerAdvancementLoaderMixin {

    @Inject(method = "method_20723",
            at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
    locals = LocalCapture.CAPTURE_FAILHARD)
    public void stopRecallingUnRecallable(Map map, Identifier id, JsonElement json, CallbackInfo ci, JsonObject jsonObject, Advancement.Builder builder) {
        if (json.getAsJsonObject().has("chyzlib:recallable") && !json.getAsJsonObject().get("chyzlib:recallable").getAsBoolean()) {
            ((AdvancementDuck.BuilderDuck)builder).chyzlib$markUnRecallable();
        }
    }
}