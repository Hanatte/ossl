package jp.fishmans.ossl.registry;

import jp.fishmans.ossl.skill.Skill;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;

public final class OsslRegistries {
    public static final Registry<Skill> SKILL = FabricRegistryBuilder.createSimple(OsslRegistryKeys.SKILL).buildAndRegister();

    public static void init() {
    }
}
