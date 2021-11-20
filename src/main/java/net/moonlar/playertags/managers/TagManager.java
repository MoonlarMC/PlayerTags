package net.moonlar.playertags.managers;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import net.moonlar.playertags.PlayerTags;
import net.moonlar.playertags.objects.Tag;
import net.moonlar.playertags.utils.ChatUtils;
import net.moonlar.playertags.utils.TeamUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
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

    FileConfiguration config = plugin.getConfig();
    ConfigurationSection section = config.getConfigurationSection("Tags");

    for(String key : section.getKeys(false)) {
      String prefix = section.getString(key + ".Prefix");
      String suffix = section.getString(key + ".Suffix");
      int priority = section.getInt(key + ".Priority");

      Tag tag = new Tag(key, prefix, suffix, Math.abs(priority));
      tags.put(key, tag);
    }

    Chat chat = plugin.getVaultChat();
    Permission permission = plugin.getVaultPermission();

    for(String group : permission.getGroups()) {
      Tag tag = getTag(group);

      if(tag != null) continue;

      String prefix = chat.getGroupPrefix((String) null, group);
      String suffix = chat.getGroupSuffix((String) null, group);

      if(prefix == null && suffix == null) continue;

      tags.put(group, new Tag(group, prefix, suffix, 0));
    }

    int interval = config.getInt("UpdateInterval", 100);
    task = plugin.getScheduler().repeat(this::updateAll, Math.abs(interval));
  }

  public void updateAll() {
    plugin.getServer().getOnlinePlayers().forEach(this::update);
  }

  public void update(Player player) {
    clear(player);

    Tag tag = getPrimaryTag(player);

    if(tag == null) return;

    String teamName = tagToTeamName(tag);

    if(!TeamUtils.isPlayerInTeam(player, teamName)) {
      applyTag(player, tag);
    }
  }

  public void resetAll() {
    for(Player player : plugin.getServer().getOnlinePlayers()) {
      reset(player);
      clear(player);
    }
  }

  public void clear(Player player) {
    Permission permission = plugin.getVaultPermission();

    for(Tag tag : tags.values()) {
      if(!permission.playerInGroup(player, tag.getId())) {
        String teamName = TagManager.tagToTeamName(tag);
        TeamUtils.removePlayerFromTeam(player, teamName);
      }
    }
  }

  public void reset(Player player) {
    Tag tag = getPrimaryTag(player);

    if(tag != null) {
      String teamName = tagToTeamName(tag);
      TeamUtils.removePlayerFromTeam(player, teamName);
    }
  }

  private void applyTag(Player player, Tag tag) {
    String teamName = TagManager.tagToTeamName(tag);
    Team team = TeamUtils.getNewTeam(player, teamName);

    if(!team.hasEntry(player.getName())) {
      team.addEntry(player.getName());
    }

    String prefix = tag.getPrefix();
    String suffix = tag.getSuffix();

    if(prefix != null) {
      prefix = ChatUtils.clampAndTranslateColors(prefix, 16);
      team.setPrefix(prefix);
    }

    if(suffix != null) {
      suffix = ChatUtils.clampAndTranslateColors(suffix, 16);
      team.setSuffix(suffix);
    }
  }

  public Tag getTag(String tagName) {
    return tags.get(tagName);
  }

  public Tag getPrimaryTag(Player player) {
    Permission permission = plugin.getVaultPermission();
    Tag tag = null;

    for(String group : tags.keySet()) {
      if(permission.playerInGroup(player, group)) {
        Tag had = getTag(group);

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

  public static String tagToTeamName(Tag tag) {
    Validate.notNull(tag, "Tag is null");

    StringBuilder teamNameBuilder = new StringBuilder();

    if(tag.getPriority() < 10) teamNameBuilder.append("0");

    teamNameBuilder.append(tag.getPriority());
    teamNameBuilder.append("_");
    teamNameBuilder.append(tag.getId());

    String teamName = teamNameBuilder.toString();
    teamName = ChatUtils.clampString(teamName, 16);

    return teamName;
  }
}
