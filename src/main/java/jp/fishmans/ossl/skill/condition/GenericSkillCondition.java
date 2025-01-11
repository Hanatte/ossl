package jp.fishmans.ossl.skill.condition;

import jp.fishmans.ossl.skill.SkillContext;
import jp.fishmans.ossl.skill.SkillResult;

public enum GenericSkillCondition implements SkillCondition {
    INSTANCE;

    @Override
    public SkillResult test(SkillContext context) {
        if (context.source().hasSkillCooldown(context.skill())) {
            return SkillResult.cooldown();
        }

        if (context.source().isChanneling()) {
            return SkillResult.channeling();
        }

        if (!context.source().isPartOfGame()) {
            return SkillResult.unavailable();
        }

        return SkillResult.success();
    }
}
