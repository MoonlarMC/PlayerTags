package net.moonlar.playertags;

import net.moonlar.playertags.managers.TagManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerTags extends JavaPlugin {

  private TagManager tagManager;

  @Override
  public void onEnable() {
    saveDefaultConfig();
    reloadConfig();

    tagManager = new TagManager(this);
    tagManager.reload();
  }

  @Override
  public void onDisable() {
    tagManager = null;
  }

  public TagManager getTagManager() {
    return tagManager;
  }
}
