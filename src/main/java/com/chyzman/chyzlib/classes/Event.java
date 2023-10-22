package com.chyzman.chyzlib.classes;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Map;

public class Event {
    private final Identifier id;
    private final Map<String, AdvancementCriterion> criteria;
    private final String[][] requirements;
    private final AdvancementRewards rewards;

    public Event(
            Identifier id,
            Map<String, AdvancementCriterion> criteria,
            String[][] requirements,
            AdvancementRewards rewards
    ) {
        this.id = id;
        this.criteria = ImmutableMap.copyOf(criteria);
        this.requirements = requirements;
        this.rewards = rewards;
    }

    public static Event fromJson(Identifier id, JsonObject obj, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        AdvancementRewards rewards = obj.has("rewards") ? AdvancementRewards.fromJson(JsonHelper.getObject(obj, "rewards")) : AdvancementRewards.NONE;
        Map<String, AdvancementCriterion> criteria = AdvancementCriterion.criteriaFromJson(JsonHelper.getObject(obj, "criteria"), predicateDeserializer);
        if (criteria.isEmpty()) {
            throw new JsonSyntaxException("Event criteria cannot be empty");
        } else {
            JsonArray jsonArray = JsonHelper.getArray(obj, "requirements", new JsonArray());
            String[][] requirements = new String[jsonArray.size()][];

            for(int i = 0; i < jsonArray.size(); ++i) {
                JsonArray jsonArray2 = JsonHelper.asArray(jsonArray.get(i), "requirements[" + i + "]");
                requirements[i] = new String[jsonArray2.size()];

                for(int j = 0; j < jsonArray2.size(); ++j) {
                    requirements[i][j] = JsonHelper.asString(jsonArray2.get(j), "requirements[" + i + "][" + j + "]");
                }
            }

            if (requirements.length == 0) {
                requirements = new String[criteria.size()][];
                int i = 0;

                for(String string : criteria.keySet()) {
                    requirements[i++] = new String[]{string};
                }
            }

            for(String[] strings2 : requirements) {
                if (strings2.length == 0 && criteria.isEmpty()) {
                    throw new JsonSyntaxException("Requirement entry cannot be empty");
                }

                for(String string2 : strings2) {
                    if (!criteria.containsKey(string2)) {
                        throw new JsonSyntaxException("Unknown required criterion '" + string2 + "'");
                    }
                }
            }

            for(String string3 : criteria.keySet()) {
                boolean bl = false;

                for(String[] strings3 : requirements) {
                    if (ArrayUtils.contains(strings3, string3)) {
                        bl = true;
                        break;
                    }
                }

                if (!bl) {
                    throw new JsonSyntaxException(
                            "Criterion '" + string3 + "' isn't a requirement for completion. This isn't supported behaviour, all criteria must be required."
                    );
                }
            }
            return new Event(id, criteria, requirements, rewards);
        }
    }
}