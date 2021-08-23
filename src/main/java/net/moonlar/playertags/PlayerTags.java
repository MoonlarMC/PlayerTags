package net.moonlar.playertags;

import net.moonlar.playertags.managers.TagManager;
import net.moonlar.playertags.utils.Scheduler;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerTags extends JavaPlugin {

  private TagManager tagManager;
  private Scheduler scheduler;

  @Override
  public void onEnable() {
    saveDefaultConfig();
    reloadConfig();

    scheduler = new Scheduler(this);
    tagManager = new TagManager(this);
    tagManager.reload();
  }

  @Override
  public void onDisable() {
    tagManager = null;
    scheduler = null;
  }

  public TagManager getTagManager() {
    return tagManager;
  }

  public Scheduler getScheduler() {
    return scheduler;
  }
}
