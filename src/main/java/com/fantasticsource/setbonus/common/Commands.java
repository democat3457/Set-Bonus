package com.fantasticsource.setbonus.common;

import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.setbonus.SetBonus;
import com.fantasticsource.setbonus.config.ConfigHandler;
import com.fantasticsource.setbonus.server.ServerData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Commands extends CommandBase
{
    private static String configDir = Loader.instance().getConfigDir().getAbsolutePath() + File.separator;

    @Override
    public String getName()
    {
        return "setbonus";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "/setbonus reload";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        if (args.length == 0) sender.getCommandSenderEntity().sendMessage(new TextComponentString(getUsage(sender)));
        else
        {
            subCommand(sender, args);
        }
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        ArrayList<String> result = new ArrayList<>();
        if (args.length == 1)
        {
            result.add("reload");
        }
        return result;
    }

    private void subCommand(ICommandSender sender, String[] args)
    {
        String cmd = args[0];
        if (cmd.equals("reload"))
        {
            try
            {
                MCTools.reloadConfig(ConfigHandler.FULL_CONFIG_NAME + ".cfg", SetBonus.MODID);
                ServerData.update();

                for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
                {
                    Network.updateConfig(player);
                }
                notifyCommandListener(sender, this, SetBonus.MODID + ".cmd.reloaded");
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            notifyCommandListener(sender, this, getUsage(sender));
        }
    }
}
