package net.moonlar.playertags.utils;

import org.bukkit.plugin.java.JavaPlugin;

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

  public void delayed(Runnable callback, long delay) {
    scheduler.runTaskLater(plugin, callback, delay);
  }

  public void delayedAsync(Runnable callback, long delay) {
    scheduler.runTaskLaterAsynchronously(plugin, callback, delay);
  }

  public void repeat(Runnable callback, long delay, long period) {
    scheduler.runTaskTimer(plugin, callback, delay, period);
  }

  public void repeat(Runnable callback, long period) {
    repeat(callback, 0, period);
  }

  public void repeatAsync(Runnable callback, long delay, long period) {
    scheduler.runTaskTimerAsynchronously(plugin, callback, delay, period);
  }

  public void repeatAsync(Runnable callback, long period) {
    repeatAsync(callback, 0, period);
  }

  public void callAsync(Runnable callback) {
    scheduler.runTaskAsynchronously(plugin, callback);
  }

  public void call(Runnable callback) {
    scheduler.runTask(plugin, callback);
  }
}
