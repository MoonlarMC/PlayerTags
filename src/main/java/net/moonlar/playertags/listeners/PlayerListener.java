package net.moonlar.playertags.listeners;

import net.moonlar.playertags.managers.TagManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

  private final TagManager tagManager;

  public PlayerListener(TagManager tagManager) {
    this.tagManager = tagManager;
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    tagManager.update(event.getPlayer());
  }
}
