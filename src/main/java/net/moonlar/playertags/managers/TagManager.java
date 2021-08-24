package net.moonlar.playertags.managers;

import net.milkbowl.vault.permission.Permission;
import net.moonlar.playertags.PlayerTags;
import net.moonlar.playertags.objects.Tag;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public class TagManager {
  private final Map<String, Tag> tags = new HashMap<>();
  private final PlayerTags plugin;

  private BukkitTask task;

  public TagManager(PlayerTags plugin) {
    this.plugin = plugin;
  }

  public void reload() {
    if(task != null) {
      task.cancel();
    }

    tags.clear();

    ConfigurationSection section = plugin.getConfig().getConfigurationSection("Tags");

    for(String key : section.getKeys(false)) {
      String prefix = section.getString(key + ".Prefix");
      String suffix = section.getString(key + ".Suffix");
      int priority = section.getInt(key + ".Priority");

      Tag tag = new Tag(key, prefix, suffix, priority);
      tags.put(key, tag);
    }

    task = plugin.getScheduler().repeat(this::updateAll, 100);
  }

  public void updateAll() {
    plugin.getServer().getOnlinePlayers().forEach(this::update);
  }

  public void update(Player player) {
    Tag tag = getPrimaryTag(player);

    if(tag == null) return;

    StringBuilder teamNameBuilder = new StringBuilder();

    if(tag.getPriority() < 10) teamNameBuilder.append("0");

    teamNameBuilder.append(tag.getPriority());
    teamNameBuilder.append("_");
    teamNameBuilder.append(tag.getId());

    String teamName = teamNameBuilder.toString();

    if(teamName.length() > 16) {
      teamName = teamName.substring(0, 16);
    }

    Scoreboard scoreboard = player.getScoreboard();
    Team team = scoreboard.getTeam(teamName);

    if(team == null) {
      team = scoreboard.registerNewTeam(teamName);
    }

    if(!team.hasEntry(player.getName())) {
      team.addEntry(player.getName());
    }

    String prefix = tag.getPrefix();
    String suffix = tag.getSuffix();

    if(prefix != null) {
      if(prefix.length() > 16) {
        prefix = prefix.substring(0, 16);
      }

      team.setPrefix(prefix);
    }

    if(suffix != null) {
      if(suffix.length() > 16) {
        suffix = suffix.substring(0, 16);
      }

      team.setSuffix(suffix);
    }
  }

  public void clearAll() {
    plugin.getServer().getOnlinePlayers().forEach(this::clear);
  }

  public void clear(Player player) {

  }

  public Tag getTag(String tagName) {
    return tags.get(tagName);
  }

  public Tag getPrimaryTag(Player player) {
    Permission permission = plugin.getVaultPermission();
    Tag tag = null;

    for(String group : tags.keySet()) {
      if(permission.playerInGroup(player, group)) {
        Tag had = tags.get(group);

        if(tag == null) {
          tag = had;
          continue;
        }

        if(had.getPriority() > tag.getPriority()) {
          tag = had;
        }
      }
    }

    return tag;
  }
}
