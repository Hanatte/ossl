package jp.fishmans.ossl.registry;

import jp.fishmans.ossl.skill.Skill;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public final class OsslRegistryKeys {
    public static final RegistryKey<Registry<Skill>> SKILL = RegistryKey.ofRegistry(Identifier.of("ossl", "skill"));

    public static void init() {
    }
}
