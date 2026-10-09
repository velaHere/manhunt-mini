package org.velas.manhunt.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.customItems.CustomRecipe;
import org.velas.manhunt.game.scoreboard.ScoreManager;
import org.velas.scoreboard.api.Scoreboard;
import org.velas.scoreboard.api.team.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (strings.length == 1){

            if(commandSender.hasPermission(Settings.SETUP_PERMISSION)){
                return toSuggest(List.of("match", "team", "setlobby", "lobby", "recipe"), strings[0]);
            }
            return toSuggest(List.of("lobby"), strings[0]);
        }

        if(!commandSender.hasPermission(Settings.SETUP_PERMISSION)) return List.of();

        switch (strings[0]) {
            case "lobby" -> {

                if (!commandSender.hasPermission(Settings.SETUP_PERMISSION)) return List.of();

                if(strings.length == 2) {
                    return toSuggest(
                            Stream.concat(
                                    Stream.of("@a"),
                                    Bukkit.getOnlinePlayers().stream().map(Player::getName)
                            ).toList(),
                            strings[1]
                    );
                }
            }
            case "recipe" -> {
                if(strings.length==2) {
                    return toSuggest(new ArrayList<>(CustomRecipe.getRecipeIdList()), strings[1]);
                }
            }
            case "team" -> {

                if (strings.length == 2) {

                    List<String> toSuggestCommands = new ArrayList<>();

                    for (Team team : ScoreManager.getScoreboard().getTeams()) {
                        toSuggestCommands.add(team.getName());
                    }

                    return toSuggest(toSuggestCommands, strings[1]);

                }

                if (strings.length == 3) {

                    return toSuggest(List.of("add", "remove"), strings[2]);

                }

                if (strings.length == 4) {

                    if (strings[2].equalsIgnoreCase("add")) {

                        List<String> toSuggestCommands = new ArrayList<>();

                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

                            Scoreboard scoreboard = ScoreManager.getScoreboard();
                            Team team = scoreboard.getTeamOf(onlinePlayer.getUniqueId());

                            if (team == null) {
                                toSuggestCommands.add(onlinePlayer.getName());
                            }

                        }

                        return toSuggest(toSuggestCommands, strings[3]);

                    } else if (strings[2].equalsIgnoreCase("remove")) {

                        List<String> toSuggestCommands = new ArrayList<>();

                        Scoreboard scoreboard = ScoreManager.getScoreboard();

                        Team team = scoreboard.getTeam(strings[1]);

                        if (team != null) {
                            toSuggestCommands.addAll(
                                    team.getPlayers().stream()
                                            .map(Bukkit::getOfflinePlayer)
                                            .filter(OfflinePlayer::hasPlayedBefore)
                                            .map(OfflinePlayer::getName)
                                            .toList()
                            );
                        }

                        return toSuggest(toSuggestCommands, strings[3]);

                    }

                }
            }
            case "match" -> {

                if (strings.length == 2) {
                    return toSuggest(List.of("start", "stop", "start_previous"), strings[1]);
                }

                if (!strings[1].equalsIgnoreCase("start")) return List.of();

                if (strings.length == 3) {
                    return toSuggest(List.of("RANDOM", "PLAINS", "DESERT", "SAVANNA", "SNOWY", "TAIGA"), strings[2]);
                } else if (strings.length == 4) {
                    return toSuggest(List.of("30", "45", "60", "90", "default"), strings[3]);
                } else if (strings.length == 5) {
                    return toSuggest(List.of("random"), strings[4]);
                }
            }
        }
        return List.of();
    }

    public List<String> toSuggest(List<String> toSuggestCommands, String arg){
        List<String> commands = new ArrayList<>();
        for (String cmd: toSuggestCommands){
            if (cmd.toLowerCase().startsWith(arg.toLowerCase())) commands.add(cmd);
        }
        return commands;
    }
}