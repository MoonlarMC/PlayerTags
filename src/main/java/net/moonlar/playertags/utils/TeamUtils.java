package net.moonlar.playertags.utils;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class TeamUtils {
  private TeamUtils() { }

  public static Team getTeam(Player player, String teamName) {
    Scoreboard scoreboard = player.getScoreboard();
    Team team = scoreboard.getTeam(teamName);

    if(team == null) {
      team = scoreboard.registerNewTeam(teamName);
    }

    return team;
  }

  public static void removePlayerFromTeam(Player player, String teamName) {
    Team team = TeamUtils.getTeam(player, teamName);

    if(team.hasEntry(player.getName())) {
      team.removeEntry(player.getName());
    }
  }
}
