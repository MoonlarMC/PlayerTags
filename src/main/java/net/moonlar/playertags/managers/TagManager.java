package net.moonlar.playertags.managers;

import net.moonlar.playertags.PlayerTags;
import net.moonlar.playertags.objects.Tag;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

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

      Tag tag = new Tag(prefix, suffix, priority);
      tags.put(key, tag);
    }

    task = plugin.getScheduler().repeat(this::updateAll, 100);
  }

  public void updateAll() {
    plugin.getServer().getOnlinePlayers().forEach(this::update);
  }

  public void update(Player player) {

  }

  public void clearAll() {
    plugin.getServer().getOnlinePlayers().forEach(this::clear);
  }

  public void clear(Player player) {

  }

  public Tag getTag(String tagName) {
    return tags.get(tagName);
  }
}
