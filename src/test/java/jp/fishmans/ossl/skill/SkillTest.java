package jp.fishmans.ossl.skill;

import jp.fishmans.ossl.registry.OsslRegistries;
import jp.fishmans.ossl.skill.condition.GenericSkillCondition;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class SkillTest implements ModInitializer {
    private final Skill testSkill = Skill.builder()
            .setAction(context -> {
                context.source().setSkillCooldown(
                        context.skill(),
                        50
                );
                context.source().getWorld().spawnEntity(
                        new SmallFireballEntity(
                                context.source().getWorld(),
                                context.source(),
                                context.source().getRotationVector()
                        )
                );
                return SkillResult.success();
            })
            .setCondition(GenericSkillCondition.INSTANCE)
            .build();

    @Override
    public void onInitialize() {
        Registry.register(OsslRegistries.SKILL, Identifier.of("ossl-test", "test"), testSkill);
    }
}
