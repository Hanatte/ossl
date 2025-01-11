package jp.fishmans.ossl.skill;

import net.minecraft.text.Text;

public sealed interface SkillResult {
    static SkillResult success() {
        return new Success();
    }

    static SkillResult failure(Text reason) {
        return new Failure(reason);
    }

    static SkillResult cooldown() {
        return failure(Text.translatable("skill.failure.cooldown"));
    }

    static SkillResult channeling() {
        return failure(Text.translatable("skill.failure.channeling"));
    }

    static SkillResult unavailable() {
        return failure(Text.translatable("skill.failure.unavailable"));
    }

    record Success() implements SkillResult {
    }

    record Failure(Text reason) implements SkillResult {
    }
}
