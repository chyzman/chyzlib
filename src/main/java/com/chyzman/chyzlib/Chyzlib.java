package com.chyzman.chyzlib;

import com.chyzman.chyzlib.criterion.UseItemCriterion;
import com.chyzman.chyzlib.registry.ChyzlibEntitySelectorOptionRegistry;
import com.chyzman.chyzlib.registry.ServerEventListeners;
import net.fabricmc.api.ModInitializer;
import net.minecraft.advancement.criterion.Criteria;

public class Chyzlib implements ModInitializer {
    public static final String MODID = "chyzlib";
    public static final UseItemCriterion USE_ITEM = Criteria.register(new UseItemCriterion());


    @Override
    public void onInitialize() {
        ChyzlibEntitySelectorOptionRegistry.init();
        ServerEventListeners.init();
    }
}