package net.moonlar.playertags.managers;

import net.moonlar.playertags.PlayerTags;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class TagManager {
  private final PlayerTags plugin;
  private BukkitTask task;

  public TagManager(PlayerTags plugin) {
    this.plugin = plugin;
  }

  public void reload() {
    stop();
    start();
  }

  public void start() {
    task = plugin.getScheduler().repeat(this::updateAll, 100);
  }

  private void stop() {
    if(task != null) {
      task.cancel();
      task = null;
    }
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
}
