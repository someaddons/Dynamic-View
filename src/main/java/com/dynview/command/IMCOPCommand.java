package com.dynview.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

/**
 * Interface for commands requiring OP rights to execute.
 */
public interface IMCOPCommand extends IMCCommand
{
    /**
     * Executes pre-checks before issuing the command. Checks for the senders type and OP rights.
     */
    @Override
    default boolean checkPreCondition(final CommandContext<CommandSourceStack> context)
    {
        if (context.getSource().permissions().hasPermission(Permissions.COMMANDS_ADMIN))
        {
            return true;
        }

        final Entity sender = context.getSource().getEntity();
        if (!(sender instanceof Player))
        {
            return false;
        }

        return true;
    }
}
