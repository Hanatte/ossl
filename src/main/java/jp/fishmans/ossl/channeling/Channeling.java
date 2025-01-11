package jp.fishmans.ossl.channeling;

import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class Channeling {
    private final Set<? extends Map.Entry<? extends RegistryEntry<EntityAttribute>, ? extends EntityAttributeModifier>> attributeModifiers;
    private final Consumer<ChannelingContext> startBehavior;
    private final Consumer<ChannelingContext> tickBehavior;
    private final Consumer<ChannelingContext> completeBehavior;
    private final Predicate<ChannelingContext> cancelCondition;
    private final Predicate<ChannelingContext> interruptCondition;
    private final int duration;

    private Channeling(
            Collection<? extends Map.Entry<? extends RegistryEntry<EntityAttribute>, ? extends EntityAttributeModifier>> attributeModifiers,
            Consumer<ChannelingContext> startBehavior,
            Consumer<ChannelingContext> tickBehavior,
            Consumer<ChannelingContext> completeBehavior,
            Predicate<ChannelingContext> cancelCondition,
            Predicate<ChannelingContext> interruptCondition,
            int duration
    ) {
        this.attributeModifiers = Set.copyOf(attributeModifiers);
        this.startBehavior = startBehavior;
        this.tickBehavior = tickBehavior;
        this.completeBehavior = completeBehavior;
        this.cancelCondition = cancelCondition;
        this.interruptCondition = interruptCondition;
        this.duration = duration;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void addModifiers(AttributeContainer container) {
        for (var entry : attributeModifiers) {
            var instance = container.getCustomInstance(entry.getKey());
            if (instance != null) {
                instance.removeModifier(entry.getValue());
                instance.addTemporaryModifier(entry.getValue());
            }
        }
    }

    public void removeModifiers(AttributeContainer container) {
        for (var entry : attributeModifiers) {
            var instance = container.getCustomInstance(entry.getKey());
            if (instance != null) {
                instance.removeModifier(entry.getValue());
            }
        }
    }

    public Consumer<ChannelingContext> getStartBehavior() {
        return startBehavior;
    }

    public Consumer<ChannelingContext> getTickBehavior() {
        return tickBehavior;
    }

    public Consumer<ChannelingContext> getCompleteBehavior() {
        return completeBehavior;
    }

    public Predicate<ChannelingContext> getCancelCondition() {
        return cancelCondition;
    }

    public Predicate<ChannelingContext> getInterruptCondition() {
        return interruptCondition;
    }

    public int getDuration() {
        return duration;
    }

    public static final class Builder {
        private final Set<Map.Entry<? extends RegistryEntry<EntityAttribute>, ? extends EntityAttributeModifier>> attributeModifiers = new HashSet<>();
        private Consumer<ChannelingContext> startBehavior = context -> {};
        private Consumer<ChannelingContext> tickBehavior = context -> {};
        private Consumer<ChannelingContext> completeBehavior = context -> {};
        private Predicate<ChannelingContext> cancelCondition = context -> true;
        private Predicate<ChannelingContext> interruptCondition = context -> true;
        private int duration;

        private Builder() {
        }

        public Builder addAttributeModifier(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier) {
            this.attributeModifiers.add(Map.entry(attribute, modifier));
            return this;
        }

        public Builder setStartBehavior(Consumer<ChannelingContext> startBehavior) {
            this.startBehavior = startBehavior;
            return this;
        }

        public Builder setTickBehavior(Consumer<ChannelingContext> tickBehavior) {
            this.tickBehavior = tickBehavior;
            return this;
        }

        public Builder setCompleteBehavior(Consumer<ChannelingContext> completeBehavior) {
            this.completeBehavior = completeBehavior;
            return this;
        }

        public Builder setCancelCondition(Predicate<ChannelingContext> cancelCondition) {
            this.cancelCondition = cancelCondition;
            return this;
        }

        public Builder setInterruptCondition(Predicate<ChannelingContext> interruptCondition) {
            this.interruptCondition = interruptCondition;
            return this;
        }

        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public Channeling build() {
            return new Channeling(
                    attributeModifiers,
                    startBehavior,
                    tickBehavior,
                    completeBehavior,
                    cancelCondition,
                    interruptCondition,
                    duration
            );
        }
    }
}
