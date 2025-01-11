package jp.fishmans.ossl.skill.action;

import jp.fishmans.ossl.skill.SkillContext;
import jp.fishmans.ossl.skill.SkillResult;

public enum EmptySkillAction implements SkillAction {
    INSTANCE;

    @Override
    public SkillResult execute(SkillContext context) {
        return SkillResult.success();
    }
}
