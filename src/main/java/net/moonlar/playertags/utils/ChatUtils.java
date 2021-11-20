package net.moonlar.playertags.utils;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;

public final class ChatUtils {
  private ChatUtils() { }

  public static String translateColors(String message) {
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public static String clampString(String message, int maxLength) {
    if(message.length() > maxLength) {
      message = message.substring(0, maxLength);
    }

    return message;
  }

  public static String clampAndTranslateColors(String message, int maxLength) {
    message = clampString(message, maxLength);
    message = translateColors(message);

    return message;
  }

  public static String getGroupPrefix(Chat chat, String group, String priority) {
    if(priority != null) return priority;

    return chat.getGroupPrefix((String) null, group);
  }

  public static String getGroupSuffix(Chat chat, String group, String priority) {
    if(priority != null) return priority;

    return chat.getGroupSuffix((String) null, group);
  }
}
