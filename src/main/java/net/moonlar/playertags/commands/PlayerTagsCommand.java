package net.moonlar.playertags.commands;

import net.moonlar.playertags.PlayerTags;
import net.moonlar.playertags.commands.subcommands.ReloadCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PlayerTagsCommand implements CommandExecutor {

  private final Map<String, CommandExecutor> subCommands = new HashMap<>();
  private final PlayerTags plugin;

  public PlayerTagsCommand(PlayerTags plugin) {
    this.plugin = plugin;

    subCommands.put("reload", new ReloadCommand(plugin));
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(args.length == 0) {
      displayInfo(sender);
      return true;
    }

    CommandExecutor subCommand = subCommands.get(args[0]);

    if(subCommand != null) {
      return subCommand.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
    }

    sender.sendMessage(ChatColor.RED + "Unknown sub-command.");
    return true;
  }

  private void displayInfo(CommandSender sender) {
    sender.sendMessage(ChatColor.GREEN + "PlayerTags v" + plugin.getDescription().getVersion());
  }
}
