package com.chyzman.chyzlib.pond;

public interface EntitySelectorDuck {
    default void chyzlib$setTarget() {
        throw new UnsupportedOperationException();
    }
    default void chyzlib$setLength(double length) {
        throw new UnsupportedOperationException();
    }
    default void chyzlib$setMargin(double margin) {
        throw new UnsupportedOperationException();
    }

}