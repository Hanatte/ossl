package jp.fishmans.ossl.skill.condition;

import jp.fishmans.ossl.skill.SkillContext;
import jp.fishmans.ossl.skill.SkillResult;

public interface SkillCondition {
    SkillResult test(SkillContext context);

    default SkillCondition and(SkillCondition other) {
        return context -> test(context) instanceof SkillResult.Failure failure ?
                failure :
                other.test(context);
    }

    default SkillCondition or(SkillCondition other) {
        return context -> test(context) instanceof SkillResult.Success success ?
                success :
                other.test(context);
    }
}
