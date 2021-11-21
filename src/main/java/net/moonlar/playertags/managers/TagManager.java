package net.moonlar.playertags.managers;

import net.milkbowl.vault.chat.Chat;
import net.moonlar.playertags.PlayerTags;
import net.moonlar.playertags.objects.Tag;
import net.moonlar.playertags.utils.ChatUtils;
import net.moonlar.playertags.utils.TagUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TagManager {
  private static final String PREFIX = "PTAGS";

  private final Map<String, Tag> playerTags = new HashMap<>();
  private final Map<String, Tag> groupTags = new HashMap<>();
  private final PlayerTags plugin;

  private Scoreboard scoreboard;
  private BukkitTask task;

  public TagManager(PlayerTags plugin) {
    this.plugin = plugin;
  }

  public void reload() {
    cleanup();

    FileConfiguration config = plugin.getConfig();

    TagUtils.loadTags(playerTags, config.getConfigurationSection("Tags.Players"));
    TagUtils.loadTags(groupTags, config.getConfigurationSection("Tags.Groups"));

    scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();

    int interval = config.getInt("UpdateInterval", 100);
    task = plugin.getScheduler().repeat(this::updateAll, Math.abs(interval));
  }

  public void updateAll() {
    plugin.getServer().getOnlinePlayers().forEach(this::update);
  }

  public void update(Player player) {
    Tag tag = getPrimaryTag(player);

    if(tag != null) {
      applyTag(player, tag);
    }
  }

  public void reset(Player player) {
    if(scoreboard != null) {
      Team team = scoreboard.getEntryTeam(player.getName());

      if(team != null && team.getName().startsWith(PREFIX)) {
        team.removeEntry(player.getName());
      }
    }
  }

  private void applyTag(Player player, Tag tag) {
    String teamName = tagToTeamName(tag);
    Team team = scoreboard.getTeam(teamName);

    if(team == null) {
      team = scoreboard.registerNewTeam(teamName);
      team.setPrefix(tag.getPrefix());
      team.setSuffix(tag.getSuffix());
    }

    team.addEntry(player.getName());
  }

  public Tag getPrimaryTag(Player player) {
    String playerName = player.getName().toLowerCase();
    Tag tag = playerTags.get(playerName);

    if(tag != null) {
      return tag;
    }

    String group = plugin.getVaultPermission().getPrimaryGroup(player).toLowerCase();
    tag = groupTags.get(group);

    if(tag != null) {
      return tag;
    }

    Chat chat = plugin.getVaultChat();
    String prefix = chat.getPlayerPrefix(player);
    String suffix = chat.getPlayerSuffix(player);

    if(prefix != null && suffix != null) {
      tag = new Tag(prefix, suffix, 0);
      playerTags.put(playerName, tag);

      return tag;
    }

    prefix = chat.getGroupPrefix((String) null, group);
    suffix = chat.getGroupPrefix((String) null, group);
    tag = new Tag(prefix, suffix, 0);
    groupTags.put(group, tag);

    return tag;
  }

  public void cleanup() {
    if(task != null) {
      task.cancel();
      task = null;
    }

    if(scoreboard != null) {
      for(Team team : scoreboard.getTeams()) {
        if (team.getName().startsWith(PREFIX)) {
          team.unregister();
        }
      }

      scoreboard = null;
    }
  }

  public static String tagToTeamName(Tag tag) {
    StringBuilder teamNameBuilder = new StringBuilder();
    teamNameBuilder.append(PREFIX);

    if(tag.getPriority() < 10) teamNameBuilder.append("0");

    teamNameBuilder.append(tag.getPriority());
    teamNameBuilder.append(tag.getShortHash());

    String teamName = teamNameBuilder.toString();
    teamName = ChatUtils.clampString(teamName, 16);

    return teamName;
  }
}
