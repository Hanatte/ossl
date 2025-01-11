package jp.fishmans.ossl.skill;

import jp.fishmans.ossl.registry.OsslRegistries;
import jp.fishmans.ossl.skill.action.EmptySkillAction;
import jp.fishmans.ossl.skill.action.SkillAction;
import jp.fishmans.ossl.skill.condition.GenericSkillCondition;
import jp.fishmans.ossl.skill.condition.SkillCondition;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

public final class Skill {
    private final SkillAction action;
    private final SkillCondition condition;

    private @Nullable String translationKey;
    private @Nullable Text name;

    private Skill(SkillAction action, SkillCondition condition) {
        this.action = action;
        this.condition = condition;
    }

    public static Builder builder() {
        return new Builder();
    }

    public SkillAction getAction() {
        return action;
    }

    public SkillCondition getCondition() {
        return condition;
    }

    public String getTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.createTranslationKey("skill", OsslRegistries.SKILL.getId(this));
        }

        return translationKey;
    }

    public Text getName() {
        if (name == null) {
            name = Text.translatable(getTranslationKey());
        }

        return name;
    }

    public static final class Builder {
        private SkillAction action = EmptySkillAction.INSTANCE;
        private SkillCondition condition = GenericSkillCondition.INSTANCE;

        public Builder setAction(SkillAction action) {
            this.action = action;
            return this;
        }

        public Builder setCondition(SkillCondition condition) {
            this.condition = condition;
            return this;
        }

        public Skill build() {
            return new Skill(action, condition);
        }
    }
}
