package com.chyzman.chyzlib.util;

import com.chyzman.chyzlib.Chyzlib;
import net.minecraft.util.Identifier;

public class ChyzlibRegistryHelper {
    public static Identifier id(String path) {
        return new Identifier(Chyzlib.MODID, path);
    }
}