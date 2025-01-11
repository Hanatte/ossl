package jp.fishmans.ossl.channeling;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class ChannelingTest implements ModInitializer {
    private final Channeling testChanneling = Channeling.builder()
            .addAttributeModifier(
                    EntityAttributes.MOVEMENT_SPEED,
                    new EntityAttributeModifier(
                            Identifier.of("ossl-test", "test"),
                            -0.7,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
            )
            .setDuration(10)
            .setStartBehavior(context -> {
                if (context.source() instanceof ServerPlayerEntity player) {
                    player.sendMessage(Text.literal("channeling started: " + context.time()));
                }
            })
            .setTickBehavior(context -> {
                if (context.source() instanceof ServerPlayerEntity player) {
                    player.sendMessage(Text.literal("channeling ticked: " + context.time()));
                }
            })
            .setCompleteBehavior(context -> {
                context.source().getWorld().spawnEntity(
                        new SmallFireballEntity(
                                context.source().getWorld(),
                                context.source(),
                                context.source().getRotationVector()
                        )
                );
                if (context.source() instanceof ServerPlayerEntity player) {
                    player.sendMessage(Text.literal("channeling completed: " + context.time()));
                }
            })
            .setCancelCondition(context -> {
                if (context.source() instanceof ServerPlayerEntity player) {
                    player.sendMessage(Text.literal("channeling canceled: " + context.time()));
                }

                return true;
            })
            .setInterruptCondition(context -> {
                if (context.source() instanceof ServerPlayerEntity player) {
                    player.sendMessage(Text.literal("channeling canceled: " + context.time()));
                }

                return true;
            })
            .build();

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(
                        CommandManager.literal("channelingtest")
                                .executes(context -> {
                                    ((LivingEntity) context.getSource().getEntityOrThrow()).startChanneling(testChanneling);
                                    return 1;
                                })
                )
        );
    }
}
