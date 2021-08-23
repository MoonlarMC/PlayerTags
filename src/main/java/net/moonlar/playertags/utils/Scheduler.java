package net.moonlar.playertags.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class Scheduler {
  private final JavaPlugin plugin;
  private final org.bukkit.scheduler.BukkitScheduler scheduler;

  public Scheduler(JavaPlugin plugin) {
    this.plugin = plugin;
    this.scheduler = plugin.getServer().getScheduler();
  }

  public JavaPlugin getPlugin() {
    return plugin;
  }

  public BukkitTask delayed(Runnable callback, long delay) {
    return scheduler.runTaskLater(plugin, callback, delay);
  }

  public BukkitTask delayedAsync(Runnable callback, long delay) {
    return scheduler.runTaskLaterAsynchronously(plugin, callback, delay);
  }

  public BukkitTask repeat(Runnable callback, long delay, long period) {
    return scheduler.runTaskTimer(plugin, callback, delay, period);
  }

  public BukkitTask repeat(Runnable callback, long period) {
    return repeat(callback, 0, period);
  }

  public BukkitTask repeatAsync(Runnable callback, long delay, long period) {
    return scheduler.runTaskTimerAsynchronously(plugin, callback, delay, period);
  }

  public BukkitTask repeatAsync(Runnable callback, long period) {
    return repeatAsync(callback, 0, period);
  }

  public BukkitTask callAsync(Runnable callback) {
    return scheduler.runTaskAsynchronously(plugin, callback);
  }

  public BukkitTask call(Runnable callback) {
    return scheduler.runTask(plugin, callback);
  }
}
