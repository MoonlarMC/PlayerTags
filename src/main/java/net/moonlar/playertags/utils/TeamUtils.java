package net.moonlar.playertags.utils;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class TeamUtils {
  private TeamUtils() { }

  public static Team getNewTeam(Player player, String teamName) {
    Scoreboard scoreboard = player.getScoreboard();
    Team team = scoreboard.getTeam(teamName);

    if(team != null) {
      team.unregister();
    }

    team = scoreboard.registerNewTeam(teamName);

    return team;
  }

  public static void removePlayerFromTeam(Player player, String teamName) {
    Team team = TeamUtils.getNewTeam(player, teamName);

    if(team.hasEntry(player.getName())) {
      team.removeEntry(player.getName());
    }
  }
}
