package jp.fishmans.ossl.mixin;

import jp.fishmans.ossl.channeling.Channeling;
import jp.fishmans.ossl.channeling.ChannelingContext;
import jp.fishmans.ossl.entity.LivingEntityExtensions;
import jp.fishmans.ossl.skill.Skill;
import jp.fishmans.ossl.skill.SkillContext;
import jp.fishmans.ossl.skill.SkillResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = LivingEntity.class)
public abstract class LivingEntityMixin implements LivingEntityExtensions {
    @Unique
    private final Map<RegistryEntry<Skill>, Integer> ossl$skillCooldowns = new HashMap<>();
    @Unique
    private @Nullable Channeling ossl$channeling;
    @Unique
    private int ossl$channelingTime;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public boolean isChanneling() {
        return ossl$channeling != null;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public boolean startChanneling(Channeling channeling) {
        if (ossl$channeling != null) {
            return false;
        }

        ossl$channelingTime = 0;
        ossl$channeling = channeling;
        ossl$channeling.addModifiers(getAttributes());
        ossl$channeling.getStartBehavior().accept(ossl$channelingContext(ossl$channeling));
        return true;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public boolean cancelChanneling() {
        if (ossl$channeling == null || !ossl$channeling.getCancelCondition().test(ossl$channelingContext(ossl$channeling))) {
            return false;
        }

        ossl$channeling.removeModifiers(getAttributes());
        ossl$channeling = null;
        return true;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public boolean interruptChanneling() {
        if (ossl$channeling == null || !ossl$channeling.getInterruptCondition().test(ossl$channelingContext(ossl$channeling))) {
            return false;
        }

        ossl$channeling.removeModifiers(getAttributes());
        ossl$channeling = null;
        return true;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public boolean hasSkillCooldown(RegistryEntry<Skill> skill) {
        return getSkillCooldown(skill) > 0;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public int getSkillCooldown(RegistryEntry<Skill> skill) {
        return ossl$skillCooldowns.getOrDefault(skill, 0);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public void setSkillCooldown(RegistryEntry<Skill> skill, int cooldown) {
        ossl$skillCooldowns.put(skill, cooldown);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public SkillResult canCastSkill(RegistryEntry<Skill> skill) {
        return skill.value().getCondition().test(ossl$skillContext(skill));
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public SkillResult castSkill(RegistryEntry<Skill> skill) {
        var skillResult = canCastSkill(skill);
        if (skillResult instanceof SkillResult.Failure) {
            return skillResult;
        }

        return skill.value().getAction().execute(ossl$skillContext(skill));
    }

    @Shadow
    public abstract AttributeContainer getAttributes();

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void ossl$tick(CallbackInfo ci) {
        var livingEntity = (LivingEntity) (Object) this;
        if (!livingEntity.isPartOfGame()) {
            return;
        }

        ossl$channelingTick();
        ossl$skillCooldownsTick();
    }

    @Inject(method = "onRemoval", at = @At(value = "TAIL"))
    private void ossl$onRemoval(ServerWorld world, Entity.RemovalReason reason, CallbackInfo ci) {
        interruptChanneling();
    }

    @Unique
    private void ossl$channelingTick() {
        if (ossl$channeling != null) {
            ossl$channelingTime++;
            if (ossl$channelingTime < ossl$channeling.getDuration()) {
                ossl$channeling.getTickBehavior().accept(ossl$channelingContext(ossl$channeling));
            } else {
                ossl$channeling.getCompleteBehavior().accept(ossl$channelingContext(ossl$channeling));
                ossl$channeling.removeModifiers(getAttributes());
                ossl$channeling = null;
            }
        }
    }

    @Unique
    private void ossl$skillCooldownsTick() {
        ossl$skillCooldowns.replaceAll((skill, cooldown) -> cooldown - 1);
        ossl$skillCooldowns.values().removeIf(cooldown -> cooldown <= 0);
    }

    @Unique
    private ChannelingContext ossl$channelingContext(Channeling channeling) {
        return new ChannelingContext(channeling, (LivingEntity) (Object) this, ossl$channelingTime);
    }

    @Unique
    private SkillContext ossl$skillContext(RegistryEntry<Skill> skill) {
        return new SkillContext(skill, (LivingEntity) (Object) this);
    }
}
