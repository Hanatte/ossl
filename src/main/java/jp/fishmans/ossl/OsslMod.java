package jp.fishmans.ossl;

import jp.fishmans.ossl.command.CastCommand;
import jp.fishmans.ossl.compatibility.OsslPolymerResourcePack;
import jp.fishmans.ossl.registry.OsslRegistries;
import jp.fishmans.ossl.registry.OsslRegistryKeys;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;

public final class OsslMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                CastCommand.register(dispatcher)
        );
        OsslRegistries.init();
        OsslRegistryKeys.init();

        if (FabricLoader.getInstance().isModLoaded("polymer-resource-pack")) {
            OsslPolymerResourcePack.init();
        }
    }
}
