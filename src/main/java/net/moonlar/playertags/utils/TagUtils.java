package net.moonlar.playertags.utils;

import net.moonlar.playertags.objects.Tag;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public final class TagUtils {
  private TagUtils() { }

  public static void loadTags(Map<String, Tag> tags, ConfigurationSection source) {
    tags.clear();

    for(String key : source.getKeys(false)) {
      String prefix = source.getString(key + ".Prefix");
      String suffix = source.getString(key + ".Suffix");
      int priority = source.getInt(key + ".Priority");

      Tag tag = new Tag(prefix, suffix, Math.abs(priority));
      tags.put(key, tag);
    }
  }
}
