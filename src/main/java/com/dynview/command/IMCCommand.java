package com.dynview.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;

/**
 * Interface for commands, uses Mojang's Brigadier command framework, @see <a href=https://github.com/Mojang/brigadier></a> .
 */
public interface IMCCommand
{
    static final int OP_PERM_LEVEL = 4;

    /**
     * Builds the command, overwrite and add further arguments etc for nonsimple commands. When overwriting this make sure to check the preconditions.
     *
     * @return the built command.
     */
    default LiteralArgumentBuilder<CommandSourceStack> build()
    {
        return newLiteral(getName()).executes(this::checkPreConditionAndExecute);
    }

    /**
     * Helper function for typing.
     *
     * @param name name of the new command.
     * @return the builder.
     */
    static LiteralArgumentBuilder<CommandSourceStack> newLiteral(final String name)
    {
        return LiteralArgumentBuilder.literal(name);
    }

    static <T> RequiredArgumentBuilder<CommandSourceStack, T> newArgument(final String name, final ArgumentType<T> type)
    {
        return RequiredArgumentBuilder.argument(name, type);
    }

    /**
     * Executes pre-checks before issuing the command
     *
     * @param context the context.
     * @return 1 if successful and 0 if incomplete.
     */
    default int checkPreConditionAndExecute(final CommandContext<CommandSourceStack> context)
    {
        if (!checkPreCondition(context))
        {
            return 0;
        }

        return onExecute(context);
    }

    default ICommandCallbackBuilder<CommandSourceStack> executePreConditionCheck()
    {
        return executeCallback -> context -> {
            if (!checkPreCondition(context))
            {
                return 0;
            }

            return executeCallback.run(context);
        };
    }

    /**
     * Preconditions to check before executing
     *
     * @return true if fine.
     */
    default boolean checkPreCondition(final CommandContext<CommandSourceStack> context)
    {
        return context.getSource().getEntity() instanceof Player || context.getSource().hasPermission(OP_PERM_LEVEL);
    }

    /**
     * What happens when the command is executed after preConditions are successful.
     *
     * @param context the context of the command execution
     * @return 1 if successful and 0 if incomplete.
     */
    int onExecute(final CommandContext<CommandSourceStack> context);

    /**
     * Name string of the command.
     */
    String getName();

    static boolean isPlayerOped(final Player player)
    {
        if (player.getServer() == null)
        {
            return false;
        }

        return player.getServer().getPlayerList().isOp(player.getGameProfile());
    }

    interface ICommandCallbackBuilder<S>
    {

        Command<S> then(final Command<CommandSourceStack> executeCallback);
    }
}