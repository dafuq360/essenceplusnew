package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSetSpawnpoint extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getName()
    {
        return "spawnpoint";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    /**
     * Gets the usage string for the command.
     *  
     * @param sender The ICommandSender who is requesting usage details
     */
    public String getUsage(ICommandSender sender)
    {
        return "commands.spawnpoint.usage";
    }

    /**
     * Callback for when the command is executed
     *  
     * @param server The server instance
     * @param sender The sender who executed the command
     * @param args The arguments that were passed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length > 1 && args.length < 4)
        {
            throw new WrongUsageException("commands.spawnpoint.usage", new Object[0]);
        }
        else
        {
            EntityPlayerMP entityplayermp = args.length > 0 ? getPlayer(server, sender, args[0]) : getCommandSenderAsPlayer(sender);
            BlockPos blockpos = args.length > 3 ? parseBlockPos(sender, args, 1, true) : entityplayermp.getPosition();

            if (entityplayermp.world != null)
            {
                entityplayermp.setSpawnPoint(blockpos, true);
                notifyCommandListener(sender, this, "commands.spawnpoint.success", new Object[] {entityplayermp.getName(), Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ())});
            }
        }
    }

    /**
     * Get a list of options for when the user presses the TAB key
     *  
     * @param server The server instance
     * @param sender The ICommandSender to get tab completions for
     * @param args Any arguments that were present when TAB was pressed
     * @param targetPos The block that the player's mouse is over, <tt>null</tt> if the mouse is not over a block
     */
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : (args.length > 1 && args.length <= 4 ? getTabCompletionCoordinate(args, 1, targetPos) : Collections.<String>emptyList());
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     *  
     * @param args The arguments of the command invocation
     * @param index The index
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}