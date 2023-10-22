package com.chyzman.chyzlib.registry;

import com.chyzman.chyzlib.pond.EntitySelectorReaderDuck;
import com.chyzman.chyzlib.util.ChyzlibRegistryHelper;
import net.fabricmc.fabric.api.command.v2.EntitySelectorOptionRegistry;
import net.minecraft.text.Text;

public class ChyzlibEntitySelectorOptionRegistry {
    public static void init() {
        EntitySelectorOptionRegistry.registerNonRepeatable(
                ChyzlibRegistryHelper.id("length"),
                Text.translatable("argument.entity.options.length.description"),
                reader -> {
                    final double length = reader.getReader().readDouble();
                    if (length > 0) {
                        ((EntitySelectorReaderDuck)reader).chyzlib$setLength(length);
                    }
                }
        );
        EntitySelectorOptionRegistry.registerNonRepeatable(
                ChyzlibRegistryHelper.id("margin"),
                Text.translatable("argument.entity.options.margin.description"),
                reader -> {
                    final double margin = reader.getReader().readDouble();
                    if (margin >= 0) {
                        ((EntitySelectorReaderDuck)reader).chyzlib$setMargin(margin);
                    }
                }
        );
    }
}