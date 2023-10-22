package com.chyzman.chyzlib.criterion;

import com.chyzman.chyzlib.util.ChyzlibRegistryHelper;
import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class UseItemCriterion extends AbstractCriterion<net.minecraft.advancement.criterion.UsingItemCriterion.Conditions> {
    static final Identifier ID = ChyzlibRegistryHelper.id("use_item");

    @Override
    public Identifier getId() {
        return ID;
    }

    public net.minecraft.advancement.criterion.UsingItemCriterion.Conditions conditionsFromJson(
            JsonObject jsonObject, LootContextPredicate lootContextPredicate, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer
    ) {
        ItemPredicate itemPredicate = ItemPredicate.fromJson(jsonObject.get("item"));
        return new net.minecraft.advancement.criterion.UsingItemCriterion.Conditions(lootContextPredicate, itemPredicate);
    }

    public void trigger(ServerPlayerEntity player, ItemStack stack) {
        this.trigger(player, conditions -> conditions.test(stack));
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final ItemPredicate item;

        public Conditions(LootContextPredicate player, ItemPredicate item) {
            super(ID, player);
            this.item = item;
        }

        public static net.minecraft.advancement.criterion.UsingItemCriterion.Conditions create(EntityPredicate.Builder player, ItemPredicate.Builder item) {
            return new net.minecraft.advancement.criterion.UsingItemCriterion.Conditions(EntityPredicate.asLootContextPredicate(player.build()), item.build());
        }

        public boolean test(ItemStack stack) {
            return this.item.test(stack);
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.add("item", this.item.toJson());
            return jsonObject;
        }
    }
}