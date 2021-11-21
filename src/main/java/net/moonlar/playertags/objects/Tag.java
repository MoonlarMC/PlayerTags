package net.moonlar.playertags.objects;

import net.moonlar.playertags.utils.ChatUtils;

public class Tag {
   private String prefix;
  private String suffix;

  private int priority;

  public Tag(String prefix, String suffix, int priority) {
    this.prefix = prefix != null ? ChatUtils.translateColors(prefix) : "";
    this.suffix = suffix != null ? ChatUtils.translateColors(suffix) : "";
    this.priority = Math.abs(priority);
  }

  // ColoredTags
  public int getShortHash() {
    return ((this.prefix.hashCode() + 31) * 31) + this.suffix.hashCode();
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public String getSuffix() {
    return suffix;
  }

  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }
}
