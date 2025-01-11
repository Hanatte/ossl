package jp.fishmans.ossl.skill.action;

import jp.fishmans.ossl.skill.SkillContext;
import jp.fishmans.ossl.skill.SkillResult;

public interface SkillAction {
    SkillResult execute(SkillContext context);
}
