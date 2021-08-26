package net.moonlar.playertags.commands.subcommands;

import net.moonlar.playertags.PlayerTags;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

  private final PlayerTags plugin;

  public ReloadCommand(PlayerTags plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(!sender.hasPermission("playertags.reload")) {
      sender.sendMessage(ChatColor.RED + "No permission.");
      return true;
    }

    plugin.reload();
    sender.sendMessage(ChatColor.GREEN + "Reloaded successfully!");

    return true;
  }
}
