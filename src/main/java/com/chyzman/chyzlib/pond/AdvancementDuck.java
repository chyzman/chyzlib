package com.chyzman.chyzlib.pond;

public interface AdvancementDuck {
    default void chyzlib$markUnRecallable() {
        throw new UnsupportedOperationException();
    }

    default boolean chyzlib$isRecallable() {
        throw new UnsupportedOperationException();
    }

    public interface BuilderDuck {
        default void chyzlib$markUnRecallable() {
            throw new UnsupportedOperationException();
        }
    }
}