package net.moonlar.playertags.utils;

import org.bukkit.ChatColor;

public final class ChatUtils {
  private ChatUtils() { }

  public static String colorize(String message) {
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public static String clampString(String message, int maxLength) {
    if(message.length() > maxLength) {
      message = message.substring(0, maxLength);
    }

    return message;
  }
}
