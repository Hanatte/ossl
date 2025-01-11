package jp.fishmans.ossl.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import jp.fishmans.ossl.registry.OsslRegistries;
import jp.fishmans.ossl.skill.Skill;
import jp.fishmans.ossl.skill.SkillResult;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.List;

public final class CastCommand {
    private static final DynamicCommandExceptionType UNKNOWN_SKILL_EXCEPTION = new DynamicCommandExceptionType(
            id -> Text.stringifiedTranslatable("skill.notFound", id)
    );
    private static final DynamicCommandExceptionType FAILED_ENTITY_EXCEPTION = new DynamicCommandExceptionType(
            entityName -> Text.translatable("commands.cast.failed.entity", entityName)
    );
    private static final Dynamic3CommandExceptionType FAILED_EXCEPTION = new Dynamic3CommandExceptionType(
            (entityName, skillName, reason) -> Text.translatable("commands.cast.failed", entityName, skillName, reason)
    );

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("cast")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(
                                CommandManager.argument("skill", IdentifierArgumentType.identifier())
                                        .suggests((context, builder) -> CommandSource.suggestIdentifiers(OsslRegistries.SKILL.getIds(), builder))
                                        .executes(context ->
                                                executeCast(
                                                        context.getSource(),
                                                        getSkill(IdentifierArgumentType.getIdentifier(context, "skill")),
                                                        List.of(context.getSource().getEntityOrThrow())
                                                )
                                        ).then(
                                                CommandManager.argument("targets", EntityArgumentType.entities())
                                                        .executes(context ->
                                                                executeCast(
                                                                        context.getSource(),
                                                                        getSkill(IdentifierArgumentType.getIdentifier(context, "skill")),
                                                                        EntityArgumentType.getEntities(context, "targets")
                                                                )
                                                        )
                                        )
                        )
        );
    }

    private static int executeCast(ServerCommandSource source, RegistryEntry<Skill> skill, Collection<? extends Entity> targets) throws CommandSyntaxException {
        for (var target : targets) {
            if (getLivingEntity(target).castSkill(skill) instanceof SkillResult.Failure(Text reason)) {
                throw FAILED_EXCEPTION.create(target.getName(), skill.value().getName(), reason);
            }
        }

        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.cast.success.single", targets.iterator().next().getName(), skill.value().getName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.cast.success.multiple", targets.size(), skill.value().getName()), true);
        }

        return targets.size();
    }

    private static LivingEntity getLivingEntity(Entity entity) throws CommandSyntaxException {
        if (entity instanceof LivingEntity livingEntity) {
            return livingEntity;
        } else {
            throw FAILED_ENTITY_EXCEPTION.create(entity.getName());
        }
    }

    private static RegistryEntry<Skill> getSkill(Identifier id) throws CommandSyntaxException {
        return OsslRegistries.SKILL
                .getEntry(id)
                .orElseThrow(() -> UNKNOWN_SKILL_EXCEPTION.create(id));
    }
}
