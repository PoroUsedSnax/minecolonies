package com.minecolonies.commands;

import com.minecolonies.colony.CitizenData;
import com.minecolonies.colony.Colony;
import com.minecolonies.colony.ColonyManager;
import com.minecolonies.colony.IColony;
import com.minecolonies.colony.permissions.Permissions;
import com.minecolonies.entity.EntityCitizen;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * List all colonies.
 */
public class ColonyInfo extends AbstractSingleCommand
{

    private static final String ID_TEXT                 = "§2ID: §f";
    private static final String NAME_TEXT               = "§2 Name: §f";
    private static final String MAYOR_TEXT              = "§2Mayor: §f";
    private static final String COORDINATES_TEXT        = "§2Coordinates: §f";
    private static final String COORDINATES_XYZ         = "§4x=§f%s §4y=§f%s §4z=§f%s";
    private static final String CITIZENS                = "§2Citizens: §f";
    private static final String NO_COLONY_FOUND_MESSAGE = "Colony %d not found.";

    public static final String DESC                     = "info";

    /**
     * Initialize this SubCommand with it's parents.
     *
     * @param parents an array of all the parents.
     */
    public ColonyInfo(@NotNull final String... parents)
    {
        super(parents);
    }

    @NotNull
    @Override
    public String getCommandUsage(@NotNull final ICommandSender sender)
    {
        return super.getCommandUsage(sender) + "";
    }

    @Override
    public void execute(@NotNull final MinecraftServer server, @NotNull final ICommandSender sender, @NotNull final String... args) throws CommandException
    {
        int colonyId = -1;

        //todo Find a way to get colony by name
        if (args.length != 0)
        {
            try
            {
                colonyId = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e)
            {
                //ignore and assume caller's colony
                //todo get colony by name
            }
        }

        Colony colony = null;
        final IColony tempColony;
        if(colonyId >= 0)
        {
            tempColony = ColonyManager.getColony(colonyId);
        }
        else
        {
            tempColony = ColonyManager.getIColonyByOwner(sender.getEntityWorld(), sender.getCommandSenderEntity().getUniqueID());
        }
        if(tempColony != null)
        {
            colony = ColonyManager.getColony(sender.getEntityWorld(), tempColony.getCenter());
            colony = colony == null ? ColonyManager.getColony(colonyId) : colony;

            if(colony != null)
            {
                colonyId = colony.getID();
            }
        }

        if(colony == null || colonyId == -1)
        {
            sender.addChatMessage(new TextComponentString(String.format(NO_COLONY_FOUND_MESSAGE, colonyId)));
            return;
        }

        final BlockPos position = colony.getCenter();
        sender.addChatMessage(new TextComponentString(ID_TEXT + colony.getID() + NAME_TEXT + colony.getName()));
        final String mayor = colony.getPermissions().getPlayersByRank(Permissions.Rank.OWNER).iterator().next().getName();
        sender.addChatMessage(new TextComponentString(MAYOR_TEXT + mayor));
        sender.addChatMessage(new TextComponentString(CITIZENS + colony.getCitizens().size() + "/" + colony.getMaxCitizens()));
        sender.addChatMessage(new TextComponentString(COORDINATES_TEXT + String.format(COORDINATES_XYZ, position.getX(), position.getY(), position.getZ())));
    }

    @NotNull
    @Override
    public List<String> getTabCompletionOptions(
                                                 @NotNull final MinecraftServer server,
                                                 @NotNull final ICommandSender sender,
                                                 @NotNull final String[] args,
                                                 @Nullable final BlockPos pos)
    {
        return new ArrayList<>();
    }

    @Override
    public boolean isUsernameIndex(@NotNull final String[] args, final int index)
    {
        return false;
    }
}
