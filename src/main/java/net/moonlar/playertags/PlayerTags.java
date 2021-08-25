package net.moonlar.playertags;

import net.milkbowl.vault.permission.Permission;
import net.moonlar.playertags.listeners.PlayerListener;
import net.moonlar.playertags.managers.TagManager;
import net.moonlar.playertags.utils.Scheduler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerTags extends JavaPlugin {

  private TagManager tagManager;
  private Scheduler scheduler;
  private Permission permission;

  @Override
  public void onEnable() {
    if(!loadVault()) {
      getLogger().severe("Could not hook Vault Permission, disabling.");
      getServer().getPluginManager().disablePlugin(this);
      return;
    }

    saveDefaultConfig();
    reloadConfig();

    scheduler = new Scheduler(this);
    tagManager = new TagManager(this);
    tagManager.reload();

    getServer().getPluginManager().registerEvents(new PlayerListener(tagManager), this);
  }

  @Override
  public void onDisable() {
    tagManager = null;
    scheduler = null;
    permission = null;
  }

  private boolean loadVault() {
    Plugin vault = getServer().getPluginManager().getPlugin("Vault");

    if(vault == null || !getServer().getPluginManager().isPluginEnabled(vault)) {
      getLogger().severe("Vault isn't enabled.");
      return false;
    }

    RegisteredServiceProvider<Permission> provider =
      getServer().getServicesManager().getRegistration(Permission.class);

    if(provider == null) {
      getLogger().severe("No Vault Permission provider registered.");
      return false;
    }

    permission = provider.getProvider();
    return permission != null;
  }

  public TagManager getTagManager() {
    return tagManager;
  }

  public Scheduler getScheduler() {
    return scheduler;
  }

  public Permission getVaultPermission() {
    return permission;
  }
}
