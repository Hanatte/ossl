package jp.fishmans.ossl.entity;

import jp.fishmans.ossl.channeling.Channeling;
import jp.fishmans.ossl.skill.Skill;
import jp.fishmans.ossl.skill.SkillResult;
import net.minecraft.registry.entry.RegistryEntry;

public interface LivingEntityExtensions {
    default boolean isChanneling() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default boolean startChanneling(Channeling channeling) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default boolean cancelChanneling() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default boolean interruptChanneling() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default boolean hasSkillCooldown(RegistryEntry<Skill> skill) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default int getSkillCooldown(RegistryEntry<Skill> skill) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default void setSkillCooldown(RegistryEntry<Skill> skill, int cooldown) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default SkillResult canCastSkill(RegistryEntry<Skill> skill) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default SkillResult castSkill(RegistryEntry<Skill> skill) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }
}
