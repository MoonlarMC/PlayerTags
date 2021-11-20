package net.moonlar.playertags;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import net.moonlar.playertags.commands.PlayerTagsCommand;
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
  private Chat chat;

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
    getCommand("playertags").setExecutor(new PlayerTagsCommand(this));
  }

  @Override
  public void onDisable() {
    tagManager.resetAll();
    tagManager = null;
    scheduler = null;
    permission = null;
  }

  public void reload() {
    reloadConfig();
    tagManager.resetAll();
    tagManager.reload();
  }

  private boolean loadVault() {
    Plugin vault = getServer().getPluginManager().getPlugin("Vault");

    if(vault == null || !getServer().getPluginManager().isPluginEnabled(vault)) {
      getLogger().severe("Vault isn't enabled.");
      return false;
    }

    permission = getServiceProvider(Permission.class);

    if(permission == null) {
      getLogger().severe("No Vault Permission provider registered.");
      return false;
    }

    chat = getServiceProvider(Chat.class);

    if(chat == null) {
      getLogger().severe("No Vault Chat provider registered.");
      return false;
    }

    return true;
  }

  private <T> T getServiceProvider(Class<T> service) {
    RegisteredServiceProvider<T> serviceProvider = getServer().getServicesManager().getRegistration(service);

    return serviceProvider == null ? null : serviceProvider.getProvider();
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

  public Chat getVaultChat() {
    return chat;
  }
}
