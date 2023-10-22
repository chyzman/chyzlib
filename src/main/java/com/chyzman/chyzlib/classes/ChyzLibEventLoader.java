package com.chyzman.chyzlib.classes;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.advancement.Advancement;
import net.minecraft.loot.LootManager;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import org.slf4j.Logger;

import java.util.Map;

public class ChyzLibEventLoader extends JsonDataLoader {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().create();
    private final LootManager conditionManager;

    public ChyzLibEventLoader(LootManager conditionManager) {
        super(GSON, "events");
        this.conditionManager = conditionManager;
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> map, ResourceManager manager, Profiler profiler) {
        Map<Identifier, Event> eventMap = Maps.newHashMap();
        map.forEach((id, json) -> {
            try {
                JsonObject jsonObject = JsonHelper.asObject(json, "advancement");
                Event event = Event.fromJson(id, jsonObject, new AdvancementEntityPredicateDeserializer(id, this.conditionManager));
                eventMap.put(id, event);
            } catch (Exception var6) {
                LOGGER.error("Parsing error loading custom advancement {}: {}", id, var6.getMessage());
            }
        });
    }
}